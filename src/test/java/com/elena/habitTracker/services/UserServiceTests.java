package com.elena.habitTracker.services;


import com.elena.habitTracker.areas.roles.entities.Role;
import com.elena.habitTracker.areas.roles.enums.RoleEnum;
import com.elena.habitTracker.areas.roles.repositories.RoleRepository;
import com.elena.habitTracker.areas.users.entities.User;
import com.elena.habitTracker.areas.users.models.binding.UserEditBindingModel;
import com.elena.habitTracker.areas.users.models.binding.UserRegisterBindingModel;
import com.elena.habitTracker.areas.users.models.view.UsersPageViewModel;
import com.elena.habitTracker.areas.users.repositories.UserRepository;
import com.elena.habitTracker.areas.users.services.UserService;
import com.elena.habitTracker.areas.users.services.UserServiceImpl;
import com.elena.habitTracker.errors.ResourceNotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@EnableSpringDataWebSupport
public class UserServiceTests {
    private static final String ENCODED_PASSWORD = "Password_Encoded";

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private UserService userService;

    private UserRegisterBindingModel userModelEli;

    private User eli;

    private PageRequest pageable;

    @Before
    public void setUp() {
        userService = new UserServiceImpl(userRepository, roleRepository, this.bCryptPasswordEncoder, new ModelMapper());

        this.userModelEli = new UserRegisterBindingModel("eli123", "123456", "eli123@gmail.com", "Elena", "Nikolova");


        this.eli = new User("eli123", "123456", "eli123@gmail.com", "Elena", "Nikolova");

        pageable = PageRequest.of(1, 2);

        when(this.userRepository.save(any()))
                .thenAnswer(a -> a.getArgument(0));

        Role userRole = new Role();
        userRole.setRole(RoleEnum.USER.getRoleName());

        when(this.roleRepository.findByRole(RoleEnum.USER.getRoleName()))
                .thenAnswer(a -> userRole);

        Role adminRole = new Role();
        adminRole.setRole(RoleEnum.ADMIN.getRoleName());

        when(this.roleRepository.findByRole(RoleEnum.ADMIN.getRoleName()))
                .thenAnswer(a -> adminRole);


        when(this.bCryptPasswordEncoder.encode(any()))
                .thenAnswer(a -> ENCODED_PASSWORD);

        when(this.userRepository.findByUsername(eli.getUsername()))
                .thenAnswer(a -> eli);

        when(this.userRepository.findById(1L))
                .thenAnswer(a -> Optional.ofNullable(eli));

        when(this.userRepository.findById(2L))
                .thenAnswer(a -> Optional.ofNullable(null));
    }

    @Test
    public void testSaveUser_givenValidUser_shouldNotReturnNull() {
        //act
        User createdUser = this.userService.saveUser(this.userModelEli);

        //assert
        Assert.assertNotNull("User is null after creation", createdUser);
    }


    @Test
    public void testSaveUser_givenValidUser_shouldHashPasswordRight() {
        //act
        User createdUser = this.userService.saveUser(this.userModelEli);

        //assert
        Assert.assertEquals("Password was not correctly hashed after user is created", ENCODED_PASSWORD, createdUser.getPassword());
    }

    @Test
    public void testSaveUser_givenValidUser_shouldMapAuthoritiesFieldRight() {
        //act
        User createdUser = this.userService.saveUser(this.userModelEli);
        Iterator<Role> iterator = createdUser.getAuthorities().iterator();

        //assert
        Assert.assertTrue("Wrong authorities number after user is created", iterator.hasNext());
        Assert.assertEquals("Wrong authorities after user is created", RoleEnum.USER.getRoleName(), iterator.next().getRole());
    }

    @Test
    public void testGetAllUsers_givenValidUsers_shouldMapCorrectly() {
        //arrange
        List<User> users = new ArrayList<>();
        users.add(eli);
        users.add(new User("vili321", "654321", "vili123@gmail.com", "Violeta", "Nikolova"));

        when(this.userRepository.findAll(pageable))
                .thenAnswer(a -> new PageImpl<User>(users, pageable, users.size()));

        //act
        UsersPageViewModel pageViewModel = this.userService.getAllUsers(pageable);

        //assert
        Assert.assertNotNull("Page is null after creation", pageViewModel);
        Assert.assertNotNull("Users in page are null after creation", pageViewModel.getUsers());

        for (int i = 0; i < pageViewModel.getUsers().getContent().size(); i++) {
            Assert.assertEquals("Users in page differ", users.get(i).toString(), pageViewModel.getUsers().getContent().get(i).toString());
        }
    }

