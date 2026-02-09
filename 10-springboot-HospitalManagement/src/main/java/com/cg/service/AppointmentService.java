package com.cg.service;
 
import java.time.LocalDate;

import java.util.List;

import java.util.stream.Collectors;
 
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
 
import com.cg.dto.AppointmentDTO;

import com.cg.dto.PaymentDTO;

import com.cg.model.Appointment;

import com.cg.model.Billing;

import com.cg.model.Department;

import com.cg.model.Doctor;

import com.cg.model.Patient;

import com.cg.model.Payment;

import com.cg.repository.AppointmentRepository;

import com.cg.repository.BillingRepository;

import com.cg.repository.DepartmentRepository;

import com.cg.repository.DoctorRepository;

import com.cg.repository.PatientRepository;

import com.cg.repository.PaymentRepository;
 
import jakarta.transaction.Transactional;
 
@Service

public class AppointmentService implements IAppointmentService {
 
    @Autowired

    private AppointmentRepository appointmentRepository;
 
    @Autowired

    private PatientRepository patientRepository;
 
    @Autowired

    private DoctorRepository doctorRepository;

    @Autowired

    private DepartmentRepository departmentRepository;

    @Autowired

    private PaymentRepository paymentRepository;
 
    @Autowired

    private BillingRepository billingRepository;
 
 
    @Override

    public List<AppointmentDTO> getAllAppointments() {

        return appointmentRepository.findAll().stream()

                .map(this::convertToDTO)

                .collect(Collectors.toList());

    }
 
    @Override

    public AppointmentDTO getAppointmentById(Long id) {

        return appointmentRepository.findById(id)

                .map(this::convertToDTO)

                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + id));

    }
 
    @Override

    public void deleteAppointment(Long id) {

        if (!appointmentRepository.existsById(id)) {

            throw new RuntimeException("Cannot delete. Appointment not found with id: " + id);

        }

        appointmentRepository.deleteById(id);

    }
 
    @Override

    public AppointmentDTO book(String username, Long departmentId, Long doctorId, LocalDate date, String time, String reason) {

        Patient patient = patientRepository.findByUsername(username)

                .orElseThrow(() -> new RuntimeException("Patient not found"));
 
        Appointment appointment = new Appointment();

        appointment.setPatient(patient);

        // ✅ SAVE THE DEPARTMENT (Crucial for Admin filtering)

        Department dept = departmentRepository.findById(departmentId)

                .orElseThrow(() -> new RuntimeException("Department not found"));

        appointment.setDepartment(dept);
 
        appointment.setAppointmentDate(date);

        appointment.setTimeSlot(time);

        appointment.setReasonForVisit(reason);

        // doctorId will be null from your current form

        appointment.setDoctor(null); 

        appointment.setStatus("PENDING_ALLOTMENT");
 
        return convertToDTO(appointmentRepository.save(appointment));

    }
 
 
    @Override

    public List<AppointmentDTO> listForPatient(Long patientId) {

        // Fetches list of entities and transforms them into DTOs

        return appointmentRepository.findByPatientId(patientId).stream()

                .map(this::convertToDTO)

                .collect(Collectors.toList());

    }
 
    /**

     * Helper Method: Entity -> DTO conversion

     * Prevents circular dependency issues in JSON serialization.

     */

    private AppointmentDTO convertToDTO(Appointment appointment) {

        AppointmentDTO dto = new AppointmentDTO();

        dto.setId(appointment.getId());

        dto.setPatient(appointment.getPatient());

        dto.setDoctor(appointment.getDoctor());

        dto.setDepartment(appointment.getDepartment()); 

        dto.setAppointmentDate(appointment.getAppointmentDate());

        dto.setTimeSlot(appointment.getTimeSlot());

        dto.setStatus(appointment.getStatus());

        dto.setReasonForVisit(appointment.getReasonForVisit());

        dto.setBilling(appointment.getBilling());

        dto.setMedicalRecord(appointment.getMedicalRecord());

        return dto;

    }

    @Override

    public void assignDoctorToAppointment(Long appointmentId, Long doctorId) {

        // 1. Find the appointment

        Appointment appointment = appointmentRepository.findById(appointmentId)

                .orElseThrow(() -> new RuntimeException("Appointment not found: " + appointmentId));
 
        // 2. Find the doctor

        Doctor doctor = doctorRepository.findById(doctorId)

                .orElseThrow(() -> new RuntimeException("Doctor not found: " + doctorId));
 
        // 3. Perform Allotment

        appointment.setDoctor(doctor);

        appointment.setStatus("CONFIRMED");
 
        // 4. Save

        appointmentRepository.save(appointment);

    }

    @Override

    @Transactional

    public void finalizeBookingWithPayment(PaymentDTO paymentDto) {

        // 1. Fetch the Appointment

        Appointment appointment = appointmentRepository.findById(paymentDto.getAppointmentId())

                .orElseThrow(() -> new RuntimeException("Appointment not found"));
 
        // 2. Create and save the Payment record

        Payment payment = new Payment();

        payment.setAppointment(appointment);

        payment.setPaymentMethod(paymentDto.getPaymentMethod());

        payment.setAmount(paymentDto.getAmount());

        payment.setStatus("COMPLETED");

        payment.setPaymentDate(java.time.LocalDateTime.now());

        paymentRepository.save(payment);
 
        // 3. Update Appointment Status

        // Since payment is successful, move from AWAITING_PAYMENT to CONFIRMED

        appointment.setStatus("CONFIRMED");

        appointmentRepository.save(appointment);
 
        // 4. Automatically generate a Billing record (Financial history)

       Billing bill = new Billing();

        bill.setAppointment(appointment);

        bill.setPatient(appointment.getPatient());

        bill.setAmount(paymentDto.getAmount());

        bill.setTotalAmount(paymentDto.getAmount()); // Add tax logic here if needed

        bill.setPaymentStatus("PAID");

        bill.setPaymentMethod(paymentDto.getPaymentMethod());

        bill.setBillingDate(java.time.LocalDate.now());

        billingRepository.save(bill);

    }

    public List<Billing> getBillsByPatientId(Long patientId) {

        return billingRepository.findByPatientId(patientId);

    }
 
 
}

 