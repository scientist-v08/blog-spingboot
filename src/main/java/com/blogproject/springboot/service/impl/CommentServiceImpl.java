package com.blogproject.springboot.service.impl;

import com.blogproject.springboot.dto.CommentDto;
import com.blogproject.springboot.entity.Comment;
import com.blogproject.springboot.entity.Post;
import com.blogproject.springboot.exception.ResourceNotFoundException;
import com.blogproject.springboot.repository.CommentRepository;
import com.blogproject.springboot.repository.PostsRepository;
import com.blogproject.springboot.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {
    
    private CommentRepository commentRepository;
    private PostsRepository postsRepository;
    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {
        Comment comment = mapToEntity(commentDto);
        // retrieve post entity by id
        Post post = postsRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", String.valueOf(postId)));
        // set post to comment entity
        comment.setPost(post);
        // comment entity to DB
        Comment newComment =  commentRepository.save(comment);
        return mapToDTO(newComment);
    }

    private CommentDto mapToDTO(Comment comment){
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setName(comment.getName());
        commentDto.setEmail(comment.getEmail());
        commentDto.setBody(comment.getBody());
        return  commentDto;
    }

    private Comment mapToEntity(CommentDto commentDto){
        Comment comment = new Comment();
        comment.setId(commentDto.getId());
        comment.setName(commentDto.getName());
        comment.setEmail(commentDto.getEmail());
        comment.setBody(commentDto.getBody());
        return  comment;
    }
}
