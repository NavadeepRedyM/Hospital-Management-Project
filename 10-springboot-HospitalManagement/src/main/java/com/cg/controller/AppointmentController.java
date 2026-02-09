package com.cg.controller;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        Long deptId = appointment.getDepartment().getId();
        
        // ✅ Filter to only include active doctors
        List<Doctor> activeDoctors = doctorRepository.findByDepartmentId(deptId)
                .stream()
                .filter(Doctor::isActive)
                .toList();
        
        model.addAttribute("appointment", appointment);
        model.addAttribute("deptName", appointment.getDepartment().getDeptName());
        model.addAttribute("doctors", activeDoctors);
        return "hospital/assign-doctor";
    }
    @GetMapping("/reassign/{id}")
    public String showReassignForm(@PathVariable Long id, Model model) {
        AppointmentDTO appointment = appointmentService.getAppointmentById(id);
        Long deptId = appointment.getDepartment().getId();
        
        // ✅ Filter to only include active doctors for reassignment
        List<Doctor> sameDeptActiveDoctors = doctorRepository.findByDepartmentId(deptId)
                .stream()
                .filter(Doctor::isActive)
                .toList();
        
        model.addAttribute("appointment", appointment);
        model.addAttribute("deptName", appointment.getDepartment().getDeptName());
        model.addAttribute("doctors", sameDeptActiveDoctors);
        return "hospital/reassign-appointment"; 
    }

    @PostMapping("/reassign")
    public String processReassign(@RequestParam Long appointmentId, 
                                  @RequestParam Long doctorId, 
                                  RedirectAttributes ra) {
        try {
            // This calls the service logic we created earlier
            appointmentService.reassignDoctor(appointmentId, doctorId);
            ra.addFlashAttribute("success", "Appointment reassigned successfully!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        return "redirect:/appointments"; // Corrected redirect
    }
    @PostMapping("/cancel/{id}")
    public String cancelAppt(@PathVariable Long id, RedirectAttributes ra) {
        appointmentService.cancelAppointment(id);
        ra.addFlashAttribute("success", "Appointment marked as Cancelled.");
        // FIXED: Changed from /admin/appointments to /appointments
        return "redirect:/appointments"; 
    }




    
}
 