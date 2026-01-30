package com.cg.repository;

import org.springframework.stereotype.Repository;

import com.cg.model.Doctor;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long>
{

}
