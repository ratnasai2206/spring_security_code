package com.codewithproject.springsecurity.services;

import com.codewithproject.springsecurity.dto.JwtAuthenticationResponse;
import com.codewithproject.springsecurity.dto.RefreshTokenRequest;
import com.codewithproject.springsecurity.dto.SignInRequest;
import com.codewithproject.springsecurity.dto.SignUpRequest;
import com.codewithproject.springsecurity.entity.User;

public interface AuthenticationService {

	User singup(SignUpRequest signUpRequest);

	JwtAuthenticationResponse signin(SignInRequest signInRequest);

	JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
}
