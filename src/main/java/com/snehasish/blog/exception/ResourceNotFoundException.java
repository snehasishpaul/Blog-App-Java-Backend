package com.snehasish.blog.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResourceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	String resourceName;
	String fieldName;
	Long field;
	String email;

	public ResourceNotFoundException(String resourceName, String fieldName, Long field) {
		super(String.format("%s not found with %s : %s", resourceName, fieldName, field));
		this.resourceName = resourceName;
		this.fieldName = fieldName;
		this.field = field;
	}

	public ResourceNotFoundException(String resourceName, String fieldName, String email) {
		super(String.format("%s not found with %s : %s", resourceName, fieldName, email));
		this.resourceName = resourceName;
		this.fieldName = fieldName;
		this.email = email;
	}

}
