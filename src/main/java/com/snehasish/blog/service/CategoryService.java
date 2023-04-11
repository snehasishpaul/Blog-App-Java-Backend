package com.snehasish.blog.service;

import java.util.List;

import com.snehasish.blog.payload.CategoryDto;

public interface CategoryService {

	// create
	public CategoryDto createCategory(CategoryDto categoryDto);

	// read
	public CategoryDto getCategory(Long categoryId);

	// read all
	public List<CategoryDto> getAllCategory();

	// update
	public CategoryDto updateCategory(CategoryDto categoryDto, Long categoryId);

	// delete
	public void deleteCategory(Long categoryId);
}
