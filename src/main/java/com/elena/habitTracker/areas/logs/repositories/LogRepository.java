package com.elena.habitTracker.areas.logs.repositories;

import com.elena.habitTracker.areas.logs.entities.ApplicationLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends JpaRepository<ApplicationLog, Long> {
    Page<ApplicationLog> findAllByOrderByTimeDesc(Pageable pageable);
}
