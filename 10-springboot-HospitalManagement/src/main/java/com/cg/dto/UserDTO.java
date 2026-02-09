package com.cg.dto;
 
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
 
public class UserDTO {
 
    @NotBlank(message = "Username is required")

    @Size(min = 4, max = 20, message = "Username must be between 4 and 20 characters")

    private String username;
 
    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 25, message = "Password must be between 8 and 25 characters long")
    @Pattern(
        regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$",
        message = "Password must contain at least one digit, one lowercase, one uppercase, and one special character (@#$%^&+=!)"
    )
    private String password;

 
    private String role; // Typically set automatically by the service layer
 
    // Standard Getters and Setters

    public String getUsername() {

        return username;

    }
 
    public void setUsername(String username) {

        this.username = username;

    }
 
    public String getPassword() {

        return password;

    }
 
    public void setPassword(String password) {

        this.password = password;

    }
 
    public String getRole() {

        return role;

    }
 
    public void setRole(String role) {

        this.role = role;

    }

}

 