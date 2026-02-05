package com.cg.controller;

import com.cg.model.Patient;
import com.cg.model.MedicalRecord;
import com.cg.dto.BookAppointmentForm;
import com.cg.model.Appointment;
import com.cg.model.Department;
import com.cg.service.IPatientService;
import com.cg.service.DepartmentService;
import com.cg.service.IAppointmentService;

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
    

    /**
     * Helper method to fetch the logged-in patient details.
     */
    private Patient getCurrentPatient(UserDetails userDetails) {
        String username = userDetails.getUsername();
        return patientService.findByUsername(username).orElse(null);
    }


    @GetMapping("/profile")
    public String viewProfile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        String userName = userDetails.getUsername();
        // Using 'patient' as the key to match your dashboard fragment
        System.out.println("user name"+userName);
        Patient patient = patientService.findByUsername(userName)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        model.addAttribute("patient", patient);
        return "hospital/patient-profile";
    }

    @GetMapping("/appointments")
    public String myAppointments(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Patient patient = getCurrentPatient(userDetails);
        List<Appointment> appointments = appointmentService.listForPatient(patient.getId());
        
        model.addAttribute("patient", patient);
        model.addAttribute("appointments", appointments);
        return "hospital/patient-appointments";
    }

//    @GetMapping("/records")
//    public String myMedicalRecords(@AuthenticationPrincipal UserDetails userDetails, Model model) {
//        Patient patient = getCurrentPatient(userDetails);
//        // Ensure your Patient model has a getMedicalRecords() method
//        model.addAttribute("patient", patient);
//        model.addAttribute("records", patient.getMedicalRecords());
//        return "hospital/patient-records";
//    }

    @GetMapping("/book-appointment")
    public String bookAppointmentForm(@AuthenticationPrincipal UserDetails userDetails, Model model) {
    	List<Department> departments = departmentService.getAllDepartments();
        Patient patient = getCurrentPatient(userDetails);
        model.addAttribute("patient", patient); // For showing name/email in UI
        model.addAttribute("form", new BookAppointmentForm());
        model.addAttribute("departments", departments); 
        // For the form submission
        return "hospital/patient-book-appointment";
    }
    
    @PostMapping("/book-appointment")
    public String bookSubmit(
            @AuthenticationPrincipal UserDetails user,
            @RequestParam("patientId") Long patientId, // Capture the hidden ID
            @ModelAttribute("form") BookAppointmentForm form,
            Model model
    ) {
        // 1. Fetch department to assign a doctor (avoids the 'ID must not be null' error)
        Department dept = departmentService.getDepartmentById(form.getDepartmentId());
        Long assignedDoctorId = dept.getDoctors().get(0).getId();

        // 2. Use the username from Security for better safety
        Appointment appt = appointmentService.book(
                user.getUsername(),
                form.getDepartmentId(),
                assignedDoctorId,
                form.getDate(),
                form.getTime(),
                form.getReason()
        );

        model.addAttribute("appointment", appt);
        return "hospital/appointment-success"; 
    }

    


//    @GetMapping("/bills")
//    public String myBills(@AuthenticationPrincipal UserDetails userDetails, Model model) {
//        Patient patient = getCurrentPatient(userDetails);
//        model.addAttribute("patient", patient);
//        // Assuming your Patient model has a getBills() method
//        model.addAttribute("bills", patient.getBills());
//        return "hospital/patient-bills";
//    }
    
}
