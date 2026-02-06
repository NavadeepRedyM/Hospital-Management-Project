package com.cg.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "patients")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
<<<<<<< Updated upstream

    @NotBlank(message = "Full Name is required")
    @Column(name = "full_name", nullable = false)
=======
    
    @Column(name = "full_name")
>>>>>>> Stashed changes
    private String name;

    @Column(name = "gender")
    private String gender;
    
    @Column(name="age")
    private Integer age;

    // ✅ Only digits, exactly 10 digits
    @Pattern(regexp = "^[0-9]{10}$",
             message = "Contact number must be exactly 10 digits (digits only)")
    @Column(name = "contact_number")
    private String contactNumber;

    // ✅ Gmail only + valid email syntax
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@gmail\\.com$",
             message = "Email must be a Gmail address (e.g., user@gmail.com)")
    @Email(message = "Email format is invalid")
    @Column(name = "email")
    private String email;

    @Column(name = "address")
    private String address;

    @Column(name = "blood_group")
    private String bloodGroup;
    
    @Column(name="user_name")
    private String username;

    // ---- keep your relations as they are (examples below) ----
    // @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    // private List<Appointment> appointments = new ArrayList<>();
    //
    // @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    // private List<MedicalRecord> medicalRecords = new ArrayList<>();
    //
    // @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    // private List<Billing> bills = new ArrayList<>();

    // ---------- Getters & Setters ----------
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	
    
    
}