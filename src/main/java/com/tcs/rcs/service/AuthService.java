package com.tcs.rcs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tcs.rcs.repository.UserRepository;
import com.tcs.rcs.security.JwtUtil;

@Service
public class AuthService {

	@Autowired
	private UserRepository userRepo;
	@Autowired
	private PasswordEncoder encoder;
	@Autowired
	private JwtUtil jwt;

	public String authenticate(String userName, String password) {
		var user = userRepo.findByUserName(userName)
				.orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
		if (!encoder.matches(password, user.getPasswordHash()) || !user.isEnabled()) {
			throw new IllegalArgumentException("Invalid credentials");
		}
		var roles = user.getRoles().stream().map(r -> r.getName()).toList();
		return jwt.generateToken(userName, roles);
	}
}
