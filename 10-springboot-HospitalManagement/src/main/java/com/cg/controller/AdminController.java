package com.cg.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cg.dto.DoctorDTO;
import com.cg.model.Department;
import com.cg.model.Doctor;
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
        // Initialize DTO instead of Entity
        DoctorDTO doctorDTO = new DoctorDTO();
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

        // Ensure the role is assigned correctly before saving
        if (doctorDto.getUser() != null) {
            doctorDto.getUser().setRole("ROLE_DOCTOR");
        }
        
        doctorService.addDoctor(doctorDto); 
        return "redirect:/admin/manage-doctors";
    }

    @GetMapping("/edit-doctor/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        // Service returns DTO automatically
        DoctorDTO doctor = doctorService.findDoctorById(id);
        model.addAttribute("doctor", doctor);
        return "hospital/edit-doctor";
    }

    @PostMapping("/delete-doctor/{id}")
    public String deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
        return "redirect:/admin/manage-doctors";
    }
}
