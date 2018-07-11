package com.softserve.mosquito.controllers;

import com.softserve.mosquito.dtos.UserDto;
import com.softserve.mosquito.dtos.UserLoginDto;
import com.softserve.mosquito.security.JwtTokenProvider;
import com.softserve.mosquito.security.LoginResponse;
import com.softserve.mosquito.services.api.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/")
@Api(description = "Controller for authorization user by using spring security")
public class AuthController {

    private AuthenticationManager authenticationManager;

    private UserService userService;

    private PasswordEncoder passwordEncoder;

    private JwtTokenProvider tokenProvider;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserService userService, PasswordEncoder passwordEncoder, JwtTokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody @Valid UserLoginDto loginRequest, HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse(false, "Login or password is not correct."));
        }

        if (!userService.isConfirmed(loginRequest.getEmail())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse(false, "Please check your email for confirmation."));
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        response.setHeader("Authorization", "Bearer " + jwt);

        UserDto authenticatedUser = userService.getByEmail(loginRequest.getEmail());
        return ResponseEntity.ok(UserDto.builder().id(authenticatedUser.getId())
                .firstName(authenticatedUser.getFirstName())
                .lastName(authenticatedUser.getLastName())
                .build());
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody @Valid UserDto signUpRequest) {
        if (userService.getByEmail(signUpRequest.getEmail()) != null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    body(new LoginResponse(false, "Email Address already in use!"));

        if (!signUpRequest.getPassword().equals(signUpRequest.getConfirmPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    body(new LoginResponse(false, "Password and Confirm Password should be same"));
        }

        signUpRequest.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        userService.save(signUpRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(signUpRequest);
    }

    @GetMapping("/activate/{key}")
    public ResponseEntity activateAccount(@PathVariable("key") String key) {

        userService.activateUser(key);

        return ResponseEntity.ok().build();
    }
}
