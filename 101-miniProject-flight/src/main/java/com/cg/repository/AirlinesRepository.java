package com.cg.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cg.model.Airlines;

public interface AirlinesRepository extends JpaRepository<Airlines, Integer> {

}
