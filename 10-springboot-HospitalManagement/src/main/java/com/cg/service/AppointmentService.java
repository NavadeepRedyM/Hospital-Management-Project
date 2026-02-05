package com.cg.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.model.Appointment;
import com.cg.model.Doctor;
import com.cg.model.Patient;
import com.cg.repository.AppointmentRepository;
import com.cg.repository.DoctorRepository;
import com.cg.repository.PatientRepository;

@Service
public class AppointmentService implements IAppointmentService {

	@Autowired
    private AppointmentRepository appointmentRepository;
	
	@Autowired
	PatientRepository patientRepository;
	
	@Autowired
	DoctorRepository doctorRepository;
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
      @Override
    public Appointment book(String username, Long departmentId, Long doctorId, LocalDate date, String time, String reason) {
        // 1. Fetch Patient
        Patient patient = patientRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Patient not found: " + username));

        // 2. Fetch Doctor
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found: " + doctorId));

        // 3. Create Appointment using your model's setters
        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentDate(date);
        appointment.setTimeSlot(time);
        appointment.setReasonForVisit(reason);// Matches your private String timeSlot
        appointment.setStatus("PENDING_PAYMENT");

        return appointmentRepository.save(appointment);
    }


	@Override
	public List<Appointment> listForPatient(Long patientId) {
		// TODO Auto-generated method stub
		return appointmentRepository.findByPatientId(patientId);
	}

}
