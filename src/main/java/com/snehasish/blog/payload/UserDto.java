package com.snehasish.blog.payload;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {
	private Long id;

	@NotNull
	@NotBlank
	@NotEmpty
	@Size(min = 4, message = "Username must contain atleast 4 characters !!")
	private String name;

	@Email(message = "Email address is not valid !!")
	@NotBlank
	@NotNull
	@NotEmpty
	private String email;

	@NotNull
	@NotBlank
	@NotEmpty
	@Size(min = 3, max = 10, message = "Password must contain size of characters between 3 to 10 !!")
	private String password;

	@NotNull
	@NotBlank
	@NotEmpty
	private String about;

	@NotNull
	@NotBlank
	@NotEmpty
	private Set<RoleDto> roles = new HashSet<>();

}
