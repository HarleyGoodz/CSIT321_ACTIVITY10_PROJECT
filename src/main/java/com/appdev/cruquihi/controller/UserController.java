package com.appdev.cruquihi.controller;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.appdev.cruquihi.entity.UserEntity;
import com.appdev.cruquihi.repository.UserRepository;
import com.appdev.cruquihi.service.UserService;

@RestController
@RequestMapping(path = "/api/user")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    @Autowired
    UserService sserv;

    // CREATE
    @PostMapping("/add")
    public ResponseEntity<?> addUser(@RequestBody UserEntity newUser) {
        try {
            UserEntity saved = sserv.createUser(newUser);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Error creating user");
        }
    }

    // READ ALL
    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers() {
        List<UserEntity> users = sserv.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable int id) {
        try {
            UserEntity user = sserv.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body("User not found");
        }
    }

    // UPDATE
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable int id, @RequestBody UserEntity newData) {
        try {
            UserEntity updated = sserv.updateUser(id, newData);
            return ResponseEntity.ok(updated);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body("User not found");
        }
    }

    // DELETE
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable int id) {
        String msg = sserv.deleteUser(id);
        return ResponseEntity.ok(msg);
    }

    // LOGIN (DO NOT TOUCH)
    @PostMapping("/login")
    public String login(@RequestBody UserEntity loginData) {

        try {
            String input = loginData.getEmailAddress();
            String password = loginData.getPassword();

            Optional<UserEntity> user = sserv.findByEmailOrFullname(input);

            if (user.isEmpty()) {
                return "User not found";
            }

            if (!user.get().getPassword().equals(password)) {
                return "Wrong password";
            }

            return "Success";

        } catch (Exception e) {
            return "Server error";
        }
    }
}
