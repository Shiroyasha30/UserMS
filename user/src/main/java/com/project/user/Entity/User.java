package com.project.user.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	long id;
	
	String username;
	String password;
	
	@Column(name = "num_of_ratings")
	long numOfRatings;
	
	@Column(name = "level_of_user")
	String levelOfUser;

	public User(String username, String password, long numOfRatings, String levelOfUser) {
		super();
		this.username = username;
		this.password = password;
		this.numOfRatings = numOfRatings;
		this.levelOfUser = levelOfUser;
	}
	
}
