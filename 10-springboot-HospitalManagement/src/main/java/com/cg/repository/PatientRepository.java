package com.cg.repository;

import com.cg.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    // Use this if your field in Patient.java is 'fullName'
    List<Patient> findByNameContainingIgnoreCase(String fullName);

    // Use this if your field in Patient.java is 'email'
    Optional<Patient> findByEmail(String email);

    // This matches the findByUsername call in your service
    Optional<Patient> findByUsername(String username);
}
