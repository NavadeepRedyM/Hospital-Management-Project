package com.cg.model;

import java.util.List;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "department")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Department Name is Required")
    @Pattern(regexp = "[A-Za-z ]+$", message = "Department Name must contain only Letters")
    @Column(nullable = false)
    private String DeptName;
    
    @NotBlank(message = "HOD Name is Required")
    @Pattern(regexp = "[A-Za-z ]+$", message = "HOD Name must contain only Letters")
    @Column(nullable = false)
    private String HodName;
    
    @NotBlank(message = "Location is Required")
    @Column(nullable = false)
    private String Location;
    
    private String Description;

    // --- NEW FIELD ADDED HERE ---
    @Positive(message = "Consultation fee must be greater than zero")
    @Column(nullable = false)
    private double consultationFee;

    @OneToMany(mappedBy = "department",cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Doctor> doctors;
    @Column(columnDefinition = "boolean default true")
    private boolean active = true;

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public Department() {
        super();
    }

    // --- Updated Getters and Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDeptName() { return DeptName; }
    public void setDeptName(String deptName) { DeptName = deptName; }

    public String getHodName() { return HodName; }
    public void setHodName(String hodName) { HodName = hodName; }

    public String getLocation() { return Location; }
    public void setLocation(String location) { Location = location; }

    public String getDescription() { return Description; }
    public void setDescription(String description) { Description = description; }

    public double getConsultationFee() { return consultationFee; }
    public void setConsultationFee(double consultationFee) { this.consultationFee = consultationFee; }

    public List<Doctor> getDoctors() { return doctors; }
    public void setDoctors(List<Doctor> doctors) { this.doctors = doctors; }
}
