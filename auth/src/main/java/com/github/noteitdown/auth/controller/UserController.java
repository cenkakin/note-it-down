package com.github.noteitdown.auth.controller;

import com.github.noteitdown.auth.domain.User;
import com.github.noteitdown.auth.exception.BadRequestException;
import com.github.noteitdown.auth.exception.EmailAlreadyExistsException;
import com.github.noteitdown.auth.request.SignUpRequest;
import com.github.noteitdown.auth.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController("/users")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping
	public ResponseEntity<?> createUser(@Valid @RequestBody SignUpRequest signUpRequest) {
		User user = User.fromSignUpRequest(signUpRequest);
		try {
			userService.registerUser(user);
		} catch (EmailAlreadyExistsException e) {
			throw new BadRequestException(e.getMessage());
		}
		URI location = ServletUriComponentsBuilder
			.fromCurrentContextPath().path("/users/{email}")
			.buildAndExpand(user.getEmail()).toUri();
		return ResponseEntity
			.created(location)
			.build();
	}
}
