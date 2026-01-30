package com.cg.service;

import java.util.List;
import java.util.Optional;

import com.cg.model.Doctor;

public interface IDoctorService {
	    Doctor saveDoctor(Doctor doctor);

	    Optional<Doctor> findDoctorById(Long id);

	    List<Doctor> findAllDoctors();

	    List<Doctor> findDoctorsByQualification(String qualification);

	    void deleteDoctor(Long id);
	}


