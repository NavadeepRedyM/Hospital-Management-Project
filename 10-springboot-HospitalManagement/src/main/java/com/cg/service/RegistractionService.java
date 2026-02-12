package com.cg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.model.Patient;
import com.cg.repository.PatientRepository;

@Service
public class RegistractionService {
   
	@Autowired
    private PatientRepository patientRepository;
	
	
	public Patient savePatient(Patient placeholder) {
		return patientRepository.save(placeholder);
	}
}
