package com.elena.habitTracker.areas.users.services;

import com.elena.habitTracker.areas.activities.entities.Activity;
import com.elena.habitTracker.areas.activities.repositories.ActivityRepository;
import com.elena.habitTracker.areas.habits.entities.Habit;
import com.elena.habitTracker.areas.habits.repositories.HabitRepository;
import com.elena.habitTracker.areas.roles.entities.Role;
import com.elena.habitTracker.areas.roles.enums.RoleEnum;
import com.elena.habitTracker.areas.roles.repositories.RoleRepository;
import com.elena.habitTracker.areas.users.entities.User;
import com.elena.habitTracker.areas.users.models.binding.UserEditBindingModel;
import com.elena.habitTracker.areas.users.models.binding.UserRegisterBindingModel;
import com.elena.habitTracker.areas.users.models.view.UserViewModel;
import com.elena.habitTracker.areas.users.models.view.UsersPageViewModel;
import com.elena.habitTracker.areas.users.repositories.UserRepository;
import com.elena.habitTracker.areas.users.util.Constants;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ActivityRepository activityRepository;
    private final HabitRepository habitRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ModelMapper modelMapper;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                           ActivityRepository activityRepository, HabitRepository habitRepository,
                           BCryptPasswordEncoder bCryptPasswordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.activityRepository = activityRepository;
        this.habitRepository = habitRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.modelMapper = modelMapper;
    }

    @Override
    public User getByUsername(final String username) {
        return this.userRepository.findByUsername(username);
    }

    @Override
    public void saveUser(final UserRegisterBindingModel userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        user.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
        user.setAuthorities(Stream.of(roleRepository.findByRole(RoleEnum.USER.getRoleName())).collect(Collectors.toSet()));
        this.userRepository.save(user);
    }

    @Override
    public UsersPageViewModel getAllUsers(Pageable pageable) {
        Page<User> usersPage = this.userRepository.findAll(pageable);
        int totalElements = (int) usersPage.getTotalElements();

        Page<UserViewModel> usersViewModelPage = new PageImpl<>(
                usersPage.stream()
                        .map(log -> this.modelMapper.map(log, UserViewModel.class))
                        .collect(Collectors.toList()), pageable, totalElements);

        UsersPageViewModel usersPageViewModel = new UsersPageViewModel();
        usersPageViewModel.setUsers(usersViewModelPage);
        usersPageViewModel.setTotalPagesCount(this.getTotalPages());

        return usersPageViewModel;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        User user = this.userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(Constants.INCORRECT_USERNAME_OR_PASSWORD);
        }

        return user;
    }

    @Override
    public UserEditBindingModel getUserEditDtoById(Long id) {
        Optional<User> userOptional = this.userRepository.findById(id);
        UserEditBindingModel userEditBindingModel = null;
        if (userOptional.isPresent()) {
            userEditBindingModel = modelMapper.map(userOptional.get(), UserEditBindingModel.class);
        }
        return userEditBindingModel;
    }

    @Override
    public UserViewModel getUserViewDtoById(Long id) {
        Optional<User> userOptional = this.userRepository.findById(id);
        UserViewModel userViewModel = null;
        if (userOptional.isPresent()) {
            userViewModel = modelMapper.map(userOptional.get(), UserViewModel.class);
        }
        return userViewModel;
    }

    @Override
    public void editUser(Long id, UserEditBindingModel userEditBindingModel) {
        User user = this.userRepository.findById(id).get();
        user.setEmail(userEditBindingModel.getEmail());
        user.setFirstName(userEditBindingModel.getFirstName());
        user.setLastName(userEditBindingModel.getLastName());
        Set<Role> roles = new HashSet<>();
        for (String role : userEditBindingModel.getAuthorities()) {
            roles.add(this.roleRepository.findByRole(RoleEnum.valueOf(role.toUpperCase()).getRoleName()));
        }
        user.setAuthorities(roles);
        this.userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        User user = this.userRepository.findById(id).get();
        for (Activity activity : user.getActivities()) {
            this.activityRepository.deleteById(activity.getId());
        }
        for (Habit habit : user.getHabits()) {
            this.habitRepository.deleteById(habit.getId());
        }

        this.userRepository.deleteById(id);
    }

    @Override
    public String getUsernameById(Long id) {
        return this.userRepository.findById(id).get().getUsername();
    }

    @Override
    public long getTotalPages(int size) {
        return this.userRepository.count() / size;
    }
}
