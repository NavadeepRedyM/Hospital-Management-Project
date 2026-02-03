package com.cg.service;

import java.util.List;
import com.cg.model.Department;

public interface DepartmentService{
	
	Department addDepartment(Department department);
	List<Department> getAllDepartments();
	Department getDepartmentById(Long id);
	Department updateDepartment(Long id, Department department);
	void deleteDepartment(Long id);

}
