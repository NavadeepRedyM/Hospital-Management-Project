// PatientService.java
package com.cg.service;

import java.util.List;
import java.util.Optional;

import com.cg.model.Patient;

public interface IPatientService {
	List<Patient> findAll();
    Patient findById(Long id);
    void save(Patient patient);
    void deleteById(Long id);
    List<Patient> search(String query);
    Optional<Patient> findByUsername(String username);
    
}