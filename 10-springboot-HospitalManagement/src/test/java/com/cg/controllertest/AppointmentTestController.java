package com.cg.controllertest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.MatcherAssert.assertThat;

import com.cg.controller.AppointmentController;
import com.cg.dto.AppointmentDTO;
import com.cg.model.Department;
import com.cg.model.Doctor;
import com.cg.repository.DoctorRepository;
import com.cg.service.AppointmentService;
import com.cg.service.IAppointmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class AppointmentTestController {

	@Mock private AppointmentService appointmentService;
    @Mock private Model model;

    @InjectMocks private AppointmentController appointmentController;

    private AppointmentDTO mockAppt;

    @BeforeEach
    void setUp() {
        // Build the nested object structure your controller expects
        mockAppt = new AppointmentDTO();
        mockAppt.setId(1L);
        
        Department dept = new Department();
        dept.setId(99L);
        dept.setDeptName("Cardiology");
        
        mockAppt.setDepartment(dept);
    }

    // --- 3 POSITIVE TEST CASES ---

    @Test
    void testShowAssignForm_FiltersActiveDoctors() {
        // Arrange: We must mock the SERVICE call, not the repo
        Doctor activeDoc = new Doctor(); activeDoc.setId(10L);
        List<Doctor> serviceResults = List.of(activeDoc);

        when(appointmentService.getAppointmentById(1L)).thenReturn(mockAppt);
        when(appointmentService.findByDepartmentId(99L)).thenReturn(serviceResults);

        // Act
        String view = appointmentController.showAssignForm(1L, model);

        // Assert
        assertEquals("doctor/assign-doctor", view);
        verify(model).addAttribute("doctors", serviceResults);
        verify(model).addAttribute("deptName", "Cardiology");
    }

    @Test
    void testShowAssignForm_NoDoctors_ReturnsViewWithEmptyList() {
        when(appointmentService.getAppointmentById(1L)).thenReturn(mockAppt);
        when(appointmentService.findByDepartmentId(99L)).thenReturn(Collections.emptyList());

        String view = appointmentController.showAssignForm(1L, model);

        assertEquals("doctor/assign-doctor", view);
        verify(model).addAttribute("doctors", Collections.emptyList());
    }

    @Test
    void testShowAssignForm_VerifyBreadcrumbData() {
        when(appointmentService.getAppointmentById(1L)).thenReturn(mockAppt);
        
        appointmentController.showAssignForm(1L, model);

        // Ensures the specific department name is sent to UI
        verify(model).addAttribute("deptName", "Cardiology");
    }

    // --- 3 NEGATIVE TEST CASES ---

    @Test
    void testShowAssignForm_InvalidApptId_ThrowsException() {
        // Scenario: Service cannot find the appointment
        when(appointmentService.getAppointmentById(404L)).thenThrow(new RuntimeException("Appt Not Found"));

        assertThrows(RuntimeException.class, () -> appointmentController.showAssignForm(404L, model));
    }

    @Test
    void testShowAssignForm_NullDepartment_ThrowsNPE() {
        // Scenario: Appointment exists but has no department assigned
        mockAppt.setDepartment(null); 
        when(appointmentService.getAppointmentById(1L)).thenReturn(mockAppt);

        // This tests that your controller fails if data integrity is broken
        assertThrows(NullPointerException.class, () -> appointmentController.showAssignForm(1L, model));
    }

    @Test
    void testShowAssignForm_ServiceLayerFailure_ReturnsError() {
        // Scenario: Appointment service is down/erroring
        when(appointmentService.getAppointmentById(1L)).thenReturn(mockAppt);
        when(appointmentService.findByDepartmentId(99L)).thenThrow(new RuntimeException("DB Error"));

        assertThrows(RuntimeException.class, () -> appointmentController.showAssignForm(1L, model));
    }
}

