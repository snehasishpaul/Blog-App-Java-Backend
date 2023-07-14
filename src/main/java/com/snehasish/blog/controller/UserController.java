package com.snehasish.blog.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.snehasish.blog.payload.ApiResponse;
import com.snehasish.blog.payload.UserDto;
import com.snehasish.blog.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
@Validated
public class UserController {

	@Autowired
	private UserService userService;

	// POST - create user
	@PostMapping("/")
	public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
		UserDto createdUserDto = this.userService.createUser(userDto);
		return new ResponseEntity<UserDto>(createdUserDto, HttpStatus.CREATED);
	}

	// GET - get single user
	@GetMapping("/{userId}")
	public ResponseEntity<UserDto> getSingleUser(@PathVariable("userId") Long userId) {
		UserDto user = this.userService.getUserById(userId);
		return new ResponseEntity<UserDto>(user, HttpStatus.OK);
	}

	// GET - get all user
	@GetMapping("/")
	public ResponseEntity<List<UserDto>> getAllUsers() {
		List<UserDto> userList = this.userService.getAllUsers();
		return new ResponseEntity<List<UserDto>>(userList, HttpStatus.OK);
	}

	// PUT - update single user
	@PutMapping("/{userId}")
	public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto,
			@PathVariable("userId") Long userId) {
		UserDto updatedUser = this.userService.updateUser(userDto, userId);
		return ResponseEntity.ok(updatedUser);
	}

	// DELETE - delete single user
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{userId}")
	public ResponseEntity<ApiResponse> deleteUser(@PathVariable("userId") Long userId) {
		this.userService.deleteUserById(userId);
		return new ResponseEntity<ApiResponse>(new ApiResponse("User Deleted Successfully", true), HttpStatus.OK);
	}

}
