package com.appdev.cruquihi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.appdev.cruquihi.entity.CheckinEntity;

@Repository
public interface CheckinRepository extends JpaRepository<CheckinEntity, Integer> { }
