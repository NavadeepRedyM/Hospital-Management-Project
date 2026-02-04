package com.cg.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "department")
public class Department {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "Department Name is Required")
	@Pattern(
			regexp = "[A-Za-z ]+$",
			message = "Department Name must conatin only Letters")
	@Column (nullable = false)
	private String DeptName;
	
	@NotBlank(message = "HOD Name is Required")
	@Pattern(
			regexp = "[A-Za-z ]+$",
			message = "HOD Name must conatin only Letters")
	@Column (nullable = false)
	private String HodName;
	
	@NotBlank(message = "Department Name is Required")
	@Column (nullable = false)
	private String Location;
	
	
	private String Description;
	
	@OneToMany(mappedBy = "department",cascade = CascadeType.ALL)
  private List<Doctor>doctors;

	public Department() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDeptName() {
		return DeptName;
	}

	public void setDeptName(String deptName) {
		DeptName = deptName;
	}

	public String getHodName() {
		return HodName;
	}

	public void setHodName(String hodName) {
		HodName = hodName;
	}

	public String getLocation() {
		return Location;
	}

	public void setLocation(String location) {
		Location = location;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public List<Doctor> getDoctors() {
		return doctors;
	}

	public void setDoctors(List<Doctor> doctors) {
		this.doctors = doctors;
	}
	
}
