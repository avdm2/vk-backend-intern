package com.vk.services;

import com.vk.dto.posts.CreatePostRequest;
import com.vk.dto.posts.CreatePostResponse;
import com.vk.dto.posts.GetPostCommentResponse;
import com.vk.dto.posts.GetPostResponse;
import com.vk.dto.posts.UpdatePostRequest;
import com.vk.dto.posts.UpdatePostResponse;
import com.vk.entities.Log;
import com.vk.repositories.LogRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.endpoint.InvalidEndpointRequestException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Service
public class PostService {

    private final RestTemplate restTemplate = new RestTemplate();

    private final LogRepository logRepository;

    public PostService(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @Value("${app.urls.posts}")
    private String postsUrl;

    public GetPostResponse[] getAllPosts() {
        ResponseEntity<GetPostResponse[]> response = restTemplate
                .getForEntity(postsUrl, GetPostResponse[].class);

        logRepository.save(new Log()
                .setUsername("username")
                .setTime(LocalDateTime.now())
                .setRequest("GET " + postsUrl)
                .setBody(null)
                .setStatusCode(response.getStatusCode().value()));

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new InvalidEndpointRequestException("response error", postsUrl);
        }

        return response.getBody();
    }

    public GetPostResponse getPost(Integer postId) {
        String url = postsUrl + "/" + postId;
        ResponseEntity<GetPostResponse> response = restTemplate
                .getForEntity(url, GetPostResponse.class);

        logRepository.save(new Log()
                .setUsername("username")
                .setTime(LocalDateTime.now())
                .setRequest("GET " + url)
                .setBody(null)
                .setStatusCode(response.getStatusCode().value()));

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new InvalidEndpointRequestException("response error", url);
        }

        return response.getBody();
    }

    public GetPostCommentResponse[] getPostComments(Integer postId) {
        String url = postsUrl + "/" + postId + "/comments";
        ResponseEntity<GetPostCommentResponse[]> response = restTemplate
                .getForEntity(url, GetPostCommentResponse[].class);

        logRepository.save(new Log()
                .setUsername("username")
                .setTime(LocalDateTime.now())
                .setRequest("GET " + url)
                .setBody(null)
                .setStatusCode(response.getStatusCode().value()));

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new InvalidEndpointRequestException("response error", url);
        }

        return response.getBody();
    }

    public CreatePostResponse createPost(CreatePostRequest request) {
        ResponseEntity<CreatePostResponse> response = restTemplate
                .postForEntity(postsUrl, request, CreatePostResponse.class);

        logRepository.save(new Log()
                .setUsername("username")
                .setTime(LocalDateTime.now())
                .setRequest("POST " + postsUrl)
                .setBody(request.toString())
                .setStatusCode(response.getStatusCode().value()));

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new InvalidEndpointRequestException("response error", postsUrl);
        }

        return response.getBody();
    }

    public UpdatePostResponse updatePost(Integer postId, UpdatePostRequest request) {
        String url = postsUrl + "/" + postId;
        ResponseEntity<UpdatePostResponse> response = restTemplate
                .exchange(url,
                        HttpMethod.PUT,
                        new HttpEntity<>(request, null),
                        UpdatePostResponse.class);

        logRepository.save(new Log()
                .setUsername("username")
                .setTime(LocalDateTime.now())
                .setRequest("PUT " + url)
                .setBody(request.toString())
                .setStatusCode(response.getStatusCode().value()));

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new InvalidEndpointRequestException("response error", url);
        }

        return response.getBody();
    }

    public void deletePost(Integer postId) {
        String url = postsUrl + "/" + postId;
        ResponseEntity<?> response = restTemplate
                .exchange(url,
                        HttpMethod.DELETE,
                        new HttpEntity<>(null, null),
                        Void.class);

        logRepository.save(new Log()
                .setUsername("username")
                .setTime(LocalDateTime.now())
                .setRequest("DELETE " + url)
                .setBody(null)
                .setStatusCode(response.getStatusCode().value()));

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new InvalidEndpointRequestException("response error", url);
        }
    }
}
