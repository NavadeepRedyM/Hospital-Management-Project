package com.cg.servicetest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.cg.dto.MedicalRecordDTO;
import com.cg.model.Appointment;
import com.cg.model.Doctor;
import com.cg.model.MedicalRecord;
import com.cg.model.Patient;
import com.cg.repository.AppointmentRepository;
import com.cg.repository.DoctorRepository;
import com.cg.repository.MedicalRecordRepository;
import com.cg.repository.PatientRepository;
import com.cg.service.MedicalRecordService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class MedicalRecordTestService {

    @Mock private MedicalRecordRepository medicalRecordRepository;
    @Mock private PatientRepository patientRepository;
    @Mock private DoctorRepository doctorRepository;
    @Mock private AppointmentRepository appointmentRepository;

    @InjectMocks
    private MedicalRecordService medicalRecordService;

    private Patient mockPatient;
    private Doctor mockDoctor;
    private Appointment mockAppointment;
    private MedicalRecord mockRecord;

    @BeforeEach
    void setUp() {
        mockPatient = new Patient(); mockPatient.setId(1L);
        mockDoctor = new Doctor(); mockDoctor.setId(2L);
        mockAppointment = new Appointment(); mockAppointment.setId(3L);

        mockRecord = new MedicalRecord();
        mockRecord.setId(100L);
        mockRecord.setPatient(mockPatient);
        mockRecord.setDiagnosis("Common Cold");
    }

    /**
     * Test 1: Create Medical Record - Success
     */
    @Test
    void testCreateMedicalRecord_Success() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(mockPatient));
        when(doctorRepository.findById(2L)).thenReturn(Optional.of(mockDoctor));
        when(appointmentRepository.findById(3L)).thenReturn(Optional.of(mockAppointment));
        when(medicalRecordRepository.findByAppointmentId(3L)).thenReturn(Optional.empty());
        when(medicalRecordRepository.save(any(MedicalRecord.class))).thenReturn(mockRecord);

        MedicalRecordDTO result = medicalRecordService.createMedicalRecord(1L, 2L, 3L, "S", "D", "T");

        assertNotNull(result);
        assertEquals("Common Cold", result.getDiagnosis());
        verify(medicalRecordRepository).save(any(MedicalRecord.class));
    }
    /**
     * Negative Test : Create Medical Record - Patient Not Found
     * Verifies that the service fails early if the patient ID is invalid.
     */
    @Test
    void testCreateMedicalRecord_PatientNotFound_Negative() {
        when(patientRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            medicalRecordService.createMedicalRecord(99L, 2L, 3L, "S", "D", "T");
        });
        
        verify(medicalRecordRepository, never()).save(any());
    }

    /**
     * Test 2: Create Medical Record - Already Exists (Business Rule)
     */
    @Test
    void testCreateMedicalRecord_ThrowsExceptionIfRecordExists() {
        when(patientRepository.findById(anyLong())).thenReturn(Optional.of(mockPatient));
        when(doctorRepository.findById(anyLong())).thenReturn(Optional.of(mockDoctor));
        when(appointmentRepository.findById(3L)).thenReturn(Optional.of(mockAppointment));
        when(medicalRecordRepository.findByAppointmentId(3L)).thenReturn(Optional.of(mockRecord));

        assertThrows(RuntimeException.class, () -> {
            medicalRecordService.createMedicalRecord(1L, 2L, 3L, "S", "D", "T");
        }, "Should throw exception if record already exists for appointment ID");
    }
    
    /**
     * Test 3: Get Records by Patient ID
     */
    @Test
    void testGetMedicalRecordsByPatient_ReturnsList() {
        List<MedicalRecord> records = Arrays.asList(mockRecord);
        when(medicalRecordRepository.findByPatientId(1L)).thenReturn(records);

        List<MedicalRecordDTO> result = medicalRecordService.getMedicalRecordsByPatient(1L);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(medicalRecordRepository).findByPatientId(1L);
    }
    /**
     * Negative Test : Update Medical Record - Record ID Does Not Exist
     */
    @Test
    void testUpdateMedicalRecord_IdNotFound_Negative() {
        when(medicalRecordRepository.findById(500L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            medicalRecordService.updateMedicalRecord(500L, "S", "D", "T");
        });

        verify(medicalRecordRepository, never()).save(any());
    }
    
    /**
     * Test 4: Update Medical Record - Success
     */
    @Test
    void testUpdateMedicalRecord_Success() {
        when(medicalRecordRepository.findById(100L)).thenReturn(Optional.of(mockRecord));
        when(medicalRecordRepository.save(any(MedicalRecord.class))).thenReturn(mockRecord);

        MedicalRecordDTO result = medicalRecordService.updateMedicalRecord(100L, "New Symptoms", "Updated Diagnosis", "Plan A");

        assertNotNull(result);
        assertEquals("Updated Diagnosis", mockRecord.getDiagnosis()); // Verify entity was modified
        verify(medicalRecordRepository).save(mockRecord);
    }
}

