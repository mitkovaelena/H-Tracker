package org.softuni.habitTracker.services;

import org.modelmapper.ModelMapper;
import org.softuni.habitTracker.domain.entities.User;
import org.softuni.habitTracker.domain.models.binding.UserRegisterDTO;
import org.softuni.habitTracker.repositories.RoleRepository;
import org.softuni.habitTracker.repositories.UserRepository;
import org.softuni.habitTracker.util.Constants;
import org.softuni.habitTracker.util.enums.RoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.modelMapper = modelMapper;
    }

    @Override
    public User findByUsername(final String username) {
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
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        User user = this.userRepository.findByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException(Constants.INCORRECT_USERNAME_OR_PASSWORD);
        }

        return user;
    }
}
