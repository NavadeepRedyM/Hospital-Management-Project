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
import com.cg.repository.MedicalRecordRepository;

@Service
public class DoctorService implements IDoctorService {

    @Autowired
    private DoctorRepository doctorRepository;
    
    @Autowired
    private MedicalRecordRepository medicalRecordRepository;
    
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
            
            doctor.setName(doctorDTO.getName());
            doctor.setQualification(doctorDTO.getQualification());
            doctor.setYearsOfExperience(doctorDTO.getYearsOfExperience());
            
            // ✅ Consultation fee is now handled automatically via Department
            doctor.setDepartment(doctorDTO.getDepartment());

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
        
        // ✅ No setter needed: DTO's getConsultationFee() pulls from this department object
        dto.setDepartment(doctor.getDepartment());
        dto.setUser(doctor.getUser());
        
        dto.setAppointments(doctor.getAppointments());
        dto.setMedicalRecords(doctor.getMedicalRecords());
        return dto;
    }

    // Helper: DTO -> Entity
    private Doctor convertToEntity(DoctorDTO dto) {
        Doctor doctor = new Doctor();
        doctor.setId(dto.getId());
        doctor.setName(dto.getName());
        doctor.setQualification(dto.getQualification());
        doctor.setYearsOfExperience(dto.getYearsOfExperience());
        
        // ✅ Fee is not set here; it's inherited from this department
        doctor.setDepartment(dto.getDepartment());
        doctor.setUser(dto.getUser());
        
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
