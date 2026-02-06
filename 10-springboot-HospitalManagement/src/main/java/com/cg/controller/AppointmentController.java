package com.cg.controller;
 
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cg.dto.AppointmentDTO;
import com.cg.model.Doctor;
import com.cg.repository.DoctorRepository;
import com.cg.service.IAppointmentService;
 
@Controller
@RequestMapping("/appointments")
public class AppointmentController {
 
    @Autowired
    private IAppointmentService appointmentService;
    
    @Autowired
    private DoctorRepository doctorRepository;
 
    // ONLY VIEW LIST
    @GetMapping
    public String viewAppointments(Model model) {
        model.addAttribute(
                "appointments",
                appointmentService.getAllAppointments()
        );
        return "hospital/view-appointments";
    }
    
    @PostMapping("/save-assignment")
    public String saveAssignment(@RequestParam("appointmentId") Long appointmentId, 
                                 @RequestParam("doctorId") Long doctorId) {
        appointmentService.assignDoctorToAppointment(appointmentId, doctorId);
        return "redirect:/appointments";
    }
    
    @GetMapping("/assign/{id}")
    public String showAssignForm(@PathVariable Long id, Model model) {
        AppointmentDTO appointment = appointmentService.getAppointmentById(id);
        
        // ✅ Use the Department ID saved during the patient's booking
        Long deptId = appointment.getDepartment().getId();
        
        // Fetch only doctors from THIS department
        List<Doctor> filteredDoctors = doctorRepository.findByDepartmentId(deptId);
        
        model.addAttribute("appointment", appointment);
        model.addAttribute("deptName", appointment.getDepartment().getDeptName());
        model.addAttribute("doctors", filteredDoctors);
        return "hospital/assign-doctor";
    }



    
}
 