package com.cg.controllertest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.cg.controller.DoctorController;
import com.cg.dto.DoctorDTO;
import com.cg.model.MedicalRecord;
import com.cg.service.DoctorService;
import com.cg.service.MedicalRecordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class DoctorTestController {

    @Mock
    private DoctorService doctorService;

    @Mock
    private MedicalRecordService medicalRecordService;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private DoctorController doctorController;

    private Model model;
    private DoctorDTO mockDoctor;

    @BeforeEach
    void setUp() {
        model = new BindingAwareModelMap(); // Fake model to capture attributes
        mockDoctor = new DoctorDTO();
        mockDoctor.setId(101L);
        mockDoctor.setName("Dr. Strange");
        
        // Mock the UserDetails username for getCurrentDoctor() calls
        when(userDetails.getUsername()).thenReturn("doctor_user");
        when(doctorService.getDoctorByUsername("doctor_user")).thenReturn(mockDoctor);
    }


    @Test
    void testDoctorDashboard_ReturnsCorrectView() {
        String viewName = doctorController.doctorDashboard(userDetails, model);

        assertEquals("doctor/doctor-index", viewName);
        assertEquals(mockDoctor, model.getAttribute("doctor"));
    }
      
    /**
     * Negative Test: Patient Records - Empty Search Results
     * Verifies that an empty result set is handled gracefully without crashing.
     */
    @Test
    void testPatientRecords_NoMatches_Negative() {
        String keyword = "ZikaVirus";
        // 101L matches the mockDoctor in your setUp
        when(medicalRecordService.searchByDoctorAndKeyword(101L, keyword)).thenReturn(new ArrayList<>());

        String viewName = doctorController.patientRecords(userDetails, keyword, model);

        List<?> records = (List<?>) model.getAttribute("records");
        assertNotNull(records);
        assertEquals(0, records.size());
        assertEquals("doctor/doctor-records", viewName);
    }    

    @Test
    void testPatientRecords_WithSearchQuery() {
        String search = "Diabetes";
        List<MedicalRecord> records = new ArrayList<>();
        when(medicalRecordService.searchByDoctorAndKeyword(101L, search)).thenReturn(records);

        String viewName = doctorController.patientRecords(userDetails, search, model);

        assertEquals("doctor/doctor-records", viewName);
        assertEquals(records, model.getAttribute("records"));
        assertEquals(search, model.getAttribute("searchKeyword"));
    }
      
    /**
     * Negative Test: Save Diagnosis - Medical Record Object is Null
     */
    @Test
    void testSaveDiagnosis_NullRecord_Negative() {
        assertThrows(NullPointerException.class, () -> {
            doctorController.saveDiagnosis(userDetails, null);
        });
    }


    @Test
    void testSaveDiagnosis_RedirectsToRecords() {
        MedicalRecord record = new MedicalRecord();

        String viewName = doctorController.saveDiagnosis(userDetails, record);

        assertEquals("redirect:/doctor/records", viewName);
        // Verify that the doctor ID was set and service was called
        assertEquals(101L, record.getDoctor().getId());
        verify(doctorService, times(1)).addMedicalRecord(record);
    }
}
