package org.softuni.habitTracker.areas.users.services;

import org.modelmapper.ModelMapper;
import org.softuni.habitTracker.areas.activities.entities.Activity;
import org.softuni.habitTracker.areas.activities.repositories.ActivityRepository;
import org.softuni.habitTracker.areas.habits.entities.Habit;
import org.softuni.habitTracker.areas.habits.repositories.HabitRepository;
import org.softuni.habitTracker.areas.roles.entities.Role;
import org.softuni.habitTracker.areas.roles.enums.RoleEnum;
import org.softuni.habitTracker.areas.roles.repositories.RoleRepository;
import org.softuni.habitTracker.areas.users.entities.User;
import org.softuni.habitTracker.areas.users.models.binding.UserEditDto;
import org.softuni.habitTracker.areas.users.models.binding.UserRegisterDTO;
import org.softuni.habitTracker.areas.users.models.view.UserViewDto;
import org.softuni.habitTracker.areas.users.repositories.UserRepository;
import org.softuni.habitTracker.areas.users.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
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
    public void saveUser(final UserRegisterDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        user.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
        user.setAuthorities(Stream.of(roleRepository.findByRole(RoleEnum.USER.getRoleName())).collect(Collectors.toSet()));
        this.userRepository.save(user);
    }

    @Override
    public List<UserViewDto> getAllUsers() {
        List<User> users = this.userRepository.findAll();
        List<UserViewDto> userViewDtos = new ArrayList<>();

        for (User user : users) {
            userViewDtos.add(modelMapper.map(user, UserViewDto.class));
        }

        return userViewDtos;
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
    public UserEditDto getUserEditDtoById(Long id) {
        Optional<User> userOptional = this.userRepository.findById(id);
        UserEditDto userEditDto = null;
        if (userOptional.isPresent()) {
            userEditDto = modelMapper.map(userOptional.get(), UserEditDto.class);
        }
        return userEditDto;
    }

    @Override
    public UserViewDto getUserViewDtoById(Long id) {
        Optional<User> userOptional = this.userRepository.findById(id);
        UserViewDto userViewDto = null;
        if (userOptional.isPresent()) {
            userViewDto = modelMapper.map(userOptional.get(), UserViewDto.class);
        }
        return userViewDto;
    }

    @Override
    public void editUser(Long id, UserEditDto userEditDto) {
        User user = this.userRepository.findById(id).get();
        user.setEmail(userEditDto.getEmail());
        user.setFirstName(userEditDto.getFirstName());
        user.setLastName(userEditDto.getLastName());
        Set<Role> roles = new HashSet<>();
        for (String role : userEditDto.getAuthorities()) {
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
}
