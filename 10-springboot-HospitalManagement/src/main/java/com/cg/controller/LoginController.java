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
	DoctorService doctorservice;
	
	@GetMapping("/login")
	public String showLoginPage() {
	    return "hospital/login"; // Returns login.html
	}

    @GetMapping("/redirect")
    public String redirectAfterLogin(Authentication authentication,Model model) {
        // Get the list of roles for the logged-in user
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        String username = authentication.getName();

        if (roles.contains("ROLE_ADMIN")) {
        	return "hospital/admin-index"; 
        } else if (roles.contains("ROLE_DOCTOR")) {
        	Doctor doctor = doctorservice.getDoctorByUsername(username); 
            model.addAttribute("doctor", doctor); 
            return "hospital/doctor-index";
            // maps to doctor-index.html
        } else if (roles.contains("ROLE_PATIENT")) {
            return "hospital/patient-index"; // maps to patient-index.html
        }
        return "login";
    }
}

