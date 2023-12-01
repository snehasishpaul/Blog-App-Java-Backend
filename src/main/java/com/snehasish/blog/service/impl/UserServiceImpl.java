package com.snehasish.blog.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.snehasish.blog.config.AppConstant;
import com.snehasish.blog.entity.Role;
import com.snehasish.blog.entity.User;
import com.snehasish.blog.exception.ResourceNotFoundException;
import com.snehasish.blog.payload.UserDto;
import com.snehasish.blog.repository.RoleRepo;
import com.snehasish.blog.repository.UserRepo;
import com.snehasish.blog.service.EmailService;
import com.snehasish.blog.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private EmailService emailService;

    @Override
    public UserDto createUser(UserDto userDto) {

        User user = this.userDtoToUser(userDto);
        User savedUser = this.userRepo.save(user);
        return this.userToUserDto(savedUser);
    }

    @Override
    public UserDto updateUser(UserDto userDto, Long userId) {

        User user = this.userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", " id ", userId));

        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setAbout(userDto.getAbout());

        User updatedUser = this.userRepo.save(user);

        return this.userToUserDto(updatedUser);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = this.userRepo.findAll();

        List<UserDto> userDtoList = users.stream().map(user -> this.userToUserDto(user)).collect(Collectors.toList());

        return userDtoList;
    }

    @Override
    public UserDto getUserById(Long userId) {
        User user = this.userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", " id ", userId));
        return this.userToUserDto(user);
    }

    @Override
    public void deleteUserById(Long userId) {
        User user = this.userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", " id ", userId));
        this.userRepo.delete(user);
    }

    @Override
    public UserDto registerNewUser(UserDto userDto) {
        User user = this.modelMapper.map(userDto, User.class);

        // password encoded
        String originalPass = user.getPassword();
        String encodedPassword = this.passwordEncoder.encode(originalPass);
        user.setPassword(encodedPassword);

        // get the role to be assigned to the user
        Role role = this.roleRepo.findById(AppConstant.ROLE_NORMAL).get();

        user.getRoles().add(role);
        User newUser = this.userRepo.save(user);
        if (newUser != null) {
            try {
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            emailService.sendSimpleMailMessage("weishenpov@gmail.com", user.getEmail(), "Blog App: Registration", user.getName(), user.getUsername(), originalPass);
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }.start();
            } catch (Exception e) {
                System.out.println("ERROR" + e.getMessage());
            }
        }

        return this.modelMapper.map(newUser, UserDto.class
        );
    }

    @Override
    public UserDto registerNewUserById(Long userId) {
        User user = this.userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", " id ", userId));

        // password encoder
        String encodedPass = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPass);

        // role assigning
        Role role = this.roleRepo.findById(AppConstant.ROLE_NORMAL).get();
        user.getRoles().add(role);

        User updatedUser = this.userRepo.save(user);

        return this.modelMapper.map(updatedUser, UserDto.class
        );
    }

    public User
            userDtoToUser(UserDto userDto) {

        // using ModelMapper class to map UserDto class to User class
        User user = this.modelMapper.map(userDto, User.class
        );

//		user.setId(userDto.getId());
//		user.setName(userDto.getName());
//		user.setEmail(userDto.getEmail());
//		user.setPassword(userDto.getPassword());
//		user.setAbout(userDto.getAbout());
        return user;
    }

    public UserDto
            userToUserDto(User user) {

        // using ModelMapper class to map User class to UserDto class
        UserDto userDto = this.modelMapper.map(user, UserDto.class
        );

//		userDto.setId(user.getId());
//		userDto.setName(user.getName());
//		userDto.setEmail(user.getEmail());
//		userDto.setPassword(user.getPassword());
//		userDto.setAbout(user.getAbout());
        return userDto;
    }

}
