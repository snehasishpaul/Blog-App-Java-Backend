package com.snehasish.blog.payload;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CategoryDto {

	private Long categoryId;

	@NotBlank
	@Size(min = 4, message = "title must be more than 3 characters")
	private String categoryTitle;

	@NotBlank
	@Size(min = 10, message = "description must be atleast 10 characters")
	private String categoryDescription;

}
