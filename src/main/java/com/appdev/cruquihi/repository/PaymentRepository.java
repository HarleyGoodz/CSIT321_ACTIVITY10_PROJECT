package com.appdev.cruquihi.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.appdev.cruquihi.entity.PaymentEntity;



@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Integer> {

   
    
}
