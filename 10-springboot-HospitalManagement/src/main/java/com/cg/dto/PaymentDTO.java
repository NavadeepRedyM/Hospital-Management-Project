package com.cg.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public class PaymentDTO {

    private Long id;

    @NotNull(message = "Appointment ID is mandatory to link the payment")
    private Long appointmentId;

    @NotBlank(message = "Payment method must be selected")
    @Pattern(regexp = "^(UPI|CREDIT_CARD|DEBIT_CARD|CASH|NET_BANKING)$", 
             message = "Invalid payment method. Allowed: UPI, CREDIT_CARD, DEBIT_CARD, CASH, NET_BANKING")
    private String paymentMethod;

    @Positive(message = "Payment amount must be greater than zero")
    private double amount;

    @NotBlank(message = "Payment status is required")
    @Pattern(regexp = "^(PENDING|COMPLETED|FAILED)$", message = "Status must be PENDING, COMPLETED, or FAILED")
    private String status;

    // Transaction ID can be null for 'CASH' payments initially
    private String transactionId;

    @PastOrPresent(message = "Payment date cannot be in the future")
    private LocalDateTime paymentDate;
    @Positive(message ="Tax amount must be greater than zero")
    private double tax;
    
    private double total;

    // Constructors
    public PaymentDTO() {}

    public PaymentDTO(Long id, @NotNull(message = "Appointment ID is mandatory to link the payment") Long appointmentId,
			@NotBlank(message = "Payment method must be selected") @Pattern(regexp = "^(UPI|CREDIT_CARD|DEBIT_CARD|CASH|NET_BANKING)$", message = "Invalid payment method. Allowed: UPI, CREDIT_CARD, DEBIT_CARD, CASH, NET_BANKING") String paymentMethod,
			@Positive(message = "Payment amount must be greater than zero") double amount,
			@Positive(message = "Tax amount must be greater than zero") double tax, double total,
			@NotBlank(message = "Payment status is required") @Pattern(regexp = "^(PENDING|COMPLETED|FAILED)$", message = "Status must be PENDING, COMPLETED, or FAILED") String status,
			String transactionId,
			@PastOrPresent(message = "Payment date cannot be in the future") LocalDateTime paymentDate) {
		super();
		this.id = id;
		this.appointmentId = appointmentId;
		this.paymentMethod = paymentMethod;
		this.amount = amount;
		this.tax = tax;
		this.total = total;
		this.status = status;
		this.transactionId = transactionId;
		this.paymentDate = paymentDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAppointmentId() {
		return appointmentId;
	}

	public void setAppointmentId(Long appointmentId) {
		this.appointmentId = appointmentId;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public LocalDateTime getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(LocalDateTime paymentDate) {
		this.paymentDate = paymentDate;
	}

	public double getTax() {
		return tax;
	}

	public void setTax(double tax) {
		this.tax = tax;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

   
}
