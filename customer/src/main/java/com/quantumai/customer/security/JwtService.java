package com.quantumai.customer.security;

import java.security.Key;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {
	
	  @Value("${application.security.jwt.secret-key}")
	  private String secretKey;
	  @Value("${application.security.jwt.expiration}")
	  private long jwtExpiration;
	  @Value("${application.security.jwt.refresh-token.expiration}")
	  private long refreshExpiration;

	  public String extractUserEmail(String token) {
	    return extractClaim(token, Claims::getSubject);
	  }

	  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
	    final Claims claims = extractAllClaims(token);
	    return claimsResolver.apply(claims);
	  }

	  public String generateToken(UserDetails userDetails) {
		  Map<String,Object> myMap=new HashMap<String,Object>();
		  myMap.put("Role", userDetails.getAuthorities());
	    return generateToken(myMap, userDetails);
	  }

	  public String generateToken(
	      Map<String, Object> extraClaims,
	      UserDetails userDetails
	  ) {
		 
	    return buildToken(extraClaims, userDetails, jwtExpiration);
	  }

	  public String generateRefreshToken(
	      UserDetails userDetails
	  ) {
		Map<String,Object> myMap=new HashMap<String,Object>();
		myMap.put("Role", userDetails.getAuthorities());

	    return buildToken(myMap, userDetails, refreshExpiration);
	  }

	  private String buildToken(
	          Map<String, Object> extraClaims,
	          UserDetails userDetails,
	          long expiration
	  ) {
		 
	    return Jwts
	            .builder()
	            .setClaims(extraClaims)
	            .setSubject(userDetails.getUsername())
	            .setIssuedAt(new Date(System.currentTimeMillis()))
	            .setExpiration(new Date(System.currentTimeMillis() + expiration))
	            .signWith(getSignInKey(), SignatureAlgorithm.HS256)
	            .compact();
	  }

	  public boolean isTokenValid(String token, UserDetails userDetails) {
	    final String userEmail = extractUserEmail(token);
	    return (userEmail.equals(userDetails.getUsername())) && !isTokenExpired(token);
	  }

	  private boolean isTokenExpired(String token) {
	    return extractExpiration(token).before(new Date());
	  }

	  private Date extractExpiration(String token) {
	    return extractClaim(token, Claims::getExpiration);
	  }

	  private Claims extractAllClaims(String token) {
	    return Jwts
	        .parserBuilder()
	        .setSigningKey(getSignInKey())
	        .build()
	        .parseClaimsJws(token)
	        .getBody();
	  }

	  private Key getSignInKey() {
	    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
	    return Keys.hmacShaKeyFor(keyBytes);
	  }

}
