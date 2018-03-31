package org.softuni.habitTracker.services;

import org.softuni.habitTracker.domain.entities.User;
import org.softuni.habitTracker.domain.models.binding.ActivityAddDTO;
import org.softuni.habitTracker.domain.models.view.ActivityViewDTO;
import org.softuni.habitTracker.domain.models.view.HabitViewDTO;

import java.util.List;

public interface ActivityService {
    List<ActivityViewDTO> getAllActivitiesOrderedByDateDesc(User user);

    void saveActivity(ActivityAddDTO activityAddDTO);
}
