package com.cg.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.model.Patient;
import com.cg.repository.PatientRepository;

@Service
public class PatientService implements IPatientService{
	
	@Autowired
	PatientRepository patientRepository;
	

	@Override
    public List<Patient> findAll() {
        return patientRepository.findAll();
    }

    @Override
    public Patient findById(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));
    }

    @Override
    public void save(Patient patient) {
        patientRepository.save(patient); // Handles both Insert and Update
    }

    @Override
    public void deleteById(Long id) {
        if (patientRepository.existsById(id)) {
            patientRepository.deleteById(id);
        }
    }

    @Override
    public List<Patient> search(String query) {
        return patientRepository.findByNameContainingIgnoreCase(query);
    }

    @Override
    public Optional<Patient> findByUsername(String username) {
        return patientRepository.findByUsername(username);
    }
	

}
