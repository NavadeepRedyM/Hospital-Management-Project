package com.cg.repository;

import org.springframework.stereotype.Repository;

import com.cg.model.Doctor;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long>
{

	Doctor findByName(String name);

	List<Doctor> findByQualification(String qualification);


}
