package com.snehasish.blog.controller;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.snehasish.blog.entity.User;
import com.snehasish.blog.payload.JWTAuthenticationRequest;
import com.snehasish.blog.payload.JWTAuthenticationResponse;
import com.snehasish.blog.payload.UserDto;
import com.snehasish.blog.security.JWTTokenHelper;
import com.snehasish.blog.service.UserService;

@RestController
@RequestMapping("/api/auth/")
@Validated
public class AuthController {

	@Autowired
	private JWTTokenHelper jwtTokenHelper;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserService userService;

	@Autowired
	private ModelMapper modelMapper;

	// login API
	@PostMapping("/login")
	public ResponseEntity<JWTAuthenticationResponse> createToken(@RequestBody JWTAuthenticationRequest req) {
		this.authenticate(req.getUsername(), req.getPassword());

		UserDetails userDetails = this.userDetailsService.loadUserByUsername(req.getUsername());

		String token = this.jwtTokenHelper.generateToken(userDetails);

		JWTAuthenticationResponse jwtAuthenticationResponse = new JWTAuthenticationResponse();
		jwtAuthenticationResponse.setToken(token);
		jwtAuthenticationResponse.setUserDto(this.modelMapper.map((User) userDetails, UserDto.class));
		return new ResponseEntity<JWTAuthenticationResponse>(jwtAuthenticationResponse, HttpStatus.OK);
	}

	// register new user API
	@PostMapping("/register")
	public ResponseEntity<UserDto> registerUser(@Valid @RequestBody UserDto userDto) {
		UserDto registeredUser = this.userService.registerNewUser(userDto);
		return new ResponseEntity<UserDto>(registeredUser, HttpStatus.CREATED);
	}

	// register new user by ID API
	@PostMapping("/register/{userId}")
	public ResponseEntity<UserDto> registerUser(@Valid @PathVariable Long userId) {
		UserDto registeredUser = this.userService.registerNewUserById(userId);
		return new ResponseEntity<UserDto>(registeredUser, HttpStatus.CREATED);
	}

	private void authenticate(String username, String password) {

		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
				password);

		try {
			this.authenticationManager.authenticate(authenticationToken);
		} catch (BadCredentialsException e) {
			throw new BadCredentialsException("Bad Credentials !! Invalid Username or Password");
		}

	}
}
