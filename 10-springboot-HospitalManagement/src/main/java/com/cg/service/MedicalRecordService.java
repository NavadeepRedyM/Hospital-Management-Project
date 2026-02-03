package com.cg.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.model.Appointment;
import com.cg.model.Doctor;
import com.cg.model.MedicalRecord;
import com.cg.model.Patient;
import com.cg.repository.AppointmentRepository;
import com.cg.repository.DoctorRepository;
import com.cg.repository.MedicalRecordRepository;
import com.cg.repository.PatientRepository;

@Service
public class MedicalRecordService implements IMedicalRecord {

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;
    
    @Autowired
    private PatientRepository patientRepository;
    
    @Autowired
    private DoctorRepository doctorRepository;
    
    @Autowired
    private AppointmentRepository appointmentRepository;

    @Override
    public MedicalRecord createMedicalRecord(Long patientId, Long doctorId, Long appointmentId, 
                                           String symptoms, String diagnosis, String treatmentPlan) {
        
        Optional<Patient> patientOpt = patientRepository.findById(patientId);
        Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);
        Optional<Appointment> appointmentOpt = appointmentRepository.findById(appointmentId);

        // Validating all required entities exist before using .get()
        if (patientOpt.isPresent() && doctorOpt.isPresent() && appointmentOpt.isPresent()) {
            
            // Business Rule: Ensure one record per appointment
            if (medicalRecordRepository.findByAppointmentId(appointmentId).isPresent()) {
                throw new RuntimeException("Medical record already exists for this appointment");
            }

            MedicalRecord record = new MedicalRecord();
            record.setPatient(patientOpt.get());
            record.setDoctor(doctorOpt.get());
            record.setAppointment(appointmentOpt.get());
            record.setSymptoms(symptoms);
            record.setDiagnosis(diagnosis);
            record.setTreatmentPlan(treatmentPlan);
            record.setRecordDate(LocalDate.now());
            
            return medicalRecordRepository.save(record);
        } else {
            throw new RuntimeException("Invalid Patient, Doctor, or Appointment ID provided");
        }
    }

    @Override
    public MedicalRecord getMedicalRecordById(Long id) {
        Optional<MedicalRecord> recordOpt = medicalRecordRepository.findById(id);
        if (recordOpt.isPresent()) {
            return recordOpt.get();
        }
        throw new RuntimeException("Medical record not found for ID: " + id);
    }

    @Override
    public List<MedicalRecord> getMedicalRecordsByPatient(Long patientId) {
        // Returns a list; does not require .get() as it returns an empty list if none found
        return medicalRecordRepository.findByPatientId(patientId);
    }

    @Override
    public List<MedicalRecord> getMedicalRecordsByDoctor(Long doctorId) {
        return medicalRecordRepository.findByDoctorId(doctorId);
    }

    @Override
    public MedicalRecord updateMedicalRecord(Long id, String symptoms, String diagnosis, String treatmentPlan) {
        Optional<MedicalRecord> recordOpt = medicalRecordRepository.findById(id);
        
        if (recordOpt.isPresent()) {
            MedicalRecord record = recordOpt.get();
            record.setSymptoms(symptoms);
            record.setDiagnosis(diagnosis);
            record.setTreatmentPlan(treatmentPlan);
            return medicalRecordRepository.save(record);
        }
        throw new RuntimeException("Cannot update: Record not found for ID: " + id);
    }
}
