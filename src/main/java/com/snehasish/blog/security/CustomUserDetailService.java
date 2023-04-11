package com.snehasish.blog.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.snehasish.blog.entity.User;
import com.snehasish.blog.exception.ResourceNotFoundException;
import com.snehasish.blog.repository.UserRepo;

@Service
public class CustomUserDetailService implements UserDetailsService {

	@Autowired
	private UserRepo useRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = this.useRepo.findByEmail(username)
				.orElseThrow(() -> new ResourceNotFoundException("User", "email", username));
		return user;
	}

}
