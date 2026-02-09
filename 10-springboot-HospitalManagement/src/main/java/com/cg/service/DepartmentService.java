package com.cg.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cg.dto.DepartmentDTO;
import com.cg.model.Department;
import com.cg.model.Doctor;
import com.cg.repository.AppointmentRepository;
import com.cg.repository.DepartmentRepository;

import jakarta.transaction.Transactional;

@Service
public class DepartmentService implements IDepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;
    
    @Autowired
    private AppointmentRepository appointmentRepository;

 // In DepartmentService.java
    @Override
    public List<DepartmentDTO> getAllDepartments() {
        // Change from findAll() to your new filtered method
        return departmentRepository.findAllActive().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    @Override
    public DepartmentDTO getDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Department not found"));
    }
    @Override
    @Transactional
    public void deleteDepartment(Long id) {
        Department dept = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        // 1. Safety Check: Still block if there are ACTIVE doctors
        boolean hasActiveDoctors = dept.getDoctors().stream()
                .anyMatch(Doctor::isActive);

        if (hasActiveDoctors) {
            throw new IllegalStateException("Cannot delete: " + dept.getDeptName() + " still has active doctors.");
        }

        // 2. SOFT DELETE: Simply mark as inactive
        // This allows all MedicalRecords, Payments, and Bills to stay perfectly linked!
        dept.setActive(false);
        
        departmentRepository.save(dept);
    }

    



    // Helper: DTO -> Entity
    private Department convertToEntity(DepartmentDTO dto) {
        Department department = new Department();
        department.setId(dto.getId()); 
        department.setDeptName(dto.getDeptName());
        department.setHodName(dto.getHodName());
        department.setLocation(dto.getLocation());
        department.setDescription(dto.getDescription());
        // ✅ Added mapping for fee
        department.setConsultationFee(dto.getConsultationFee()); 
        return department;
    }

    // Helper: Entity -> DTO
    private DepartmentDTO convertToDTO(Department dept) {
        return new DepartmentDTO(
            dept.getId(), 
            dept.getDeptName(), 
            dept.getHodName(), 
            dept.getLocation(), 
            dept.getDescription(), 
            dept.getConsultationFee(), // ✅ Added mapping for fee
            dept.getDoctors()
        );
    }

    @Override
    public DepartmentDTO addDepartment(DepartmentDTO departmentDTO) {
        Department department = convertToEntity(departmentDTO);
        return convertToDTO(departmentRepository.save(department));
    }

    @Override
    public DepartmentDTO updateDepartment(Long id, DepartmentDTO dto) {
        Department existing = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + id));
        
        existing.setDeptName(dto.getDeptName());
        existing.setHodName(dto.getHodName());
        existing.setLocation(dto.getLocation());
        existing.setDescription(dto.getDescription());
        // ✅ Added mapping for fee in update
        existing.setConsultationFee(dto.getConsultationFee()); 
        
        return convertToDTO(departmentRepository.save(existing));
    }
}
