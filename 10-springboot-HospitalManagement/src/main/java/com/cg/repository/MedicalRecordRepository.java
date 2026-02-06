package com.cg.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cg.model.MedicalRecord;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long>{
	List<MedicalRecord>findByPatientId(Long patientId);
	List<MedicalRecord>findByDoctorId(Long doctorId);
	Optional<MedicalRecord>findByAppointmentId(Long appointmentId);
	List<MedicalRecord>findByRecordDate(LocalDate recordDate);
	 @Query("SELECT m FROM MedicalRecord m WHERE " +
	           "m.doctor.id = :doctorId AND (" +
	           "CAST(m.patient.id AS string) LIKE %:keyword% OR " +
	           "LOWER(m.patient.name) LIKE LOWER(CONCAT('%', :keyword, '%')))")
	    List<MedicalRecord> searchByPatient(@Param("doctorId") Long doctorId, @Param("keyword") String keyword);
	

}
