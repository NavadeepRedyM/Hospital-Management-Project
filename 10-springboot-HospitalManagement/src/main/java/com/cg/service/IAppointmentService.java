package com.cg.service;

import com.cg.model.Appointment;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


public interface IAppointmentService {

	Appointment book(String username, Long departmentId, Long doctorId, LocalDate date, String time, String reason);
    List<Appointment> listForPatient(Long patientId);
    List<Appointment> getAllAppointments();
    
    // Add these so the implementation class doesn't fail
    Appointment getAppointmentById(Long id);
    void deleteAppointment(Long id);
}
