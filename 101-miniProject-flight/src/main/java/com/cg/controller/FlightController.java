package com.cg.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cg.model.Flight;
import com.cg.service.AirlinesService;
import com.cg.service.FlightService;
import com.cg.dto.FlightDTO;
import com.cg.exception.ResourceNotFound;

@Controller
@RequestMapping("/home")
public class FlightController {
	
	@Autowired
	FlightService service;
	
	@Autowired
	AirlinesService airlinesService;
	
	@GetMapping({"/", "/login"})
    public String showInitialPage() {
	   return "login";
    }
	
	@GetMapping("/user-index")
    public String showUserDashboard(Model model) {
		 List<FlightDTO> allFlights = service.findAll();
		 model.addAttribute("flights", allFlights);
	     return "user-index";
    }
	
	@GetMapping("/admin-index")
    public String showAdminDashboard(Model model) {
		 List<FlightDTO> allFlights = service.findAll();
		 model.addAttribute("flights", allFlights);
	     return "admin-index";
    }
	 
	@GetMapping("/add")
	public String showAddFlight(Model model) {
		 model.addAttribute("flight", new Flight());
		 model.addAttribute("airlinesList", airlinesService.findAll());
		 return "add-flight";
	}

	@PostMapping("/create")
	public String createFlight(Flight flight) {
		service.create(flight);
		return "redirect:/home/admin-index";
	}

	@GetMapping("/delete/{fid}")
	public String deleteFlight(@PathVariable("fid") int id) throws ResourceNotFound {
		if (!service.findFlightById(id).isPresent()) {
			throw new ResourceNotFound("Flight ID " + id + " not found for deletion.");
		}
		service.delete(id);
		return "redirect:/home/admin-index";
	}
	
	@GetMapping("/edit/{fid}")
	public String getFlightById(@PathVariable("fid") int id, Model model) throws ResourceNotFound {
		Flight flight = service.findFlightById(id)
                .orElseThrow(() -> new ResourceNotFound("Flight with ID " + id + " not found"));
        
		model.addAttribute("flight", flight);
		model.addAttribute("airlinesList", airlinesService.findAll());
		return "edit-flight";
	}
	
	@PostMapping("/update")
	public String updateFlight(Flight flight) {
		service.update(flight);
		return "redirect:/home/admin-index";
	}
	
	
	@GetMapping("/search")
	public String searchFlights(@RequestParam(value = "pickUp", required = false) String pickUp, 
	                            @RequestParam(value = "destination", required = false) String destination,
	                            @RequestParam(value = "fclass", required = false) String fclass, 
	                            Model model) {
	    List<FlightDTO> searchResults = service.searchFlights(pickUp, destination, fclass);
	    
	    model.addAttribute("flights", searchResults);
	    return "admin-index";
	}

	
	@GetMapping("/flights/book/{id}")
	public String bookFlight(@PathVariable("id") int id, Model model) throws ResourceNotFound {
	    Optional<Flight> flight = service.findFlightById(id);
	    
	    if (flight.isPresent()) {
	        model.addAttribute("message", "Flight Booked Successfully!");
	        model.addAttribute("flightId", id);
	        model.addAttribute("airline", flight.get().getEntreprise());
	        
	        return "booking-success";
	    } else {
	        throw new ResourceNotFound("Flight not found with ID: " + id);
	    }
	}

}
