package com.suvel.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.suvel.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer>{

	
	public Customer findByName(String name);
	
	
}
