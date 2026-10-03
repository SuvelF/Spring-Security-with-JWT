package com.suvel.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.service.annotation.GetExchange;

import com.suvel.entity.Customer;
import com.suvel.repo.CustomerRepository;
import com.suvel.service.JwtService;

@RestController
@RequestMapping("/api")
public class CustomerRestController {

	@Autowired
	private CustomerRepository customerRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private JwtService jwt;
	
	
	@GetMapping("/welcome")
	public String welcome() {
		
		return "Welcome To Spring Security JWT";
	}
	
	
	
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody Customer c){
		
		UsernamePasswordAuthenticationToken token =  new UsernamePasswordAuthenticationToken(c.getName(), c.getPassword());
		
		Authentication authenticate = authManager.authenticate(token);
		
		if(authenticate.isAuthenticated()) {
			String jwtToken = jwt.generateToken(c.getName());
			return new ResponseEntity<>(jwtToken, HttpStatus.OK);
		}
		
		return new ResponseEntity<>("Invalid Credentials", HttpStatus.BAD_REQUEST);
		
	}
	
	
	@PostMapping("/register")
	public String registerCustomer(@RequestBody Customer c) {
		
		String encodedPwd = passwordEncoder.encode(c.getPassword());
		c.setPassword(encodedPwd);
		customerRepo.save(c);
		
		return "Customer Registered Successfilly";
		
	}
	
	
}
