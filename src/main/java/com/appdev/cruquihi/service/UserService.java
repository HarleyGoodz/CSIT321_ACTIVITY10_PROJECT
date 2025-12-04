package com.appdev.cruquihi.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appdev.cruquihi.entity.UserEntity;
import com.appdev.cruquihi.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository urepo;

    public UserService() {
        super();
    }

    // CREATE
    public UserEntity createUser(UserEntity user) {
        // NOTE: currently saving raw password (plaintext).
        // For production, hash with BCrypt and store the hash instead.
        return urepo.save(user);
    }

    // READ ALL
    public List<UserEntity> getAllUsers() {
        return urepo.findAll();
    }

    // READ BY ID
    public UserEntity getUserById(int id) {
        return urepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User with ID " + id + " not found"));
    }

    // UPDATE
    public UserEntity updateUser(int id, UserEntity newUser) {

        UserEntity user = urepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User with ID " + id + " not found"));

        user.setEmailAddress(newUser.getEmailAddress());
        user.setFullname(newUser.getFullname());
        // If client provided a new password, update it (plaintext here)
        if (newUser.getPassword() != null && !newUser.getPassword().isEmpty()) {
            user.setPassword(newUser.getPassword());
        }
        user.setRole(newUser.getRole());

        return urepo.save(user);
    }

    // DELETE
    public String deleteUser(int id) {
        if (urepo.findById(id).isPresent()) {
            urepo.deleteById(id);
            return "User with ID " + id + " has been deleted.";
        } else {
            return "User with ID " + id + " not found.";
        }
    }

    // FIND BY EMAIL OR FULLNAME (LOGIN)
    public Optional<UserEntity> findByEmailOrFullname(String value) {
        Optional<UserEntity> user = urepo.findByEmailAddress(value);

        if (user.isEmpty()) {
            user = urepo.findByFullname(value);
        }

        return user;
    }

    // helper to check raw password vs stored value (plaintext comparison here)
    public boolean checkPassword(UserEntity user, String rawPassword) {
        if (user == null || rawPassword == null) return false;
        return rawPassword.equals(user.getPassword());
    }
}
