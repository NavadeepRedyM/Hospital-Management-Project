package com.cg.service;
 
import com.cg.model.Billing;
import java.util.List;
 
public interface IBillingServiceImpl {
 
    Billing createBilling(Billing billing);
 
    List<Billing> getAllBillings();
 
    Billing getBillingById(Long id);
 
    List<Billing> getBillsByPatientId(Long patientId);
 
    Billing getBillByAppointmentId(Long appointmentId);
 
    List<Billing> getBillsByPaymentStatus(String status);
 
    Billing updateBilling(Long id, Billing billing);
 
    void deleteBilling(Long id);
}