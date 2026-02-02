package com.cg.controller;

import com.cg.service.DoctorService;
import com.cg.model.Doctor;
import com.cg.model.MedicalRecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/doctor")
public class DoctorController {

		@Autowired
	    DoctorService doctorService;
		
		private Doctor getCurrentDoctor(UserDetails userDetails) {
	        String loggedInUserName = userDetails.getUsername();
	        System.out.println("Logging in as: " + loggedInUserName);
	        

	        return doctorService.getDoctorByUsername(loggedInUserName); 
	        
	    }
		

		@GetMapping("/profile")
	    public String viewProfile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
			Doctor doctor = getCurrentDoctor(userDetails);
			System.out.println("Doctor Name from DB: " + (doctor != null ? doctor.getName() : "NULL"));
	        model.addAttribute("doctor", getCurrentDoctor(userDetails));
	        
	        return "hospital/doctor-profile";
	    }

		@GetMapping("/today-appointments")
	    public String todayAppointments(@AuthenticationPrincipal UserDetails userDetails, Model model) {
	        // Pass the whole doctor object so the HTML can use ${doctor.appointments}
	        model.addAttribute("doctor", getCurrentDoctor(userDetails));
	        return "hospital/doctor-appointments";
	    }

		@GetMapping("/records")
	    public String patientRecords(@AuthenticationPrincipal UserDetails userDetails, Model model) {
	        Doctor doctor = getCurrentDoctor(userDetails);
	        // Note: Make sure doctor-records.html uses 'records' as the variable name
	        model.addAttribute("records", doctor.getMedicalRecords());
	        return "hospital/doctor-records";
	    }
		@GetMapping("/add-diagnosis")
	    public String diagnosisForm(Model model) {
	        // This provides an empty object for the form binding in doctor-add-diagnosis.html
	        model.addAttribute("medicalRecord", new MedicalRecord());
	        return "hospital/doctor-add-diagnosis";
	    }
	}

