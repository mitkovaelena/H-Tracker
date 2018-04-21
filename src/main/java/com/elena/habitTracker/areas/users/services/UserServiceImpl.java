package com.elena.habitTracker.areas.users.services;

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
import com.elena.habitTracker.errors.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
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
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ModelMapper modelMapper;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                           BCryptPasswordEncoder bCryptPasswordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.modelMapper = modelMapper;
    }

    @Override
    public User getByUsername(final String username) {
        return this.userRepository.findByUsername(username);
    }

    @Override
    public User saveUser(final UserRegisterBindingModel userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        user.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
        user.setAuthorities(Stream.of(roleRepository.findByRole(RoleEnum.USER.getRoleName())).collect(Collectors.toSet()));
        return this.userRepository.save(user);
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
        usersPageViewModel.setTotalPagesCount(usersViewModelPage.getTotalPages());

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
        return  modelMapper.map(this.getUserById(id), UserEditBindingModel.class);
    }

    @Override
    public User getUserById(Long id) {
        Optional<User> userOptional = this.userRepository.findById(id);
        if (!userOptional.isPresent()) {
            throw new ResourceNotFoundException();
        }

        return userOptional.get();
    }

    @Override
    public User editUser(Long id, UserEditBindingModel userEditBindingModel) {
        User user = this.getUserById(id);
        user.setEmail(userEditBindingModel.getEmail());
        user.setFirstName(userEditBindingModel.getFirstName());
        user.setLastName(userEditBindingModel.getLastName());
        Set<Role> roles = new HashSet<>();

        for (String role : userEditBindingModel.getAuthorities()) {
            roles.add(this.roleRepository.findByRole(RoleEnum.valueOf(role.toUpperCase()).getRoleName()));
        }
        user.setAuthorities(roles);

        return this.userRepository.save(user);
    }

    @Override
    @Async
    public void deleteUser(Long id) {
        this.userRepository.deleteById(id);
    }

    @Override
    public String getUsernameById(Long id) {
        return this.getUserById(id).getUsername();
    }
}
