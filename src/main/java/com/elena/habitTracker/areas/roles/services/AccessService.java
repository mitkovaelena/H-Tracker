package com.elena.habitTracker.areas.roles.services;

import com.elena.habitTracker.areas.habits.services.HabitService;
import com.elena.habitTracker.areas.users.entities.User;
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

        return authentication.getAuthorities().stream().anyMatch(ga -> ga.getAuthority().equals("ROLE_ADMIN"))
                || this.habitService.getHabitById(id).getUser().getId().equals(user.getId());
    }

    public boolean isOwner(Authentication authentication, Long id) {
        User user = (User) authentication.getPrincipal();

        return this.habitService.getHabitById(id).getUser().getId().equals(user.getId());
    }
}
