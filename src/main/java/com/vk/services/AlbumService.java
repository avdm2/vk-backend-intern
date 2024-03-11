package com.vk.services;

import com.vk.dto.albums.CreateAlbumRequest;
import com.vk.dto.albums.CreateAlbumResponse;
import com.vk.dto.albums.GetAlbumCommentsResponse;
import com.vk.dto.albums.GetAlbumResponse;
import com.vk.dto.albums.UpdateAlbumRequest;
import com.vk.dto.albums.UpdateAlbumResponse;
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
public class AlbumService {

    @Value("${app.urls.albums}")
    private String albumsUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final LogRepository logRepository;

    public AlbumService(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    public GetAlbumResponse[] getAllAlbums() {
        ResponseEntity<GetAlbumResponse[]> response = restTemplate
                .getForEntity(albumsUrl, GetAlbumResponse[].class);

        logRepository.save(new Log()
                .setUsername("username")
                .setTime(LocalDateTime.now())
                .setInternalRequest("GET /api/albums")
                .setExternalRequest("GET " + albumsUrl)
                .setBody(null)
                .setStatusCode(response.getStatusCode().value()));

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new InvalidEndpointRequestException("response error", albumsUrl);
        }

        return response.getBody();
    }

    public GetAlbumResponse getAlbum(Integer albumId) {
        String url = albumsUrl + "/" + albumId;
        ResponseEntity<GetAlbumResponse> response = restTemplate
                .getForEntity(url, GetAlbumResponse.class);

        logRepository.save(new Log()
                .setUsername("username")
                .setTime(LocalDateTime.now())
                .setInternalRequest("GET /api/albums/" + albumId)
                .setExternalRequest("GET " + url)
                .setBody(null)
                .setStatusCode(response.getStatusCode().value()));

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new InvalidEndpointRequestException("response error", url);
        }

        return response.getBody();
    }

    public GetAlbumCommentsResponse[] getPostComments(Integer albumId) {
        String url = albumsUrl + "/" + albumId + "/comments";
        ResponseEntity<GetAlbumCommentsResponse[]> response = restTemplate
                .getForEntity(url, GetAlbumCommentsResponse[].class);

        logRepository.save(new Log()
                .setUsername("username")
                .setTime(LocalDateTime.now())
                .setInternalRequest("GET /api/albums/" + albumId + "/comments")
                .setExternalRequest("GET " + url)
                .setBody(null)
                .setStatusCode(response.getStatusCode().value()));

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new InvalidEndpointRequestException("response error", url);
        }

        return response.getBody();
    }

    public CreateAlbumResponse createAlbum(CreateAlbumRequest request) {
        ResponseEntity<CreateAlbumResponse> response = restTemplate
                .postForEntity(albumsUrl, request, CreateAlbumResponse.class);

        logRepository.save(new Log()
                .setUsername("username")
                .setTime(LocalDateTime.now())
                .setInternalRequest("POST /api/albums")
                .setExternalRequest("POST " + albumsUrl)
                .setBody(request.toString())
                .setStatusCode(response.getStatusCode().value()));

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new InvalidEndpointRequestException("response error", albumsUrl);
        }

        return response.getBody();
    }

    public UpdateAlbumResponse updateAlbum(Integer albumId, UpdateAlbumRequest request) {
        String url = albumsUrl + "/" + albumId;
        ResponseEntity<UpdateAlbumResponse> response = restTemplate
                .exchange(url,
                        HttpMethod.PUT,
                        new HttpEntity<>(request, null),
                        UpdateAlbumResponse.class);

        logRepository.save(new Log()
                .setUsername("username")
                .setTime(LocalDateTime.now())
                .setInternalRequest("PUT /api/albums/" + albumId)
                .setExternalRequest("PUT " + url)
                .setBody(request.toString())
                .setStatusCode(response.getStatusCode().value()));

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new InvalidEndpointRequestException("response error", url);
        }

        return response.getBody();
    }

    public void deleteAlbum(Integer albumId) {
        String url = albumsUrl + "/" + albumId;
        ResponseEntity<Void> response = restTemplate
                .exchange(url,
                        HttpMethod.DELETE,
                        new HttpEntity<>(null, null),
                        Void.class);

        logRepository.save(new Log()
                .setUsername("username")
                .setTime(LocalDateTime.now())
                .setInternalRequest("DELETE /api/albums/" + albumId)
                .setExternalRequest("DELETE " + url)
                .setBody(null)
                .setStatusCode(response.getStatusCode().value()));

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new InvalidEndpointRequestException("response error", url);
        }
    }
}
