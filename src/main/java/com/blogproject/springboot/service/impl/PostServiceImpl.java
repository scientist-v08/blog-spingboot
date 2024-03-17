package com.blogproject.springboot.service.impl;

import com.blogproject.springboot.dto.PostDto;
import com.blogproject.springboot.dto.PostPaginationDto;
import com.blogproject.springboot.entity.Post;
import com.blogproject.springboot.exception.ResourceNotFoundException;
import com.blogproject.springboot.mapper.PostsMapper;
import com.blogproject.springboot.repository.PostsRepository;
import com.blogproject.springboot.service.PostService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.hibernate.query.sqm.tree.SqmCopyContext;
import org.hibernate.query.sqm.tree.select.SqmSelectStatement;
import org.hibernate.query.sqm.tree.select.SqmSubQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private final PostsRepository postsRepository;
    private final EntityManager entityManager;
    private final CriteriaBuilder criteriaBuilder;

    public PostServiceImpl(
        PostsRepository postsRepository,
        EntityManager entityManager
    ) {
        this.entityManager = entityManager;
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
        this.postsRepository = postsRepository;
    }

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
    public PostPaginationDto getPosts(int pageNo, int pageSize,String sortBy,String sortOrder, String title) {
        
        CriteriaQuery<Post> criteriaQuery = criteriaBuilder.createQuery(Post.class); // CriteriaQuery writes queries in a programmatic/dynamic way.
        Root<Post> root = criteriaQuery.from(Post.class); // Root tells us from which DB to take the data from.
        List<Predicate> predicates = new ArrayList<>();
        if(!isEmptyInputString(title)){
            predicates.add(
                this.criteriaBuilder.like(root.get("title"),"%"+title+"%")
            );
        }
        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        Sort sort = sortOrder.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable= PageRequest.of(pageNo,pageSize, sort);
        if(sortOrder.equalsIgnoreCase("asc")){
            criteriaQuery.orderBy(this.criteriaBuilder.asc(root.get(sortBy)));
        }
        else {
            criteriaQuery.orderBy(this.criteriaBuilder.desc(root.get(sortBy)));
        }

        TypedQuery<Post> typedQuery = this.entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult(pageNo * pageSize);
        typedQuery.setMaxResults(pageSize);

        Long employeesCount = this.count(this.criteriaBuilder, criteriaQuery);
        
        Page<Post> posts = new PageImpl<>(typedQuery.getResultList(), pageable, employeesCount);
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

    private boolean isEmptyInputString(String inputString) {
        return inputString == null || inputString.isEmpty() || inputString.equals("null") || inputString.length() < 1;
    }

    private Long count(CriteriaBuilder builder, CriteriaQuery<Post> query) {
        var countBuilder = (HibernateCriteriaBuilder) builder; //CriteriaBuilder from class level
        var countQuery = countBuilder.createQuery(Long.class);
        var subQuery = countQuery.subquery(Tuple.class);

        var sqmSubQuery = (SqmSubQuery<Tuple>) subQuery;
        var sqmOriginalQuery = (SqmSelectStatement) query; // CriteriaQuery from class level
        var sqmOriginalQuerySpec = sqmOriginalQuery.getQuerySpec();
        var sqmSubQuerySpec = sqmOriginalQuerySpec.copy(SqmCopyContext.simpleContext());

        sqmSubQuery.setQueryPart(sqmSubQuerySpec);
        Root<?> subQuerySelectRoot = subQuery.getRoots().iterator().next();
        sqmSubQuery.multiselect(subQuerySelectRoot.get("id").alias("id"));

        countQuery.multiselect(countBuilder.count(countBuilder.literal(1)));
        countQuery.from(sqmSubQuery.distinct(true).orderBy(builder.asc(subQuerySelectRoot.get("id"))));

        return entityManager.createQuery(countQuery).getSingleResult();
    }
}
