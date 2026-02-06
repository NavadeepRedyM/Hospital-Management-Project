package com.cg.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cg.dto.MedicalRecordDTO;
import com.cg.service.MedicalRecordService;

@Controller
@RequestMapping("/patient")
public class MedicalRecordController {
	
	@Autowired

    private MedicalRecordService medicalRecordService;

    @GetMapping("/records")

    public String viewPatientRecords(

            @AuthenticationPrincipal UserDetails userDetails,

            Model model) {

        String username = userDetails.getUsername();

        List<MedicalRecordDTO> records =

                medicalRecordService.getMedicalRecordsByPatientUsername(username);

        model.addAttribute("records", records);

        return "hospital/patient-records";

    }

}
 


	