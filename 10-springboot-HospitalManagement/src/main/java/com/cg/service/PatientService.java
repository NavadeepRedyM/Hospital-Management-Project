package com.cg.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.dto.PatientDTO;
import com.cg.model.Patient;
import com.cg.repository.PatientRepository;

import jakarta.validation.Valid;

@Service
public class PatientService implements IPatientService {
	
    @Autowired
    private PatientRepository patientRepository;

    @Override
    public List<PatientDTO> findAll() {
        return patientRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PatientDTO findById(Long id) {
        return patientRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));
    }

    @Override
    public void save(@Valid PatientDTO patientDto) { // Changed parameter to DTO
        Patient patient = convertToEntity(patientDto);
        patientRepository.save(patient); 
    }

    @Override
    public void deleteById(Long id) {
        if (!patientRepository.existsById(id)) {
            throw new RuntimeException("Cannot delete. Patient not found with id: " + id);
        }
        patientRepository.deleteById(id);
    }

    @Override
    public List<PatientDTO> search(String query) {
        // Ensure this method is defined in PatientRepository
        return patientRepository.findByNameContainingIgnoreCase(query).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<PatientDTO> findByUsername(String username) {
        return patientRepository.findByUsername(username)
                .map(this::convertToDTO);
    }

    // Helper: Entity -> DTO
    private PatientDTO convertToDTO(Patient patient) {
        PatientDTO dto = new PatientDTO();
        dto.setId(patient.getId());
        dto.setName(patient.getName());
        dto.setGender(patient.getGender());
        dto.setAge(patient.getAge());
        dto.setContactNumber(patient.getContactNumber());
        dto.setEmail(patient.getEmail());
        dto.setAddress(patient.getAddress());
        dto.setBloodGroup(patient.getBloodGroup());
        dto.setUsername(patient.getUsername());
        return dto;
    }

    // Helper: DTO -> Entity
    private Patient convertToEntity(PatientDTO dto) {
        Patient patient = new Patient();
        if (dto.getId() != null) {
            patient.setId(dto.getId());
        }
        patient.setName(dto.getName());
        patient.setGender(dto.getGender());
        patient.setAge(dto.getAge());
        patient.setContactNumber(dto.getContactNumber());
        patient.setEmail(dto.getEmail());
        patient.setAddress(dto.getAddress());
        patient.setBloodGroup(dto.getBloodGroup());
        patient.setUsername(dto.getUsername());
        return patient;
    }
}
