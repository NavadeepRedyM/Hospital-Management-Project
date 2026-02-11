package com.cg.servicetest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cg.dto.DepartmentDTO;
import com.cg.model.Department;
import com.cg.model.Doctor;
import com.cg.repository.DepartmentRepository;
import com.cg.service.DepartmentService;

@ExtendWith(MockitoExtension.class)
public class DepartmentTestService {

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentService departmentService;

    private Department mockDept;
    private DepartmentDTO mockDto;

    @BeforeEach
    void setUp() {
        mockDept = new Department();
        mockDept.setId(1L);
        mockDept.setDeptName("Cardiology");
        mockDept.setConsultationFee(500.0);
        mockDept.setDoctors(new ArrayList<>());
        mockDept.setActive(true);

        mockDto = new DepartmentDTO();
        mockDto.setId(1L);
        mockDto.setDeptName("Cardiology");
        mockDto.setConsultationFee(500.0);
    }

    /**
     * Test 1: Get Department by ID (Success)
     */
    @Test
    void testGetDepartmentById_Success() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(mockDept));

        DepartmentDTO result = departmentService.getDepartmentById(1L);

        assertNotNull(result);
        assertEquals("Cardiology", result.getDeptName());
        assertEquals(500.0, result.getConsultationFee());
        verify(departmentRepository).findById(1L);
    }
    
    /**
     * Negative Test : Update Department - Non-Existent ID
     */
    @Test
    void testUpdateDepartment_NotFound_Negative() {
        // Arrange
        when(departmentRepository.findById(1L)).thenReturn(Optional.empty());
        DepartmentDTO updateDto = new DepartmentDTO();

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            departmentService.updateDepartment(1L, updateDto);
        });

        verify(departmentRepository, never()).save(any());
    }

    /**
     * Test 2: Soft Delete - Verifies 'active' set to false
     */
    @Test
    void testDeleteDepartment_SoftDeleteSuccess() {
        // Arrange: No doctors assigned
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(mockDept));

        // Act
        departmentService.deleteDepartment(1L);

        // Assert
        assertFalse(mockDept.isActive(), "Department should be marked as inactive (Soft Delete)");
        verify(departmentRepository).save(mockDept);
    }

    /**
     * Test 3: Delete Failure - Still has Active Doctors
     */
    @Test
    void testDeleteDepartment_ThrowsExceptionIfDoctorsActive() {
        // Arrange: Add an active doctor to the department
        Doctor activeDoc = new Doctor();
        activeDoc.setActive(true);
        mockDept.setDoctors(Arrays.asList(activeDoc));
        
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(mockDept));

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            departmentService.deleteDepartment(1L);
        });

        assertTrue(exception.getMessage().contains("still has active doctors"));
        verify(departmentRepository, never()).save(any());
    }
    
    /**
     * Negative Test : Delete Department - ID Not Found
     * Verifies that you cannot delete something that doesn't exist.
     */
    @Test
    void testDeleteDepartment_NotFound_Negative() {
        // Arrange
        when(departmentRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            departmentService.deleteDepartment(1L);
        });

        verify(departmentRepository, never()).save(any());
    }


    /**
     * Test 4: Update Department - Verifies Fee update
     */
    @Test
    void testUpdateDepartment_UpdatesFee() {
        // Arrange
        DepartmentDTO updateDto = new DepartmentDTO();
        updateDto.setDeptName("Neurology");
        updateDto.setConsultationFee(750.0);

        when(departmentRepository.findById(1L)).thenReturn(Optional.of(mockDept));
        when(departmentRepository.save(any(Department.class))).thenReturn(mockDept);

        // Act
        DepartmentDTO result = departmentService.updateDepartment(1L, updateDto);

        // Assert
        assertEquals(750.0, result.getConsultationFee());
        assertEquals("Neurology", mockDept.getDeptName());
    }
}

