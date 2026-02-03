package com.cg.repository;
 
import com.cg.model.Billing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
 
import java.util.List;
 
@Repository
public interface BillingRepository extends JpaRepository<Billing, Long> {
 
    // 🔹 Find all bills of a particular patient
    List<Billing> findByPatientId(Long patientId);
 
    // 🔹 Find bill by appointment id
    Billing findByAppointmentId(Long appointmentId);
 
    // 🔹 Find bills by payment status (PAID / UNPAID)
    List<Billing> findByPaymentStatus(String paymentStatus);
}