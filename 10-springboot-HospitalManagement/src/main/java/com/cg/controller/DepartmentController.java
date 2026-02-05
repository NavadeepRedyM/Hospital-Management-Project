package com.cg.controller;





import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cg.model.Department;
import com.cg.model.Doctor;
import com.cg.repository.DepartmentRepository;
import com.cg.repository.DoctorRepository;
import com.cg.service.DepartmentService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/departments")
public class DepartmentController {

	@Autowired
	private DepartmentService departmentService;
	
	@Autowired
	private DoctorRepository doctorRepository;

	@GetMapping
	public String viewDepartments(Model model) {
		model.addAttribute("departments", departmentService.getAllDepartments());
		return "hospital/manage-department";

	}

	@GetMapping("/add")
	public String showAddForm(Model model) {
		model.addAttribute("department", new Department());
		return "hospital/add-department";
	}

	@PostMapping("/save")
	public String saveDepartment(@Valid @ModelAttribute("department")Department department,BindingResult result) {
		if (result.hasErrors()) {
			return "hospital/add-department";
		}
		departmentService.saveDepartment(department);
		return "redirect:/admin/departments";

	}

	@GetMapping("/edit/{id}")
	public String showEditForm(@PathVariable Long id, Model model) {
		model.addAttribute("department", departmentService.getDepartmentById(id));
		return "hospital/add-department";

	}

	@GetMapping("/delete/{id}")
	public String deleteDepartment(@PathVariable Long id) {
		departmentService.deleteDepartment(id);
		return "redirect:/admin/departments";

	}
	@GetMapping("/{id}/doctors")
	public String viewDoctorsByDepartment(@PathVariable Long id, Model model) {
	   Department department = departmentService.getDepartmentById(id);
	   model.addAttribute("department", department);
	   model.addAttribute("doctors",department.getDoctors());
	   return "hospital/department-doctors";
	}
	

}
