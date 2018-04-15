package com.elena.habitTracker.areas.activities.services;

import com.elena.habitTracker.areas.activities.models.binding.ActivityAddBindingModel;
import com.elena.habitTracker.areas.activities.models.view.ActivityViewModel;
import com.elena.habitTracker.areas.users.entities.User;

import java.util.List;

public interface ActivityService {
    List<ActivityViewModel> getAllActivitiesOrderedByDateDesc(User user);

    void saveActivity(ActivityAddBindingModel activityAddBindingModel);
}
