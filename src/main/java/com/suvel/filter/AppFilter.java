package com.suvel.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.suvel.service.CustomerService;
import com.suvel.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AppFilter extends OncePerRequestFilter{

	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	CustomerService customerService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
	String token = null;	
	String username = null;
	String header =  request.getHeader("Authorization");
		
	if(header!=null && header.startsWith("Bearer ")) {
		
	  token = header.substring(7);
	 username = jwtService.extractUsername(token);
		
		
	}
	
	if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
		
	UserDetails userDetails	= customerService.loadUserByUsername(username);
	
	 Boolean isValid = jwtService.validateToken(token, userDetails);
	 
	 if(isValid) {
		 
		 UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
		
		authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
	    SecurityContextHolder.getContext().setAuthentication(authToken);
	    
		
	}
		
	}
	filterChain.doFilter(request, response);
		
	}

	
	
}
