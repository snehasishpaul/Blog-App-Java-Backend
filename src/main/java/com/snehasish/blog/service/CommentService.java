package com.snehasish.blog.service;

import java.util.List;

import com.snehasish.blog.payload.CommentDto;

public interface CommentService {

	// create
	CommentDto createComment(CommentDto commentDto, Long postId, Long userId);

	// read
	CommentDto getComment(Long commentID);

	// read all
	List<CommentDto> getAllComments();

	// update
	CommentDto updateComment(CommentDto commentDto, Long commentId);

	// delete
	void deleteComment(Long commendId);
}
