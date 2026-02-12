package com.cg.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 1. Handle Resource Not Found (404)
    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleResourceNotFound(ResourceNotFoundException ex, Model model) {
       // logger.error("Resource not found: {}", ex.getMessage());
        
        // Match the HTML: ${error.message} and ${error.details}
        model.addAttribute("error", Map.of(
            "message", "404 - Not Found",
            "details", ex.getMessage()
        ));
        return "error/error-page"; // Ensure this matches your HTML file name
    }

    // 2. Handle Validation Errors (400)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleValidationExceptions(MethodArgumentNotValidException ex, Model model) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.toList());

        model.addAttribute("error", Map.of(
            "message", "Validation Failed",
            "details", "Please correct the highlighted fields below."
        ));
        model.addAttribute("errors", errors); // Matches th:each="error : ${errors}"
        
        return "error/error-page";
    }

    // 3. Handle Generic Runtime Errors (500)
    @ExceptionHandler(RuntimeException.class)
    public String handleRuntimeException(RuntimeException ex, Model model) {
       // logger.error("Internal Error: ", ex);
        model.addAttribute("error", Map.of(
            "message", "500 - Internal Server Error",
            "details", "Something went wrong on our end. " + ex.getMessage()
        ));
        return "error/error-page";
    }
    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNoResourceFound(NoResourceFoundException ex, Model model) {
        //logger.error("404 Error: Resource not found - {}", ex.getMessage());
        
        // Map attributes to match your HTML: ${error.message} and ${error.details}
        model.addAttribute("error", Map.of(
            "message", "404 - Page Not Found",
            "details", "The URL you requested does not exist: " + ex.getResourcePath()
        ));
        
        return "error/error-page"; // Path to your Thymeleaf template
    }
}
