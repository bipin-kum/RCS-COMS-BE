package com.tcs.rcs.controller;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcs.rcs.dto.CreateUserDto;
import com.tcs.rcs.dto.UserDto;
import com.tcs.rcs.entity.Role;
import com.tcs.rcs.entity.User;
import com.tcs.rcs.repository.RoleRepository;
import com.tcs.rcs.repository.UserRepository;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@PostMapping("/users")
	@PreAuthorize("hasRole('ADMIN')")
	public UserDto createUser(@RequestBody CreateUserDto dto) {

		User user = new User();
		user.setUserName(dto.userName());
		user.setPasswordHash(passwordEncoder.encode(dto.password()));
		user.setEnabled(true);

		List<Role> roles = roleRepository.findAllById(dto.roleIds());
		user.setRoles(Set.copyOf(roles));
		User saved = userRepository.save(user);

		return new UserDto(saved.getId(), saved.getUserName(), saved.isEnabled(),
				saved.getRoles().stream().map(Role::getName).collect(Collectors.toSet()));
	}

	@GetMapping("/users")
	@PreAuthorize("hasRole('ADMIN')")
	public Page<UserDto> listUsers(Pageable pageable) {
		return userRepository.findAll(pageable).map(user -> new UserDto(user.getId(), user.getUserName(),
				user.isEnabled(), user.getRoles().stream().map(Role::getName).collect(Collectors.toSet())));
	}

	@GetMapping("/users/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public UserDto getUser(@PathVariable Long id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
		return new UserDto(user.getId(), user.getUserName(), user.isEnabled(),
				user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()));
	}

	@GetMapping("/roles")
	@PreAuthorize("hasRole('ADMIN')")
	public List<String> listRoles() {
		return roleRepository.findAll().stream().map(Role::getName).collect(Collectors.toList());
	}
}
