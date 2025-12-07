package com.appdev.cruquihi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.appdev.cruquihi.entity.PaymentEntity;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Integer> {

    // ✅ Correct path: user.userId AND ticket.event.eventId
    boolean existsByUserUserIdAndTicketEventEventId(int userId, int eventId);

}
