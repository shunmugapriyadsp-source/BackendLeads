package com.example.employee.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.employee.api.entity.Lead;
import com.example.employee.api.repository.LeadRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class LeadService {

	private static final Logger logger = LoggerFactory.getLogger(LeadService.class);

	@Autowired
	private LeadRepository leadRepository;

	public Page<Lead> getLeads(int page, int size) {
		try {

			logger.info("Fetching leads - page: {}, size: {}", page, size);

			Pageable pageable = PageRequest.of(page, size);

			Page<Lead> leads = leadRepository.findAll(pageable);

			logger.info("Successfully fetched {} leads", leads.getNumberOfElements());

			return leads;
		} catch (Exception e) {
			throw new RuntimeException("Unable to fetch lead records", e);
		}
	}
}