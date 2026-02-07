package com.cg.service;

import com.cg.dto.AppointmentDTO;
import com.cg.dto.PaymentDTO;
import com.cg.model.Billing;

import java.time.LocalDate;
import java.util.List;

public interface IAppointmentService {
    AppointmentDTO book(String username, Long departmentId, Long doctorId, LocalDate date, String time, String reason);
    List<AppointmentDTO> listForPatient(Long patientId);
    List<AppointmentDTO> getAllAppointments();
    AppointmentDTO getAppointmentById(Long id);
    void deleteAppointment(Long id);
    
    // ✅ Add this for Admin Allotment
    void assignDoctorToAppointment(Long appointmentId, Long doctorId);
    void finalizeBookingWithPayment(PaymentDTO paymentDto);
	List<Billing> getBillsByPatientId(Long id);
}
