package com.cg.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cg.model.MedicalRecord;
import com.cg.repository.MedicalRecordRepository;

@Service
public class MedicalRecordImpl implements IMedicalRecord {
	
	
	private final MedicalRecordRepository medicalRecordRepository;
	   public  final PatientRepository patientRepository;
	   public final DoctorRepository doctorRepository;
	   public  final AppointmentRepository appointmentRepository;

	
	 public MedicalRecordServiceImpl(
	           MedicalRecordRepository medicalRecordRepository,
	           PatientRepository patientRepository,
	           DoctorRepository doctorRepository,
	           AppointmentRepository appointmentRepository) {
		 
	       this.medicalRecordRepository = medicalRecordRepository;
	       this.patientRepository = patientRepository;
	       this.doctorRepository = doctorRepository;
	       this.appointmentRepository = appointmentRepository;
	   }

	@Override
	public MedicalRecord createMedicalRecord(Long patientId, Long doctorId, Long appointmentId, String symptoms,
			String diagnosis, String treatmentPlan) {
		// 1️⃣ Validate Patient
	       Patient patient = patientRepository.findById(patientId)
	               .orElseThrow(() -> new RuntimeException("Patient not found"));
	       // 2️⃣ Validate Doctor
	       Doctor doctor = doctorRepository.findById(doctorId)
	               .orElseThrow(() -> new RuntimeException("Doctor not found"));
	       // 3️⃣ Validate Appointment
	       Appointment appointment = appointmentRepository.findById(appointmentId)
	               .orElseThrow(() -> new RuntimeException("Appointment not found"));
	       // 4️⃣ Ensure one appointment → one medical record
	       medicalRecordRepository.findByAppointmentId(appointmentId)
	               .ifPresent(record -> {
	                   throw new RuntimeException("Medical record already exists for this appointment");
	               });
	       // 5️⃣ Create MedicalRecord
	       MedicalRecord record = new MedicalRecord();
	       record.setPatient(patient);
	       record.setDoctor(doctor);
	       record.setAppointment(appointment);
	       record.setSymptoms(symptoms);
	       record.setDiagnosis(diagnosis);
	       record.setTreatmentPlan(treatmentPlan);
	       record.setRecordDate(LocalDate.now());
	       return medicalRecordRepository.save(record);
	   }
	
	

	@Override
	public MedicalRecord getMedicalRecordById(Long id) {
		   return MedicalRecordRepository.findById(id)
	               .orElseThrow(() -> new RuntimeException("Medical record not found"));
	}

	@Override
	public List<MedicalRecord> getMedicalRecordsByPatient(Long patientId) {
		 return medicalRecordRepository.findByPatientId(patientId);
	}

	@Override
	public List<MedicalRecord> getMedicalRecordsByDoctor(Long doctorId) {
		 return medicalRecordRepository.findByDoctorId(doctorId);
	}

	@Override
	public MedicalRecord updateMedicalRecord(Long id, String symptoms, String diagnosis, String treatmentPlan) {
		MedicalRecord record = MedicalRecordRepository.findById(id)
	               .orElseThrow(() -> new RuntimeException("Medical record not found"));
	       record.setSymptoms(symptoms);
	       record.setDiagnosis(diagnosis);
	       record.setTreatmentPlan(treatmentPlan);
	       return MedicalRecordRepository.save(record);
	}

}
