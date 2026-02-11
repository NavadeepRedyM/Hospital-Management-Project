package com.cg.servicetest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.cg.dto.AppointmentDTO;
import com.cg.dto.PaymentDTO;
import com.cg.model.*;
import com.cg.repository.*;
import com.cg.service.AppointmentService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AppointmentTestService {

    @Mock private AppointmentRepository appointmentRepository;
    @Mock private PatientRepository patientRepository;
    @Mock private DoctorRepository doctorRepository;
    @Mock private DepartmentRepository departmentRepository;
    @Mock private PaymentRepository paymentRepository;
    @Mock private BillingRepository billingRepository;

    @InjectMocks
    private AppointmentService appointmentService;

    private Patient mockPatient;
    private Doctor mockDoctor;
    private Department mockDept;
    private Appointment mockAppointment;

    @BeforeEach
    void setUp() {
        mockPatient = new Patient(); mockPatient.setId(1L);
        mockDoctor = new Doctor(); mockDoctor.setId(2L);
        mockDept = new Department(); mockDept.setId(3L); mockDept.setConsultationFee(100.0);
        
        mockAppointment = new Appointment();
        mockAppointment.setId(10L);
        mockAppointment.setStatus("PENDING_ALLOTMENT");
        mockAppointment.setDepartment(mockDept);
        mockAppointment.setPatient(mockPatient);
    }

    /**
     * Test 1: Book Appointment - Success (sets PENDING_ALLOTMENT)
     */
    @Test
    void testBookAppointment_Success() {
        when(patientRepository.findByUsername("user123")).thenReturn(Optional.of(mockPatient));
        when(departmentRepository.findById(3L)).thenReturn(Optional.of(mockDept));
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(mockAppointment);

        AppointmentDTO result = appointmentService.book("user123", 3L, null, LocalDate.now(), "09:00", "Checkup");

        assertNotNull(result);
        assertEquals("PENDING_ALLOTMENT", result.getStatus());
        verify(appointmentRepository).save(any(Appointment.class));
    }

    /**
     * Test 2: Finalize Booking with Payment - Transactional Flow
     */
    @Test
    void testFinalizeBookingWithPayment_Success() {
        PaymentDTO paymentDto = new PaymentDTO();
        paymentDto.setAppointmentId(10L);
        paymentDto.setAmount(100.0);
        paymentDto.setPaymentMethod("Credit Card");

        when(appointmentRepository.findById(10L)).thenReturn(Optional.of(mockAppointment));
        when(paymentRepository.save(any(Payment.class))).thenReturn(new Payment());
        when(billingRepository.save(any(Billing.class))).thenReturn(new Billing());

        appointmentService.finalizeBookingWithPayment(paymentDto);

        // Verify status changed to CONFIRMED
        assertEquals("CONFIRMED", mockAppointment.getStatus());
        
        // Verify payment and billing records were created and saved
        verify(paymentRepository).save(any(Payment.class));
        verify(billingRepository).save(any(Billing.class));
        verify(appointmentRepository).save(mockAppointment);
    }
    
    /**
     * Negative Test 1: Book Appointment - Patient Not Found
     */
    @Test
    void testBookAppointment_PatientNotFound() {
        when(patientRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            appointmentService.book("unknown", 3L, null, LocalDate.now(), "09:00", "Checkup");
        });
        verify(appointmentRepository, never()).save(any());
    }
    
    /**
     * Test 3: Assign Doctor to Appointment (Admin Action)
     */
    @Test
    void testAssignDoctorToAppointment_Success() {
        when(appointmentRepository.findById(10L)).thenReturn(Optional.of(mockAppointment));
        when(doctorRepository.findById(2L)).thenReturn(Optional.of(mockDoctor));

        appointmentService.assignDoctorToAppointment(10L, 2L);

        assertEquals("CONFIRMED", mockAppointment.getStatus());
        assertEquals(mockDoctor, mockAppointment.getDoctor());
        verify(appointmentRepository).save(mockAppointment);
    }
    
    /**
     * Test 4: Reassign Doctor - Failure (Wrong Department)
     */
    @BeforeEach
    void setUp1() {
        mockPatient = new Patient(); mockPatient.setId(1L);
        mockDoctor = new Doctor(); mockDoctor.setId(2L);
        mockDept = new Department(); 
        mockDept.setId(3L); 
        mockDept.setConsultationFee(100.0);
        // *** ADD THIS LINE ***
        mockDept.setDeptName("General Care"); 
        
        mockAppointment = new Appointment();
        mockAppointment.setId(10L);
        mockAppointment.setStatus("PENDING_ALLOTMENT");
        mockAppointment.setDepartment(mockDept);
        mockAppointment.setPatient(mockPatient);
    }
  
    /**
     * Negative Test 2: Assign Doctor - Doctor Not Found
     */
    @Test
    void testAssignDoctor_DoctorNotFound() {
        when(appointmentRepository.findById(10L)).thenReturn(Optional.of(mockAppointment));
        when(doctorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            appointmentService.assignDoctorToAppointment(10L, 99L);
        });
        // Status should remain PENDING_ALLOTMENT
        assertEquals("PENDING_ALLOTMENT", mockAppointment.getStatus());
    }

    
    /**
     * Test 5: Cancel Appointment (Soft Delete Status Change)
     */
    @Test
    void testCancelAppointment_ChangesStatusToCancelled() {
        when(appointmentRepository.findById(10L)).thenReturn(Optional.of(mockAppointment));

        appointmentService.cancelAppointment(10L);

        assertEquals("CANCELLED", mockAppointment.getStatus());
        verify(appointmentRepository).save(mockAppointment);
    }
}
