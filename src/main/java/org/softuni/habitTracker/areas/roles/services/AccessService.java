package org.softuni.habitTracker.areas.roles.services;

import org.softuni.habitTracker.areas.habits.services.HabitService;
import org.softuni.habitTracker.areas.users.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AccessService {
    private final HabitService habitService;

    @Autowired
    public AccessService(HabitService habitService) {
        this.habitService = habitService;
    }

    public boolean hasAccess(Authentication authentication, Long id) {
        User user = (User) authentication.getPrincipal();
        return this.habitService.getHabitById(id).getUser().getId().equals(user.getId());
    }
}
