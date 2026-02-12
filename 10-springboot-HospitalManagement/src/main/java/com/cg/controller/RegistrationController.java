package com.cg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.cg.dto.DoctorDTO;
import com.cg.dto.UserDTO;
import com.cg.model.User;
import com.cg.model.Patient;
import com.cg.repository.PatientRepository;
import com.cg.service.DoctorService;
import com.cg.service.RegistractionService;
import com.cg.service.UserService;

import jakarta.validation.Valid;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private DoctorService doctorService;
    
    @Autowired
    private RegistractionService registrationService;

    // 1. PATIENT REGISTRATION (Self-Service)
    @GetMapping("/register-user")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User()); 
        return "hospital/register-user";
    }

    @PostMapping("/register-user")
    public String saveRegisteredUser(@Valid @ModelAttribute("user") UserDTO userDto, BindingResult result) {
        // 1. Check if the password/username meets constraints
        if (result.hasErrors()) {
            // If password is weak, this returns to the form and 
            // your GlobalExceptionHandler/BindingResult shows the errors.
            return "hospital/register-user"; 
        }

        User user = new User();
        user.setUsername(userDto.getUsername());
        // The password here is plain text; your UserServiceImpl should encode it!
        user.setPassword(userDto.getPassword()); 
        user.setRole("ROLE_PATIENT"); // Ensure this matches the ROLE_ prefix used in Security
        
        userService.saveUser(user); 
        
        // Create placeholder
        Patient placeholder = new Patient();
        placeholder.setUsername(user.getUsername());
        placeholder.setName("PENDING_DETAILS"); 
        registrationService.savePatient(placeholder);
        
        return "redirect:/login?success";
    }

    
    // 2. DOCTOR ACCOUNT ACTIVATION
    @GetMapping("/register-doctor")
    public String showDoctorRegistration() {
        return "doctor/register-doctor";
    }
    
    @PostMapping("/doctor-registration")
    public String processDoctorRegistration(@RequestParam String username, 
                                            @RequestParam String password, 
                                            Model model) {
        
        // 1. Fetch the doctor profile
        DoctorDTO doctorDto = doctorService.getDoctorByUsername(username);

        if (doctorDto == null) {
            model.addAttribute("error", "Username not assigned to any doctor profile.");
            return "doctor/register-doctor";
        }

        // 2. MANUAL VALIDATION for Doctor Password
        // This matches the regex pattern in your UserDTO
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
        if (!password.matches(passwordPattern)) {
            model.addAttribute("error", "Password is too weak! Must be 8+ chars, include Uppercase, Lowercase, Digit, and Special Character.");
            return "doctor/register-doctor";
        }

        User user = doctorDto.getUser(); 
        if (user != null) {
            user.setPassword(password); 
            userService.saveUser(user); // Your service should handle the BCrypt encoding
            doctorService.addDoctor(doctorDto); 
        }

        return "redirect:/login?registered";
    }
}
