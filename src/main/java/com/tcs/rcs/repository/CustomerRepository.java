package com.tcs.rcs.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tcs.rcs.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
