package com.cg.controller;

import com.cg.service.DoctorService;
import com.cg.dto.DoctorDTO;
import com.cg.model.Doctor;
import com.cg.model.MedicalRecord;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService; // Interface usage preferred

    private DoctorDTO getCurrentDoctor(UserDetails userDetails) {
        String loggedInUserName = userDetails.getUsername();
        // Service now returns DoctorDTO, so we match that type
        return doctorService.getDoctorByUsername(loggedInUserName); 
    }

    @GetMapping("/profile")
    public String viewProfile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        DoctorDTO doctor = getCurrentDoctor(userDetails);
        model.addAttribute("doctor", doctor);
        return "hospital/doctor-profile";
    }

    @GetMapping("/today-appointments")
    public String todayAppointments(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        // Thymeleaf will now use ${doctor.appointments} from the DTO
        model.addAttribute("doctor", getCurrentDoctor(userDetails));
        return "hospital/doctor-appointments";
    }

    @GetMapping("/records")
    public String patientRecords(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        DoctorDTO doctor = getCurrentDoctor(userDetails);
        // Accessing the list from the DTO
        model.addAttribute("records", doctor.getMedicalRecords());
        return "hospital/doctor-records";
    }

    @GetMapping("/add-diagnosis")
    public String diagnosisForm(Model model) {
        // You can keep MedicalRecord entity here if you haven't created MedicalRecordDTO yet
        model.addAttribute("medicalRecord", new MedicalRecord());
        return "hospital/doctor-add-diagnosis";
    }
}
