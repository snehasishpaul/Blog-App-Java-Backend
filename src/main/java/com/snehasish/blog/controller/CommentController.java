package com.snehasish.blog.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.snehasish.blog.payload.ApiResponse;
import com.snehasish.blog.payload.CommentDto;
import com.snehasish.blog.service.CommentService;

@RestController
@RequestMapping("/api")
public class CommentController {

	@Autowired
	private CommentService commentService;

	// create
	@PostMapping("/users/{userId}/posts/{postId}/comments")
	public ResponseEntity<CommentDto> createComment(@RequestBody CommentDto commentDto,
			@PathVariable("postId") Long postId, @PathVariable("userId") Long userId) {
		CommentDto createdComment = this.commentService.createComment(commentDto, postId, userId);
		return new ResponseEntity<CommentDto>(createdComment, HttpStatus.CREATED);
	}

	// read
	@GetMapping("/comments/{commentId}")
	public ResponseEntity<CommentDto> getComment(@PathVariable("commentId") Long commentId) {
		CommentDto comment = this.commentService.getComment(commentId);
		return new ResponseEntity<CommentDto>(comment, HttpStatus.OK);
	}

	// read all
	@GetMapping("/comments")
	public ResponseEntity<List<CommentDto>> getAllComments() {
		List<CommentDto> allComments = this.commentService.getAllComments();
		return new ResponseEntity<List<CommentDto>>(allComments, HttpStatus.OK);
	}

	// update
	@PutMapping("/comments/{commentId}")
	public ResponseEntity<CommentDto> updateComment(@RequestBody CommentDto commentDto,
			@PathVariable("commentId") Long commentId) {
		CommentDto updateComment = this.commentService.updateComment(commentDto, commentId);
		return new ResponseEntity<CommentDto>(updateComment, HttpStatus.OK);
	}

	// delete
	@DeleteMapping("/comments/{commentId}")
	public ResponseEntity<ApiResponse> deleteComment(@PathVariable("commentId") Long commmentId) {
		this.commentService.deleteComment(commmentId);
		return new ResponseEntity<ApiResponse>(new ApiResponse("Comment deleted successfully", true), HttpStatus.OK);
	}
}
