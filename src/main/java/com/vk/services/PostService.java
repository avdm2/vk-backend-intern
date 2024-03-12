package com.vk.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vk.components.CacheService;
import com.vk.dto.CacheLogDto;
import com.vk.dto.posts.CreatePostRequest;
import com.vk.dto.posts.CreatePostResponse;
import com.vk.dto.posts.GetPostCommentsResponse;
import com.vk.dto.posts.GetPostResponse;
import com.vk.dto.posts.UpdatePostRequest;
import com.vk.dto.posts.UpdatePostResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.endpoint.InvalidEndpointRequestException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class PostService {

    @Value("${app.urls.posts}")
    private String postsUrl;

    private final CacheService cacheService;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public PostService(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    public GetPostResponse[] getAllPosts() throws JsonProcessingException {
        CacheLogDto cacheLogDto = new CacheLogDto()
                .setInternalRequest("GET /api/posts")
                .setRequestBody("null");
        if (cacheService.get(cacheLogDto) != null) {
            log.info("[GET /api/posts] from cache");
            return objectMapper.readValue(cacheService.get(cacheLogDto), GetPostResponse[].class);
        }

        ResponseEntity<GetPostResponse[]> response = restTemplate
                .getForEntity(postsUrl, GetPostResponse[].class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new InvalidEndpointRequestException("response error", postsUrl);
        }

        cacheService.put(cacheLogDto, objectMapper.writeValueAsString(response.getBody()));
        return response.getBody();
    }

    public GetPostResponse getPost(Integer postId) throws JsonProcessingException {
        String url = postsUrl + "/" + postId;
        CacheLogDto cacheLogDto = new CacheLogDto()
                .setInternalRequest("GET /api/posts/" + postId)
                .setRequestBody("null");
        if (cacheService.get(cacheLogDto) != null) {
            log.info("[GET /api/posts/" + postId + "] from cache");
            return objectMapper.readValue(cacheService.get(cacheLogDto), GetPostResponse.class);
        }

        ResponseEntity<GetPostResponse> response = restTemplate
                .getForEntity(url, GetPostResponse.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new InvalidEndpointRequestException("response error", url);
        }

        cacheService.put(cacheLogDto, objectMapper.writeValueAsString(response.getBody()));
        return response.getBody();
    }

    public GetPostCommentsResponse[] getPostComments(Integer postId) throws JsonProcessingException {
        String url = postsUrl + "/" + postId + "/comments";
        CacheLogDto cacheLogDto = new CacheLogDto()
                .setInternalRequest("GET /api/posts/" + postId + "/comments")
                .setRequestBody("null");
        if (cacheService.get(cacheLogDto) != null) {
            log.info("[GET /api/posts/ " + postId + "/comments] from cache");
            return objectMapper.readValue(cacheService.get(cacheLogDto), GetPostCommentsResponse[].class);
        }

        ResponseEntity<GetPostCommentsResponse[]> response = restTemplate
                .getForEntity(url, GetPostCommentsResponse[].class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new InvalidEndpointRequestException("response error", url);
        }

        cacheService.put(cacheLogDto, objectMapper.writeValueAsString(response.getBody()));
        return response.getBody();
    }

    public CreatePostResponse createPost(CreatePostRequest request) throws JsonProcessingException {
        CacheLogDto cacheLogDto = new CacheLogDto()
                .setInternalRequest("POST /api/posts")
                .setRequestBody(objectMapper.writeValueAsString(request));
        if (cacheService.get(cacheLogDto) != null) {
            log.info("[POST /api/posts] from cache");
            return objectMapper.readValue(cacheService.get(cacheLogDto), CreatePostResponse.class);
        }

        ResponseEntity<CreatePostResponse> response = restTemplate
                .postForEntity(postsUrl, request, CreatePostResponse.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new InvalidEndpointRequestException("response error", postsUrl);
        }

        cacheService.put(cacheLogDto, objectMapper.writeValueAsString(response.getBody()));
        return response.getBody();
    }

    public UpdatePostResponse updatePost(Integer postId, UpdatePostRequest request) throws JsonProcessingException {
        String url = postsUrl + "/" + postId;
        CacheLogDto cacheLogDto = new CacheLogDto()
                .setInternalRequest("PUT /api/posts/" + postId)
                .setRequestBody(objectMapper.writeValueAsString(request));
        if (cacheService.get(cacheLogDto) != null) {
            log.info("[PUT /api/posts/ " + postId + "] from cache");
            return objectMapper.readValue(cacheService.get(cacheLogDto), UpdatePostResponse.class);
        }

        ResponseEntity<UpdatePostResponse> response = restTemplate
                .exchange(url,
                        HttpMethod.PUT,
                        new HttpEntity<>(request, null),
                        UpdatePostResponse.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new InvalidEndpointRequestException("response error", url);
        }

        cacheService.put(cacheLogDto, objectMapper.writeValueAsString(response.getBody()));
        return response.getBody();
    }

    public void deletePost(Integer postId) throws JsonProcessingException {
        String url = postsUrl + "/" + postId;
        CacheLogDto cacheLogDto = new CacheLogDto()
                .setInternalRequest("DELETE /api/posts/" + postId)
                .setRequestBody("null");
        if (cacheService.get(cacheLogDto) != null) {
            log.info("[DELETE /api/posts/ " + postId + "] from cache");
            return;
        }

        ResponseEntity<Void> response = restTemplate
                .exchange(url,
                        HttpMethod.DELETE,
                        new HttpEntity<>(null, null),
                        Void.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new InvalidEndpointRequestException("response error", url);
        }

        cacheService.put(cacheLogDto, objectMapper.writeValueAsString(response.getBody()));
    }
}
