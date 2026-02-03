package com.cg.controller;
 
import com.cg.model.Billing;
import com.cg.repository.BillingRepository;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
 
import java.util.List;
import java.util.Optional;
 
@RestController
@RequestMapping("/api/billings")
public class BillingController {
 
    @Autowired
    private BillingRepository billingRepository;
 
    //  Create Billing
    @PostMapping
    public Billing createBilling(@RequestBody Billing billing) {
        return billingRepository.save(billing);
    }
 
    // Get All Billings
    @GetMapping
    public List<Billing> getAllBillings() {
        return billingRepository.findAll();
    }
 
    //  Get Billing By Id
    @GetMapping("/{id}")
    public ResponseEntity<Billing> getBillingById(@PathVariable Long id) {
 
        Optional<Billing> billing = billingRepository.findById(id);
 
        if (billing.isPresent()) {
            return ResponseEntity.ok(billing.get());
        }
        return ResponseEntity.notFound().build();
    }
 
    //  Get Bills By Patient Id
    @GetMapping("/patient/{patientId}")
    public List<Billing> getBillsByPatientId(@PathVariable Long patientId) {
        return billingRepository.findByPatientId(patientId);
    }
 
    // Get Bill By Appointment Id
    @GetMapping("/appointment/{appointmentId}")
    public Billing getBillByAppointmentId(@PathVariable Long appointmentId) {
        return billingRepository.findByAppointmentId(appointmentId);
    }
 
    //  Get Bills By Payment Status
    @GetMapping("/status/{status}")
    public List<Billing> getBillsByPaymentStatus(@PathVariable String status) {
        return billingRepository.findByPaymentStatus(status);
    }
 
    //  Update Billing
    @PutMapping("/{id}")
    public ResponseEntity<Billing> updateBilling(
            @PathVariable Long id,
            @RequestBody Billing billingDetails) {
 
        Optional<Billing> optionalBilling = billingRepository.findById(id);
 
        if (optionalBilling.isPresent()) {
            Billing billing = optionalBilling.get();
 
            billing.setAmount(billingDetails.getAmount());
            billing.setTax(billingDetails.getTax());
            billing.setTotalAmount(billingDetails.getTotalAmount());
            billing.setPaymentStatus(billingDetails.getPaymentStatus());
            billing.setPaymentMethod(billingDetails.getPaymentMethod());
            billing.setBillingDate(billingDetails.getBillingDate());
            billing.setAppointment(billingDetails.getAppointment());
            billing.setPatient(billingDetails.getPatient());
 
            return ResponseEntity.ok(billingRepository.save(billing));
        }
        return ResponseEntity.notFound().build();
    }
 
    //  Delete Billing
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBilling(@PathVariable Long id) {
 
        if (billingRepository.existsById(id)) {
            billingRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
 