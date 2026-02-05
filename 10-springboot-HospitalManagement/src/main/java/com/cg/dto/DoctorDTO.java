package com.cg.dto;
 
import java.util.List;

import com.cg.model.Department;

import com.cg.model.Appointment;

import com.cg.model.MedicalRecord;

import com.cg.model.User;

import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.Min;

import jakarta.validation.constraints.Positive;
 
public class DoctorDTO {
 
    private Long id;
 
    @NotBlank(message = "Doctor name is required")

    private String name;
 
    @NotBlank(message = "Qualification is required")

    private String qualification;
 
    @Min(value = 0, message = "Experience cannot be negative")

    private int yearsOfExperience;
 
    @Positive(message = "Consultation fee must be greater than zero")

    private double consultationFee;
 
    private User user;
 
    private Department department;
 
    private List<Appointment> appointments;
 
    private List<MedicalRecord> medicalRecords;
 
    // Default Constructor

    public DoctorDTO() {

    }
 
    // Parameterized Constructor

    public DoctorDTO(Long id, String name, String qualification, int yearsOfExperience, 

                     double consultationFee, Department department, 

                     List<Appointment> appointments, List<MedicalRecord> medicalRecords, User user) {

        this.id = id;

        this.name = name;

        this.qualification = qualification;

        this.yearsOfExperience = yearsOfExperience;

        this.consultationFee = consultationFee;

        this.department = department;

        this.appointments = appointments;

        this.medicalRecords = medicalRecords;

        this.user = user;

    }
 
    // Getters and Setters

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }
 
    public String getName() { return name; }

    public void setName(String name) { this.name = name; }
 
    public String getQualification() { return qualification; }

    public void setQualification(String qualification) { this.qualification = qualification; }
 
    public int getYearsOfExperience() { return yearsOfExperience; }

    public void setYearsOfExperience(int yearsOfExperience) { this.yearsOfExperience = yearsOfExperience; }
 
    public double getConsultationFee() { return consultationFee; }

    public void setConsultationFee(double consultationFee) { this.consultationFee = consultationFee; }
 
    public Department getDepartment() { return department; }

    public void setDepartment(Department department) { this.department = department; }
 
    public List<Appointment> getAppointments() { return appointments; }

    public void setAppointments(List<Appointment> appointments) { this.appointments = appointments; }
 
    public List<MedicalRecord> getMedicalRecords() { return medicalRecords; }

    public void setMedicalRecords(List<MedicalRecord> medicalRecords) { this.medicalRecords = medicalRecords; }
 
    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

}

 