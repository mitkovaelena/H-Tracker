package org.softuni.habitTracker.services;

import org.softuni.habitTracker.domain.entities.User;
import org.softuni.habitTracker.domain.models.binding.HabitAddDTO;
import org.springframework.security.core.Authentication;

import java.security.Principal;

public interface HabitService {
    boolean saveHabit(HabitAddDTO habitAddDTO, User user);
}
