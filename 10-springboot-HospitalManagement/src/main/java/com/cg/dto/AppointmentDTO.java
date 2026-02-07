package com.cg.dto;

import java.time.LocalDate;
import com.cg.model.Patient;
import com.cg.model.Payment;
import com.cg.model.Doctor;
import com.cg.model.Billing;
import com.cg.model.Department;
import com.cg.model.MedicalRecord;

public class AppointmentDTO {

    private Long id;
    private Patient patient;
    private Doctor doctor;
    private LocalDate appointmentDate;
    private String timeSlot;
    private String status;
    private String reasonForVisit;
    private Billing billing;
    private MedicalRecord medicalRecord;
    private Payment payment; 
    private Department department; 
    
    // ✅ ADDED THIS FIELD TO RESOLVE THE ERROR
    private String paymentMethod; 

    // Default Constructor
    public AppointmentDTO() {
    }

    // Parameterized Constructor
    public AppointmentDTO(Long id, Patient patient, Doctor doctor, LocalDate appointmentDate, 
                          String timeSlot, String status, String reasonForVisit, 
                          Billing billing, MedicalRecord medicalRecord, String paymentMethod) {
        this.id = id;
        this.patient = patient;
        this.doctor = doctor;
        this.appointmentDate = appointmentDate;
        this.timeSlot = timeSlot;
        this.status = status;
        this.reasonForVisit = reasonForVisit;
        this.billing = billing;
        this.medicalRecord = medicalRecord;
        this.paymentMethod = paymentMethod;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }

    public Doctor getDoctor() { return doctor; }
    public void setDoctor(Doctor doctor) { this.doctor = doctor; }

    public LocalDate getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(LocalDate appointmentDate) { this.appointmentDate = appointmentDate; }

    public String getTimeSlot() { return timeSlot; }
    public void setTimeSlot(String timeSlot) { this.timeSlot = timeSlot; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getReasonForVisit() { return reasonForVisit; }
    public void setReasonForVisit(String reasonForVisit) { this.reasonForVisit = reasonForVisit; }

    public Billing getBilling() { return billing; }
    public void setBilling(Billing billing) { this.billing = billing; }

    public MedicalRecord getMedicalRecord() { return medicalRecord; }
    public void setMedicalRecord(MedicalRecord medicalRecord) { this.medicalRecord = medicalRecord; }

    public Department getDepartment() { return department; }
    public void setDepartment(Department department) { this.department = department; }
    
    public Payment getPayment() { return payment; }
    public void setPayment(Payment payment) { this.payment = payment; }

    // ✅ ADDED GETTER AND SETTER FOR paymentMethod
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
}
