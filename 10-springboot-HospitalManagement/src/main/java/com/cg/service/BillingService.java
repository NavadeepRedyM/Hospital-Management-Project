package com.cg.service;
 
import com.cg.model.Billing;
import com.cg.repository.BillingRepository;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 
import java.util.List;
 
@Service
public class BillingService implements IBillingServiceImpl {
 
    @Autowired
    private BillingRepository billingRepository;
 
    @Override
    public Billing createBilling(Billing billing) {
        return billingRepository.save(billing);
    }
 
    @Override
    public List<Billing> getAllBillings() {
        return billingRepository.findAll();
    }
 
    @Override
    public Billing getBillingById(Long id) {
        return billingRepository.findById(id).orElse(null);
    }
 
    @Override
    public List<Billing> getBillsByPatientId(Long patientId) {
        return billingRepository.findByPatientId(patientId);
    }
 
    @Override
    public Billing getBillByAppointmentId(Long appointmentId) {
        return billingRepository.findByAppointmentId(appointmentId);
    }
 
    @Override
    public List<Billing> getBillsByPaymentStatus(String status) {
        return billingRepository.findByPaymentStatus(status);
    }
 
    @Override
    public Billing updateBilling(Long id, Billing billing) {
        Billing existing = billingRepository.findById(id).orElse(null);
 
        if (existing != null) {
            existing.setAmount(billing.getAmount());
            existing.setTax(billing.getTax());
            existing.setTotalAmount(billing.getTotalAmount());
            existing.setPaymentStatus(billing.getPaymentStatus());
            existing.setPaymentMethod(billing.getPaymentMethod());
            existing.setBillingDate(billing.getBillingDate());
            existing.setAppointment(billing.getAppointment());
            existing.setPatient(billing.getPatient());
 
            return billingRepository.save(existing);
        }
        return null;
    }
 
    @Override
    public void deleteBilling(Long id) {
        billingRepository.deleteById(id);
    }
}
 