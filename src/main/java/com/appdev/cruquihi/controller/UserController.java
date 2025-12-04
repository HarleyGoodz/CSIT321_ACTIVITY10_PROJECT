package com.appdev.cruquihi.controller;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.appdev.cruquihi.entity.UserEntity;
import com.appdev.cruquihi.service.UserService;

@RestController
@RequestMapping(path = "/api/user")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class UserController {

    @Autowired
    private UserService sserv;

    // CREATE (signup)
    @PostMapping("/add")
    public ResponseEntity<?> addUser(@RequestBody UserEntity newUser) {
        try {
            UserEntity saved = sserv.createUser(newUser);
            // hide password before returning
            saved.setPassword(null);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Error creating user: " + e.getMessage());
        }
    }

    // READ ALL
    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers() {
        List<UserEntity> users = sserv.getAllUsers();
        // hide passwords
        users.forEach(u -> u.setPassword(null));
        return ResponseEntity.ok(users);
    }

    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable int id) {
        try {
            UserEntity user = sserv.getUserById(id);
            user.setPassword(null);
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
            updated.setPassword(null);
            return ResponseEntity.ok(updated);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body("User not found");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Error updating user: " + e.getMessage());
        }
    }

    // DELETE
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable int id) {
        String msg = sserv.deleteUser(id);
        return ResponseEntity.ok(msg);
    }

    // LOGIN - sets HttpSession attribute and returns user (without password)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserEntity loginData, HttpSession session) {
        try {
            String input = loginData.getEmailAddress(); // can be email or fullname
            String password = loginData.getPassword();

            Optional<UserEntity> userOpt = sserv.findByEmailOrFullname(input);

            if (userOpt.isEmpty()) {
                return ResponseEntity.status(404).body("User not found");
            }

            UserEntity user = userOpt.get();

            // Plaintext password check (replace with hashed check in production)
            if (!sserv.checkPassword(user, password)) {
                return ResponseEntity.status(401).body("Wrong password");
            }

            // Set session attributes
            session.setAttribute("userId", user.getUserId());
            session.setAttribute("userEmail", user.getEmailAddress());

            // hide password before returning
            user.setPassword(null);
            return ResponseEntity.ok(user);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Server error: " + e.getMessage());
        }
    }

    // GET current user from session
    @GetMapping("/me")
    public ResponseEntity<?> me(HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).body("Not authenticated");
        }
        try {
            UserEntity user = sserv.getUserById(userId);
            user.setPassword(null);
            return ResponseEntity.ok(user);
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(404).body("User not found");
        }
    }

    // LOGOUT - invalidate session
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Logged out");
    }
}
