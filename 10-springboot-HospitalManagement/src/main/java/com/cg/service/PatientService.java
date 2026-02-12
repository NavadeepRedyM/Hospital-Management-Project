package com.cg.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.dto.PatientDTO;
import com.cg.exception.PatientNotFoundException;
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
                .orElseThrow(() -> new PatientNotFoundException("Patient not found with id: " + id));
    }

    @Override
    public void save(@Valid PatientDTO patientDto) {
        Patient patient;

        // 1. If ID exists, we are EDITING an existing complete record
        if (patientDto.getId() != null) {
            patient = patientRepository.findById(patientDto.getId())
                    .orElseThrow(() -> new PatientNotFoundException("Patient not found with id: " + patientDto.getId()));
        } 
        // 2. If ID is null, we are COMPLETING a placeholder record via the Add form
        else {
            patient = patientRepository.findByUsername(patientDto.getUsername())
                    .orElseGet(() -> new Patient());
        }

        // 3. Map DTO values to the fetched Entity
        patient.setName(patientDto.getName());
        patient.setAge(patientDto.getAge());
        patient.setGender(patientDto.getGender());
        patient.setContactNumber(patientDto.getContactNumber());
        patient.setEmail(patientDto.getEmail());
        patient.setAddress(patientDto.getAddress());
        patient.setBloodGroup(patientDto.getBloodGroup());
        
        // Ensure the username remains consistent
        patient.setUsername(patientDto.getUsername());

        // 4. Save the managed entity
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
    public List<String> getIncompleteUsernames() {
        return patientRepository.findIncompletePatientUsernames();
    }
}
