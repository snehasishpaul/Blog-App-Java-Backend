package com.snehasish.blog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.snehasish.blog.entity.Category;
import com.snehasish.blog.entity.Post;
import com.snehasish.blog.entity.User;

public interface PostRepo extends JpaRepository<Post, Long> {

	List<Post> findByUser(User user);

	List<Post> findByCategory(Category category);

	List<Post> findByCategory_CategoryId(Long categoryId);

	List<Post> findByPostTitleContaining(String title);
}
