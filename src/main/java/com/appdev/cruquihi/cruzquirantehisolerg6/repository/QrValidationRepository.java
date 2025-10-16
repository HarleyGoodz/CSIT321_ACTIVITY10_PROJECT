package com.appdev.cruquihi.cruzquirantehisolerg6.repository;

 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.appdev.cruquihi.cruzquirantehisolerg6.entity.QrValidationEntity;
 
 
@Repository
public interface QrValidationRepository extends JpaRepository<QrValidationEntity, Integer> {
 
 
}