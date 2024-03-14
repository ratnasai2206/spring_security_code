package com.codewithproject.springsecurity.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.codewithproject.springsecurity.entity.User;
import com.codewithproject.springsecurity.util.UserRole;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	
	Optional<User> findByEmail(String email);
	
	User findByRole(UserRole role);
}
