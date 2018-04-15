package com.elena.habitTracker.areas.users.services;


import com.elena.habitTracker.areas.users.entities.User;
import com.elena.habitTracker.areas.users.models.binding.UserRegisterBindingModel;
import com.elena.habitTracker.areas.users.models.view.UserViewModel;
import com.elena.habitTracker.areas.users.models.binding.UserEditBindingModel;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    User getByUsername(String username);

    void saveUser(UserRegisterBindingModel userDTO);

    List<UserViewModel> getAllUsers();

    UserEditBindingModel getUserEditDtoById(Long id);

    UserViewModel getUserViewDtoById(Long id);

    void editUser(Long id, UserEditBindingModel userEditBindingModel);

    void deleteUser(Long id);

    String getUsernameById(Long id);

}
