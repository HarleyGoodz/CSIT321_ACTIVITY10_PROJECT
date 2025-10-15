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
    UserRepository urepo;

    public UserService() {
        super();
    }

    // CREATE
    public UserEntity createUser(UserEntity user) {
        return urepo.save(user);
    }

    // READ ALL
    public List<UserEntity> getAllUsers() {
        return urepo.findAll();
    }

    // READ BY ID
    public Optional<UserEntity> getUserById(Integer id) {
        return urepo.findById(id);
    }

    // UPDATE
    public UserEntity updateUser(Integer id, UserEntity newUserDetails) {
        UserEntity user = new UserEntity();
        try{
            user = urepo.findById(id).get();
            user.setFullname(newUserDetails.getFullname());
            user.setEmail(newUserDetails.getEmailAddress());
        } catch (NoSuchElementException e) {
           throw new NoSuchElementException("User with ID " + id + " not found.");
        } finally {
            return urepo.save(user);
        }
    }

    // DELETE
    public String deleteUser(Integer id) {
        String msg = "";

        if (urepo.findById(id) != null) {
            urepo.deleteById(id);
            msg = "User with ID " + id + " has been deleted.";
        } else {
            msg = "User with ID " + id + " not found.";
        }

        return msg;
    }

}
