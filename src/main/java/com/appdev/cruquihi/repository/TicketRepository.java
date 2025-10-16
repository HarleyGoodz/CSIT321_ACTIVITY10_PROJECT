package com.appdev.cruquihi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.appdev.cruquihi.entity.TicketEntity;

public interface TicketRepository extends JpaRepository<TicketEntity, Integer> { }
