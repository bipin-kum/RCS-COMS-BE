package com.tcs.rcs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.tcs.rcs.dto.CreateCustomerDto;
import com.tcs.rcs.dto.CustomerDto;
import com.tcs.rcs.entity.Customer;
import com.tcs.rcs.repository.CustomerRepository;

@Service
public class CustomerService {

	@Autowired
	private CustomerRepository customerRepository;

	public CustomerDto create(CreateCustomerDto dto) {
		Customer customer = new Customer();
		customer.setName(dto.name());
		Customer saved = customerRepository.save(customer);
		return new CustomerDto(saved.getId(), saved.getName());
	}

	public CustomerDto update(Long id, CreateCustomerDto dto) {
		Customer customer = customerRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Customer not found: " + id));
		customer.setName(dto.name());
		Customer updated = customerRepository.save(customer);
		return new CustomerDto(updated.getId(), updated.getName());
	}

	public CustomerDto get(Long id) {
		Customer customer = customerRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Customer not found: " + id));
		return new CustomerDto(customer.getId(), customer.getName());
	}

	public Page<CustomerDto> list(Pageable pageable) {
		return customerRepository.findAll(pageable).map(cust -> new CustomerDto(cust.getId(), cust.getName()));
	}
}
