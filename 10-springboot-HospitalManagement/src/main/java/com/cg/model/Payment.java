	package com.cg.model;
	
	import jakarta.persistence.*;
	import java.time.LocalDateTime;
	
	@Entity
	@Table(name = "payments")
	public class Payment {
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generated ID
	    private Long id;
	
	    // Link to the specific appointment being paid for
	    @OneToOne(optional = false)
	    @JoinColumn(name = "appointment_id", nullable = false)
	    private Appointment appointment;
	
	    @Column(nullable = false)
	    private String paymentMethod; // UPI, CARD, CASH, etc.
	
	    @Column(nullable = false)
	    private double amount;
	
	    private String transactionId; // Reference for online payments
	
	    @Column(nullable = false)
	    private String status; // PENDING, COMPLETED, FAILED
	
	    @Column(name = "payment_date")
	    private LocalDateTime paymentDate;
	
	    // Default Constructor
	    public Payment() {
	        this.paymentDate = LocalDateTime.now();
	        this.status = "PENDING";
	    }
	
	    // Getters and Setters
	    public Long getId() { return id; }
	    public void setId(Long id) { this.id = id; }
	
	    public Appointment getAppointment() { return appointment; }
	    public void setAppointment(Appointment appointment) { this.appointment = appointment; }
	
	    public String getPaymentMethod() { return paymentMethod; }
	    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
	
	    public double getAmount() { return amount; }
	    public void setAmount(double amount) { this.amount = amount; }
	
	    public String getTransactionId() { return transactionId; }
	    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
	
	    public String getStatus() { return status; }
	    public void setStatus(String status) { this.status = status; }
	
	    public LocalDateTime getPaymentDate() { return paymentDate; }
	    public void setPaymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; }
	}
