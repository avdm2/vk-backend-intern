package com.vk.controllers;

import com.vk.dto.posts.CreatePostRequest;
import com.vk.dto.posts.CreatePostResponse;
import com.vk.dto.posts.GetPostCommentResponse;
import com.vk.dto.posts.GetPostResponse;
import com.vk.dto.posts.UpdatePostRequest;
import com.vk.dto.posts.UpdatePostResponse;
import com.vk.services.PostService;
import org.springframework.http.ResponseEntity;
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
public class PostsController {

    private final PostService postService;

    public PostsController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<GetPostResponse[]> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @GetMapping("/{postId}")
    public ResponseEntity<GetPostResponse> getPost(@PathVariable("postId") Integer postId) {
        return ResponseEntity.ok(postService.getPost(postId));
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<GetPostCommentResponse[]> getPostComments(@PathVariable("postId") Integer postId) {
        return ResponseEntity.ok(postService.getPostComments(postId));
    }

    @PostMapping
    public ResponseEntity<CreatePostResponse> createPost(@RequestBody CreatePostRequest request) {
        return ResponseEntity.ok(postService.createPost(request));
    }

    @PutMapping("/{postId}")
    public ResponseEntity<UpdatePostResponse> updatePost(@PathVariable("postId") Integer postId,
                                                         @RequestBody UpdatePostRequest request) {
        return ResponseEntity.ok(postService.updatePost(postId, request));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable("postId") Integer postId) {
        postService.deletePost(postId);
        return ResponseEntity.ok().build();
    }
}
