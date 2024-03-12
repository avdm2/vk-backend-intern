package com.vk.services;

import com.vk.dto.posts.CreatePostRequest;
import com.vk.dto.posts.CreatePostResponse;
import com.vk.dto.posts.GetPostCommentsResponse;
import com.vk.dto.posts.GetPostResponse;
import com.vk.dto.posts.UpdatePostRequest;
import com.vk.dto.posts.UpdatePostResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.endpoint.InvalidEndpointRequestException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PostService {

    @Value("${app.urls.posts}")
    private String postsUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public GetPostResponse[] getAllPosts() {
        ResponseEntity<GetPostResponse[]> response = restTemplate
                .getForEntity(postsUrl, GetPostResponse[].class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new InvalidEndpointRequestException("response error", postsUrl);
        }

        return response.getBody();
    }

    public GetPostResponse getPost(Integer postId) {
        String url = postsUrl + "/" + postId;
        ResponseEntity<GetPostResponse> response = restTemplate
                .getForEntity(url, GetPostResponse.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new InvalidEndpointRequestException("response error", url);
        }

        return response.getBody();
    }

    public GetPostCommentsResponse[] getPostComments(Integer postId) {
        String url = postsUrl + "/" + postId + "/comments";
        ResponseEntity<GetPostCommentsResponse[]> response = restTemplate
                .getForEntity(url, GetPostCommentsResponse[].class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new InvalidEndpointRequestException("response error", url);
        }

        return response.getBody();
    }

    public CreatePostResponse createPost(CreatePostRequest request) {
        ResponseEntity<CreatePostResponse> response = restTemplate
                .postForEntity(postsUrl, request, CreatePostResponse.class);

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

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new InvalidEndpointRequestException("response error", url);
        }

        return response.getBody();
    }

    public void deletePost(Integer postId) {
        String url = postsUrl + "/" + postId;
        ResponseEntity<Void> response = restTemplate
                .exchange(url,
                        HttpMethod.DELETE,
                        new HttpEntity<>(null, null),
                        Void.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new InvalidEndpointRequestException("response error", url);
        }
    }
}
