package com.quantummaintenance.workorder.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.quantummaintenance.workorder.entity.Customer;
import com.quantummaintenance.workorder.controller.WorkOrderAPI;

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
	private WorkOrderAPI workOrderAPI; 
	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,@NonNull FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpServletResponse res = (HttpServletResponse) response;
        HttpServletRequest req = (HttpServletRequest) request;
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods",
                "ACL, CANCELUPLOAD, CHECKIN, CHECKOUT, COPY, DELETE, GET, HEAD, LOCK, MKCALENDAR, MKCOL, MOVE, OPTIONS, POST, PROPFIND, PROPPATCH, PUT, REPORT, SEARCH, UNCHECKOUT, UNLOCK, UPDATE, VERSION-CONTROL");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers",
                "Origin, X-Requested-With, Content-Type, Accept, Key, Authorization");

        if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
            res.setStatus(HttpServletResponse.SC_OK);
        } else {
        	final String authHeader=request.getHeader("Authorization");
    		System.out.println("------------------------>"+authHeader);
    		final String jwt;
    		final String userEmail;
    		if(authHeader==null || !authHeader.startsWith("Bearer ")) {
    			filterChain.doFilter(request, response);
    			return;
    		}
    		jwt=authHeader.substring(7);
    		System.out.println("JWT->"+jwt);
    		userEmail=jwtService.extractUserEmail(jwt);
    		if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
    	      
    		      ResponseEntity<Customer> customer=workOrderAPI.getCustomerDetails(userEmail, authHeader);
//    		      UserDetails userDetails = this.userDetailsService.loadUserByUsername(customer.get());
    		      if (jwtService.isTokenValid(jwt, customer.getBody())) {
    		        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
    		        	customer.getBody(),
    		            null,
    		            customer.getBody().getAuthorities()
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
	}


