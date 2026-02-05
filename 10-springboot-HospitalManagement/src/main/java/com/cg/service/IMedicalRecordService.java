package com.cg.service;

import java.util.List;
import com.cg.dto.MedicalRecordDTO; // Import your DTO

public interface IMedicalRecordService {
    
    // Returns List of DTOs instead of Entities
    List<MedicalRecordDTO> getMedicalRecordByPatientUsername(String username);
}
