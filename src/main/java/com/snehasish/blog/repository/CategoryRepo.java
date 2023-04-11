package com.snehasish.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.snehasish.blog.entity.Category;

public interface CategoryRepo extends JpaRepository<Category, Long> {

}
