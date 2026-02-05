package com.cg.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.dto.AppointmentDTO;
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
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

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
        // 1. Fetch Patient Entity using username from Security
        Patient patient = patientRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Patient not found: " + username));

        // 2. Fetch Doctor Entity using ID from the booking form
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found: " + doctorId));

        // 3. Create Appointment Entity
        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentDate(date);
        appointment.setTimeSlot(time);
        appointment.setReasonForVisit(reason);
        appointment.setStatus("PENDING_PAYMENT");

        // 4. Save and return as DTO
        Appointment savedAppointment = appointmentRepository.save(appointment);
        return convertToDTO(savedAppointment);
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
        dto.setAppointmentDate(appointment.getAppointmentDate());
        dto.setTimeSlot(appointment.getTimeSlot());
        dto.setStatus(appointment.getStatus());
        dto.setReasonForVisit(appointment.getReasonForVisit());
        dto.setBilling(appointment.getBilling());
        dto.setMedicalRecord(appointment.getMedicalRecord());
        return dto;
    }
}
