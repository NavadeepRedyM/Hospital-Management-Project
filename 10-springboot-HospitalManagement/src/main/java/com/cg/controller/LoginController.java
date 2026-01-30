package com.cg.controller;

import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {
	@GetMapping("/login")
	public String showLoginPage() {
	    return "hospital/login"; // Returns login.html
	}

    @GetMapping("/redirect")
    public String redirectAfterLogin(Authentication authentication) {
        // Get the list of roles for the logged-in user
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

        if (roles.contains("ROLE_ADMIN")) {
            return "hospital/admin-index"; // maps to admin-index.html
        } else if (roles.contains("ROLE_DOCTOR")) {
            return "hospital/doctor-index"; // maps to doctor-index.html
        } else if (roles.contains("ROLE_PATIENT")) {
            return "hospital/patient-index"; // maps to patient-index.html
        }
        return "login";
    }
}

