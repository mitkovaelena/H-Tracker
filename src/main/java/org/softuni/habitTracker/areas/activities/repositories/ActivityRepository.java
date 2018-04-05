package org.softuni.habitTracker.areas.activities.repositories;

import org.softuni.habitTracker.areas.activities.entities.Activity;
import org.softuni.habitTracker.areas.users.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findAllByUserOrderByDateDesc(User user);
}