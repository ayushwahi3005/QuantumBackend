package com.quantumai.customer.security;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.quantumai.customer.entity.Customer;
import com.quantumai.customer.repository.CustomerRepository;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	
	private final JwtService jwtService;

	@Autowired
	private CustomerRepository customerRepository; 
	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,@NonNull FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		final String authHeader=request.getHeader("Authorization");
		System.out.println("------------------------>"+authHeader);
		final String jwt;
		final String userEmail;
		if(authHeader==null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}
		jwt=authHeader.substring(7);
		userEmail=jwtService.extractUserEmail(jwt);
		if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
	      
		      Optional<Customer> customer=customerRepository.findByEmail(userEmail);
//		      UserDetails userDetails = this.userDetailsService.loadUserByUsername(customer.get());
		      if (jwtService.isTokenValid(jwt, customer.get())) {
		        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
		        	customer.get(),
		            null,
		            customer.get().getAuthorities()
		        );
		        authToken.setDetails(
		            new WebAuthenticationDetailsSource().buildDetails(request)
		        );
		        SecurityContextHolder.getContext().setAuthentication(authToken);
		      }
		    }
		    filterChain.doFilter(request, response);
		  }
	}


