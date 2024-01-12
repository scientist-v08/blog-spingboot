package com.blogproject.springboot.mapper;

import com.blogproject.springboot.dto.PostDto;
import com.blogproject.springboot.entity.Post;

public class PostsMapper {
    public static PostDto mapToPostDto(Post post){
        return new PostDto(
                post.getId(),
                post.getTitle(),
                post.getDescription(),
                post.getContent()
        );
    }

    public static Post mapToPost(PostDto postDto){
        return new Post(
                postDto.getId(),
                postDto.getTitle(),
                postDto.getDescription(),
                postDto.getContent()
        );
    }
}
