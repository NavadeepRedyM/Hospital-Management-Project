package com.cg.servicetest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.cg.dto.BillingDTO;
import com.cg.model.*;
import com.cg.repository.AppointmentRepository;
import com.cg.repository.BillingRepository;
import com.cg.service.BillingService;

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
public class BillingTestService {

    @Mock private BillingRepository billingRepository;
    @Mock private AppointmentRepository appointmentRepository;

    @InjectMocks
    private BillingService billingService;

    private Appointment mockAppointment;
    private Billing mockBilling;
    private BillingDTO inputDto;

    @BeforeEach
    void setUp() {
        // Setup dependent entities
        Patient p = new Patient(); p.setId(1L);
        Department d = new Department(); d.setId(1L); d.setConsultationFee(500.0);
        
        mockAppointment = new Appointment();
        mockAppointment.setId(10L);
        mockAppointment.setPatient(p);
        mockAppointment.setDepartment(d);

        // Setup mock billing entity as if it came from the DB
        mockBilling = new Billing();
        mockBilling.setId(100L);
        mockBilling.setAppointment(mockAppointment);
        mockBilling.setPatient(p);
        mockBilling.setAmount(500.0);
        mockBilling.setTax(90.0);
        mockBilling.setTotalAmount(590.0);

        // Setup input DTO for creation
        inputDto = new BillingDTO();
        inputDto.setAppointmentId(10L);
        inputDto.setPaymentStatus("PENDING");
    }

    /**
     * Test 1: Create Billing - Success Scenario (Verifies calculations)
     */
    @Test
    void testCreateBilling_SuccessAndCalculationsCorrect() {
        when(appointmentRepository.findById(10L)).thenReturn(Optional.of(mockAppointment));
        when(billingRepository.save(any(Billing.class))).thenReturn(mockBilling);

        BillingDTO result = billingService.createBilling(inputDto);

        assertNotNull(result);
        assertEquals(590.0, result.getTotalAmount()); // 500 * 1.18 = 590
        assertEquals(90.0, result.getTax());
        verify(billingRepository).save(any(Billing.class));
    }
    
    /**
     * Negative Test : Create Billing - Missing Consultation Fee
     * Checks if the service handles departments with null or zero fees correctly.
     */
    @Test
    void testCreateBilling_ZeroFee_Negative() {
        mockAppointment.getDepartment().setConsultationFee(0.0);
        when(appointmentRepository.findById(10L)).thenReturn(Optional.of(mockAppointment));
        
        // Using Answer to capture the saved object to verify internal logic
        when(billingRepository.save(any(Billing.class))).thenAnswer(i -> i.getArguments()[0]);

        BillingDTO result = billingService.createBilling(inputDto);

        assertEquals(0.0, result.getTax());
        assertEquals(0.0, result.getTotalAmount());
    }


    /**
     * Test 2: Create Billing - Appointment Not Found
     */
    @Test
    void testCreateBilling_ThrowsExceptionIfAppointmentNotFound() {
        when(appointmentRepository.findById(99L)).thenReturn(Optional.empty());
        inputDto.setAppointmentId(99L);

        assertThrows(RuntimeException.class, () -> {
            billingService.createBilling(inputDto);
        }, "Should throw an exception if appointment ID is invalid.");
        
        verify(billingRepository, never()).save(any());
    }

    /**
     * Test 3: Get Bills by Patient ID - Verifies list retrieval
     */
    @Test
    void testGetBillsByPatientId_ReturnsListOfDTOs() {
        List<Billing> bills = Arrays.asList(mockBilling);
        when(billingRepository.findByPatientId(1L)).thenReturn(bills);

        List<BillingDTO> results = billingService.getBillsByPatientId(1L);

        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertEquals(10L, results.get(0).getAppointmentId());
        verify(billingRepository).findByPatientId(1L);
    }
    
    /**
     * Negative Test : Get Bills By Patient - Patient with No Billing History
     * Verifies that the service returns an empty list rather than null.
     */
    @Test
    void testGetBillsByPatientId_EmptyResult_Negative() {
        when(billingRepository.findByPatientId(999L)).thenReturn(Arrays.asList());

        List<BillingDTO> result = billingService.getBillsByPatientId(999L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(billingRepository).findByPatientId(999L);
    }
    /**
     * Test 4: Verify Payment Method Retrieval Logic
     */
    @Test
    void testCreateBilling_PullsPaymentMethodFromAppointment() {
        // Arrange: Add a Payment to the mock Appointment
        Payment pmt = new Payment();
        pmt.setPaymentMethod("Card");
        mockAppointment.setPayment(pmt); 
        
        when(appointmentRepository.findById(10L)).thenReturn(Optional.of(mockAppointment));
        when(billingRepository.save(any(Billing.class))).thenReturn(mockBilling);

        // Act
        BillingDTO result = billingService.createBilling(inputDto);

        // Assert
        assertEquals("Card", result.getPaymentMethod()); // Should pull "Card", ignoring inputDto's "PENDING"
    }
}
