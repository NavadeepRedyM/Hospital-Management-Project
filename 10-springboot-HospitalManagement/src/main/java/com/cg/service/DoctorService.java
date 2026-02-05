package com.cg.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.dto.DoctorDTO;
import com.cg.model.Doctor;
import com.cg.repository.DoctorRepository;

@Service
public class DoctorService implements IDoctorService {

    @Autowired
    private DoctorRepository doctorRepository;
    
    @Override
    public DoctorDTO findDoctorById(Long id) {
        return doctorRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + id));
    }
    
    @Override
    public List<DoctorDTO> findAllDoctors() { // Renamed from getAllDoctors to match Interface
        return doctorRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<DoctorDTO> findDoctorsByQualification(String qualification) {
        return doctorRepository.findByQualification(qualification).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
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
    public DoctorDTO addDoctor(DoctorDTO doctorDTO) {
        Doctor doctor = convertToEntity(doctorDTO);
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
        doctor.setConsultationFee(dto.getConsultationFee());
        doctor.setUser(dto.getUser());
        doctor.setDepartment(dto.getDepartment());
        doctor.setAppointments(dto.getAppointments());
        doctor.setMedicalRecords(dto.getMedicalRecords());
        return doctor;
    }
}
