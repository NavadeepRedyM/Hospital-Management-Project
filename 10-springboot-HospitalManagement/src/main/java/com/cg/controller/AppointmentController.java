package com.cg.controller;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
 
import com.cg.service.IAppointmentService;
 
@Controller
@RequestMapping("/appointments")
public class AppointmentController {
 
    @Autowired
    private IAppointmentService appointmentService;
 
    // ONLY VIEW LIST
    @GetMapping
    public String viewAppointments(Model model) {
        model.addAttribute(
                "appointments",
                appointmentService.getAllAppointments()
        );
        return "hospital/view-appointments";
    }
    
}
 