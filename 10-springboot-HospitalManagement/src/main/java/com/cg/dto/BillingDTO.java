package com.cg.dto;

import java.time.LocalDate;
import jakarta.validation.constraints.*;

public class BillingDTO {

    private Long id;

    @NotNull(message = "Appointment ID is required")
    private Long appointmentId;

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    @Positive(message = "Amount must be positive")
    private double amount;

    // Tax and TotalAmount are now calculated fields
    private double tax;
    private double totalAmount;

    @NotBlank(message = "Payment status is required")
    private String paymentStatus; // PAID, PENDING, CANCELLED

    @NotBlank(message = "Payment method is required")
    private String paymentMethod; // UPI, CARD, CASH

    @NotNull(message = "Billing date is required")
    @PastOrPresent(message = "Billing date cannot be in the future")
    private LocalDate billingDate;

    // Default Constructor
    public BillingDTO() {
    }

    // Parameterized Constructor
    public BillingDTO(Long id, Long appointmentId, Long patientId, double amount, 
                      String paymentStatus, String paymentMethod, LocalDate billingDate) {
        this.id = id;
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.amount = amount;
        // Logic: Calculate tax and total based on the 18% rule
        this.tax = amount * 0.18;
        this.totalAmount = amount + this.tax;
        this.paymentStatus = paymentStatus;
        this.paymentMethod = paymentMethod;
        this.billingDate = billingDate;
    }

    // ---------- Getters and Setters ----------
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Long appointmentId) { this.appointmentId = appointmentId; }

    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { 
        this.amount = amount;
        // Keep DTO values synced with entity logic
        this.tax = amount * 0.18;
        this.totalAmount = amount + this.tax;
    }

    public double getTax() { return tax; }
    public void setTax(double tax) { this.tax = tax; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public LocalDate getBillingDate() { return billingDate; }
    public void setBillingDate(LocalDate billingDate) { this.billingDate = billingDate; }
}
