package com.example.oneplusn.repository;

import com.example.oneplusn.domain.Customer;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
