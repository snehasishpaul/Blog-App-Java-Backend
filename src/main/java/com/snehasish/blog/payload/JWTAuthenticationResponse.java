package com.snehasish.blog.payload;

import lombok.Data;

@Data
public class JWTAuthenticationResponse {
//	private String token;

	private UserDto userDto;
}
