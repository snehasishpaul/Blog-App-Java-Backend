package com.snehasish.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.snehasish.blog.entity.Comment;

public interface CommentRepo extends JpaRepository<Comment, Long> {

}
