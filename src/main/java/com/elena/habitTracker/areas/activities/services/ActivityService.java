package com.elena.habitTracker.areas.activities.services;

import com.elena.habitTracker.areas.activities.models.binding.ActivityAddBindingModel;
import com.elena.habitTracker.areas.activities.models.view.ActivitiesPageViewModel;
import com.elena.habitTracker.areas.users.entities.User;
import com.elena.habitTracker.util.ApplicationConstants;
import org.springframework.data.domain.Pageable;

public interface ActivityService {
    ActivitiesPageViewModel getAllActivitiesOrderedByDateDesc(User user, Pageable pageable);

    default long getTotalPages() {
        return getTotalPages(ApplicationConstants.DEFAULT_VIEWS_COUNT_PER_PAGE);
    }

    long getTotalPages(int size);

    void saveActivity(ActivityAddBindingModel activityAddBindingModel);
}
