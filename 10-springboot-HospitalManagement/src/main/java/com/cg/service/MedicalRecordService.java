package com.cg.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.dto.MedicalRecordDTO;
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
    public MedicalRecordDTO createMedicalRecord(Long patientId, Long doctorId, Long appointmentId, 
                                           String symptoms, String diagnosis, String treatmentPlan) {
        
        // Use .orElseThrow for cleaner "fail-fast" logic
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found: " + patientId));
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found: " + doctorId));
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found: " + appointmentId));

        if (medicalRecordRepository.findByAppointmentId(appointmentId).isPresent()) {
            throw new RuntimeException("Medical record already exists for this appointment");
        }

        MedicalRecord record = new MedicalRecord();
        record.setPatient(patient);
        record.setDoctor(doctor);
        record.setAppointment(appointment);
        record.setSymptoms(symptoms);
        record.setDiagnosis(diagnosis);
        record.setTreatmentPlan(treatmentPlan);
        record.setRecordDate(LocalDate.now());
        
        MedicalRecord savedRecord = medicalRecordRepository.save(record);
        return convertToDTO(savedRecord);
    }

    @Override
    public MedicalRecordDTO getMedicalRecordById(Long id) {
        return medicalRecordRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Medical record not found for ID: " + id));
    }

    @Override
    public List<MedicalRecordDTO> getMedicalRecordsByPatient(Long patientId) {
        return medicalRecordRepository.findByPatientId(patientId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<MedicalRecordDTO> getMedicalRecordsByDoctor(Long doctorId) {
        return medicalRecordRepository.findByDoctorId(doctorId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public MedicalRecordDTO updateMedicalRecord(Long id, String symptoms, String diagnosis, String treatmentPlan) {
        MedicalRecord record = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cannot update: Record not found for ID: " + id));
        
        record.setSymptoms(symptoms);
        record.setDiagnosis(diagnosis);
        record.setTreatmentPlan(treatmentPlan);
        
        MedicalRecord updatedRecord = medicalRecordRepository.save(record);
        return convertToDTO(updatedRecord);
    }

    // Helper: Entity -> DTO
    private MedicalRecordDTO convertToDTO(MedicalRecord record) {
        MedicalRecordDTO dto = new MedicalRecordDTO();
        dto.setId(record.getId());
        dto.setPatient(record.getPatient());
        dto.setDoctor(record.getDoctor());
        dto.setAppointment(record.getAppointment());
        dto.setSymptoms(record.getSymptoms());
        dto.setDiagnosis(record.getDiagnosis());
        dto.setTreatmentPlan(record.getTreatmentPlan());
        dto.setRecordDate(record.getRecordDate());
        return dto;
    }
<<<<<<< Updated upstream
}
=======
    
    public List<MedicalRecord> searchByDoctorAndKeyword(Long doctorId, String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            // If no keyword, you might want to return all for that doctor 
            // or handle it in the controller
            return medicalRecordRepository.findByDoctorId(doctorId); 
        }
        return medicalRecordRepository.searchByPatient(doctorId, keyword);
    }

	@Override
	public List<MedicalRecordDTO> getMedicalRecordsByPatientUsername(String username) {
		// TODO Auto-generated method stub
		return medicalRecordRepository
		           .findByPatientUsername(username)
		           .stream()
		           .map(this::convertToDTO)
		           .toList();
		}
	}

>>>>>>> Stashed changes
