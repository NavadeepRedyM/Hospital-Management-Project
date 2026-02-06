package com.cg.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cg.dto.DoctorDTO;
import com.cg.model.Doctor;
import com.cg.model.MedicalRecord;
import com.cg.repository.DoctorRepository;

@Service
public class DoctorService implements IDoctorService {

    @Autowired
    private DoctorRepository doctorRepository;
    
    @Autowired
    private com.cg.repository.MedicalRecordRepository medicalRecordRepository;
    
    @Override
    public DoctorDTO findDoctorById(Long id) {
        return doctorRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + id));
    }
    
    @Override
    public List<DoctorDTO> findAllDoctors() {
        return doctorRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public DoctorDTO addDoctor(DoctorDTO doctorDTO) {
        Doctor doctor;
        
        if (doctorDTO.getId() != null) {
            // UPDATE MODE
            doctor = doctorRepository.findById(doctorDTO.getId())
                    .orElseThrow(() -> new RuntimeException("Doctor not found"));
            
            // Update only personal info
            doctor.setName(doctorDTO.getName());
            doctor.setQualification(doctorDTO.getQualification());
            doctor.setYearsOfExperience(doctorDTO.getYearsOfExperience());
            doctor.setConsultationFee(doctorDTO.getConsultationFee());
            doctor.setDepartment(doctorDTO.getDepartment());

            // FIX: Don't replace the User object. Only update username if needed.
            // This preserves the existing password in the database.
            if (doctor.getUser() != null && doctorDTO.getUser() != null) {
                doctor.getUser().setUsername(doctorDTO.getUser().getUsername());
            }
        } else {
            // NEW DOCTOR MODE
            doctor = convertToEntity(doctorDTO);
            if (doctor.getUser() != null) {
                doctor.getUser().setRole("ROLE_DOCTOR");
            }
        }

        // Keep appointments and medicalRecords untouched to avoid the previous SQL error
        Doctor savedDoctor = doctorRepository.save(doctor);
        return convertToDTO(savedDoctor);
    }


    // Helper: Entity -> DTO
    private DoctorDTO convertToDTO(Doctor doctor) {
        DoctorDTO dto = new DoctorDTO();
        dto.setId(doctor.getId());
        dto.setName(doctor.getName());
        dto.setQualification(doctor.getQualification());
        dto.setYearsOfExperience(doctor.getYearsOfExperience());
        dto.setConsultationFee(doctor.getConsultationFee());
        dto.setUser(doctor.getUser());
        dto.setDepartment(doctor.getDepartment());
        
        // Only load these for viewing, not for saving back
        dto.setAppointments(doctor.getAppointments());
        dto.setMedicalRecords(doctor.getMedicalRecords());
        return dto;
    }

    // Helper: DTO -> Entity (Simplified to avoid the error)
    private Doctor convertToEntity(DoctorDTO dto) {
        Doctor doctor = new Doctor();
        doctor.setId(dto.getId());
        doctor.setName(dto.getName());
        doctor.setQualification(dto.getQualification());
        doctor.setYearsOfExperience(dto.getYearsOfExperience());
        doctor.setConsultationFee(dto.getConsultationFee());
        doctor.setUser(dto.getUser());
        doctor.setDepartment(dto.getDepartment());
        
        // DO NOT map lists here as it causes the constraint violation
        return doctor;
    }

    @Override
    public List<DoctorDTO> findDoctorsByQualification(String qualification) {
        return doctorRepository.findByQualification(qualification).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteDoctor(Long id) {
        if (!doctorRepository.existsById(id)) {
            throw new RuntimeException("Cannot delete. Doctor not found with id: " + id);
        }
        doctorRepository.deleteById(id);
    }

    @Override
    public DoctorDTO getDoctorByUsername(String username) {
        Doctor doctor = doctorRepository.findDoctorByUserName(username);
        return (doctor != null) ? convertToDTO(doctor) : null;
    }

    @Override
    @Transactional
    public void addMedicalRecord(MedicalRecord medicalRecord) {
        if (medicalRecord.getRecordDate() == null) {
            medicalRecord.setRecordDate(java.time.LocalDate.now());
        }
        medicalRecordRepository.save(medicalRecord);
    }
}
