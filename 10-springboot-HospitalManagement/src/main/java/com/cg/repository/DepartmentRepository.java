package com.cg.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cg.model.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
	// In DepartmentRepository.java
	@Query("SELECT d FROM Department d WHERE d.active = true")
	List<Department> findAllActive();

}
