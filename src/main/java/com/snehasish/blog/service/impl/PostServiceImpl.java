package com.snehasish.blog.service.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.snehasish.blog.entity.Category;
import com.snehasish.blog.entity.Post;
import com.snehasish.blog.entity.User;
import com.snehasish.blog.exception.ResourceNotFoundException;
import com.snehasish.blog.payload.PostDto;
import com.snehasish.blog.payload.PostResponse;
import com.snehasish.blog.repository.CategoryRepo;
import com.snehasish.blog.repository.PostRepo;
import com.snehasish.blog.repository.UserRepo;
import com.snehasish.blog.service.PostService;

@Service
public class PostServiceImpl implements PostService {

	@Autowired
	private PostRepo postRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private CategoryRepo categoryRepo;

	@Override
	public PostDto createPost(PostDto postDto, Long userId, Long categoryId) {

		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", " User id ", userId));

		Category category = this.categoryRepo.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", " Category id ", categoryId));

		Post post = this.modelMapper.map(postDto, Post.class);
		post.setPostImage("default.png");
		post.setAddedDate(new Date());
		post.setUser(user);
		post.setCategory(category);

		Post newPost = this.postRepo.save(post);

		return this.modelMapper.map(newPost, PostDto.class);

	}

	@Override
	public PostDto findPostByID(Long postId) {
		Post post = this.postRepo.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("post", " post id ", postId));
		return this.modelMapper.map(post, PostDto.class);
	}

	public List<PostDto> findAllPosts() {
		// normal getAll post method

		List<Post> posts = this.postRepo.findAll();
		List<PostDto> postDtoList = posts.stream().map(post -> this.modelMapper.map(post, PostDto.class))
				.collect(Collectors.toList());
		return postDtoList;
	}

	@Override
	public PostResponse findAllPostsPaginated(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {

		// pagination implemented with get all posts

		Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

		Pageable pageObject = PageRequest.of(pageNumber, pageSize, sort);

		Page<Post> postPages = this.postRepo.findAll(pageObject);
		List<Post> posts = postPages.getContent();

		List<PostDto> postDtoList = posts.stream().map((p) -> this.modelMapper.map(p, PostDto.class))
				.collect(Collectors.toList());

		PostResponse postResponse = new PostResponse();

		postResponse.setContent(postDtoList);
		postResponse.setPageNumber(postPages.getNumber());
		postResponse.setPageSize(postPages.getSize());
		postResponse.setTotalElements(postPages.getTotalElements());
		postResponse.setTotalPages(postPages.getTotalPages());
		postResponse.setLastPage(postPages.isLast());

		return postResponse;

	}

	@Override
	public List<PostDto> findAllPostsByCategoryId(Long categoryId) {

//		Category category = this.categoryRepo.findById(categoryId)
//				.orElseThrow(() -> new ResourceNotFoundException("category", " category id ", categoryId));
//
//		List<Post> posts = this.postRepo.findByCategory(category);

		List<Post> posts = this.postRepo.findByCategory_CategoryId(categoryId);

		List<PostDto> postDtoList = posts.stream().map((e) -> this.modelMapper.map(e, PostDto.class))
				.collect(Collectors.toList());

		return postDtoList;
	}

	@Override
	public List<PostDto> findAllPostsByUserId(Long userId) {
		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("user", " user id ", userId));
		List<Post> posts = this.postRepo.findByUser(user);
		List<PostDto> postDtoList = posts.stream().map((post) -> this.modelMapper.map(post, PostDto.class))
				.collect(Collectors.toList());
		return postDtoList;
	}

	@Override
	public List<PostDto> searchPosts(String keyword) {
		List<Post> posts = this.postRepo.findByPostTitleContaining(keyword);
		List<PostDto> result = posts.stream().map((p) -> this.modelMapper.map(p, PostDto.class))
				.collect(Collectors.toList());
		return result;
	}

	@Override
	public PostDto updatePost(PostDto postDto, Long postId) {
		Post post = this.postRepo.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("post", " post id ", postId));
		post.setPostTitle(postDto.getPostTitle());
		post.setPostContent(postDto.getPostContent());
		post.setPostImage(postDto.getPostImage());

		Post updatedPost = this.postRepo.save(post);
		return this.modelMapper.map(updatedPost, PostDto.class);
	}

	@Override
	public void deletePost(Long postId) {
		Post post = this.postRepo.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("post", " post id ", postId));
		this.postRepo.delete(post);

	}

}
