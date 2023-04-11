package com.snehasish.blog.service;

import java.util.List;

import com.snehasish.blog.payload.UserDto;

public interface UserService {

	UserDto createUser(UserDto userDto);

	UserDto updateUser(UserDto userDto, Long userId);

	List<UserDto> getAllUsers();

	UserDto getUserById(Long userId);

	void deleteUserById(Long userId);

	UserDto registerNewUser(UserDto userDto);

	UserDto registerNewUserById(Long userId);

}
