package com.snehasish.blog.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentDto {

	private Long commentId;

	private String commentContent;

//	private PostDto post;
//
//	private UserDto user;
}
