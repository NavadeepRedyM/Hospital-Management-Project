package com.cg.model;

import java.time.LocalDate;
import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity

	public class Appointment {
	 
	    @Id

	    @GeneratedValue(strategy = GenerationType.IDENTITY)

	    private Long id;
	 
	    @ManyToOne

	    @JoinColumn(name = "patient_id")

	    private Patient patient; // Assuming a Patient entity exists
	 
	    @ManyToOne

	    @JoinColumn(name = "doctor_id")

	    private Doctor doctor; // Assuming a Doctor entity exists
	 
	    private LocalDate appointmentDate;

	    private String timeSlot;

	    private String status;

	    private String reasonForVisit;
	 
	    @OneToOne(mappedBy = "appointment")

	    private Billing billing; // Assuming a Billing entity exists
	 
	    @OneToOne(mappedBy = "appointment")

	    private MedicalRecord medicalRecord; // Assuming a MedicalRecord entity exists
	 
	    // Standard getters and setters
	    
	    
	    @ManyToOne
	    @JoinColumn(name = "department_id")
	    private Department department; // This stores the patient's choice


	    public Long getId() { 

	    	return id; }

	    public void setId(Long id) { 

	    	this.id = id; }

	    public Patient getPatient() { 

	    	return patient; }

	    public void setPatient(Patient patient) { 

	    	this.patient = patient; }

	    public Doctor getDoctor() { 

	    	return doctor; }

	    public void setDoctor(Doctor doctor) { 

	    	this.doctor = doctor; }

	    public LocalDate getAppointmentDate() { 

	    	return appointmentDate; }

	    public void setAppointmentDate(LocalDate date) {

	    	this.appointmentDate = date; }

	    public String getTimeSlot() { 

	    	return timeSlot; }

	    public void setTimeSlot(String timeSlot) {

	    	this.timeSlot = timeSlot; }

	    public String getStatus() { 

	    	return status; }

	    public void setStatus(String status) { 

	    	this.status = status; }

	    public String getReasonForVisit() { 

	    	return reasonForVisit; }

	    public void setReasonForVisit(String reasonForVisit) {

	    	this.reasonForVisit = reasonForVisit; }

	    public Billing getBilling() { 

	    	return billing; }

	    public void setBilling(Billing billing) {

	    	this.billing = billing; }

	    public MedicalRecord getMedicalRecord() {

	    	return medicalRecord; }

	    public void setMedicalRecord(MedicalRecord medicalRecord) { this.medicalRecord = medicalRecord; }

		public Department getDepartment() {
			return department;
		}

		public void setDepartment(Department department) {
			this.department = department;
		}

	    
	}

	 
