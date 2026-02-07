package com.cg.controller;

import com.cg.dto.BillingDTO;
import com.cg.model.Billing;
import com.cg.repository.BillingRepository;
import com.cg.service.IBillingServiceImpl; // Preferred over direct Repo access

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/api/billings")
public class BillingController {

    @Autowired
    private BillingRepository billingRepository;

    // Create Billing
    // Logic: In your service, ensure 'amount' is pulled from the Department fee
    @PostMapping
    public ResponseEntity<Billing> createBilling(@RequestBody Billing billing) {
        // Logic: 18% Tax Calculation
        double amount = billing.getAmount();
        double tax = amount * 0.18;
        
        billing.setTax(tax);
        billing.setTotalAmount(amount + tax);
        
        return ResponseEntity.ok(billingRepository.save(billing));
    }


    // Get All Billings
    @GetMapping
    public List<Billing> getAllBillings() {
        return billingRepository.findAll();
    }

    // Get Billing By Id
    @GetMapping("/{id}")
    public ResponseEntity<Billing> getBillingById(@PathVariable Long id) {
        return billingRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get Bills By Patient Id
    @GetMapping("/patient/{patientId}")
    public List<Billing> getBillsByPatientId(@PathVariable Long patientId) {
        return billingRepository.findByPatientId(patientId);
    }

    // Get Bill By Appointment Id
    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<Billing> getBillByAppointmentId(@PathVariable Long appointmentId) {
        Billing billing = billingRepository.findByAppointmentId(appointmentId);
        return (billing != null) ? ResponseEntity.ok(billing) : ResponseEntity.notFound().build();
    }

    // Update Billing
    @PutMapping("/{id}")
    public ResponseEntity<Billing> updateBilling(@PathVariable Long id, @RequestBody Billing details) {
        return billingRepository.findById(id)
                .map(existing -> {
                    existing.setAmount(details.getAmount());
                    existing.setTax(details.getTax());
                    existing.setTotalAmount(details.getTotalAmount());
                    existing.setPaymentStatus(details.getPaymentStatus());
                    existing.setPaymentMethod(details.getPaymentMethod());
                    existing.setBillingDate(details.getBillingDate());
                    return ResponseEntity.ok(billingRepository.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete Billing
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBilling(@PathVariable Long id) {
        if (billingRepository.existsById(id)) {
            billingRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    
}
