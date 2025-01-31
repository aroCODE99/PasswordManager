package com.aro;

import com.aro.Service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PasswordManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(PasswordManagerApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(UserService userService) {
		return args -> {
			userService.displayUsers();
			System.out.println("Application started....");
		};
	}
}

// now we have finished with the login let's work with the storing the login stuff's
// for that we will need the new database with the one_to_many relationship with the user and userState
// 		password_id int PRIMARY KEY AUTO_INCREMENTED,
// if we created this table we need to make the entity
// Create Table passwords {
// 		user_id NOT NULL,
// 		url VARCHAR(255) NOT NULL,
// 		password VARCHAR(255) NOT NULL,
// 		created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
// 		FOREIGN KEY (user_id) REFERENCES user(user_id)
// };

// now this is going to be my main Passwords database;