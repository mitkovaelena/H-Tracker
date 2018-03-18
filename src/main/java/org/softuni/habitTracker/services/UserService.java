package org.softuni.habitTracker.services;


import org.softuni.habitTracker.domain.entities.User;
import org.softuni.habitTracker.domain.models.binding.UserRegisterDTO;

public interface UserService {
    User findByUsername(String username);

    void saveUser(UserRegisterDTO userDTO);

}
