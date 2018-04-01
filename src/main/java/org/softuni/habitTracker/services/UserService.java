package org.softuni.habitTracker.services;


import org.softuni.habitTracker.domain.entities.User;
import org.softuni.habitTracker.domain.models.binding.UserEditDto;
import org.softuni.habitTracker.domain.models.binding.UserRegisterDTO;
import org.softuni.habitTracker.domain.models.view.UserViewDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.validation.Valid;
import java.util.List;

public interface UserService extends UserDetailsService{
    User getByUsername(String username);

    void saveUser(UserRegisterDTO userDTO);

    List<UserViewDto> getAllUsers();

    UserEditDto getUserEditDtoById(Long id);

    void editUser(Long id, UserEditDto userEditDto);

    void deleteUser(Long id);

    String getUsernameById(Long id);
}
