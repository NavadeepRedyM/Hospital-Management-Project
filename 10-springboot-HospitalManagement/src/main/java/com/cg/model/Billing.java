package com.cg.model;
 
import jakarta.persistence.*;

import java.time.LocalDate;
 
@Entity
@Table(name = "billing")
public class Billing {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    // One Billing -> One Appointment
    @OneToOne
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;
 
    // Many Bills -> One Patient
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;
 
    private double amount;
    private double tax;
    private double totalAmount;
    private String paymentStatus;
    private String paymentMethod;
 
    @Column(name = "billing_date")
    private LocalDate  billingDate;
    
    @PrePersist
    @PreUpdate
    public void calculateTaxAndTotal() {
        // Logic: 18% Tax Calculation
        this.tax = this.amount * 0.18;
        this.totalAmount = this.amount + this.tax;
    }
// Changed from java.util.Date

    public LocalDate getBillingDate() {
        return billingDate;
    }

    public void setBillingDate(LocalDate billingDate) {
        this.billingDate = billingDate;
    }
 
    // ---------- Constructors ----------
    public Billing() {
    }
 
    // ---------- Getters & Setters ----------
    public Long getId() {
        return id;
    }
 
    public void setId(Long id) {
        this.id = id;
    }
 
    public Appointment getAppointment() {
        return appointment;
    }
 
    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }
 
    public Patient getPatient() {
        return patient;
    }
 
    public void setPatient(Patient patient) {
        this.patient = patient;
    }
 
    public double getAmount() {
        return amount;
    }
 
    public void setAmount(double amount) {
        this.amount = amount;
    }
 
    public double getTax() {
        return tax;
    }
 
    public void setTax(double tax) {
        this.tax = tax;
    }
 
    public double getTotalAmount() {
        return totalAmount;
    }
 
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
 
    public String getPaymentStatus() {
        return paymentStatus;
    }
 
    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
 
    public String getPaymentMethod() {
        return paymentMethod;
    }
 
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

	public Billing(Long id, Appointment appointment, Patient patient, double amount, double tax, double totalAmount,
			String paymentStatus, String paymentMethod, LocalDate billingDate) {
		super();
		this.id = id;
		this.appointment = appointment;
		this.patient = patient;
		this.amount = amount;
		this.tax = tax;
		this.totalAmount = totalAmount;
		this.paymentStatus = paymentStatus;
		this.paymentMethod = paymentMethod;
		this.billingDate = billingDate;
	}
 
}
 