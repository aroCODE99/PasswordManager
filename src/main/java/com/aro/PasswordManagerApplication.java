package com.aro;

import com.aro.Service.OTPService;
import io.jsonwebtoken.Jwts;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Base64;

@SpringBootApplication
@EnableScheduling
public class PasswordManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(PasswordManagerApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(OTPService otpService) {
		return args -> {
			System.out.println("Application started....");
		};
	}

	// now let's make the function to make the jwt token secret key
	public void generateAecKey() {
		try {
			KeyGenerator keygen = KeyGenerator.getInstance("AES");
			keygen.init(256);
			SecretKey secretKey = keygen.generateKey(); // now this is in binary form we have to convert it to the human-readable form to store it
			String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
			System.out.println(encodedKey);
			System.out.println(encodedKey.getBytes().length);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void generateJwtKey() {
		SecretKey secretKey = Jwts.SIG.HS384.key().build();
		String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
		byte[] bytesToCheck = Base64.getDecoder().decode(encodedKey);
		System.out.println(encodedKey);
		System.out.println("This is the " + bytesToCheck.length + " bytes");
	}
}

// now what is the plan of doing