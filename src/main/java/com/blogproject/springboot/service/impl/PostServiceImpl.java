package com.blogproject.springboot.service.impl;

import com.blogproject.springboot.dto.PostDto;
import com.blogproject.springboot.dto.PostPaginationDto;
import com.blogproject.springboot.entity.Post;
import com.blogproject.springboot.exception.ResourceNotFoundException;
import com.blogproject.springboot.mapper.PostsMapper;
import com.blogproject.springboot.repository.PostsRepository;
import com.blogproject.springboot.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService {

    private PostsRepository postsRepository;

    @Override
    public String createPost(PostDto postDto) {
        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());
        this.postsRepository.save(post);
        return "Data has been successfully created";
    }

    @Override
    public PostPaginationDto getPosts(int pageNo, int pageSize,String sortBy,String sortOrder) {
        Sort sort = sortOrder.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable= (Pageable) PageRequest.of(pageNo,pageSize, sort);
        Page<Post> posts = this.postsRepository.findAll(pageable);
        List<Post> listOfPosts = posts.getContent();
        List<PostDto> content = listOfPosts.stream().map((post)->PostsMapper.mapToPostDto(post))
                                                    .collect(Collectors.toList());
        return new PostPaginationDto(
                content,
                posts.getNumber(),
                posts.getSize(),
                posts.getTotalElements(),
                posts.getTotalPages(),
                posts.isLast()
        );
    }

    @Override
    public PostDto getPostById(long id) {
        String idNumber = String.valueOf(id);
        Post post = this.postsRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", idNumber));
        return PostsMapper.mapToPostDto(post);
    }

    @Override
    public String updatePost(PostDto postDto) {
        Post post = new Post();
        post.setId(postDto.getId());
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());
        this.postsRepository.save(post);
        return "Data has been updated";
    }

    @Override
    public void deletePostById(long id) {
        Post post = this.postsRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                    "Post",
                    "id",
                    String.valueOf(id))
            );
        if(post != null){
            this.postsRepository.delete(post);
        }
    }
}
