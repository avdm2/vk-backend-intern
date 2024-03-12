package com.vk.security.services;

import com.vk.security.entities.User;
import com.vk.security.exception.UsernameAlreadyExistsException;
import com.vk.security.repositories.UserRepository;
import com.vk.security.dto.JwtAuthenticationResponse;
import com.vk.security.dto.SignInRequest;
import com.vk.security.dto.SignUpRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                                 JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public JwtAuthenticationResponse signUp(SignUpRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new UsernameAlreadyExistsException("User with such username is already exists.");
        }

        User user = new User().setUsername(request.getUsername())
                .setPassword(passwordEncoder.encode(request.getPassword()))
                .setRole(request.getRole());
        userRepository.save(user);
        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse().setToken(jwt);
    }

    public JwtAuthenticationResponse signIn(SignInRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password."));
        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse().setToken(jwt);
    }
}
