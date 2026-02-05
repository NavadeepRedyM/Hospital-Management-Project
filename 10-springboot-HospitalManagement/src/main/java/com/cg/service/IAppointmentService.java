package com.cg.service;

import com.cg.dto.AppointmentDTO;
import java.time.LocalDate;
import java.util.List;

public interface IAppointmentService {
    AppointmentDTO book(String username, Long departmentId, Long doctorId, LocalDate date, String time, String reason);
    List<AppointmentDTO> listForPatient(Long patientId);
    List<AppointmentDTO> getAllAppointments();
    AppointmentDTO getAppointmentById(Long id);
    void deleteAppointment(Long id);
}
