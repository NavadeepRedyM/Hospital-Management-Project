package com.cg.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cg.model.Department;
import com.cg.model.Doctor;
import com.cg.model.User;
import com.cg.service.DoctorService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminController {
	@Autowired
    DoctorService doctorService;

    @GetMapping("/manage-doctors")
    public String showDoctorsList(Model model) {
        model.addAttribute("listOfDoctors", doctorService.getAllDoctors());
        return "hospital/manage-doctors";
    }
    @GetMapping("/add-doctor")
    public String showAddForm(Model model) {
    	Doctor doctor=new Doctor();
        doctor.setDepartment(new Department()); // <--- Initialize this
        doctor.setUser(new User()); 
        model.addAttribute("doctor", doctor);
    
        return "hospital/add-doctor";
    }

    // 2. SAVE DOCTOR (Used for both Add and Update)
  @PostMapping("/save-doctor")
public String saveDoctor(@Valid @ModelAttribute("doctor") DoctorDTO doctorDto, BindingResult result) {
    // 1. Validation check (Catch errors early)
    if (result.hasErrors()) {
        return "hospital/add-doctor";
    }

    // 2. Security Logic (From your 'Incoming' change)
    if (doctorDto.getUser() != null) {
        doctorDto.getUser().setRole("ROLE_DOCTOR");
    }
    
    // 3. Save DTO (Matches your new Service layer)
    doctorService.addDoctor(doctorDto); 
    return "redirect:/admin/manage-doctors";
}



    // 3. EDIT DOCTOR - Show Form with existing data
    @GetMapping("/edit-doctor/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Doctor doctor = doctorService.findDoctorById(id);
        model.addAttribute("doctor", doctor);
        return "hospital/edit-doctor";
    }

    // 4. DELETE DOCTOR
    @PostMapping("/delete-doctor/{id}")
    public String deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctorById(id);
        return "redirect:/admin/manage-doctors";
    }
}
