package org.softuni.habitTracker.services;

import org.softuni.habitTracker.domain.entities.User;
import org.softuni.habitTracker.domain.models.binding.UserRegisterDTO;
import org.softuni.habitTracker.repositories.UserRepository;
import org.softuni.habitTracker.util.ModelParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public User findByUsername(final String username) {
        return this.userRepository.findByUsername(username);
    }

    @Override
    public void saveUser(final UserRegisterDTO userDTO) {
        User user = ModelParser.getInstance().map(userDTO, User.class);
        user.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
        this.userRepository.save(user);
    }
}
