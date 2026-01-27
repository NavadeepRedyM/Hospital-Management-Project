package com.cg.dto;

import java.util.Date;
public  class FlightDTO {
    
    private int fId;
    private String airlineName;
    private String pickUp;
    private String destination;
    private Date departureDate;
    private String flightClass;
    private double price;
    private long availableSeats;

    public FlightDTO() {
    }

    public FlightDTO(int fId, String airlineName, String pickUp, String destination, 
                     Date departureDate, String flightClass, double price, long availableSeats) {
        this.fId = fId;
        this.airlineName = airlineName;
        this.pickUp = pickUp;
        this.destination = destination;
        this.departureDate = departureDate;
        this.flightClass = flightClass;
        this.price = price;
        this.availableSeats = availableSeats;
    }

    // Getters and Setters
    public int getfId() { 
    	return fId; 
    	}
    public void setfId(int fId) { 
    	this.fId = fId; 
    	}

    public String getAirlineName() {
    	return airlineName;
    	}
    public void setAirlineName(String airlineName) { 
    	this.airlineName = airlineName;
    	}

    public String getPickUp() {
    	return pickUp;
    	}
    public void setPickUp(String pickUp) {
    	this.pickUp = pickUp;
    	}

    public String getDestination() { 
    	return destination; 
    	}
    public void setDestination(String destination) {
    	this.destination = destination; 
    	}

    public Date getDepartureDate() {
    	return departureDate; 
    	}
    public void setDepartureDate(Date departureDate) {
    	this.departureDate = departureDate; 
    	}

    public String getFlightClass() {
    	return flightClass; 
    	}
    public void setFlightClass(String flightClass) {
    	this.flightClass = flightClass;
    	}

    public double getPrice() {
    	return price; 
    	}
    public void setPrice(double price) { 
    	this.price = price; 
    	}

    public long getAvailableSeats() {
    	return availableSeats;
    	}
    public void setAvailableSeats(long availableSeats) { 
    	this.availableSeats = availableSeats; 
    	}
}
