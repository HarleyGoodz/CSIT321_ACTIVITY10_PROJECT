package com.appdev.cruquihi.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.appdev.cruquihi.entity.TicketEntity;

@Repository
public interface TicketRepository extends JpaRepository<TicketEntity, Integer> {
    // find tickets belonging to an event by event id
    List<TicketEntity> findAllByEvent_EventId(Integer eventId);
    void deleteAllByEvent_EventId(Integer eventId);
}