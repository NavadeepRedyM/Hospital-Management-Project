package com.cg.servicetest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.cg.dto.PatientDTO;
import com.cg.model.Patient;
import com.cg.repository.PatientRepository;
import com.cg.service.PatientService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class PatientTestService {

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientService patientService;

    private Patient mockPatient;
    private PatientDTO mockPatientDTO;

    @BeforeEach
    void setUp() {
        mockPatient = new Patient();
        mockPatient.setId(1L);
        mockPatient.setName("John Doe");
        mockPatient.setUsername("johndoe");

        mockPatientDTO = new PatientDTO();
        mockPatientDTO.setId(1L);
        mockPatientDTO.setName("John Doe");
        mockPatientDTO.setUsername("johndoe");
    }

    @Test
    void testFindById_Success() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(mockPatient));

        PatientDTO result = patientService.findById(1L);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        verify(patientRepository).findById(1L);
    }
    /**
    * Negative Test: Find By ID - Null Input
    * Verifies system stability when a null ID is passed.
    */
   @Test
   void testFindById_NullId_Negative() {
       // Act & Assert
       assertThrows(Exception.class, () -> {
           patientService.findById(null);
       });
   }


    @Test
    void testFindById_NotFound() {
        when(patientRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            patientService.findById(99L);
        });
    }

    @Test
    void testSave_UpdatesExistingPatient() {
        // Arrange
        PatientDTO updateDto = new PatientDTO();
        updateDto.setId(1L);
        updateDto.setName("New Name");
        updateDto.setUsername("johndoe");

        when(patientRepository.findById(1L)).thenReturn(Optional.of(mockPatient));

        // Act
        patientService.save(updateDto);

        // Assert
        assertEquals("New Name", mockPatient.getName());
        verify(patientRepository).save(mockPatient);
    }
    /**
     * Negative Test: Find By Username - Non-existent User
     * Verifies that the service returns an empty Optional rather than throwing 
     * an exception when a username is not found.
     */
    @Test
    void testFindByUsername_NotFound_Negative() {
        // Arrange
        when(patientRepository.findByUsername("ghostuser")).thenReturn(Optional.empty());

        // Act
        Optional<PatientDTO> result = patientService.findByUsername("ghostuser");

        // Assert
        assertFalse(result.isPresent(), "Result should be empty for non-existent username");
        verify(patientRepository).findByUsername("ghostuser");
    }

    @Test
    void testFindByUsername_Success() {
        when(patientRepository.findByUsername("johndoe")).thenReturn(Optional.of(mockPatient));

        Optional<PatientDTO> result = patientService.findByUsername("johndoe");

        assertTrue(result.isPresent());
        assertEquals("johndoe", result.get().getUsername());
    }

    @Test
    void testDeleteById_Success() {
        when(patientRepository.existsById(1L)).thenReturn(true);

        patientService.deleteById(1L);

        verify(patientRepository).deleteById(1L);
    }
}
