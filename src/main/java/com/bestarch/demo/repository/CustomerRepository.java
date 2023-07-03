package com.bestarch.demo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bestarch.demo.domain.Customer;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, String> {
    
}