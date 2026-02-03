package com.cg.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.model.Department;
import com.cg.repository.DepartmentRepository;

@Service
public class DepartmentServiceImpl implements DepartmentService {
	
	@Autowired
	private DepartmentRepository departmentRepository;
	
	@Override
	public Department addDepartment(Department department) {
		return departmentRepository.save(department);
	}
	@Override
	public  List<Department>getAllDepartments(){
    return departmentRepository.findAll();
	}
	@Override
	public Department updateDepartment(Long id,Department department) {
		Department existing = departmentRepository.findById(id).orElse(null);
		if (existing != null) {
			existing.setDeptName(department.getDeptName());
			existing.setHodName(department.getHodName());
			existing.setLocation(department.getLocation());
			existing.setDescription(department.getDescription());
		return departmentRepository.save(existing);
		}
		return null;
	}
	@Override
	public void deleteDepartment(Long id) {
		departmentRepository.deleteById(id);
	}
	@Override
	public Department getDepartmentById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
