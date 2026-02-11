package com.cg.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cg.model.Doctor;
import com.cg.service.DoctorService;


@Controller
public class LoginController {
    
    @Autowired
    private DoctorService doctorservice; // Ensure this is the IDoctorService interface
    
    @GetMapping("/login")
    public String showLoginPage() {
        return "hospital/login"; 
    }

    @GetMapping("/redirect")
    public String redirectAfterLogin(Authentication authentication, Model model) {
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        String username = authentication.getName();

        if (roles.contains("ROLE_ADMIN")) {
            return "hospital/admin-index"; 
        } else if (roles.contains("ROLE_DOCTOR")) {
            // FIX: Change 'Doctor' entity to 'DoctorDTO'
            com.cg.dto.DoctorDTO doctor = doctorservice.getDoctorByUsername(username); 
            model.addAttribute("doctor", doctor); 
            return "doctor/doctor-index";
        } else if (roles.contains("ROLE_PATIENT")) {
            return "patient/patient-index"; 
        }
        return "redirect:/login?error";
    }
}
