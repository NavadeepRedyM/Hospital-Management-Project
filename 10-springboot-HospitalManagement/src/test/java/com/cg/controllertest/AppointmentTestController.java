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
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class AppointmentTestController {

    @Mock
    private IAppointmentService appointmentService;

    @Mock
    private DoctorRepository doctorRepository;

    @InjectMocks
    private AppointmentController appointmentController;

    private Model model;
    private AppointmentDTO mockApptDto;
    private Department mockDept;

    @BeforeEach
    void setUp() {
        model = new BindingAwareModelMap();
        mockDept = new Department();
        mockDept.setId(99L);
        mockDept.setDeptName("General Care");

        mockApptDto = new AppointmentDTO();
        mockApptDto.setId(1L);
        mockApptDto.setDepartment(mockDept);
    }

    /**
     * Test 1: View Appointments List
     */
    @Test
    void testViewAppointments_PopulatesModel() {
        List<AppointmentDTO> list = Arrays.asList(mockApptDto);
        when(appointmentService.getAllAppointments()).thenReturn(list);

        String view = appointmentController.viewAppointments(model);

        assertEquals("appointment/view-appointments", view);
        assertEquals(list, model.getAttribute("appointments"));
    }
    /**
     * Negative Test : View Appointments - Empty List
     */
    @Test
    void testViewAppointments_EmptyList() {
        when(appointmentService.getAllAppointments()).thenReturn(Arrays.asList());

        String view = appointmentController.viewAppointments(model);

        assertEquals("appointment/view-appointments", view);
        List<AppointmentDTO> appointments = (List<AppointmentDTO>) model.getAttribute("appointments");
        assertTrue(appointments.isEmpty(), "The appointment list should be empty but initialized.");
    }

    /**
     * Test 2: Show Assign Form - Filters active doctors only
     */
    @Test
    void testShowAssignForm_FiltersActiveDoctors() {
        // Arrange Doctors
        Doctor activeDoc = new Doctor(); activeDoc.setId(10L); activeDoc.setActive(true);
        Doctor inactiveDoc = new Doctor(); inactiveDoc.setId(11L); inactiveDoc.setActive(false);
        List<Doctor> allDoctorsInDept = Arrays.asList(activeDoc, inactiveDoc);

        when(appointmentService.getAppointmentById(1L)).thenReturn(mockApptDto);
        when(doctorRepository.findByDepartmentId(99L)).thenReturn(allDoctorsInDept);

        // Act
        String view = appointmentController.showAssignForm(1L, model);

        // Assert
        assertEquals("doctor/assign-doctor", view);
        List<Doctor> doctorsInModel = (List<Doctor>) model.getAttribute("doctors");
        // Verify only the active doctor made it to the model
        assertThat(doctorsInModel, hasSize(1)); 
        assertEquals(10L, doctorsInModel.get(0).getId());
    }

    /**
     * Test 3: Save Assignment Post
     */
    @Test
    void testSaveAssignment_RedirectsToList() {
        Long appointmentId = 1L;
        Long doctorId = 5L;
        
        // Act
        String view = appointmentController.saveAssignment(appointmentId, doctorId);

        // Assert
        assertEquals("redirect:/appointments", view);
        // Verify that the service method was called with correct IDs
        verify(appointmentService).assignDoctorToAppointment(appointmentId, doctorId);
    }

    /**
     * Test 4: Reassign POST - Success flash attribute
     */
    @Test
    void testProcessReassign_SuccessMessage() {
        RedirectAttributes ra = new RedirectAttributesModelMap();
        Long apptId = 1L;
        Long newDoctorId = 20L;

        String view = appointmentController.processReassign(apptId, newDoctorId, ra);

        assertEquals("redirect:/appointments", view);
        assertEquals("Appointment reassigned successfully!", ra.getFlashAttributes().get("success"));
        verify(appointmentService).reassignDoctor(apptId, newDoctorId);
    }

    /**
     * Test 5: Cancel Appointment
     */
    @Test
    void testCancelAppt_RedirectsAndAddsFlashAttribute() {
        RedirectAttributes ra = new RedirectAttributesModelMap();
        Long apptId = 1L;

        String view = appointmentController.cancelAppt(apptId, ra);

        assertEquals("redirect:/appointments", view);
        assertEquals("Appointment marked as Cancelled.", ra.getFlashAttributes().get("success"));
        verify(appointmentService).cancelAppointment(apptId);
    }
    /**
     * Negative Test : Cancel Appointment - Already Cancelled/Not Found
     */
    @Test
    void testCancelAppt_NonExistentId() {
        RedirectAttributes ra = new RedirectAttributesModelMap();
        Long invalidId = 404L;

        // Simulate service failing to find the ID
        doThrow(new IllegalArgumentException("Appointment not found"))
            .when(appointmentService).cancelAppointment(invalidId);

        assertThrows(IllegalArgumentException.class, () -> {
            appointmentController.cancelAppt(invalidId, ra);
        });
}
}

