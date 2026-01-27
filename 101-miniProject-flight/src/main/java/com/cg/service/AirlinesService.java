package com.cg.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.model.Airlines;
import com.cg.repository.AirlinesRepository;

@Service
public class AirlinesService {
@Autowired
AirlinesRepository airRep;

public List<Airlines> findAll(){
	return airRep.findAll();
	
}
}
