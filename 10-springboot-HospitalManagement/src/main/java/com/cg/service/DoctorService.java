package com.cg.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.model.Doctor;
import com.cg.repository.DoctorRepository;

@Service
public class DoctorService implements IDoctorService {

	    

  @Autowired
    DoctorRepository doctorRepository;

    public Doctor findDoctorByName(String name) {
        return doctorRepository.findByName(name);
    }

    public List<Doctor> findByQualification(String qualification) {
        return doctorRepository.findByQualification(qualification);
    }

    public List<Doctor> findAllDoctors() {
        return doctorRepository.findAll();
    }


	@Override
	public Optional<Doctor> findDoctorById(Long id) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public List<Doctor> findDoctorsByQualification(String qualification) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteDoctor(Long id) {
		// TODO Auto-generated method stub
		
	}

	public Doctor getDoctorByUsername(String loggedInUserName) {
	         return doctorRepository.findDoctorByUserName(loggedInUserName);
	}
	
	public List<Doctor> getAllDoctors(){
		return doctorRepository.findAll();
	}
	public void deleteDoctorById(long id) {
		doctorRepository.deleteById(id);
	}
	
	public Doctor addDoctor(Doctor doctor) {
		return doctorRepository.save(doctor);
	}
	}

