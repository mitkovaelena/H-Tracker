package com.elena.habitTracker.interceptors;

import com.elena.habitTracker.areas.habits.models.view.HabitViewModel;
import com.elena.habitTracker.areas.habits.services.HabitService;
import com.elena.habitTracker.areas.users.services.UserService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.List;

@Component
public class StreakInterceptor extends HandlerInterceptorAdapter {
    private HabitService habitService;
    private UserService userService;

    public StreakInterceptor(HabitService habitService, UserService userService) {
        this.habitService = habitService;
        this.userService = userService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        List<HabitViewModel> habits = this.habitService.getAllHabitsByUser(
                this.userService.getByUsername(request.getUserPrincipal().getName()));
        for (HabitViewModel habit : habits) {
            if (habit.getNextDueDate() != null && habit.getNextDueDate().isBefore(LocalDate.now())) {
                this.habitService.resetStreak(habit.getId());
                this.habitService.calculateNextDueDate(habit.getId());
            }
        }
        return super.preHandle(request, response, handler);
    }
}
