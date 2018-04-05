package org.softuni.habitTracker.areas.activities.services;

import org.softuni.habitTracker.areas.activities.models.binding.ActivityAddDTO;
import org.softuni.habitTracker.areas.activities.models.view.ActivityViewDTO;
import org.softuni.habitTracker.areas.users.entities.User;

import java.util.List;

public interface ActivityService {
    List<ActivityViewDTO> getAllActivitiesOrderedByDateDesc(User user);

    void saveActivity(ActivityAddDTO activityAddDTO);
}
