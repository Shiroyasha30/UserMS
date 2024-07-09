package com.project.user.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.user.Entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByUsername(String username);
}
