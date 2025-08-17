package com.ecommerce.user.controller;


import com.ecommerce.user.dto.UserRequest;
import com.ecommerce.user.dto.UserResponse;
import com.ecommerce.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/users")
public class UserController {

    private final UserService userService;


    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return new ResponseEntity<>(userService.fetchAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUsers(@PathVariable String  id) {
        UserResponse user = userService.fetchUser(id).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            return ResponseEntity.ok(user);
        }
    }


    @PostMapping
    public String createUser(@RequestBody UserRequest user) {
        userService.addUser(user);
        return "User created successfully";
    }


    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable String  id, @RequestBody UserRequest updatedUserRequest) {

        boolean isUpdated = userService.updateUser(id, updatedUserRequest);
        if (isUpdated) {
            return ResponseEntity.ok("User updated successfully!!");
        } else {
            return ResponseEntity.notFound().build();
        }

    }



}
