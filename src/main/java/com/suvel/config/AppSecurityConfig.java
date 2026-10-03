package com.suvel.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.suvel.filter.AppFilter;
import com.suvel.service.CustomerService;

import jakarta.servlet.Filter;
import lombok.SneakyThrows;

@Configuration
@EnableWebSecurity
public class AppSecurityConfig {

	@Autowired
	private AppFilter filter;
	
	@Autowired
	private CustomerService customerService;
	
	
	@Bean
	public BCryptPasswordEncoder pwdEncoder() {
		
		return new BCryptPasswordEncoder();
	}
	
	
	@Bean
	@SneakyThrows
	public AuthenticationManager authManger(AuthenticationConfiguration config) throws Exception{
		
		return config.getAuthenticationManager();
	}
	
	@Bean
	@SneakyThrows
	public AuthenticationProvider authProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(customerService);
		authenticationProvider.setPasswordEncoder(pwdEncoder());
		return authenticationProvider;
	}
	
	
	@Bean
	@SneakyThrows
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
	
		 http
	        .csrf(csrf -> csrf.disable())

	        .authorizeHttpRequests(auth -> auth
	            .requestMatchers("/api/login", "/api/register").permitAll()
	            .requestMatchers("/api/**").authenticated()
	            .anyRequest().permitAll()
	        )

	        .sessionManagement(session -> session
	            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	        )

	        .authenticationProvider(authProvider())

	        .addFilterBefore(
	             filter,
	            UsernamePasswordAuthenticationFilter.class
	        );

	    return http.build();
		
	}
		
	
}
