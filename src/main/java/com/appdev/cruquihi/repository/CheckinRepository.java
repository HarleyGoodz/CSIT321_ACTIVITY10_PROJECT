package com.appdev.cruquihi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.appdev.cruquihi.entity.CheckinEntity;

public interface CheckinRepository extends JpaRepository<CheckinEntity, Integer> { }
