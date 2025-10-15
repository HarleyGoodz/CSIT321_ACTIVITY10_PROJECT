package com.appdev.cruquihi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.appdev.cruquihi.entity.EventEntity;

public interface EventRepository extends JpaRepository<EventEntity, Integer> {

}
