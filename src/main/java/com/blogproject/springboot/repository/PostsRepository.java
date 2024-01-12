package com.blogproject.springboot.repository;

import com.blogproject.springboot.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostsRepository extends JpaRepository<Post,Long> {
}
