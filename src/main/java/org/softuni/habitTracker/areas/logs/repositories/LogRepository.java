package org.softuni.habitTracker.areas.logs.repositories;

import org.softuni.habitTracker.areas.logs.entities.ApplicationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogRepository extends JpaRepository<ApplicationLog, Long>{
    List<ApplicationLog> findAllByOrderByTimeDesc();
}
