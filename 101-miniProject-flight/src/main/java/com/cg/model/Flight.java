package com.cg.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="flights")
public class Flight {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
private int fId;
	@Column
private String entreprise;
	@Column
private float duration;
	@Column
	private String pickUp;
	@Column
	private String destination;
	@Column
	private long availableSeats;
	@Column
 	private double price;
	@Column
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
  	private Date departureDate;
	@ManyToOne                       
    @JoinColumn(name = "airline_id")
    private Airlines airlines; 

	@Column
	private String fclass;
public Flight() {
	
}


public Flight(int fId, String entreprise, float duration, String pickUp, String destination, long availableSeats,
		double price, Date departureDate, Airlines airlines, String fclass) {
	super();
	this.fId = fId;
	this.entreprise = entreprise;
	this.duration = duration;
	this.pickUp = pickUp;
	this.destination = destination;
	this.availableSeats = availableSeats;
	this.price = price;
	this.departureDate = departureDate;
	this.airlines = airlines;
	this.fclass = fclass;
}


public int getfId() {
	return fId;
}
public void setfId(int fId) {
	this.fId = fId;
}
public String getEntreprise() {
	return entreprise;
}
public void setEntreprise(String entreprise) {
	this.entreprise = entreprise;
}
public float getDuration() {
	return duration;
}
public void setDuration(float duration) {
	this.duration = duration;
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
public long getAvailableSeats() {
	return availableSeats;
}
public void setAvailableSeats(long availableSeats) {
	this.availableSeats = availableSeats;
}
public double getPrice() {
	return price;
}
public void setPrice(double price) {
	this.price = price;
}
public Date getDepartureDate() {
	return departureDate;
}
public void setDepartureDate(Date departureDate) {
	this.departureDate = departureDate;
}




public Airlines getAirlines() {
	return airlines;
}


public void setAirlines(Airlines airlines) {
	this.airlines = airlines;
}


public String getFclass() {
	return fclass;
}


public void setFclass(String fclass) {
	this.fclass = fclass;
}





}
