package com.cg.controllertest;

import com.cg.controller.BillingController;
import com.cg.model.Billing;
import com.cg.repository.BillingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BillingControllerTest {

    @Mock
    private BillingRepository billingRepository;

    @InjectMocks
    private BillingController billingController;

    private Billing sampleBilling;

    @BeforeEach
    void setUp() {
        sampleBilling = new Billing();
        sampleBilling.setId(1L);
        sampleBilling.setAmount(1000.0);
    }

    @Test
    void testCreateBilling_Logic() {
        when(billingRepository.save(any(Billing.class))).thenAnswer(i -> i.getArguments()[0]);

        ResponseEntity<Billing> response = billingController.createBilling(sampleBilling);

        assertNotNull(response.getBody());
        assertEquals(180.0, response.getBody().getTax());
        assertEquals(1180.0, response.getBody().getTotalAmount());
        // Fix: Use getStatusCode().value()
        assertEquals(200, response.getStatusCode().value());
    }

    /**
     * Negative Test: Get Bills By Patient ID - Returns Empty List
     */
    @Test
    void testGetBillsByPatientId_Empty_Negative() {
        // Arrange
        when(billingRepository.findByPatientId(500L)).thenReturn(Arrays.asList());

        // Act
        List<Billing> result = billingController.getBillsByPatientId(500L);

        // Assert
        assertTrue(result.isEmpty());
        assertEquals(0, result.size());
    }
    
    @Test
    void testGetBillingById_Found() {
        when(billingRepository.findById(1L)).thenReturn(Optional.of(sampleBilling));

        ResponseEntity<Billing> response = billingController.getBillingById(1L);

        // Fix: Use getStatusCode().value()
        assertEquals(200, response.getStatusCode().value());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void testGetBillingById_NotFound() {
        when(billingRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Billing> response = billingController.getBillingById(1L);

        // Fix: Use getStatusCode().value()
        assertEquals(404, response.getStatusCode().value());
    }
       
    /**
     * Negative Test: Create Billing - Repository Failure
     */
    @Test
    void testCreateBilling_RepositoryError_Negative() {
        // Arrange
        when(billingRepository.save(any(Billing.class))).thenThrow(new RuntimeException("Database connection failed"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            billingController.createBilling(sampleBilling);
        });
    }

    @Test
    void testDeleteBilling() {
        when(billingRepository.existsById(1L)).thenReturn(true);
        
        ResponseEntity<Void> response = billingController.deleteBilling(1L);

        // Fix: Use getStatusCode().value()
        assertEquals(204, response.getStatusCode().value());
        verify(billingRepository).deleteById(1L);
    }
}
