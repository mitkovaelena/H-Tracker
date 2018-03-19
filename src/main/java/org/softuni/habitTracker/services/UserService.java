package org.softuni.habitTracker.services;


import org.softuni.habitTracker.domain.entities.User;
import org.softuni.habitTracker.domain.models.binding.UserRegisterDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService{
    User findByUsername(String username);

    void saveUser(UserRegisterDTO userDTO);

}
