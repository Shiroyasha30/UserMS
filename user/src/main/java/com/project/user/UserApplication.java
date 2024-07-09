package com.project.user;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.project.user.Entity.User;
import com.project.user.enums.LevelOfUser;
import com.project.user.repo.UserRepository;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class UserApplication {
	
	@Autowired
	private UserRepository userRepository;
	
	@PostConstruct
	public void init() {
		String pswd = new BCryptPasswordEncoder().encode("pswd");
//		String pswd = "pswd";
		List<User> users = Arrays.asList(
				new User(101L, "user", pswd, 0L, LevelOfUser.LOW.name()),
				new User(102L, "dummy", pswd, 0L, LevelOfUser.LOW.name())
				);
		userRepository.saveAll(users);
	}

	public static void main(String[] args) {
		SpringApplication.run(UserApplication.class, args);
	}

}
