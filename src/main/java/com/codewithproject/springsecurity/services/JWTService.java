package com.codewithproject.springsecurity.services;

import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;

import com.codewithproject.springsecurity.entity.User;

public interface JWTService {

	 String generateToken(UserDetails userDetails);
	 
	 String extractUserName(String token);

	 boolean isTokenValid(String token,UserDetails userDetails);

	String generateRefreshToken(Map<String, Object> extractClaims,User userDetails);
}
