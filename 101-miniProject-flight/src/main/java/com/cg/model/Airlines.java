package com.cg.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
@Entity
@Table(name="Airlines")
public class Airlines {
	 @Id
	    @GeneratedValue(strategy=GenerationType.IDENTITY)
	    private int id;

	    @Column
	    private String name;

	    @OneToMany(mappedBy = "airlines",cascade = CascadeType.ALL) // Correct mapping for List of Entities
	    private List<Flight> flights;

	    @ElementCollection                  // Correct mapping for List of Basic Types
	    @CollectionTable(name = "airline_classes", joinColumns = @JoinColumn(name = "airline_id"))
	    @Column(name = "class_name")
	    private List<String> classes;
	    public Airlines() {
	    	
	    }

public Airlines(int id, String name, List<Flight> flights) {
	super();
	this.id = id;
	this.name = name;
	this.flights = flights;
}

public int getId() {
	return id;
}

public void setId(int id) {
	this.id = id;
}

public String getName() {
	return name;
}

public void setName(String name) {
	this.name = name;
}

public List<Flight> getFlights() {
	return flights;
}

public void setFlights(List<Flight> flights) {
	this.flights = flights;
}
 
}
