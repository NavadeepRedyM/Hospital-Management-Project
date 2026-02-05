package com.cg.dto;

import java.time.LocalDate;

public class BookAppointmentForm {
	private Long departmentId;
    private Long doctorId;
    private LocalDate date;
    private String time; // Maps to your timeSlot in Entity
    private String reason;
        // ... existing fields ...
        private String paymentMethod; // Add this

        public String getPaymentMethod() { return paymentMethod; }
        public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    


    // Getters and Setters
    public Long getDepartmentId() { return departmentId; }
    public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }

    public Long getDoctorId() { return doctorId; }
    public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }


}
