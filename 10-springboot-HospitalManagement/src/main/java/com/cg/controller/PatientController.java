package com.cg.controller;

import com.cg.dto.*;
import com.cg.model.Billing;
import com.cg.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/patient")
public class PatientController {

    @Autowired
    private IPatientService patientService;

    @Autowired
    private IAppointmentService appointmentService;

    @Autowired
    private DepartmentService departmentService;
    
    @Autowired
    private MedicalRecordService medicalRecordService;

    private PatientDTO getCurrentPatient(UserDetails userDetails) {
        return patientService.findByUsername(userDetails.getUsername()).orElse(null);
    }
    
    @GetMapping("/dashboard")
    public String patientDashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        PatientDTO patient = getCurrentPatient(userDetails);
        model.addAttribute("patient", patient);
        return "hospital/patient-index"; // This returns your patient-index.html
    }


    @GetMapping("/profile")
    public String viewProfile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        PatientDTO patient = patientService.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        model.addAttribute("patient", patient);
        return "hospital/patient-profile";
    }

    @GetMapping("/appointments")
    public String myAppointments(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        PatientDTO patient = getCurrentPatient(userDetails);
        List<AppointmentDTO> appointments = appointmentService.listForPatient(patient.getId());
        model.addAttribute("patient", patient);
        model.addAttribute("appointments", appointments);
        return "hospital/patient-appointments";
    }

    @GetMapping("/book-appointment")
    public String bookAppointmentForm(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        List<DepartmentDTO> departments = departmentService.getAllDepartments();
        PatientDTO patient = getCurrentPatient(userDetails);
        model.addAttribute("patient", patient);
        model.addAttribute("form", new BookAppointmentForm());
        model.addAttribute("departments", departments); 
        return "hospital/patient-book-appointment";
    }

    @PostMapping("/book-appointment")
    public String bookSubmit(
            @AuthenticationPrincipal UserDetails user,
            @ModelAttribute("form") BookAppointmentForm form
    ) {
        // ✅ Fee is now tied to the department, not a specific doctor
        AppointmentDTO appt = appointmentService.book(
                user.getUsername(),
                form.getDepartmentId(),
                null, // Doctor can be null initially (awaiting admin allotment)
                form.getDate(),
                form.getTime(),
                form.getReason()
        );

        return "redirect:/patient/payment/" + appt.getId();
    }

    @GetMapping("/payment/{id}")
    public String showPaymentPage(@PathVariable Long id, Model model) {
        AppointmentDTO appt = appointmentService.getAppointmentById(id);
        
        // Prepare Payment DTO
        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setAppointmentId(id);
        
        // ✅ CRITICAL FIX: Pull fee from Department (Available even if Doctor is null)
        double fee = appt.getDepartment().getConsultationFee();
        paymentDTO.setAmount(fee);
        paymentDTO.setStatus("PENDING");

        model.addAttribute("appointment", appt);
        model.addAttribute("payment", paymentDTO);
        return "hospital/patient-payment";
    }

    @PostMapping("/confirm-payment")
    public String confirmPayment(
            @ModelAttribute("payment") PaymentDTO paymentDto,
            Model model
    ) {
    	
    	// ✅ CALCULATE HERE (this was missing)
    	   double baseAmount = paymentDto.getAmount();
    	   double tax = baseAmount * 0.18;
    	   double total = baseAmount + tax;
    	   paymentDto.setTax(tax);
    	   paymentDto.setTotal(total);
    	   paymentDto.setStatus("COMPLETED");
    	
    	
    	
    	
        appointmentService.finalizeBookingWithPayment(paymentDto);
        AppointmentDTO finalAppt = appointmentService.getAppointmentById(paymentDto.getAppointmentId());
        model.addAttribute("appointment", finalAppt);
        model.addAttribute("payment",paymentDto);
        return "hospital/appointment-success"; 
    }
    @GetMapping("/bills")
    public String viewMyBills(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        // 1. Get the current patient profile
        PatientDTO patient = getCurrentPatient(userDetails);
        
        // 2. Fetch bills using the service we discussed earlier
        // If you haven't implemented IBillingServiceImpl yet, use the repository directly for now
        List<Billing> bills = appointmentService.getBillsByPatientId(patient.getId());
        
        // 3. Pass the list to Thymeleaf
        model.addAttribute("patient", patient);
        model.addAttribute("bills", bills);
        
        // 4. Return the HTML file (ensure the path matches your templates folder)
        return "hospital/patient-bills"; 
    }
    
    @GetMapping("/records")
    public String viewMedicalRecords(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        // 1. Get current patient
        PatientDTO patient = getCurrentPatient(userDetails);
        
        // 2. Fetch records using your new Service method
        List<MedicalRecordDTO> records = medicalRecordService.getMedicalRecordsByPatient(patient.getId());

        // 3. Add to model
        model.addAttribute("patient", patient);
        model.addAttribute("records", records);
        
        return "hospital/patient-records"; 
    }
    
    @GetMapping("/cancel-payment/{id}")
    public String cancelPayment(@PathVariable Long id) {
        // Call a service method to delete or mark the appointment as CANCELLED
        appointmentService.deleteAppointment(id); 
        
        // Redirect to dashboard with a cancel message (optional)
        return "redirect:/patient/dashboard?cancelled=true";
    }

}
