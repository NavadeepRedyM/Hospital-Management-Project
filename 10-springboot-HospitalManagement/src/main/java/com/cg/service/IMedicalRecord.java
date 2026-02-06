package com.cg.service;

import java.util.List;
import com.cg.dto.MedicalRecordDTO; // Import your DTO

public interface IMedicalRecord {
    
    // Returns DTO instead of Entity
    MedicalRecordDTO createMedicalRecord(
            Long patientId,
            Long doctorId,
            Long appointmentId,
            String symptoms,
            String diagnosis,
            String treatmentPlan
    );

    MedicalRecordDTO getMedicalRecordById(Long id);

    List<MedicalRecordDTO> getMedicalRecordsByPatient(Long patientId);
    List<MedicalRecordDTO> getMedicalRecordsByDoctor(Long doctorId);
    List<MedicalRecordDTO> getMedicalRecordsByPatientUsername(String username);

    MedicalRecordDTO updateMedicalRecord(
            Long id,
            String symptoms,
            String diagnosis,
            String treatmentPlan
    );
}
