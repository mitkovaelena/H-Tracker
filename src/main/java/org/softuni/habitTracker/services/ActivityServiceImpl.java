package org.softuni.habitTracker.services;

import org.modelmapper.ModelMapper;
import org.softuni.habitTracker.domain.entities.Activity;
import org.softuni.habitTracker.domain.entities.User;
import org.softuni.habitTracker.domain.models.binding.ActivityAddDTO;
import org.softuni.habitTracker.domain.models.view.ActivityViewDTO;
import org.softuni.habitTracker.repositories.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ActivityServiceImpl implements ActivityService {
    private final ActivityRepository activityRepository;
    private final ModelMapper modelMapper;


    @Autowired
    public ActivityServiceImpl(ActivityRepository activityRepository, ModelMapper modelMapper) {
        this.activityRepository = activityRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<ActivityViewDTO> findAllActivities(User user) {
        List<Activity> activities = this.activityRepository.findAllByUser(user);
        List<ActivityViewDTO> activityViewDTOs = new ArrayList<>();

        for (Activity activity : activities) {
            activityViewDTOs.add(modelMapper.map(activity, ActivityViewDTO.class));
        }

        return activityViewDTOs;
    }

    @Override
    public void saveActivity(ActivityAddDTO activityAddDTO) {
        Activity activity = modelMapper.map(activityAddDTO, Activity.class);
        this.activityRepository.save(activity);
    }

}
