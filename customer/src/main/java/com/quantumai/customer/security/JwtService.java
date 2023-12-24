package com.quantumai.customer.security;

import java.security.Key;
import java.util.function.Function;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	
	private static final String SECRET_KEY="9a8yxfpo15vRQ30k2kGHCHanStQ8vVrcocxvwFPVG7CMLOt5JfNb0toxK45hcWfF";

	public String extractUserEmail(String token) {
		// TODO Auto-generated method stub
		 return extractClaim(token, Claims::getSubject);
	}
	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();
	}
	 public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		    final Claims claims = extractAllClaims(token);
		    return claimsResolver.apply(claims);
		  }
	private Key getSignInKey() {
		// TODO Auto-generated method stub
		byte[] keyBytes=Decoders.BASE64.decode(SECRET_KEY);
		
		return Keys.hmacShaKeyFor(keyBytes);
	}

}
