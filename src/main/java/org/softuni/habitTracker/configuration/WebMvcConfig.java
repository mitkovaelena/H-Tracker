package org.softuni.habitTracker.configuration;

import org.softuni.habitTracker.interceptors.LogApplicationActivityInterceptor;
import org.softuni.habitTracker.interceptors.StreakInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final StreakInterceptor streakInterceptor;
    private final LogApplicationActivityInterceptor logInterceptor;

    @Autowired
    public WebMvcConfig(StreakInterceptor streakInterceptor, LogApplicationActivityInterceptor logInterceptor) {
        this.streakInterceptor = streakInterceptor;
        this.logInterceptor = logInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.streakInterceptor)
                .addPathPatterns("/activities/add");
        registry.addInterceptor(this.logInterceptor);
    }
}