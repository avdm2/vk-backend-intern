package com.vk.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vk.components.CacheService;
import com.vk.dto.CacheLogDto;
import com.vk.dto.users.CreateUserRequest;
import com.vk.dto.users.CreateUserResponse;
import com.vk.dto.users.GetUserCommentsResponse;
import com.vk.dto.users.GetUserResponse;
import com.vk.dto.users.UpdateUserRequest;
import com.vk.dto.users.UpdateUserResponse;
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
public class UserService {

    @Value("${app.urls.users}")
    private String usersUrl;

    private final CacheService cacheService;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public UserService(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    public GetUserResponse[] getAllUsers() throws JsonProcessingException {
        CacheLogDto cacheLogDto = new CacheLogDto()
                .setInternalRequest("GET /api/users")
                .setRequestBody("null");
        if (cacheService.get(cacheLogDto) != null) {
            log.info("[GET /api/users] from cache");
            return objectMapper.readValue(cacheService.get(cacheLogDto), GetUserResponse[].class);
        }

        ResponseEntity<GetUserResponse[]> response = restTemplate
                .getForEntity(usersUrl, GetUserResponse[].class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new InvalidEndpointRequestException("response error", usersUrl);
        }

        cacheService.put(cacheLogDto, objectMapper.writeValueAsString(response.getBody()));
        return response.getBody();
    }

    public GetUserResponse getUser(Integer userId) throws JsonProcessingException {
        String url = usersUrl + "/" + userId;
        CacheLogDto cacheLogDto = new CacheLogDto()
                .setInternalRequest("GET /api/users/" + userId)
                .setRequestBody("null");
        if (cacheService.get(cacheLogDto) != null) {
            log.info("[GET /api/users/ " + userId + "] from cache");
            return objectMapper.readValue(cacheService.get(cacheLogDto), GetUserResponse.class);
        }

        ResponseEntity<GetUserResponse> response = restTemplate
                .getForEntity(url, GetUserResponse.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new InvalidEndpointRequestException("response error", url);
        }

        cacheService.put(cacheLogDto, objectMapper.writeValueAsString(response.getBody()));
        return response.getBody();
    }

    public GetUserCommentsResponse[] getUserComments(Integer userId) throws JsonProcessingException {
        String url = usersUrl + "/" + userId + "/comments";
        CacheLogDto cacheLogDto = new CacheLogDto()
                .setInternalRequest("GET /api/users/" + userId + "/comments")
                .setRequestBody("null");
        if (cacheService.get(cacheLogDto) != null) {
            log.info("[GET /api/users/" + userId + "/comments] from cache");
            return objectMapper.readValue(cacheService.get(cacheLogDto), GetUserCommentsResponse[].class);
        }

        ResponseEntity<GetUserCommentsResponse[]> response = restTemplate
                .getForEntity(url, GetUserCommentsResponse[].class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new InvalidEndpointRequestException("response error", url);
        }

        cacheService.put(cacheLogDto, objectMapper.writeValueAsString(response.getBody()));
        return response.getBody();
    }

    public CreateUserResponse createUser(CreateUserRequest request) throws JsonProcessingException {
        CacheLogDto cacheLogDto = new CacheLogDto()
                .setInternalRequest("POST /api/users")
                .setRequestBody(objectMapper.writeValueAsString(request));
        if (cacheService.get(cacheLogDto) != null) {
            log.info("[POST /api/users] from cache");
            return objectMapper.readValue(cacheService.get(cacheLogDto), CreateUserResponse.class);
        }

        ResponseEntity<CreateUserResponse> response = restTemplate
                .postForEntity(usersUrl, request, CreateUserResponse.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new InvalidEndpointRequestException("response error", usersUrl);
        }

        cacheService.put(cacheLogDto, objectMapper.writeValueAsString(response.getBody()));
        return response.getBody();
    }

    public UpdateUserResponse updateUser(Integer userId, UpdateUserRequest request) throws JsonProcessingException {
        String url = usersUrl + "/" + userId;
        CacheLogDto cacheLogDto = new CacheLogDto()
                .setInternalRequest("PUT /api/users/" + userId)
                .setRequestBody(objectMapper.writeValueAsString(request));
        if (cacheService.get(cacheLogDto) != null) {
            log.info("[PUT /api/users/ " + userId + "] from cache");
            return objectMapper.readValue(cacheService.get(cacheLogDto), UpdateUserResponse.class);
        }

        ResponseEntity<UpdateUserResponse> response = restTemplate
                .exchange(url,
                        HttpMethod.PUT,
                        new HttpEntity<>(request, null),
                        UpdateUserResponse.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new InvalidEndpointRequestException("response error", url);
        }

        cacheService.put(cacheLogDto, objectMapper.writeValueAsString(response.getBody()));
        return response.getBody();
    }

    public void deleteUser(Integer userId) throws JsonProcessingException {
        String url = usersUrl + "/" + userId;
        CacheLogDto cacheLogDto = new CacheLogDto()
                .setInternalRequest("DELETE /api/users/" + userId)
                .setRequestBody("null");
        if (cacheService.get(cacheLogDto) != null) {
            log.info("[DELETE /api/users/ " + userId + "] from cache");
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
