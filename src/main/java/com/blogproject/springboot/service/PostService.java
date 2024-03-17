package com.blogproject.springboot.service;

import com.blogproject.springboot.dto.PostDto;
import com.blogproject.springboot.dto.PostPaginationDto;

public interface PostService {
    String createPost(PostDto postDto);
    PostPaginationDto getPosts(int pageNo, int pageSize,String sortBy,String sortOrder);
    PostDto getPostById(long id);
    String updatePost(PostDto postDto);
    void deletePostById(long id);
}
