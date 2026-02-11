package com.cg.controller;

import com.cg.service.DoctorService;
import com.cg.service.MedicalRecordService;
import com.cg.dto.DoctorDTO;
import com.cg.model.Doctor;
import com.cg.model.MedicalRecord;
import com.cg.model.Patient;
import com.cg.model.Appointment;

import java.util.List;
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
    private DoctorService doctorService;
    
    @Autowired
    private MedicalRecordService medicalRecordService;

    private DoctorDTO getCurrentDoctor(UserDetails userDetails) {
        return doctorService.getDoctorByUsername(userDetails.getUsername()); 
    }
    //Dashboard 
    @GetMapping("/dashboard")
    public String doctorDashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("doctor", getCurrentDoctor(userDetails));
        return "doctor/doctor-index";
    }
    @GetMapping("/profile")
    public String viewProfile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("doctor", getCurrentDoctor(userDetails));
        return "doctor/doctor-profile";
    }

    @GetMapping("/today-appointments")
    public String todayAppointments(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("doctor", getCurrentDoctor(userDetails));
        return "doctor/doctor-appointments";
    }

    @GetMapping("/records")
    public String patientRecords(
            @AuthenticationPrincipal UserDetails userDetails, 
            @RequestParam(value = "search", required = false) String search, 
            Model model) {
        DoctorDTO doctor = getCurrentDoctor(userDetails);
        List<MedicalRecord> records = medicalRecordService.searchByDoctorAndKeyword(doctor.getId(), search);
        model.addAttribute("records", records);
        model.addAttribute("searchKeyword", search);
        return "doctor/doctor-records";
    }

    /**
     * ✅ FIXED: Added 'required = false' to prevent the 400 Bad Request error.
     */
    @GetMapping("/add-diagnosis")
    public String diagnosisForm(
            @RequestParam(required = false) Long patientId, 
            @RequestParam(required = false) Long apptId, 
            Model model) {
        
        MedicalRecord record = new MedicalRecord();
        
        // Only set Patient if ID is provided
        if (patientId != null) {
            Patient patient = new Patient();
            patient.setId(patientId);
            record.setPatient(patient);
        }
        
        // Only set Appointment if ID is provided
        if (apptId != null) {
            Appointment appt = new Appointment();
            appt.setId(apptId);
            record.setAppointment(appt);
        }
        
        model.addAttribute("medicalRecord", record);
        return "doctor/doctor-add-diagnosis";
    }

    @PostMapping("/save-diagnosis")
    public String saveDiagnosis(
            @AuthenticationPrincipal UserDetails userDetails, 
            @ModelAttribute("medicalRecord") MedicalRecord medicalRecord) {
        
        DoctorDTO doctorDto = getCurrentDoctor(userDetails);
        Doctor doctorEntity = new Doctor();
        doctorEntity.setId(doctorDto.getId());
        
        medicalRecord.setDoctor(doctorEntity);
        medicalRecord.setRecordDate(java.time.LocalDate.now());

        doctorService.addMedicalRecord(medicalRecord);
        return "redirect:/doctor/records";
    }
}
