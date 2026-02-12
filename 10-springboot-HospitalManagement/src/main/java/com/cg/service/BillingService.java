package com.cg.service;

import com.cg.dto.BillingDTO;
import com.cg.exception.AppointmentNotFoundException;
import com.cg.model.Appointment;
import com.cg.model.Billing;
import com.cg.repository.AppointmentRepository;
import com.cg.repository.BillingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BillingService {

    @Autowired
    private BillingRepository billingRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Transactional
    public BillingDTO createBilling(BillingDTO dto) {
        Appointment app = appointmentRepository.findById(dto.getAppointmentId())
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found"));

        Billing billing = new Billing();
        billing.setAppointment(app);
        billing.setPatient(app.getPatient());
        
        // Fee from Department
        double fee = app.getDepartment().getConsultationFee();
        billing.setAmount(fee);
        
        // Automated 18% Tax Calculation
        billing.setTax(fee * 0.18);
        billing.setTotalAmount(fee + billing.getTax());
        
        billing.setPaymentStatus(dto.getPaymentStatus());
        // Pulling payment method from the appointment's payment field if available
        billing.setPaymentMethod(app.getPayment() != null ? app.getPayment().getPaymentMethod() : dto.getPaymentMethod());
        billing.setBillingDate(LocalDate.now());

        Billing saved = billingRepository.save(billing);
        return convertToDTO(saved);
    }

    public List<BillingDTO> getAllBillings() {
        return billingRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // ✅ IMPLEMENTED THIS METHOD FOR ADMIN CONTROLLER
    public List<BillingDTO> getBillsByPatientId(Long patientId) {
        return billingRepository.findByPatientId(patientId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private BillingDTO convertToDTO(Billing b) {
        BillingDTO dto = new BillingDTO();
        dto.setId(b.getId());
        dto.setAppointmentId(b.getAppointment().getId());
        dto.setPatientId(b.getPatient().getId());
        dto.setAmount(b.getAmount());
        dto.setTax(b.getTax());
        dto.setTotalAmount(b.getTotalAmount());
        dto.setPaymentStatus(b.getPaymentStatus());
        
        // Accessing payment method from Appointment relationship as requested
        if (b.getAppointment() != null && b.getAppointment().getPayment() != null) {
            dto.setPaymentMethod(b.getAppointment().getPayment().getPaymentMethod());
        } else {
            dto.setPaymentMethod(b.getPaymentMethod());
        }
        
        dto.setBillingDate(b.getBillingDate());
        return dto;
    }
}
