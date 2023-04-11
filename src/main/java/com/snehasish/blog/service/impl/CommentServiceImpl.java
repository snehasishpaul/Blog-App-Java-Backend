package com.snehasish.blog.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.snehasish.blog.entity.Comment;
import com.snehasish.blog.entity.Post;
import com.snehasish.blog.entity.User;
import com.snehasish.blog.exception.ResourceNotFoundException;
import com.snehasish.blog.payload.CommentDto;
import com.snehasish.blog.repository.CommentRepo;
import com.snehasish.blog.repository.PostRepo;
import com.snehasish.blog.repository.UserRepo;
import com.snehasish.blog.service.CommentService;

@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	private PostRepo postRepo;

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private CommentRepo commentRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public CommentDto createComment(CommentDto commentDto, Long postId, Long userId) {

		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", " User id ", userId));

		Post post = this.postRepo.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post", "post id", postId));

		Comment comment = this.modelMapper.map(commentDto, Comment.class);
		comment.setUser(user);
		comment.setPost(post);

		Comment savedComment = this.commentRepo.save(comment);

		return this.modelMapper.map(savedComment, CommentDto.class);
	}

	@Override
	public CommentDto getComment(Long commentId) {
		Comment comment = this.commentRepo.findById(commentId)
				.orElseThrow(() -> new ResourceNotFoundException("Comment", "comment id", commentId));

		return this.modelMapper.map(comment, CommentDto.class);
	}

	@Override
	public List<CommentDto> getAllComments() {
		List<Comment> allComments = this.commentRepo.findAll();
		List<CommentDto> comments = allComments.stream().map(e -> this.modelMapper.map(e, CommentDto.class))
				.collect(Collectors.toList());
		return comments;
	}

	@Override
	public CommentDto updateComment(CommentDto commentDto, Long commentId) {
		Comment comment = this.commentRepo.findById(commentId)
				.orElseThrow(() -> new ResourceNotFoundException("Comment", "comment id", commentId));

		comment.setCommentContent(commentDto.getCommentContent());
		Comment updatedComment = this.commentRepo.save(comment);
		return this.modelMapper.map(updatedComment, CommentDto.class);
	}

	@Override
	public void deleteComment(Long commendId) {
		Comment comment = this.commentRepo.findById(commendId)
				.orElseThrow(() -> new ResourceNotFoundException("Comment", "comment id", commendId));
		this.commentRepo.delete(comment);

	}

}
