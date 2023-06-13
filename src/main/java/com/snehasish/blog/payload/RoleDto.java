package com.snehasish.blog.payload;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class RoleDto {

	private Long id;

	@NotNull
	@NotBlank
	@NotEmpty
	private String name;
}
