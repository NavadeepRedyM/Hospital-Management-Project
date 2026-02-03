package com.cg.service;

import java.util.List;

import com.cg.model.MedicalRecord;

public interface IMedicalRecord {
	 MedicalRecord createMedicalRecord(
	           Long patientId,
	           Long doctorId,
	           Long appointmentId,
	           String symptoms,
	           String diagnosis,
	           String treatmentPlan
	   );
	   MedicalRecord getMedicalRecordById(Long id);
	   List<MedicalRecord> getMedicalRecordsByPatient(Long patientId);
	   List<MedicalRecord> getMedicalRecordsByDoctor(Long doctorId);
	   MedicalRecord updateMedicalRecord(
	           Long id,
	           String symptoms,
	           String diagnosis,
	           String treatmentPlan
	   );
	}


