package com.cg.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cg.dto.DepartmentDTO;
import com.cg.model.Department;
import com.cg.repository.DepartmentRepository;

@Service
public class DepartmentService implements IDepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public List<DepartmentDTO> getAllDepartments() {
        return departmentRepository.findAll().stream()
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
    public void deleteDepartment(Long id) {
        departmentRepository.deleteById(id);
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
