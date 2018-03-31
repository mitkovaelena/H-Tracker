package org.softuni.habitTracker.interceptors;

import org.softuni.habitTracker.domain.entities.Habit;
import org.softuni.habitTracker.domain.models.view.HabitViewDTO;
import org.softuni.habitTracker.services.HabitService;
import org.softuni.habitTracker.services.UserService;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
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
        List<HabitViewDTO> habits = this.habitService.getAllHabitsByUser(
                this.userService.getByUsername(request.getUserPrincipal().getName()));
        for (HabitViewDTO habit : habits) {
            if (habit.getNextDueDate().isBefore(LocalDate.now())) {
                this.habitService.resetStreak(habit.getId());
                this.habitService.calculateNextDueDate(habit.getId());
            }
        }
        return super.preHandle(request,response,handler);
    }
}
