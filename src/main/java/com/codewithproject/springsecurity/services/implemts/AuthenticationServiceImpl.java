package com.codewithproject.springsecurity.services.implemts;

import java.util.HashMap;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.codewithproject.springsecurity.dto.JwtAuthenticationResponse;
import com.codewithproject.springsecurity.dto.RefreshTokenRequest;
import com.codewithproject.springsecurity.dto.SignInRequest;
import com.codewithproject.springsecurity.dto.SignUpRequest;
import com.codewithproject.springsecurity.entity.User;
import com.codewithproject.springsecurity.repository.UserRepository;
import com.codewithproject.springsecurity.services.AuthenticationService;
import com.codewithproject.springsecurity.services.JWTService;
import com.codewithproject.springsecurity.util.UserRole;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

	private final UserRepository userRepository;
	
	private final PasswordEncoder passwordEncoder;
	
	private final AuthenticationManager authenticationManager;
	
	private final JWTService jwtService;
	
	public User singup(SignUpRequest signUpRequest ) {
		User user=new User();
		user.setEmail(signUpRequest.getEmail());
		user.setFirstName(signUpRequest.getFirstName());
		user.setLastName(signUpRequest.getLastName());
		user.setRole(UserRole.USER);
		user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
		return userRepository.save(user);
	}
	
	public JwtAuthenticationResponse signin(SignInRequest signInRequest) {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getEmail(), signInRequest.getPassword()));
		var user= userRepository.findByEmail(signInRequest.getEmail()).orElseThrow(()-> new IllegalArgumentException("Invalid email or password"));
		var jwt=jwtService.generateToken(user);
		var refreshToken=jwtService.generateRefreshToken(new HashMap<String, Object>(),user);
		
		JwtAuthenticationResponse authenticationResponse=new JwtAuthenticationResponse();
		authenticationResponse.setToken(jwt);
		authenticationResponse.setRefreshToken(refreshToken);
		return authenticationResponse;
	}
	
	public JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
		String userEmail=jwtService.extractUserName(refreshTokenRequest.getToken());
		User user=userRepository.findByEmail(userEmail).orElse(null);
		if(jwtService.isTokenValid(refreshTokenRequest.getToken(), user)) {
			var jwt=jwtService.generateToken(user);
			JwtAuthenticationResponse authenticationResponse=new JwtAuthenticationResponse();
			authenticationResponse.setToken(jwt);
			authenticationResponse.setRefreshToken(refreshTokenRequest.getToken());
			return authenticationResponse;
		}
		return null;
	}
}
