package com.cg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.cg.dto.DoctorDTO;
import com.cg.model.User;
import com.cg.model.Patient;
import com.cg.repository.PatientRepository;
import com.cg.service.DoctorService;
import com.cg.service.UserService;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private DoctorService doctorService;
    
    @Autowired
    private PatientRepository patientRepository;

    // 1. PATIENT REGISTRATION (Self-Service)
    @GetMapping("/register-user")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User()); 
        return "hospital/register-user";
    }

    @PostMapping("/register-user")
    public String saveRegisteredUser(@ModelAttribute("user") User user) {
        user.setRole("PATIENT"); 
        userService.saveUser(user); 
        
        // Create the placeholder patient so Admin can fill details later
        Patient placeholder = new Patient();
        placeholder.setUsername(user.getUsername());
        placeholder.setName("PENDING_DETAILS"); 
        
        patientRepository.save(placeholder);
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
        
        DoctorDTO doctorDto = doctorService.getDoctorByUsername(username);

        if (doctorDto == null) {
            model.addAttribute("error", "Username not assigned to any doctor profile. Please contact Admin.");
            return "hospital/register-doctor";
        }

        User user = doctorDto.getUser(); 
        if (user != null) {
            // Update the password
            user.setPassword(password); 
            userService.saveUser(user); 
            
            /* 
               ✅ NOTE: We do not need to set consultationFee here.
               The DoctorService.addDoctor(doctorDto) will re-link the doctor 
               to their department, automatically inheriting the fee.
            */
            doctorService.addDoctor(doctorDto); 
        }

        return "redirect:/login?registered";
    }
}
