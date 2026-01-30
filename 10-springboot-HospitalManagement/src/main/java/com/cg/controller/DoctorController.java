package com.cg.controller; // It's best practice to put controllers in a different package

import com.cg.model.Doctor;
import com.cg.service.DoctorService; // Using the concrete class provided by the user
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController // Marks this class as a Spring REST Controller
@RequestMapping("/api/doctors") // Base URL path for all endpoints in this controller
public class DoctorController {

	private final DoctorService doctorService;

	@Autowired // Constructor injection is recommended over field injection
	public DoctorController(DoctorService doctorService) {
		this.doctorService = doctorService;
	}

	// Endpoint to create a new Doctor
	@PostMapping
	public ResponseEntity<Doctor> createDoctor(@RequestBody Doctor doctor) {
		Doctor savedDoctor = doctorService.saveDoctor(doctor);
		return new ResponseEntity<>(savedDoctor, HttpStatus.CREATED);
	}

	// Endpoint to get a Doctor by ID
	@GetMapping("/{id}")
	public ResponseEntity<Doctor> getDoctorById(@PathVariable Long id) {
		Optional<Doctor> doctor = doctorService.findDoctorById(id);
		return doctor.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	// Endpoint to get all Doctors
	@GetMapping
	public List<Doctor> getAllDoctors() {
		return doctorService.findAllDoctors();
	}

	// Endpoint to find doctors by qualification (using a request parameter)
	@GetMapping("/search")
	public List<Doctor> getDoctorsByQualification(@RequestParam String qualification) {
		return doctorService.findDoctorsByQualification(qualification);
	}

	// Endpoint to delete a Doctor by ID
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
		doctorService.deleteDoctor(id);
		return ResponseEntity.noContent().build(); // HTTP 204 No Content
	}
}
