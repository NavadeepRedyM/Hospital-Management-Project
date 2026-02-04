package com.cg.service;
 
import java.util.List;
import com.cg.model.Appointment;
 
public interface IAppointmentService {
 
    List<Appointment> getAllAppointments();
 
    Appointment getAppointmentById(Long id);
 
    void deleteAppointment(Long id);
}