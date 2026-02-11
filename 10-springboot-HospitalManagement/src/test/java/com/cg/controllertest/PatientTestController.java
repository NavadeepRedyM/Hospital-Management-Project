package com.cg.controllertest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;

import com.cg.controller.PatientController;
import com.cg.dto.PatientDTO;
import com.cg.service.IAppointmentService;
import com.cg.service.IPatientService;
import com.cg.service.MedicalRecordService;
import com.cg.service.DepartmentService;

@ExtendWith(MockitoExtension.class)
public class PatientTestController {

    @Mock
    private IPatientService patientService;

    @Mock
    private IAppointmentService appointmentService;

    @Mock
    private DepartmentService departmentService;

    @Mock
    private MedicalRecordService medicalRecordService;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private PatientController patientController;

    private Model model;
    private PatientDTO mockPatient;

    @BeforeEach
    void setUp() {
        model = new BindingAwareModelMap(); // Plain Spring Model implementation
        mockPatient = new PatientDTO();
        mockPatient.setId(1L);
        mockPatient.setUsername("testuser");
    }

    /**
     * Test 1: Dashboard Logic
     */
    @Test
    void testPatientDashboard_ReturnsCorrectView() {
        // Arrange
        when(userDetails.getUsername()).thenReturn("testuser");
        when(patientService.findByUsername("testuser")).thenReturn(Optional.of(mockPatient));

        // Act
        String viewName = patientController.patientDashboard(userDetails, model);

        // Assert
        assertEquals("hospital/patient-index", viewName);
        assertEquals(mockPatient, model.getAttribute("patient"));
        verify(patientService).findByUsername("testuser");
    }
    /**
     * Negative Test : Profile View - Missing UserDetails
     * Verifies the controller's behavior when the security context is null.
     */
    @Test
    void testViewProfile_NullUser_Negative() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            patientController.viewProfile(null, model);
        });
    }


    /**
     * Test 2: Profile Retrieval
     */
    @Test
    void testViewProfile_Success() {
        // Arrange
        when(userDetails.getUsername()).thenReturn("testuser");
        when(patientService.findByUsername("testuser")).thenReturn(Optional.of(mockPatient));

        // Act
        String viewName = patientController.viewProfile(userDetails, model);

        // Assert
        assertEquals("hospital/patient-profile", viewName);
        assertEquals(mockPatient, model.getAttribute("patient"));
    }
    /**
     * Negative Test 6: Cancel Payment - Non-existent Appointment ID
     * Verifies that the service still attempts deletion but fails gracefully if logic allows.
     */
    @Test
    void testCancelPayment_InvalidId_Negative() {
        // Arrange
        Long invalidId = -1L;
        // Mocking an error behavior if the service throws an exception for bad IDs
        doThrow(new IllegalArgumentException("Invalid ID")).when(appointmentService).deleteAppointment(invalidId);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            patientController.cancelPayment(invalidId);
        });
    }


    /**
     * Test 3: Cancel Payment Logic
     */
    @Test
    void testCancelPayment_RedirectsToDashboard() {
        // Arrange
        Long appointmentId = 100L;

        // Act
        String viewName = patientController.cancelPayment(appointmentId);

        // Assert
        assertEquals("redirect:/patient/dashboard?cancelled=true", viewName);
        verify(appointmentService).deleteAppointment(appointmentId);
    }
}
