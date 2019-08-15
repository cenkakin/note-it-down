package com.github.noteitdown.auth.controller;

import com.github.noteitdown.auth.domain.User;
import com.github.noteitdown.auth.exception.BadRequestException;
import com.github.noteitdown.auth.exception.EmailAlreadyExistsException;
import com.github.noteitdown.auth.request.SignUpRequest;
import com.github.noteitdown.auth.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public Mono<ResponseEntity<Object>> createUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        return userService.registerUser(User.fromSignUpRequest(signUpRequest))
                .map(u -> ResponseEntity.ok().build())
                .onErrorMap(EmailAlreadyExistsException.class,
                        e -> new BadRequestException(e.getMessage()));
    }
}
