package org.softuni.habitTracker.services;

import org.softuni.habitTracker.repositories.HabitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HabitServiceImpl implements HabitService {
    private final HabitRepository habitRepository;

    @Autowired
    public HabitServiceImpl(HabitRepository habitRepository) {
        this.habitRepository = habitRepository;
    }
}
