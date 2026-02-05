package com.cg.service;

import java.util.List;
import java.util.Optional;
import com.cg.dto.PatientDTO; // Import your DTO

public interface IPatientService {
    
    // Returns a List of DTOs
    List<PatientDTO> findAll();

    // Returns a single DTO
    PatientDTO findById(Long id);

    // Accepts a DTO for saving/updating
    void save(PatientDTO patientDto);

    void deleteById(Long id);

    // Returns search results as DTOs
    List<PatientDTO> search(String query);

    // Returns an Optional containing a DTO
    Optional<PatientDTO> findByUsername(String username);
}
