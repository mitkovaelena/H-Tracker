package com.elena.habitTracker.areas.users.services;


import com.elena.habitTracker.areas.users.entities.User;
import com.elena.habitTracker.areas.users.models.binding.UserEditBindingModel;
import com.elena.habitTracker.areas.users.models.binding.UserRegisterBindingModel;
import com.elena.habitTracker.areas.users.models.view.UserViewModel;
import com.elena.habitTracker.areas.users.models.view.UsersPageViewModel;
import com.elena.habitTracker.util.ApplicationConstants;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User getByUsername(String username);

    void saveUser(UserRegisterBindingModel userDTO);

    UsersPageViewModel getAllUsers(Pageable pageable);

    UserEditBindingModel getUserEditDtoById(Long id);

    UserViewModel getUserViewDtoById(Long id);

    void editUser(Long id, UserEditBindingModel userEditBindingModel);

    void deleteUser(Long id);

    String getUsernameById(Long id);

    default long getTotalPages() {
        return getTotalPages(ApplicationConstants.DEFAULT_VIEWS_COUNT_PER_PAGE);
    }

    long getTotalPages(int size);

}
