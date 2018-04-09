package org.softuni.habitTracker.areas.activities.services;

import org.softuni.habitTracker.areas.activities.models.binding.ActivityAddBindingModel;
import org.softuni.habitTracker.areas.activities.models.view.ActivityViewModel;
import org.softuni.habitTracker.areas.users.entities.User;

import java.util.List;

public interface ActivityService {
    List<ActivityViewModel> getAllActivitiesOrderedByDateDesc(User user);

    void saveActivity(ActivityAddBindingModel activityAddBindingModel);
}
