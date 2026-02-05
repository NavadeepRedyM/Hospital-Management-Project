package com.cg.controller;

import com.cg.dto.PatientDTO; // Ensure this import is present
import com.cg.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/patients")
public class AdminPatientController {

    @Autowired	
    private PatientService patientService;

    @GetMapping
    public String list(@RequestParam(value = "q", required = false) String q, Model model) {
        // Updated to match your Service method names: findAll() and search()
        model.addAttribute("patients", (q == null || q.isBlank()) 
            ? patientService.findAll() 
            : patientService.search(q)); 
        model.addAttribute("q", q);
        return "hospital/manage-patients";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("patient", new PatientDTO());
        return "hospital/add-patient";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        // Matches your service method: findById()
        model.addAttribute("patient", patientService.findById(id));
        return "hospital/edit-patient";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("patient") PatientDTO patientDto,
                       BindingResult result) {

        if (result.hasErrors()) {
            return (patientDto.getId() == null) ? "hospital/add-patient" : "hospital/edit-patient";
        }

        // Using your service's unified save() method
        patientService.save(patientDto);
        
        return "redirect:/admin/patients";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        // Matches your service method: deleteById()
        patientService.deleteById(id);
        return "redirect:/admin/patients";
    }
}
