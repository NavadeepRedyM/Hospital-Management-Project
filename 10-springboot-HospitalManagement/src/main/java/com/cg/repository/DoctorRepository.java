package com.cg.repository;

import org.springframework.stereotype.Repository;

import com.cg.model.Doctor;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long>
{

	Doctor findByName(String name);
	List<Doctor> findByQualification(String qualification);
	 @Query("SELECT d FROM Doctor d WHERE d.user.username = :uname")
	    Doctor findDoctorByUserName(@Param("uname") String username);
	List<Doctor>findByDepartmentId(Long departmentId);
	@Query("SELECT d FROM Doctor d WHERE d.active = true")
	List<Doctor> findAllActive();


}
