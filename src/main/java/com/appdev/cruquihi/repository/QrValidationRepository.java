package com.appdev.cruquihi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.appdev.cruquihi.entity.QrValidationEntity;
 
 
@Repository
public interface QrValidationRepository extends JpaRepository<QrValidationEntity, Integer> {
 
 
}