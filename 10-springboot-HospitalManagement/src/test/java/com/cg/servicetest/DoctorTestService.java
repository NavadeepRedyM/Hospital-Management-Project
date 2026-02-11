package com.cg.servicetest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cg.dto.DoctorDTO;
import com.cg.model.Appointment;
import com.cg.model.Department;
import com.cg.model.Doctor;
import com.cg.model.User;
import com.cg.repository.DoctorRepository;
import com.cg.repository.MedicalRecordRepository;
import com.cg.service.DoctorService;

@ExtendWith(MockitoExtension.class)
public class DoctorTestService {

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private MedicalRecordRepository medicalRecordRepository;

    @InjectMocks
    private DoctorService doctorService;

    private Doctor mockDoctor;
    private Department mockDept;

    @BeforeEach
    void setUp() {
        mockDept = new Department();
        mockDept.setDeptName("Neurology");
        mockDept.setConsultationFee(1000.0);
        mockDept.setDoctors(new ArrayList<>());

        mockDoctor = new Doctor();
        mockDoctor.setId(10L);
        mockDoctor.setName("Dr. Strange");
        mockDoctor.setQualification("MBBS");
        mockDoctor.setDepartment(mockDept);
        mockDoctor.setAppointments(new ArrayList<>());
        mockDoctor.setActive(true);
        
        User user = new User();
        user.setEnabled(true);
        mockDoctor.setUser(user);
    }

    /**
     * Test 1: findDoctorById - Verifies DTO conversion and field mapping
     */
    @Test
    void testFindDoctorById_Success() {
        when(doctorRepository.findById(10L)).thenReturn(Optional.of(mockDoctor));

        DoctorDTO result = doctorService.findDoctorById(10L);

        assertNotNull(result);
        assertEquals("Dr. Strange", result.getName());
        assertEquals(1000.0, result.getConsultationFee()); // Pulled from Department
        verify(doctorRepository).findById(10L);
    }
    
    /**
     * Negative Test : Get Doctor By Username - Invalid Username
     */
    @Test
    void testGetDoctorByUsername_NotFound_Negative() {
        // Arrange
        when(doctorRepository.findDoctorByUserName("ghost_user")).thenReturn(null);

        // Act
        DoctorDTO result = doctorService.getDoctorByUsername("ghost_user");

        // Assert
        assertNull(result, "Should return null if username is not found");
    }
    

    /**
     * Test 2: Soft Delete - Verifies Deactivation and User disablement
     */
    @Test
    void testDeleteDoctor_SoftDeleteFlow() {
        when(doctorRepository.findById(10L)).thenReturn(Optional.of(mockDoctor));

        doctorService.deleteDoctor(10L);

        assertFalse(mockDoctor.isActive(), "Doctor should be inactive");
        assertFalse(mockDoctor.getUser().isEnabled(), "User account should be disabled");
        verify(doctorRepository).save(mockDoctor);
    }

    /**
     * Test 3: Delete Failure - Doctor has PENDING appointments
     */
    @Test
    void testDeleteDoctor_ThrowsExceptionWhenAppointmentsActive() {
        Appointment activeAppt = new Appointment();
        activeAppt.setStatus("PENDING");
        mockDoctor.setAppointments(Arrays.asList(activeAppt));

        when(doctorRepository.findById(10L)).thenReturn(Optional.of(mockDoctor));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            doctorService.deleteDoctor(10L);
        });

        assertTrue(exception.getMessage().contains("active appointments"));
        verify(doctorRepository, never()).save(any());
    }
    
    /**
     * Negative Test : Delete Doctor - ID Does Not Exist
     */
    @Test
    void testDeleteDoctor_NotFound_Negative() {
        // Arrange
        when(doctorRepository.findById(10L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            doctorService.deleteDoctor(10L);
        });

        // Verify no save was attempted
        verify(doctorRepository, never()).save(any());
    }
     

    /**
     * Test 4: addDoctor Update Mode - Verifies fields are updated
     */
    @Test
    void testAddDoctor_UpdateExisting() {
        DoctorDTO updateDto = new DoctorDTO();
        updateDto.setId(10L);
        updateDto.setName("Dr. Stephen Strange");
        updateDto.setQualification("PhD");

        when(doctorRepository.findById(10L)).thenReturn(Optional.of(mockDoctor));
        when(doctorRepository.save(any(Doctor.class))).thenReturn(mockDoctor);

        DoctorDTO result = doctorService.addDoctor(updateDto);

        assertEquals("Dr. Stephen Strange", mockDoctor.getName());
        assertEquals("PhD", mockDoctor.getQualification());
        verify(doctorRepository).save(mockDoctor);
    }
}
