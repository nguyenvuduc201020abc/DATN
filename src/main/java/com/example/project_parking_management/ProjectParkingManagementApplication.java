package com.example.project_parking_management;

import com.example.project_parking_management.Calculate.Distance;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProjectParkingManagementApplication {

	public static void main(String[] args) {

//		ConfigurableApplicationContext context = SpringApplication.run(ProjectParkingManagementApplication.class, args);
//		AccountService accountService = context.getBean(AccountService.class);
//		String username = "john.doe";
//		String password = "secret";
//		String role = "admin";
//		Accounts accounts = new Accounts(username, password, role);
//		accountService.saveAccount(accounts);
//		context.close();
//		AccountService accountService = new AccountService();

//		String token = JwtUtil.generateToken(username, password, role);
//		System.out.println("Token: " + token);
//
//		Claims claims = JwtUtil.decodeToken(token);
//		String decodedUsername = claims.get("username", String.class);
//		String decodedPassword = claims.get("password", String.class);
//		String decodedRole = claims.get("role", String.class);
//
//		System.out.println("Decoded Username: " + decodedUsername);
//		System.out.println("Decoded Password: " + decodedPassword);
//		System.out.println("Decoded Role: " + decodedRole);
//		Claims claims = JwtUtil.decodeToken(pwdecode);
//		String role_decode = (String) claims.get("role");
		SpringApplication.run(ProjectParkingManagementApplication.class, args);

	}

}
