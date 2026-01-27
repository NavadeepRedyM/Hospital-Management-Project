package com.cg.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cg.model.Flight;

public interface FlightRepository extends JpaRepository<Flight, Integer> {
	
	 //List<Flight> findByPickUpContainingIgnoreCaseAndDestinationContainingIgnoreCase(String pickUp, String destination);
	
	@Query("SELECT f FROM Flight f WHERE " +
		       "(:pickUp IS NULL OR f.pickUp LIKE %:pickUp%) AND " +
		       "(:dest IS NULL OR f.destination LIKE %:dest%) AND " +
		       "(:fclass IS NULL OR f.fclass = :fclass OR :fclass = '')")
		List<Flight> findFlightsByCriteria(@Param("pickUp") String pickUp, 
		                                   @Param("dest") String destination, 
		                                   @Param("fclass") String fclass);
}
