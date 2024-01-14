package com.blogproject.springboot.controller;

import com.blogproject.springboot.dto.PostDto;
import com.blogproject.springboot.dto.PostPaginationDto;
import com.blogproject.springboot.service.PostService;
import com.blogproject.springboot.util.AppConstants;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@AllArgsConstructor
@CrossOrigin("*")
public class PostController {
    private PostService postService;
    @Secured(AppConstants.ADMIN_USER)
    @PostMapping
    public ResponseEntity<String> createPost(@Valid @RequestBody PostDto postDto){
        String response= this.postService.createPost(postDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<PostPaginationDto> getAllPosts(
            @RequestParam(value = "pageNo",defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize",defaultValue = AppConstants.DEFAULT_PAGE_SIZE,required = false)int pageSize,
            @RequestParam(value = "sortBy",defaultValue = AppConstants.DEFAULT_SORT_BY,required = false)String sortBy,
            @RequestParam(value = "sortOrder",defaultValue = AppConstants.DEFAULT_SORT_ORDER,required = false)String sortOrder
    ){
        PostPaginationDto posts = this.postService.getPosts(pageNo,pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable(name = "id") long id){
        return ResponseEntity.ok(this.postService.getPostById(id));
    }

    @Secured(AppConstants.ADMIN_USER)
    @PutMapping("/{id}")
    public ResponseEntity<String> updatePost(@Valid @RequestBody PostDto postDto){
        String successMessage = postService.updatePost(postDto);
        return new ResponseEntity<>(successMessage, HttpStatus.OK);
    }

    @Secured(AppConstants.ADMIN_USER)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable(name = "id") long id){
        this.postService.deletePostById(id);
        return new ResponseEntity<>("Post entity deleted successfully.", HttpStatus.OK);
    }
}
