package org.softuni.habitTracker.repositories;

import org.softuni.habitTracker.domain.entities.Habit;
import org.softuni.habitTracker.domain.entities.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends JpaRepository<Log, Long> {
}