    @Test
    public void testLoadUserByUsername_givenValidUser_shouldReturnUser() {
        //act
        UserDetails loadedUser = this.userService.loadUserByUsername(eli.getUsername());

        //assert
        Assert.assertNotNull("User is null when loaded by username", loadedUser);
        Assert.assertEquals("Wrong user when loaded by username", eli.toString(), loadedUser.toString());
    }

    @Test(expected = UsernameNotFoundException.class)
    public void testLoadUserByUsername_givenNotValidUser_shouldThrowUsernameNotFoundException() {
        //act
        this.userService.loadUserByUsername("wrongUsername");
    }

    @Test
    public void testGetUserById_givenValidUser_shouldReturnUser() {
        //act
        User user = this.userService.getUserById(1L);

        //assert
        Assert.assertNotNull("User is null when loaded by id", user);
        Assert.assertEquals("Wrong email when loaded by username", eli.getEmail(), user.getEmail());
        Assert.assertEquals("Wrong first name when loaded by username", eli.getFirstName(), user.getFirstName());
        Assert.assertEquals("Wrong last name when loaded by username", eli.getLastName(), user.getLastName());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testGetUserById_givenNotValidUser_shouldThrowResourceNotFoundException() {
        //act
        this.userService.getUserById(2L);
    }

    @Test
    public void testGetUserEditDtoById_givenValidUser_shouldReturnUser() {
        //act
        UserEditBindingModel userEditBindingModel = this.userService.getUserEditDtoById(1L);

        //assert
        Assert.assertNotNull("User is null when loaded by id", userEditBindingModel);
        Assert.assertEquals("Wrong email when loaded by username", eli.getEmail(), userEditBindingModel.getEmail());
        Assert.assertEquals("Wrong first name when loaded by username", eli.getFirstName(), userEditBindingModel.getFirstName());
        Assert.assertEquals("Wrong last name when loaded by username", eli.getLastName(), userEditBindingModel.getLastName());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testGetUserEditDtoById_givenNotValidUser_shouldThrowResourceNotFoundException() {
        //act
        this.userService.getUserEditDtoById(2L);
    }


    @Test
    public void testEditUser_givenValidUser_shouldMapFieldsRight() {
        //arrange
        UserEditBindingModel userEditBindingModel = new UserEditBindingModel("eli123@gmail.com", "Elena", "Nikolova");

        userEditBindingModel.setAuthorities(Stream.of(RoleEnum.USER.toString(), RoleEnum.ADMIN.toString()).collect(Collectors.toSet()));

        //act
        User editedUser = this.userService.editUser(1L, userEditBindingModel);
        SortedSet<Role> rolesSorted = new TreeSet<>(Comparator.comparing(Role::getRole));
        rolesSorted.addAll(editedUser.getAuthorities());
        Iterator<Role> iterator = rolesSorted.iterator();

        //assert
        Assert.assertEquals("Wrong email after user is created", userEditBindingModel.getEmail(), editedUser.getEmail());
        Assert.assertEquals("Wrong first name after user is created", userEditBindingModel.getFirstName(), editedUser.getFirstName());
        Assert.assertEquals("Wrong last name after user is created", userEditBindingModel.getLastName(), editedUser.getLastName());
        Assert.assertEquals("Wrong username after user is created", eli.getUsername(), editedUser.getUsername());

        Assert.assertTrue("Wrong authorities number after user is created", iterator.hasNext());
        Assert.assertEquals("Wrong authority Admin after user is created", RoleEnum.ADMIN.getRoleName(), iterator.next().getRole());
        Assert.assertEquals("Wrong authority User after user is created", RoleEnum.USER.getRoleName(), iterator.next().getRole());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testEditUser_givenNotValidUser_shouldThrowResourceNotFoundException() {
        //act
        this.userService.getUserById(2L);
    }

    @Test
    public void testGetUsernameById_givenValidUser_shouldReturnCorrectUsername() {
        //act
        String foundUsername = this.userService.getUsernameById(1L);

        //assert
        Assert.assertNotNull("Username is null", foundUsername);
        Assert.assertEquals("Wrong username", eli.getUsername(), foundUsername);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testGetUsernameById_givenNotValidUser_shouldThrowResourceNotFoundException() {
        //act
        this.userService.getUserById(2L);
    }
}