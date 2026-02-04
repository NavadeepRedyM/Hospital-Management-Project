package com.cg.service;
 
import java.util.List;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 
import com.cg.model.Appointment;
import com.cg.repository.AppointmentRepository;
 
@Service   
public class AppointmentService implements IAppointmentService {
 
    @Autowired
    private AppointmentRepository appointmentRepository;
 
    @Override
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }
 
    @Override
    public Appointment getAppointmentById(Long id) {
        return appointmentRepository.findById(id).orElse(null);
    }
 
    @Override
    public void deleteAppointment(Long id) {
        appointmentRepository.deleteById(id);
    }
}
 