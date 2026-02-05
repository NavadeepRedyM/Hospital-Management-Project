package com.cg.controller;

import com.cg.dto.AppointmentDTO;
import com.cg.dto.BookAppointmentForm;
import com.cg.dto.DepartmentDTO;
import com.cg.dto.PatientDTO;
import com.cg.service.DepartmentService;
import com.cg.service.IAppointmentService;
import com.cg.service.IPatientService;
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
     * Updated to return PatientDTO to match Service return type
     */
    private PatientDTO getCurrentPatient(UserDetails userDetails) {
        String username = userDetails.getUsername();
        return patientService.findByUsername(username).orElse(null);
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
        // Corrected to use AppointmentDTO List
        List<AppointmentDTO> appointments = appointmentService.listForPatient(patient.getId());
        
        model.addAttribute("patient", patient);
        model.addAttribute("appointments", appointments);
        return "hospital/patient-appointments";
    }

    @GetMapping("/book-appointment")
    public String bookAppointmentForm(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        // Corrected to use DepartmentDTO List
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
            @ModelAttribute("form") BookAppointmentForm form,
            Model model
    ) {
        // 1. Fetch DepartmentDTO and safely get doctor
        DepartmentDTO dept = departmentService.getDepartmentById(form.getDepartmentId());
        
        if (dept.getDoctors() == null || dept.getDoctors().isEmpty()) {
            throw new RuntimeException("No doctors available in this department.");
        }
        
        Long assignedDoctorId = dept.getDoctors().get(0).getId();

        // 2. Call the book method which returns AppointmentDTO
        AppointmentDTO appt = appointmentService.book(
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
}
