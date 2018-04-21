package com.elena.habitTracker.areas.activities.services;

import com.elena.habitTracker.areas.activities.entities.Activity;
import com.elena.habitTracker.areas.activities.models.binding.ActivityAddBindingModel;
import com.elena.habitTracker.areas.activities.models.view.ActivitiesPageViewModel;
import com.elena.habitTracker.areas.users.entities.User;
import org.springframework.data.domain.Pageable;

public interface ActivityService {
    ActivitiesPageViewModel getAllActivitiesOrderedByDateDesc(User user, Pageable pageable);

    Activity saveActivity(ActivityAddBindingModel activityAddBindingModel);
}
