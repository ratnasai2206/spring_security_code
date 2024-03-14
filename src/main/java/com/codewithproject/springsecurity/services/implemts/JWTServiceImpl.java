package com.codewithproject.springsecurity.services.implemts;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.codewithproject.springsecurity.entity.User;
import com.codewithproject.springsecurity.services.JWTService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTServiceImpl implements JWTService {

	public String generateToken(UserDetails userDetails) {
		return Jwts.builder().setSubject(userDetails.getUsername()).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
				.signWith(getSigKey(), SignatureAlgorithm.HS256).compact();
	}

	private Key getSigKey() {
		byte[] key = Decoders.BASE64.decode("dshufvkthgiulearhgfuhsuilhguiaehgiuerhgauhgliahegaghu");
		return Keys.hmacShaKeyFor(key);
	}

	private <T> T extractClaim(String token,Function<Claims, T>function){
		final Claims claims=extractAllClaim(token);
		return function.apply(claims);
	}

	private Claims extractAllClaim(String token) {
		
		return Jwts.parserBuilder().setSigningKey(getSigKey()).build().parseClaimsJws(token).getBody();
	}
	
	public String extractUserName(String token) {
		return extractClaim(token, Claims::getSubject);
	}
	
	public boolean isTokenValid(String token,UserDetails userDetails) {
		final String username=extractUserName(token);
		return (username.equals(userDetails.getUsername())&& !isTokenExpired(token));
	}

	private boolean isTokenExpired(String token) {
		
		return extractClaim(token, Claims::getExpiration).before(new Date());
	}

	public String generateRefreshToken(Map<String, Object> extractClaims,User userDetails) {
		return Jwts.builder().setClaims(extractClaims).setSubject(userDetails.getUsername()).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 604800000))
				.signWith(getSigKey(), SignatureAlgorithm.HS256).compact();
	}

	
}
