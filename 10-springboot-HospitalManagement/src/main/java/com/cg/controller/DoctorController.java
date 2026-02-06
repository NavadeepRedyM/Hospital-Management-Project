package com.cg.controller;

import com.cg.service.DoctorService;
import com.cg.service.MedicalRecordService;
import com.cg.dto.DoctorDTO;
import com.cg.model.Doctor;
import com.cg.model.MedicalRecord;

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
    private DoctorService doctorService; // Interface usage preferred
    
    @Autowired
    private MedicalRecordService medicalRecordService;

    private DoctorDTO getCurrentDoctor(UserDetails userDetails) {
        String loggedInUserName = userDetails.getUsername();
        // Service now returns DoctorDTO, so we match that type
        return doctorService.getDoctorByUsername(loggedInUserName); 
    }

    @GetMapping("/profile")
    public String viewProfile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        DoctorDTO doctor = getCurrentDoctor(userDetails);
        model.addAttribute("doctor", doctor);
        return "hospital/doctor-profile";
    }

    @GetMapping("/today-appointments")
    public String todayAppointments(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        // Thymeleaf will now use ${doctor.appointments} from the DTO
        model.addAttribute("doctor", getCurrentDoctor(userDetails));
        return "hospital/doctor-appointments";
    }

    @GetMapping("/records")
    public String patientRecords(
            @AuthenticationPrincipal UserDetails userDetails, 
            @RequestParam(value = "search", required = false) String search, 
            Model model) {
        
        DoctorDTO doctor = getCurrentDoctor(userDetails);
        
        // ✅ Use the service to fetch filtered or all records
        List<MedicalRecord> records = medicalRecordService.searchByDoctorAndKeyword(doctor.getId(), search);

        model.addAttribute("records", records);
        model.addAttribute("searchKeyword", search);
        return "hospital/doctor-records";
    }


    @GetMapping("/add-diagnosis")
    public String diagnosisForm(@RequestParam(required = false) Long patientId, 
                                @RequestParam(required = false) Long apptId, Model model) {
        MedicalRecord record = new MedicalRecord();
        
        // Auto-set Patient ID if provided
        if (patientId != null && patientId > 0) {
            com.cg.model.Patient patient = new com.cg.model.Patient();
            patient.setId(patientId);
            record.setPatient(patient);
        }
        
        // ✅ Auto-set Appointment ID if provided
        if (apptId != null && apptId > 0) {
            com.cg.model.Appointment appt = new com.cg.model.Appointment();
            appt.setId(apptId);
            record.setAppointment(appt);
        }
        
        model.addAttribute("medicalRecord", record);
        return "hospital/doctor-add-diagnosis";
    }

    @PostMapping("/save-diagnosis")
    public String saveDiagnosis(
            @AuthenticationPrincipal UserDetails userDetails, 
            @ModelAttribute("medicalRecord") MedicalRecord medicalRecord) {
        
        // ✅ FIX 1: Null-safe check for Patient and Appointment
        if (medicalRecord.getPatient() == null || medicalRecord.getPatient().getId() == null ||
            medicalRecord.getAppointment() == null || medicalRecord.getAppointment().getId() == null) {
            return "redirect:/doctor/add-diagnosis?error=missingIds";
        }

        // ✅ FIX 2: Check for negative values after ensuring they aren't null
        if (medicalRecord.getPatient().getId() <= 0 || medicalRecord.getAppointment().getId() <= 0) {
            return "redirect:/doctor/add-diagnosis?error=invalidIds";
        }
        

        DoctorDTO doctorDto = getCurrentDoctor(userDetails);
        Doctor doctorEntity = new Doctor();
        doctorEntity.setId(doctorDto.getId());
        
        medicalRecord.setDoctor(doctorEntity);
        medicalRecord.setRecordDate(java.time.LocalDate.now());

        doctorService.addMedicalRecord(medicalRecord);
        return "redirect:/doctor/records";
    }


}
