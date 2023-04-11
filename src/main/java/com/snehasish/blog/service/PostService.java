package com.snehasish.blog.service;

import java.util.List;

import com.snehasish.blog.payload.PostDto;
import com.snehasish.blog.payload.PostResponse;

public interface PostService {

	// create
	PostDto createPost(PostDto postDto, Long userId, Long categoryId);

	// read
	PostDto findPostByID(Long postId);

	// read all
	List<PostDto> findAllPosts();

	// read all with pagination
	PostResponse findAllPostsPaginated(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

	// read all posts by category
	List<PostDto> findAllPostsByCategoryId(Long categoryId);

	// read all posts by user
	List<PostDto> findAllPostsByUserId(Long userId);

	// search post
	List<PostDto> searchPosts(String keyword);

	// update
	PostDto updatePost(PostDto postDto, Long postId);

	// delete
	void deletePost(Long postId);

}
