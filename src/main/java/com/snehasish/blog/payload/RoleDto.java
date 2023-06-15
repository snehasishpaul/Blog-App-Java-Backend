package com.snehasish.blog.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RoleDto {

	private Long id;

	@NotNull
	@NotBlank
	@NotEmpty
	private String name;
}
