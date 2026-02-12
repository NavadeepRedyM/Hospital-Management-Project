package com.cg.controller;

import com.cg.dto.PatientDTO; // Ensure this import is present
import com.cg.service.PatientService;
import com.cg.service.UserService;

import jakarta.validation.Valid;

import java.util.List;

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
        return "patient/manage-patients";
    }
    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("patient", new PatientDTO());
        
        // Logic: Fetch only patients who have a record but no profile details yet
        List<String> incomplete = patientService.getIncompleteUsernames();
        model.addAttribute("usernames", incomplete);
        
        return "patient/add-patient";
    }
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        // Matches your service method: findById()
        model.addAttribute("patient", patientService.findById(id));
        return "patient/edit-patient";
    }

    @PostMapping("/save")
    public String save( @Valid @ModelAttribute("patient") PatientDTO patientDto,
                       BindingResult result, Model model) {

        if (result.hasErrors()) {
            // ✅ Match the logic in addForm: use patientService to get incomplete records
            model.addAttribute("usernames", patientService.getIncompleteUsernames());
            
            // If editing, the dropdown isn't usually needed, but this prevents crashes
            return (patientDto.getId() == null) ? "hospital/add-patient" : "hospital/edit-patient";
        }

        patientService.save(patientDto);
        return "redirect:/admin/patients";
    }
    @PutMapping("/update")
    public String update( @Valid @ModelAttribute("patient") PatientDTO patientDto,
                       BindingResult result, Model model) {

        if (result.hasErrors()) {
            // ✅ Match the logic in addForm: use patientService to get incomplete records
            model.addAttribute("usernames", patientService.getIncompleteUsernames());
            
            // If editing, the dropdown isn't usually needed, but this prevents crashes
            return (patientDto.getId() == null) ? "hospital/add-patient" : "hospital/edit-patient";
        }

        patientService.save(patientDto);
        return "redirect:/admin/patients";
    }
    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        // Matches your service method: deleteById()
        patientService.deleteById(id);
        return "redirect:/admin/patients";
    }
}
