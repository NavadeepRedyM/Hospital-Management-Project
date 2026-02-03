package com.cg.service;

import com.cg.model.Patient;
import com.cg.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientServiceImpl implements PatientService {

	private final PatientRepository patientRepository;

	public PatientServiceImpl(PatientRepository patientRepository) {
		this.patientRepository = patientRepository;
	}

	@Override
	public Patient registerPatient(Patient patient) {
		return patientRepository.save(patient);
	}

	@Override
	public Patient getPatientById(Long id) {
		return patientRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Patient not found with ID: " + id));
	}

	@Override
	public List<Patient> getAllPatients() {
		return patientRepository.findAll();
	}

	@Override
	public Patient updatePatient(Long id, Patient updatedPatient) {
		Patient existing = getPatientById(id);
		existing.setFullName(updatedPatient.getFullName());
		existing.setGender(updatedPatient.getGender());
		existing.setContactNumber(updatedPatient.getContactNumber());
		existing.setEmail(updatedPatient.getEmail());
		existing.setAddress(updatedPatient.getAddress());
		existing.setBloodGroup(updatedPatient.getBloodGroup());
		return patientRepository.save(existing);
	}

	@Override
	public void deletePatient(Long id) {
		patientRepository.deleteById(id);
	}
}