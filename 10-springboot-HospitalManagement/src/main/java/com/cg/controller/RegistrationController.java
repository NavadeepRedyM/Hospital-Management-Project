package com.cg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.cg.dto.DoctorDTO;
import com.cg.model.Doctor;
import com.cg.model.User;
import com.cg.service.DoctorService;
import com.cg.service.UserService;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private DoctorService doctorService;

    // 1. PATIENT REGISTRATION (Using User Entity)
    @GetMapping("/register-user")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User()); 
        return "hospital/register-user";
    }

    @PostMapping("/register-user")
    public String saveRegisteredUser(@ModelAttribute("user") User user) {
        // Force role to PATIENT
        user.setRole("PATIENT"); 
        
        // UserService handles encryption and saving the Entity
        userService.saveUser(user); 
        
        return "redirect:/login?success";
    }
    
    // 2. DOCTOR ACCOUNT ACTIVATION
    @GetMapping("/register-doctor")
    public String showDoctorRegistration() {
        return "hospital/register-doctor";
    }
    
    @PostMapping("/doctor-registration")
    public String processDoctorRegistration(@RequestParam String username, 
                                            @RequestParam String password, 
                                            Model model) {
        // Fetch DoctorDTO (since your DoctorService uses DTOs)
        DoctorDTO doctorDto = doctorService.getDoctorByUsername(username);

        if (doctorDto == null) {
            model.addAttribute("error", "Username not found. Please contact Admin.");
            return "hospital/register-doctor";
        }

        // Get the User Entity from the DoctorDTO
        // Note: This assumes DoctorDTO returns the User Entity via .getUser()
        User user = doctorDto.getUser(); 
        
        if (user != null) {
            user.setPassword(password); // Set plain text; saveUser will encrypt it
            
            // Save the User Entity
            userService.saveUser(user); 
            
            // Re-save the Doctor (pass the DTO back to doctorService)
            doctorService.addDoctor(doctorDto); 
        }

        return "redirect:/login?registered";
    }
}
