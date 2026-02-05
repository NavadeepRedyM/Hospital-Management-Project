package com.cg.service;

import java.util.List;
import com.cg.dto.DepartmentDTO;

public interface IDepartmentService {
    List<DepartmentDTO> getAllDepartments();
    DepartmentDTO getDepartmentById(Long id);
    
    // Split these for better logic handling in the controller
    DepartmentDTO addDepartment(DepartmentDTO departmentDTO);
    DepartmentDTO updateDepartment(Long id, DepartmentDTO departmentDTO);
    
    void deleteDepartment(Long id);
}
