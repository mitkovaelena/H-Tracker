package org.softuni.habitTracker.configuration;

import org.softuni.habitTracker.interceptors.StreakInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final StreakInterceptor streakInterceptor;

    @Autowired
    public WebMvcConfig(StreakInterceptor streakInterceptor) {
        this.streakInterceptor = streakInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.streakInterceptor)
                .addPathPatterns("/activities/add");
    }
}