package com.cg.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.dto.FlightDTO;
import com.cg.model.Flight;
import com.cg.repository.FlightRepository;

@Service
public class FlightService {

    @Autowired
    FlightRepository repository;

    private FlightDTO convertToDTO(Flight flight) {
        return new FlightDTO(
            flight.getfId(),          
            flight.getEntreprise(),    
            flight.getPickUp(),        
            flight.getDestination(),   
            flight.getDepartureDate(), 
            flight.getFclass(),       
            flight.getPrice(),        
            flight.getAvailableSeats() 
        );
    }



    public List<FlightDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<Flight> findFlightById(int id) {
        return repository.findById(id);
    }

    public Flight create(Flight flight) {
        return repository.save(flight);
    }

    public void delete(int id) {
        repository.deleteById(id);
    }

    public Flight update(Flight flight) {
        return repository.save(flight);
    }

    public List<FlightDTO> searchFlights(String pickUp, String destination, String fclass) {
        List<Flight> flights;
        if ((pickUp == null || pickUp.isEmpty()) && 
            (destination == null || destination.isEmpty()) && 
            (fclass == null || fclass.isEmpty())) {
            flights = repository.findAll();
        } else {
            flights = repository.findFlightsByCriteria(pickUp, destination, fclass);
        }
        
        return flights.stream()
                      .map(this::convertToDTO)
                      .collect(Collectors.toList());
    }
}
