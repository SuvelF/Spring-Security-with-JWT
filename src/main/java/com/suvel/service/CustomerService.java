package com.suvel.service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.suvel.entity.Customer;
import com.suvel.repo.CustomerRepository;

@Service
public class CustomerService implements UserDetailsService{

	
	@Autowired
	private CustomerRepository customerRepository;
	
	
	@Override
	public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
		
		Customer c = customerRepository.findByName(name);
		
		if (c == null) {
	        throw new UsernameNotFoundException("No customer found with Name: " + name);
	    }
		
		return new User(c.getName(), c.getPassword(), Collections.EMPTY_LIST);
	}

	
	
}
