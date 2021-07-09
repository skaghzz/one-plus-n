package com.example.oneplusn.repository;

import java.util.List;

import com.example.oneplusn.domain.Customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Query("select DISTINCT c from Customer c join fetch c.savingsAccount")
    public List<Customer> findAllFetchJoin();
}
