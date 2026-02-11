package com.cg.controller;

import com.cg.dto.DepartmentDTO;
import com.cg.service.IDepartmentService; // Use the interface
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/departments")
public class DepartmentController {

    @Autowired
    private IDepartmentService departmentService;

    @GetMapping
    public String viewDepartments(Model model) {
        // Service returns List<DepartmentDTO>
        model.addAttribute("departments", departmentService.getAllDepartments());
        return "department/manage-department";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        // Initialize DTO for the form
        model.addAttribute("department", new DepartmentDTO());
        return "department/add-department";
    }

    @PostMapping("/save")
    public String saveDepartment(@Valid @ModelAttribute("department") DepartmentDTO departmentDto, BindingResult result) {
        if (result.hasErrors()) {
            // Returns to the form to show validation errors
            return "department/add-department";
        }
        
        if (departmentDto.getId() == null) {
            // Match the service method name exactly
            departmentService.addDepartment(departmentDto); 
        } else {
            departmentService.updateDepartment(departmentDto.getId(), departmentDto);
        }
        
        return "redirect:/admin/departments";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        // Service returns DepartmentDTO
        model.addAttribute("department", departmentService.getDepartmentById(id));
        return "department/add-department";
    }

    @GetMapping("/delete/{id}")
    public String deleteDepartment(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            departmentService.deleteDepartment(id);
            redirectAttributes.addFlashAttribute("success", "Department deleted successfully!");
        } catch (IllegalStateException e) {
            // Catch the error thrown by the service
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/departments";
    }

    @GetMapping("/{id}/doctors")
    public String viewDoctorsByDepartment(@PathVariable Long id, Model model) {
       DepartmentDTO department = departmentService.getDepartmentById(id);
       
       // Filter the list so the UI only shows working doctors
       var activeDoctors = department.getDoctors().stream()
               .filter(doc -> doc.isActive())
               .toList();

       model.addAttribute("department", department);
       model.addAttribute("doctors", activeDoctors);
       return "department/department-doctors";
    }

}
