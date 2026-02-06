package com.cg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.cg.dto.DoctorDTO;
import com.cg.model.Department;
import com.cg.model.User;
import com.cg.service.DoctorService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private DoctorService doctorService;

    @GetMapping("/manage-doctors")
    public String showDoctorsList(Model model) {
        model.addAttribute("listOfDoctors", doctorService.findAllDoctors());
        return "hospital/manage-doctors";
    }

    @GetMapping("/add-doctor")
    public String showAddForm(Model model) {
        DoctorDTO doctorDTO = new DoctorDTO();
        // Initialize objects to prevent null pointer exceptions in the view
        doctorDTO.setDepartment(new Department()); 
        doctorDTO.setUser(new User()); 
        
        model.addAttribute("doctor", doctorDTO);
        return "hospital/add-doctor";
    }

    @PostMapping("/save-doctor")
    public String saveDoctor(@Valid @ModelAttribute("doctor") DoctorDTO doctorDto, BindingResult result) {
        if (result.hasErrors()) {
            return "hospital/add-doctor";
        }

        // 1. Ensure security role is set
        if (doctorDto.getUser() != null) {
            doctorDto.getUser().setRole("ROLE_DOCTOR");
        }
        
        // 2. CRITICAL FIX: Explicitly nullify lists before passing to service
        // This stops Hibernate from trying to update/save related medical records
        doctorDto.setMedicalRecords(null);
        doctorDto.setAppointments(null);

        doctorService.addDoctor(doctorDto); 
        return "redirect:/admin/manage-doctors";
    }

    @GetMapping("/edit-doctor/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        DoctorDTO doctor = doctorService.findDoctorById(id);
        model.addAttribute("doctor", doctor);
        return "hospital/edit-doctor";
    }

    // Changed to PostMapping for better security when deleting
    @PostMapping("/delete-doctor/{id}")
    public String deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
        return "redirect:/admin/manage-doctors";
    }
}
