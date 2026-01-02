package com.tcs.rcs.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tcs.rcs.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByUserName(String userName);

	boolean existsByUserName(String userName);
}
