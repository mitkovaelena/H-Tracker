package org.softuni.habitTracker.repositories;

import org.softuni.habitTracker.domain.entities.Activity;
import org.softuni.habitTracker.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findAllByUserOrderByDateDesc(User user);
}