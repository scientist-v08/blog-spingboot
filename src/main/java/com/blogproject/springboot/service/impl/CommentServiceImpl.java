package com.blogproject.springboot.service.impl;

import com.blogproject.springboot.dto.CommentDto;
import com.blogproject.springboot.entity.Comment;
import com.blogproject.springboot.entity.Post;
import com.blogproject.springboot.exception.BlogAPIException;
import com.blogproject.springboot.exception.ResourceNotFoundException;
import com.blogproject.springboot.mapper.CommentsMapper;
import com.blogproject.springboot.repository.CommentRepository;
import com.blogproject.springboot.repository.PostsRepository;
import com.blogproject.springboot.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private PostsRepository postsRepository;
    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {
        Comment comment = CommentsMapper.mapToEntity(commentDto);
        // retrieve post entity by id
        Post post = postsRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", String.valueOf(postId)));
        // set post to comment entity
        comment.setPost(post);
        // comment entity to DB
        Comment newComment =  commentRepository.save(comment);
        return CommentsMapper.mapToDTO(newComment);
    }

    @Override
    public List<CommentDto> getCommentsByPostId(long postId) {
        // retrieve comments by postId
        List<Comment> comments = commentRepository.findByPostId(postId);
        // convert list of comment entities to list of comment dto's
        return comments.stream().map(comment -> CommentsMapper.mapToDTO(comment))
                                .collect(Collectors.toList());
    }

    @Override
    public CommentDto getCommentById(Long postId, Long commentId) {
        // retrieve post entity by id
        Post post = postsRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", String.valueOf(postId)));

        // retrieve comment by id
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new ResourceNotFoundException("Comment", "id", String.valueOf(commentId)));

        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
        }

        return CommentsMapper.mapToDTO(comment);
    }

    @Override
    public CommentDto updateComment(Long postId, long commentId, CommentDto commentRequest) {
        // retrieve post entity by id
        Post post = postsRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", String.valueOf(postId)));

        // retrieve comment by id
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new ResourceNotFoundException("Comment", "id", String.valueOf(commentId)));

        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belongs to post");
        }

        comment.setName(commentRequest.getName());
        comment.setEmail(commentRequest.getEmail());
        comment.setBody(commentRequest.getBody());

        Comment updatedComment = commentRepository.save(comment);
        return CommentsMapper.mapToDTO(updatedComment);
    }

    @Override
    public void deleteComment(Long postId, Long commentId) {
        // retrieve post entity by id
        Post post = postsRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", String.valueOf(postId)));

        // retrieve comment by id
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new ResourceNotFoundException("Comment", "id", String.valueOf(commentId)));

        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belongs to post");
        }

        commentRepository.delete(comment);
    }
}
