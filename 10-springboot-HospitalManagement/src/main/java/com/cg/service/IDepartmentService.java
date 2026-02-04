package com.cg.service;

import java.util.List;

import com.cg.model.Department;

public interface IDepartmentService {

	List<Department> getAllDepartments();
	Department getDepartmentById(Long id);
	Department saveDepartment(Department department);
	void deleteDepartment(Long id);
}
