package com.cg.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.cg.dto.BillingDTO;
import com.cg.dto.DoctorDTO;
import com.cg.model.Department;
import com.cg.model.User;
import com.cg.repository.BillingRepository;
import com.cg.service.BillingService;
import com.cg.service.DoctorService;
import com.cg.service.IDepartmentService; // Added to load departments for the dropdown

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private DoctorService doctorService;
    @Autowired
    private BillingService billingService; 

    @Autowired
    private IDepartmentService departmentService; // Needed to fetch department list
    
    
    @GetMapping("/dashboard")
    public String dashboard() {
        return "hospital/admin-index";
        // or "admin-dashboard" — use the HTML file you already have
    }

    @GetMapping("/manage-doctors")
    public String showDoctorsList(Model model) {
        model.addAttribute("listOfDoctors", doctorService.findAllDoctors());
        return "hospital/manage-doctors";
    }

    @GetMapping("/add-doctor")
    public String showAddForm(Model model) {
        DoctorDTO doctorDTO = new DoctorDTO();
        doctorDTO.setDepartment(new Department()); 
        doctorDTO.setUser(new User()); 
        
        model.addAttribute("doctor", doctorDTO);
        // ✅ Add department list so the Admin can choose a dept (and its fee)
        model.addAttribute("departments", departmentService.getAllDepartments());
        return "hospital/add-doctor";
    }

    @PostMapping("/save-doctor")
    public String saveDoctor(@Valid @ModelAttribute("doctor") DoctorDTO doctorDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            // ✅ Re-load departments if there is a validation error
            model.addAttribute("departments", departmentService.getAllDepartments());
            return "hospital/add-doctor";
        }

        if (doctorDto.getUser() != null) {
            doctorDto.getUser().setRole("ROLE_DOCTOR");
        }
        
        // Consultation fee is NOT set here anymore; it's pulled from the chosen Department
        doctorDto.setMedicalRecords(null);
        doctorDto.setAppointments(null);

        doctorService.addDoctor(doctorDto); 
        return "redirect:/admin/manage-doctors";
    }

    @GetMapping("/edit-doctor/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        DoctorDTO doctor = doctorService.findDoctorById(id);
        model.addAttribute("doctor", doctor);
        model.addAttribute("departments", departmentService.getAllDepartments());
        return "hospital/edit-doctor";
    }

    @PostMapping("/delete-doctor/{id}")
    public String deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
        return "redirect:/admin/manage-doctors";
    }
    @GetMapping("/billing-reports")
    public String showBillingReports(@RequestParam(name = "patientId", required = false) Long patientId, Model model) {
        List<BillingDTO> bills;
        if (patientId != null) {
            bills = billingService.getBillsByPatientId(patientId); // Implement this in service
        } else {
            bills = billingService.getAllBillings();
        }
        model.addAttribute("bills", bills);
        return "hospital/billing-reports"; 
    }


}
