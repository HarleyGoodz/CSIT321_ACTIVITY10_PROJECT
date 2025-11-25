package com.appdev.cruquihi.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
    UserRepository urepo;

    // CREATE
    @PostMapping("/add")
    public UserEntity addUser(@RequestBody UserEntity user) {
        return sserv.createUser(user);
    }

    // READ ALL
    @GetMapping("/all")
    public List<UserEntity> getAllUsers() {
        return sserv.getAllUsers();
    }

    // READ BY ID
    @GetMapping("/{id}")
    public Optional<UserEntity> getUserById(@PathVariable Integer id) {
        return sserv.getUserById(id);
    }

    // UPDATE
    @PutMapping("/update/{id}")
    public UserEntity updateUser(@RequestParam Integer id, @RequestBody UserEntity newUserDetails) {
        return sserv.updateUser(id, newUserDetails);
    }

    // DELETE
    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable Integer id) {
        sserv.deleteUser(id);
        return "User with ID " + id + " has been deleted.";
    }

    @PostMapping("/login")
    public String login(@RequestBody UserEntity loginData) {

        try {
            String input = loginData.getEmailAddress();
            String password = loginData.getPassword();

            // Try login using email
            Optional<UserEntity> user = sserv.urepo.findByEmailAddress(input);

            // Try username
            if (user.isEmpty()) {
                user = sserv.urepo.findByFullname(input);
            }

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
