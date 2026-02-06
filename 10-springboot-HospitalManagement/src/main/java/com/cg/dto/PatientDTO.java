package com.cg.dto;
 
import jakarta.validation.constraints.*;
 
public class PatientDTO {
 
    private Long id;
 
    private String name;
 
    private String gender;

    @Min(value = 0, message = "Age cannot be negative")

    @Max(value = 120, message = "Please enter a valid age")

    private Integer age;
 
    @Pattern(regexp = "^[0-9]{10}$", 

             message = "Contact number must be exactly 10 digits")

    private String contactNumber;
 
    @NotBlank(message = "Email is required")

    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@gmail\\.com$", 

             message = "Only Gmail addresses are allowed")

    @Email(message = "Invalid email format")

    private String email;
 
    private String address;

    private String bloodGroup;

    @NotBlank(message = "Username is required")

    private String username;
 
    // Standard Getters and Setters

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }
 
    public String getName() { return name; }

    public void setName(String name) { this.name = name; }
 
    public String getGender() { return gender; }

    public void setGender(String gender) { this.gender = gender; }
 
    public Integer getAge() { return age; }

    public void setAge(Integer age) { this.age = age; }
 
    public String getContactNumber() { return contactNumber; }

    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }
 
    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }
 
    public String getAddress() { return address; }

    public void setAddress(String address) { this.address = address; }
 
    public String getBloodGroup() { return bloodGroup; }

    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }
 
    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

}

 