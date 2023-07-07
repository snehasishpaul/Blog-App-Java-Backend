package com.snehasish.blog.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth/")
@Validated
public class AuthController {

	Logger logger = LogManager.getLogger(AuthController.class);

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
	public ResponseEntity<JWTAuthenticationResponse> createToken(@RequestBody JWTAuthenticationRequest req,
			HttpServletResponse response) {
		this.authenticate(req.getUsername(), req.getPassword());

		UserDetails userDetails = this.userDetailsService.loadUserByUsername(req.getUsername());

		String token = this.jwtTokenHelper.generateToken(userDetails);

		// Create a new cookie
		Cookie cookie = new Cookie("jwtToken", token);
		cookie.setPath("/");
		cookie.setMaxAge(3600); // Set the cookie expiration time in seconds
		cookie.setHttpOnly(true); // Make the cookie accessible only via HTTP, not JavaScript
		cookie.setSecure(true); // Make the cookie secure (HTTPS-only)

		// Add the cookie to the response
		response.addCookie(cookie);

		JWTAuthenticationResponse jwtAuthenticationResponse = new JWTAuthenticationResponse();
		jwtAuthenticationResponse.setToken(token);
		jwtAuthenticationResponse.setUserDto(this.modelMapper.map((User) userDetails, UserDto.class));
//		return new ResponseEntity<JWTAuthenticationResponse>(jwtAuthenticationResponse, HttpStatus.OK);
		return ResponseEntity.status(HttpStatus.OK).body(jwtAuthenticationResponse);
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
