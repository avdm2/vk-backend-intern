package com.vk.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vk.dto.posts.CreatePostRequest;
import com.vk.dto.posts.CreatePostResponse;
import com.vk.dto.posts.GetPostCommentsResponse;
import com.vk.dto.posts.GetPostResponse;
import com.vk.dto.posts.UpdatePostRequest;
import com.vk.dto.posts.UpdatePostResponse;
import com.vk.services.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/posts")
@PreAuthorize("hasRole('ROLE_POSTS_VIEWER')")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<GetPostResponse[]> getAllPosts() throws JsonProcessingException {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @GetMapping("/{postId}")
    public ResponseEntity<GetPostResponse> getPost(@PathVariable("postId") Integer postId)
            throws JsonProcessingException {
        return ResponseEntity.ok(postService.getPost(postId));
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<GetPostCommentsResponse[]> getPostComments(@PathVariable("postId") Integer postId)
            throws JsonProcessingException {
        return ResponseEntity.ok(postService.getPostComments(postId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_POSTS')")
    public ResponseEntity<CreatePostResponse> createPost(@RequestBody CreatePostRequest request)
            throws JsonProcessingException {
        return ResponseEntity.ok(postService.createPost(request));
    }

    @PutMapping("/{postId}")
    @PreAuthorize("hasRole('ROLE_POSTS')")
    public ResponseEntity<UpdatePostResponse> updatePost(@PathVariable("postId") Integer postId,
                                                         @RequestBody UpdatePostRequest request)
            throws JsonProcessingException {
        return ResponseEntity.ok(postService.updatePost(postId, request));
    }

    @DeleteMapping("/{postId}")
    @PreAuthorize("hasRole('ROLE_POSTS')")
    public ResponseEntity<?> deletePost(@PathVariable("postId") Integer postId) throws JsonProcessingException {
        postService.deletePost(postId);
        return ResponseEntity.ok().build();
    }
}
