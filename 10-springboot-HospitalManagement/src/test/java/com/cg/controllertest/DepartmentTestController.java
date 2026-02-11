package com.cg.controllertest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import com.cg.controller.DepartmentController;
import com.cg.dto.DepartmentDTO;
import com.cg.dto.DoctorDTO;
import com.cg.service.IDepartmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.support.BindingAwareModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class DepartmentTestController {

    @Mock
    private IDepartmentService departmentService;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private DepartmentController departmentController;

    private Model model;
    private DepartmentDTO mockDept;

    @BeforeEach
    void setUp() {
        model = new BindingAwareModelMap();
        mockDept = new DepartmentDTO();
        mockDept.setId(1L);
        // FIXED: Using getDeptName/setDeptName from your DTO
        mockDept.setDeptName("Cardiology"); 
        mockDept.setConsultationFee(500.0);
        mockDept.setDoctors(new ArrayList<>());
    }

    @Test
    void testViewDepartments_PopulatesModel() {
        List<DepartmentDTO> list = Arrays.asList(mockDept);
        when(departmentService.getAllDepartments()).thenReturn(list);

        String view = departmentController.viewDepartments(model);

        assertEquals("hospital/manage-department", view);
        assertEquals(list, model.getAttribute("departments"));
    }

    @Test
    void testSaveDepartment_RedirectsOnSuccess() {
        when(bindingResult.hasErrors()).thenReturn(false);
        // FIXED: Matches your service signature (returns DTO)
        when(departmentService.updateDepartment(eq(1L), any(DepartmentDTO.class))).thenReturn(mockDept);

        String view = departmentController.saveDepartment(mockDept, bindingResult);

        assertEquals("redirect:/admin/departments", view);
        verify(departmentService).updateDepartment(eq(1L), any(DepartmentDTO.class));
    }
    
    /**
     * Negative Test : View Doctors - Department Not Found
     * Verifies behavior when an invalid ID is passed in the URL.
     */
    @Test
    void testViewDoctorsByDepartment_NotFound_Negative() {
        // Arrange
        when(departmentService.getDepartmentById(999L)).thenReturn(null);

        // Act & Assert
        // Assuming the controller tries to access .getDoctors() on a null object
        assertThrows(NullPointerException.class, () -> {
            departmentController.viewDoctorsByDepartment(999L, model);
        });
    }

    @Test
    void testSaveDepartment_ReturnsFormOnErrors() {
        when(bindingResult.hasErrors()).thenReturn(true);

        String view = departmentController.saveDepartment(mockDept, bindingResult);

        assertEquals("hospital/add-department", view);
        // Verify no save attempt was made
        verify(departmentService, never()).addDepartment(any());
    }

    @Test
    void testViewDoctorsByDepartment_FiltersActiveOnly() {
        // Prepare mock doctors
        com.cg.model.Doctor activeDoc = new com.cg.model.Doctor();
        activeDoc.setActive(true);
        com.cg.model.Doctor inactiveDoc = new com.cg.model.Doctor();
        inactiveDoc.setActive(false);

        mockDept.setDoctors(Arrays.asList(activeDoc, inactiveDoc));
        when(departmentService.getDepartmentById(1L)).thenReturn(mockDept);

        String view = departmentController.viewDoctorsByDepartment(1L, model);

        @SuppressWarnings("unchecked")
        List<com.cg.model.Doctor> doctorsInModel = (List<com.cg.model.Doctor>) model.getAttribute("doctors");
        
        assertEquals(1, doctorsInModel.size());
        assertEquals("hospital/department-doctors", view);
    }
      
    /**
     * Negative Test : View Departments - Empty Database
     */
    @Test
    void testViewDepartments_EmptyList_Negative() {
        // Arrange
        when(departmentService.getAllDepartments()).thenReturn(new ArrayList<>());

        // Act
        String view = departmentController.viewDepartments(model);

        // Assert
        assertEquals("hospital/manage-department", view);
        List<DepartmentDTO> depts = (List<DepartmentDTO>) model.getAttribute("departments");
        assertTrue(depts.isEmpty());
    }

    @Test
    void testDeleteDepartment_HandlesException() {
        RedirectAttributes ra = new RedirectAttributesModelMap();
        // Use doThrow for void methods in Mockito
        doThrow(new IllegalStateException("Cannot delete")).when(departmentService).deleteDepartment(1L);

        String view = departmentController.deleteDepartment(1L, ra);

        assertEquals("redirect:/admin/departments", view);
        assertEquals("Cannot delete", ra.getFlashAttributes().get("error"));
    }
}
