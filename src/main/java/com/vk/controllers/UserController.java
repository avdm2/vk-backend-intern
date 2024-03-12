package com.vk.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vk.dto.users.CreateUserRequest;
import com.vk.dto.users.CreateUserResponse;
import com.vk.dto.users.GetUserCommentsResponse;
import com.vk.dto.users.GetUserResponse;
import com.vk.dto.users.UpdateUserRequest;
import com.vk.dto.users.UpdateUserResponse;
import com.vk.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasRole('ROLE_USERS_VIEWER')")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<GetUserResponse[]> getAllUsers() throws JsonProcessingException {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<GetUserResponse> getUser(@PathVariable("userId") Integer userId)
            throws JsonProcessingException {
        return ResponseEntity.ok(userService.getUser(userId));
    }

    @GetMapping("/{userId}/comments")
    public ResponseEntity<GetUserCommentsResponse[]> getUserComments(@PathVariable("userId") Integer userId)
            throws JsonProcessingException {
        return ResponseEntity.ok(userService.getUserComments(userId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USERS')")
    public ResponseEntity<CreateUserResponse> createUser(@RequestBody CreateUserRequest request)
            throws JsonProcessingException {
        return ResponseEntity.ok(userService.createUser(request));
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasRole('ROLE_USERS')")
    public ResponseEntity<UpdateUserResponse> updateUser(@PathVariable("userId") Integer userId,
                                                         @RequestBody UpdateUserRequest request)
            throws JsonProcessingException {
        return ResponseEntity.ok(userService.updateUser(userId, request));
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ROLE_USERS')")
    public ResponseEntity<?> deleteUser(@PathVariable("userId") Integer userId) throws JsonProcessingException {
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }
}
