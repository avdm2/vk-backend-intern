package com.vk.services;


import com.vk.dto.users.CreateUserRequest;
import com.vk.dto.users.CreateUserResponse;
import com.vk.dto.users.GetUserCommentsResponse;
import com.vk.dto.users.GetUserResponse;
import com.vk.dto.users.UpdateUserRequest;
import com.vk.dto.users.UpdateUserResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.endpoint.InvalidEndpointRequestException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserService {

    @Value("${app.urls.users}")
    private String usersUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public GetUserResponse[] getAllUsers() {
        ResponseEntity<GetUserResponse[]> response = restTemplate
                .getForEntity(usersUrl, GetUserResponse[].class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new InvalidEndpointRequestException("response error", usersUrl);
        }

        return response.getBody();
    }

    public GetUserResponse getUser(Integer userId) {
        String url = usersUrl + "/" + userId;
        ResponseEntity<GetUserResponse> response = restTemplate
                .getForEntity(url, GetUserResponse.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new InvalidEndpointRequestException("response error", url);
        }

        return response.getBody();
    }

    public GetUserCommentsResponse[] getUserComments(Integer userId) {
        String url = usersUrl + "/" + userId + "/comments";
        ResponseEntity<GetUserCommentsResponse[]> response = restTemplate
                .getForEntity(url, GetUserCommentsResponse[].class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new InvalidEndpointRequestException("response error", url);
        }

        return response.getBody();
    }

    public CreateUserResponse createUser(CreateUserRequest request) {
        ResponseEntity<CreateUserResponse> response = restTemplate
                .postForEntity(usersUrl, request, CreateUserResponse.class);

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

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new InvalidEndpointRequestException("response error", url);
        }
    }
}
