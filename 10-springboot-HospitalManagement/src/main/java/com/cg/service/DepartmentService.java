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

    // Helper: DTO -> Entity (CRITICAL: MUST SET ID FOR UPDATE)
    private Department convertToEntity(DepartmentDTO dto) {
        Department department = new Department();
        department.setId(dto.getId()); // If this is null, Hibernate does INSERT. If present, UPDATE.
        department.setDeptName(dto.getDeptName());
        department.setHodName(dto.getHodName());
        department.setLocation(dto.getLocation());
        department.setDescription(dto.getDescription());
        return department;
    }

    private DepartmentDTO convertToDTO(Department dept) {
        return new DepartmentDTO(dept.getId(), dept.getDeptName(), dept.getHodName(), 
                                 dept.getLocation(), dept.getDescription(), dept.getDoctors());
    }

    @Override
    public DepartmentDTO addDepartment(DepartmentDTO departmentDTO) {
        // Logic for creating a brand new department
        Department department = convertToEntity(departmentDTO);
        return convertToDTO(departmentRepository.save(department));
    }

    @Override
    public DepartmentDTO updateDepartment(Long id, DepartmentDTO dto) {
        // 1. Fetch existing entity to ensure we don't lose linked data (like doctors)
        Department existing = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + id));
        
        // 2. Map updated values from DTO to the existing Entity
        existing.setDeptName(dto.getDeptName());
        existing.setHodName(dto.getHodName());
        existing.setLocation(dto.getLocation());
        existing.setDescription(dto.getDescription());
        
        // 3. Save and return DTO
        return convertToDTO(departmentRepository.save(existing));
    }

}
