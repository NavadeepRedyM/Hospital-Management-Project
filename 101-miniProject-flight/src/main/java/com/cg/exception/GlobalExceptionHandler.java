package com.cg.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 1. Handle Resource Not Found (e.g., Flight ID not found)
    @ExceptionHandler(ResourceNotFound.class)
    public String handleResourceNotFound(ResourceNotFound ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("errorCode", "404 - Not Found");
        return "error"; // returns error.jsp or error.html
    }

    // 2. Handle Data Integrity Violations (e.g., trying to delete a flight with active bookings)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public String handleDataIntegrity(DataIntegrityViolationException ex, Model model) {
        model.addAttribute("errorMessage", "Cannot perform this action due to database constraints (e.g., related records exist).");
        return "error";
    }

    // 3. Handle Method Argument Mismatches (e.g., passing a string into an ID field)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public String handleTypeMismatch(MethodArgumentTypeMismatchException ex, Model model) {
        model.addAttribute("errorMessage", "Invalid input format provided in the URL.");
        return "error";
    }

    // 4. Fallback for all other Exceptions
    @ExceptionHandler(Exception.class)
    public String handleGlobalException(Exception ex, Model model) {
        model.addAttribute("errorMessage", "An unexpected error occurred: " + ex.getMessage());
        return "error";
    }
}
