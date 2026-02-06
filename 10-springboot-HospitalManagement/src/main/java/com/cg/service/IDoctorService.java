package com.cg.service;

import java.util.List;
import com.cg.dto.DoctorDTO; // Import your DoctorDTO
import com.cg.model.MedicalRecord;

public interface IDoctorService {

    // Returns a DTO instead of an Entity
    DoctorDTO findDoctorById(Long id);

    // Returns a List of DTOs
    List<DoctorDTO> findAllDoctors();

    // Returns a List of DTOs
    List<DoctorDTO> findDoctorsByQualification(String qualification);

    // Standard delete operation
    void deleteDoctor(Long id);

    // Returns a DTO based on username
    DoctorDTO getDoctorByUsername(String loggedInUserName);
    
    // Adding the addDoctor method to match your service implementation
    DoctorDTO addDoctor(DoctorDTO doctorDTO);
    
    void addMedicalRecord(MedicalRecord medicalRecord);

}
