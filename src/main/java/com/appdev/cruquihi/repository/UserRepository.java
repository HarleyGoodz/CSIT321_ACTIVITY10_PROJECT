package com.appdev.cruquihi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.appdev.cruquihi.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
   
}

