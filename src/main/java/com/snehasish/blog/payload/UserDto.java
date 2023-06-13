package com.snehasish.blog.payload;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {
	private Long id;

	@NotNull
	@NotBlank(message = "Name must not be empty")
	@NotEmpty(message = "Name must not be empty")
	@Size(min = 4, message = "Username must contain atleast 4 characters !!")
	private String name;

	@Email(message = "Email is not valid !!")
	@NotBlank(message = "Email must not be empty")
	@NotNull
	@NotEmpty(message = "Email must not be empty")
	private String email;

	@NotNull
	@NotBlank(message = "Password must not be empty")
	@NotEmpty(message = "Password must not be empty")
	@Size(min = 3, max = 10, message = "Password must contain size of characters between 3 to 10 !!")
	private String password;

	@NotNull
	@NotBlank(message = "About must not be empty")
	@NotEmpty(message = "About must not be empty")
	private String about;

	Set<RoleDto> roles = new HashSet<>();

	@JsonIgnore
	public String getPassword() {
		return this.password;
	}

	@JsonProperty
	public void setPassword(String password) {
		this.password = password;
	}

}