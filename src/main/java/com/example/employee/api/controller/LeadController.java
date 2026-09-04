package com.example.employee.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.employee.api.common.Constants;
import com.example.employee.api.dto.ApiResponse;
import com.example.employee.api.entity.Lead;
import com.example.employee.api.exception.LeadException;
import com.example.employee.api.service.LeadService;

@RestController
@RequestMapping("/api/leads")
public class LeadController {

	private static final Logger logger = LoggerFactory.getLogger(LeadController.class);

	@Autowired
	private LeadService leadService;

	@GetMapping
	public ResponseEntity<ApiResponse<Page<Lead>>> getLeads(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "100") int size) {
		try {

			if (size <= 0 || size > 100) {
				logger.warn("Invalid page size: {}| page{} ", size, page);
				throw new LeadException("Page size must be between 1 and 100");
			}

			Page<Lead> leads = leadService.getLeads(page, size);

			return ResponseEntity.ok().body(new ApiResponse<Page<Lead>>(HttpStatus.OK.value(), Constants.OK, leads));

		} catch (Exception e) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Unable to fetch lead records", null));
		}
	}
}