package com.cg.dto;

import java.util.List;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import com.cg.model.Doctor;

public class DepartmentDTO {

    private Long id;

    @NotBlank(message = "Department Name is Required")
    @Pattern(
        regexp = "[A-Za-z ]+$",
        message = "Department Name must contain only Letters")
    private String deptName;

    @NotBlank(message = "HOD Name is Required")
    @Pattern(
        regexp = "[A-Za-z ]+$",
        message = "HOD Name must contain only Letters")
    private String hodName;

    @NotBlank(message = "Location is Required")
    private String location;

    private String description;

    // Direct 1:1 list of Doctor entities as per your request
    private List<Doctor> doctors;

    // Default Constructor
    public DepartmentDTO() {
    }

    // Parameterized Constructor
    public DepartmentDTO(Long id, String deptName, String hodName, String location, String description, List<Doctor> doctors) {
        this.id = id;
        this.deptName = deptName;
        this.hodName = hodName;
        this.location = location;
        this.description = description;
        this.doctors = doctors;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDeptName() { return deptName; }
    public void setDeptName(String deptName) { this.deptName = deptName; }

    public String getHodName() { return hodName; }
    public void setHodName(String hodName) { this.hodName = hodName; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<Doctor> getDoctors() { return doctors; }
    public void setDoctors(List<Doctor> doctors) { this.doctors = doctors; }
}
