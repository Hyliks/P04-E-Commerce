package com.example.demo.controllers;

import java.security.SecureRandom;
import java.util.Optional;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;

@RestController
@RequestMapping("/api/user")
public class UserController {

	public static final Logger log = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private BCryptPasswordEncoder encoder;

	@GetMapping("/id/{id}")
	public ResponseEntity<User> findById(@PathVariable Long id) {
		return ResponseEntity.of(userRepository.findById(id));
	}
	
	@GetMapping("/{username}")
	public ResponseEntity<User> findByUserName(@PathVariable String username) {
		User user = userRepository.findByUsername(username);
		return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
	}
	
	@PostMapping("/create")
	public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
		log.info("Create User request for username {}",createUserRequest.getUsername());

		User user = new User();
		user.setUsername(createUserRequest.getUsername());
		Cart cart = new Cart();
		cartRepository.save(cart);
		user.setCart(cart);
		cart.setUser(user);
		log.debug("Cart created for username {}",createUserRequest.getUsername());

		Random r = new SecureRandom();
		byte[] salt = new byte[20];
		r.nextBytes(salt);
		String saltText = new String(salt);

		if(createUserRequest.getPassword().length() < 7 ||
		!createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())) {
			log.warn("Password policies dont apply for username {}",createUserRequest.getUsername());
			return ResponseEntity.badRequest().build();
		}

		user.setSalt(saltText);
		user.setPassword(encoder.encode(createUserRequest.getPassword() + saltText));
		userRepository.save(user);
		log.info("User with username {} created", createUserRequest.getUsername());
		return ResponseEntity.ok(user);
	}
	
}
