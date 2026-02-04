package com.cg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.cg.model.User;
import com.cg.service.UserService;

@Controller
public class RegistrationController {

	
	@Autowired
    private UserService userService;

    // 1. DISPLAY THE FORM: When you click "Register" on the login page
    @GetMapping("/register-user")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User()); // Provide an empty object for Thymeleaf
        return "hospital/register-user";
    }

    // 2. SAVE TO DATABASE: When you click the "Register" button on the form
    @PostMapping("/register-user")
    public String saveRegisteredUser(@ModelAttribute("user") User user) {
        // Force the role to PATIENT for public sign ups
        user.setRole("PATIENT"); 
        
        // This calls your UserService which encrypts the password and saves to DB
        userService.saveUser(user); 
        
        // After saving, redirect back to login page with a success message
        return "redirect:/login?success";
    }
}
