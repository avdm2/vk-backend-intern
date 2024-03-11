package com.vk.services;


import com.vk.dto.users.CreateUserRequest;
import com.vk.dto.users.CreateUserResponse;
import com.vk.dto.users.GetUserCommentsResponse;
import com.vk.dto.users.GetUserResponse;
import com.vk.dto.users.UpdateUserRequest;
import com.vk.dto.users.UpdateUserResponse;
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
public class UserService {

    @Value("${app.urls.users}")
    private String usersUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final LogRepository logRepository;

    public UserService(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    public GetUserResponse[] getAllUsers() {
        ResponseEntity<GetUserResponse[]> response = restTemplate
                .getForEntity(usersUrl, GetUserResponse[].class);

        logRepository.save(new Log()
                .setUsername("username")
                .setTime(LocalDateTime.now())
                .setInternalRequest("GET /api/users")
                .setExternalRequest("GET " + usersUrl)
                .setBody(null)
                .setStatusCode(response.getStatusCode().value()));

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new InvalidEndpointRequestException("response error", usersUrl);
        }

        return response.getBody();
    }

    public GetUserResponse getUser(Integer userId) {
        String url = usersUrl + "/" + userId;
        ResponseEntity<GetUserResponse> response = restTemplate
                .getForEntity(url, GetUserResponse.class);

        logRepository.save(new Log()
                .setUsername("username")
                .setTime(LocalDateTime.now())
                .setInternalRequest("GET /api/users/" + userId)
                .setExternalRequest("GET " + url)
                .setBody(null)
                .setStatusCode(response.getStatusCode().value()));

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new InvalidEndpointRequestException("response error", url);
        }

        return response.getBody();
    }

    public GetUserCommentsResponse[] getUserComments(Integer userId) {
        String url = usersUrl + "/" + userId + "/comments";
        ResponseEntity<GetUserCommentsResponse[]> response = restTemplate
                .getForEntity(url, GetUserCommentsResponse[].class);

        logRepository.save(new Log()
                .setUsername("username")
                .setTime(LocalDateTime.now())
                .setInternalRequest("GET /api/users/" + userId + "/comments")
                .setExternalRequest("GET " + url)
                .setBody(null)
                .setStatusCode(response.getStatusCode().value()));

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new InvalidEndpointRequestException("response error", url);
        }

        return response.getBody();
    }

    public CreateUserResponse createUser(CreateUserRequest request) {
        ResponseEntity<CreateUserResponse> response = restTemplate
                .postForEntity(usersUrl, request, CreateUserResponse.class);

        logRepository.save(new Log()
                .setUsername("username")
                .setTime(LocalDateTime.now())
                .setInternalRequest("POST /api/users")
                .setExternalRequest("POST " + usersUrl)
                .setBody(request.toString())
                .setStatusCode(response.getStatusCode().value()));

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new InvalidEndpointRequestException("response error", usersUrl);
        }

        return response.getBody();
    }

    public UpdateUserResponse updateUser(Integer userId, UpdateUserRequest request) {
        String url = usersUrl + "/" + userId;
        ResponseEntity<UpdateUserResponse> response = restTemplate
                .exchange(url,
                        HttpMethod.PUT,
                        new HttpEntity<>(request, null),
                        UpdateUserResponse.class);

        logRepository.save(new Log()
                .setUsername("username")
                .setTime(LocalDateTime.now())
                .setInternalRequest("PUT /api/users/" + userId)
                .setExternalRequest("PUT " + url)
                .setBody(request.toString())
                .setStatusCode(response.getStatusCode().value()));

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new InvalidEndpointRequestException("response error", url);
        }

        return response.getBody();
    }

    public void deleteUser(Integer userId) {
        String url = usersUrl + "/" + userId;
        ResponseEntity<Void> response = restTemplate
                .exchange(url,
                        HttpMethod.DELETE,
                        new HttpEntity<>(null, null),
                        Void.class);

        logRepository.save(new Log()
                .setUsername("username")
                .setTime(LocalDateTime.now())
                .setInternalRequest("DELETE /api/users/" + userId)
                .setExternalRequest("DELETE " + url)
                .setBody(null)
                .setStatusCode(response.getStatusCode().value()));

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new InvalidEndpointRequestException("response error", url);
        }
    }
}
