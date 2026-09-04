package com.example.employee.api.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.employee.api.entity.Lead;

public interface LeadRepository extends JpaRepository<Lead, String> {
	
	Page<Lead> findAll(Pageable pageable);


}