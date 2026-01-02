package com.tcs.rcs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcs.rcs.dto.CreateCustomerDto;
import com.tcs.rcs.dto.CustomerDto;
import com.tcs.rcs.service.CustomerService;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

	@Autowired
	private CustomerService customerService;

	@PostMapping
	@PreAuthorize("hasAnyRole('ADMIN','SALES')")
	public CustomerDto create(@RequestBody CreateCustomerDto dto) {
		return customerService.create(dto);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN','SALES')")
	public CustomerDto update(@PathVariable Long id, @RequestBody CreateCustomerDto dto) {
		return customerService.update(id, dto);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN','SALES','TPM')")
	public CustomerDto get(@PathVariable Long id) {
		return customerService.get(id);
	}

	@GetMapping
	@PreAuthorize("hasAnyRole('ADMIN','SALES','TPM')")
	public Page<CustomerDto> list(Pageable pageable) {
		return customerService.list(pageable);
	}
}
