package com.cg.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

@Entity
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Doctor name is required")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;

    @NotBlank(message = "Qualification is required")
    private String qualification;

    @Min(value = 0, message = "Experience cannot be negative")
    @Max(value = 60, message = "Please enter a valid years of experience")
    private int yearsOfExperience;

    // ✅ REMOVED: private double consultationFee;
    // The field is removed from the database to ensure the Department is the "Single Source of Truth".

    @Valid 
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_name", referencedColumnName = "username")
    private User user;

    @NotNull(message = "Please select a department")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @OneToMany(mappedBy = "doctor", cascade = {CascadeType.PERSIST, CascadeType.MERGE}) 
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "doctor")
    private List<MedicalRecord> medicalRecords;
    
    @Column(columnDefinition = "boolean default true")
    private boolean active = true; 

    public Doctor() {
        super();
    }

    // ✅ UPDATED: Constructor removed consultationFee parameter
    public Doctor(Long id, String name, String qualification, int yearsOfExperience,
            Department department, List<Appointment> appointments, List<MedicalRecord> medicalRecords, User user) {
        super();
        this.id = id;
        this.name = name;
        this.qualification = qualification;
        this.yearsOfExperience = yearsOfExperience;
        this.department = department;
        this.appointments = appointments;
        this.medicalRecords = medicalRecords;
        this.user = user;
    }

    // ✅ UPDATED: Getter now pulls fee from Department
    public double getConsultationFee() {
        return (this.department != null) ? this.department.getConsultationFee() : 0.0;
    }

    // ✅ REMOVED: setConsultationFee (fees are now managed via Department Admin)

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getQualification() { return qualification; }
    public void setQualification(String qualification) { this.qualification = qualification; }

    public int getYearsOfExperience() { return yearsOfExperience; }
    public void setYearsOfExperience(int yearsOfExperience) { this.yearsOfExperience = yearsOfExperience; }

    public Department getDepartment() { return department; }
    public void setDepartment(Department department) { this.department = department; }

    public List<Appointment> getAppointments() { return appointments; }
    public void setAppointments(List<Appointment> appointments) { this.appointments = appointments; }

    public List<MedicalRecord> getMedicalRecords() { return medicalRecords; }
    public void setMedicalRecords(List<MedicalRecord> medicalRecords) { this.medicalRecords = medicalRecords; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
    
}
