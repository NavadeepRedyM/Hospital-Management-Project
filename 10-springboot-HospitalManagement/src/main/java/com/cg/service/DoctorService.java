package com.cg.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.cg.model.Doctor;
import com.cg.repository.DoctorRepository;

public class DoctorService implements IDoctorService {

	    

	    @Autowired // Injects the DoctorRepository dependency via constructor
	     DoctorRepository doctorRepository;


	    public Doctor saveDoctor(Doctor doctor) {
	        // Business logic can be added here before saving
	        return doctorRepository.save(doctor);
	    }

	    public Optional<Doctor> findDoctorById(Long id) {
	        return doctorRepository.findById(id);
	    }

	    public List<Doctor> findAllDoctors() {
	        return doctorRepository.findAll();
	    }

	    public List<Doctor> findDoctorsByQualification(String qualification) {
	        // This custom method must be defined in the DoctorRepository interface
	        return doctorRepository.findByQualification(qualification);
	    }

	    public void deleteDoctor(Long id) {
	        doctorRepository.deleteById(id);
	    }
	}

