package com.appdev.cruquihi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.appdev.cruquihi.entity.PaymentEntity;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Integer> {

    // ✅ Correct path: user.userId AND ticket.event.eventId
    boolean existsByUserUserIdAndTicketEventEventId(int userId, int eventId);
    List<PaymentEntity> findAllByTicket_TicketIdIn(List<Integer> ticketIds);

    // optionally: find payments by ticket id
    List<PaymentEntity> findAllByTicket_TicketId(Integer ticketId);
    void deleteAllByTicket_TicketIdIn(List<Integer> ticketIds);

    List<PaymentEntity> findByTicketEventEventId(int eventId);

}
