package com.cg.service;

import com.cg.model.Patient;
import java.util.List;

public interface PatientService {
	Patient registerPatient(Patient patient);
	Patient getPatientById(Long id);
	
	List<Patient> getAllPatients();

	Patient updatePatient(Long id, Patient patient);
	void deletePatient(Long id);
}
