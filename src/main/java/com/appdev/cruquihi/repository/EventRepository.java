package com.appdev.cruquihi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.appdev.cruquihi.entity.EventEntity;


@Repository
public interface EventRepository extends JpaRepository<EventEntity, Integer> {

}
