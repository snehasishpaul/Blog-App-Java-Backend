package com.snehasish.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.snehasish.blog.entity.Role;

public interface RoleRepo extends JpaRepository<Role, Long> {

}
