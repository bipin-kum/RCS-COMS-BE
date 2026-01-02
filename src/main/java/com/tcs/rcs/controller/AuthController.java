package com.tcs.rcs.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcs.rcs.dto.LoginRequest;
import com.tcs.rcs.dto.RegisterRequest;
import com.tcs.rcs.entity.Customer;
import com.tcs.rcs.entity.Role;
import com.tcs.rcs.entity.User;
import com.tcs.rcs.repository.RoleRepository;
import com.tcs.rcs.repository.UserRepository;
import com.tcs.rcs.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	private AuthService authService;
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private PasswordEncoder encoder;

	@PostMapping("/login")
	public String login(@Valid @RequestBody LoginRequest req) {
		return authService.authenticate(req.getUserName(), req.getPassword());
	}

	@PostMapping("/register")
	public void register(@Valid @RequestBody RegisterRequest req) {
		if (userRepo.existsByUserName(req.getUserName())) {
			throw new IllegalArgumentException("Username already exists");
		}

		Set<Role> roles = roleRepository.findByNameIn(req.getRoleNames());
		if (roles.isEmpty()) {
			throw new IllegalArgumentException("Invalid roles: " + req.getRoleNames());
		}

		User user = new User();
		user.setUserName(req.getUserName());
		user.setPasswordHash(encoder.encode(req.getPassword()));
		user.setRoles(roles);
		if (req.getCustomerId() != null) {
			user.setCustomer(new Customer(req.getCustomerId()));
		}
		userRepo.save(user);
	}
}
