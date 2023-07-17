package com.snehasish.blog.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.snehasish.blog.config.AppConstant;
import com.snehasish.blog.payload.ApiResponse;
import com.snehasish.blog.payload.PostDto;
import com.snehasish.blog.payload.PostResponse;
import com.snehasish.blog.service.FileService;
import com.snehasish.blog.service.PostService;

@RestController
@RequestMapping("/api/v1")
public class PostController {

	@Autowired
	private PostService postService;

	@Autowired
	private FileService fileService;

	@Value("${project.image}")
	private String path;

	// create
	@PostMapping("/users/categories/posts")
	public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto) {
		PostDto post = this.postService.createPost(postDto, postDto.getPostUserId(), postDto.getPostUserId());

		return new ResponseEntity<PostDto>(post, HttpStatus.CREATED);
	}

	// read by user
	@GetMapping("/users/{userId}/posts")
	public ResponseEntity<List<PostDto>> getPostsByUser(@PathVariable("userId") Long userId) {
		List<PostDto> postDtoList = this.postService.findAllPostsByUserId(userId);
		return new ResponseEntity<List<PostDto>>(postDtoList, HttpStatus.OK);
	}

	// read by category
	@GetMapping("/categories/{categoryId}/posts")
	public ResponseEntity<List<PostDto>> getPostsByCategory(@PathVariable("categoryId") Long categoryId) {
		List<PostDto> postDtoList = this.postService.findAllPostsByCategoryId(categoryId);
		return new ResponseEntity<List<PostDto>>(postDtoList, HttpStatus.OK);
	}

	// read
	@GetMapping("/posts/{postId}")
	public ResponseEntity<PostDto> getPostById(@PathVariable("postId") Long postId) {
		PostDto post = this.postService.findPostByID(postId);
		return new ResponseEntity<PostDto>(post, HttpStatus.OK);
	}

	// read all
	@GetMapping("/posts")
	public ResponseEntity<List<PostDto>> getAllPosts() {
		List<PostDto> posts = this.postService.findAllPosts();
		return new ResponseEntity<List<PostDto>>(posts, HttpStatus.OK);
	}

	// read all paginated
	@GetMapping("/postsPaginated")
	public ResponseEntity<PostResponse> getAllPosts(
			@RequestParam(value = "pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(value = "sortBy", defaultValue = AppConstant.SORT_BY, required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = AppConstant.SORT_DIR, required = false) String sortDir) {

		PostResponse postResponse = this.postService.findAllPostsPaginated(pageNumber, pageSize, sortBy, sortDir);

		return new ResponseEntity<PostResponse>(postResponse, HttpStatus.OK);
	}

	// search post
	@GetMapping("/posts/search/{keyword}")
	public ResponseEntity<List<PostDto>> searchPosts(@PathVariable("keyword") String keyword) {
		List<PostDto> result = this.postService.searchPosts(keyword);
		return new ResponseEntity<List<PostDto>>(result, HttpStatus.OK);
	}

	// update
	@PutMapping("/posts/{postId}")
	public ResponseEntity<PostDto> updatePost(@RequestBody PostDto postDto, @PathVariable("postId") Long postId) {
		PostDto updatePost = this.postService.updatePost(postDto, postId);
		return new ResponseEntity<PostDto>(updatePost, HttpStatus.OK);
	}

	// delete
	@DeleteMapping("/posts/{postId}")
	public ResponseEntity<ApiResponse> deletePost(@PathVariable("postId") Long postId) {
		this.postService.deletePost(postId);
		return new ResponseEntity<ApiResponse>(new ApiResponse("post successfully deleted", true), HttpStatus.OK);
	}

	// post image upload
	@PostMapping("/posts/image/upload/{postId}")
	public ResponseEntity<PostDto> uploadPostImage(@PathVariable("postId") Long postId,
			@RequestParam("image") MultipartFile image) throws IOException {

		PostDto postDto = this.postService.findPostByID(postId);
		String fileName = this.fileService.uploadImage(path, image);
		postDto.setPostImage(fileName);
		PostDto updatePostDto = this.postService.updatePost(postDto, postId);
		return new ResponseEntity<PostDto>(updatePostDto, HttpStatus.OK);
	}

	// method to serve file
//	@GetMapping(value = "/posts/image/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
//	public void downloadImage(@PathVariable("imageName") String imageName, HttpServletResponse response)
//			throws IOException {
//		InputStream resource = this.fileService.getResource(path, imageName);
//		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
//		StreamUtils.copy(resource, response.getOutputStream());
//	}
}
