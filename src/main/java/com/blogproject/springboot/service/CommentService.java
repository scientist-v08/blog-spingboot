package com.blogproject.springboot.service;

import com.blogproject.springboot.dto.CommentDto;

public interface CommentService {
    CommentDto createComment(long postId, CommentDto commentDto);

}
