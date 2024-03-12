package com.vk.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vk.components.CacheService;
import com.vk.dto.CacheLogDto;
import com.vk.dto.albums.CreateAlbumRequest;
import com.vk.dto.albums.CreateAlbumResponse;
import com.vk.dto.albums.GetAlbumCommentsResponse;
import com.vk.dto.albums.GetAlbumResponse;
import com.vk.dto.albums.UpdateAlbumRequest;
import com.vk.dto.albums.UpdateAlbumResponse;
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
public class AlbumService {

    @Value("${app.urls.albums}")
    private String albumsUrl;

    private final CacheService cacheService;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AlbumService(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    public GetAlbumResponse[] getAllAlbums() throws JsonProcessingException {
        CacheLogDto cacheLogDto = new CacheLogDto()
                .setInternalRequest("GET /api/albums")
                .setRequestBody("null");
        if (cacheService.get(cacheLogDto) != null) {
            log.info("[GET /api/albums] from cache");
            return objectMapper.readValue(cacheService.get(cacheLogDto), GetAlbumResponse[].class);
        }

        ResponseEntity<GetAlbumResponse[]> response = restTemplate
                .getForEntity(albumsUrl, GetAlbumResponse[].class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new InvalidEndpointRequestException("response error", albumsUrl);
        }

        cacheService.put(cacheLogDto, objectMapper.writeValueAsString(response.getBody()));
        return response.getBody();
    }

    public GetAlbumResponse getAlbum(Integer albumId) throws JsonProcessingException {
        String url = albumsUrl + "/" + albumId;
        CacheLogDto cacheLogDto = new CacheLogDto()
                .setInternalRequest("GET /api/albums/" + albumId)
                .setRequestBody("null");
        if (cacheService.get(cacheLogDto) != null) {
            log.info("[GET /api/albums/ " + albumId + "] from cache");
            return objectMapper.readValue(cacheService.get(cacheLogDto), GetAlbumResponse.class);
        }

        ResponseEntity<GetAlbumResponse> response = restTemplate
                .getForEntity(url, GetAlbumResponse.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new InvalidEndpointRequestException("response error", url);
        }

        cacheService.put(cacheLogDto, objectMapper.writeValueAsString(response.getBody()));
        return response.getBody();
    }

    public GetAlbumCommentsResponse[] getPostComments(Integer albumId) throws JsonProcessingException {
        String url = albumsUrl + "/" + albumId + "/comments";
        CacheLogDto cacheLogDto = new CacheLogDto()
                .setInternalRequest("GET /api/albums/" + albumId + "/comments")
                .setRequestBody("null");
        if (cacheService.get(cacheLogDto) != null) {
            log.info("[GET /api/albums/ " + albumId + "/comments] from cache");
            return objectMapper.readValue(cacheService.get(cacheLogDto), GetAlbumCommentsResponse[].class);
        }

        ResponseEntity<GetAlbumCommentsResponse[]> response = restTemplate
                .getForEntity(url, GetAlbumCommentsResponse[].class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new InvalidEndpointRequestException("response error", url);
        }

        cacheService.put(cacheLogDto, objectMapper.writeValueAsString(response.getBody()));
        return response.getBody();
    }

    public CreateAlbumResponse createAlbum(CreateAlbumRequest request) throws JsonProcessingException {
        CacheLogDto cacheLogDto = new CacheLogDto()
                .setInternalRequest("POST /api/albums")
                .setRequestBody(objectMapper.writeValueAsString(request));
        if (cacheService.get(cacheLogDto) != null) {
            log.info("[POST /api/albums] from cache");
            return objectMapper.readValue(cacheService.get(cacheLogDto), CreateAlbumResponse.class);
        }

        ResponseEntity<CreateAlbumResponse> response = restTemplate
                .postForEntity(albumsUrl, request, CreateAlbumResponse.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new InvalidEndpointRequestException("response error", albumsUrl);
        }

        cacheService.put(cacheLogDto, objectMapper.writeValueAsString(response.getBody()));
        return response.getBody();
    }

    public UpdateAlbumResponse updateAlbum(Integer albumId, UpdateAlbumRequest request) throws JsonProcessingException {
        String url = albumsUrl + "/" + albumId;
        CacheLogDto cacheLogDto = new CacheLogDto()
                .setInternalRequest("PUT /api/albums/" + albumId)
                .setRequestBody(objectMapper.writeValueAsString(request));
        if (cacheService.get(cacheLogDto) != null) {
            log.info("[PUT /api/albums/ " + albumId + "] from cache");
            return objectMapper.readValue(cacheService.get(cacheLogDto), UpdateAlbumResponse.class);
        }

        ResponseEntity<UpdateAlbumResponse> response = restTemplate
                .exchange(url,
                        HttpMethod.PUT,
                        new HttpEntity<>(request, null),
                        UpdateAlbumResponse.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new InvalidEndpointRequestException("response error", url);
        }

        cacheService.put(cacheLogDto, objectMapper.writeValueAsString(response.getBody()));
        return response.getBody();
    }

    public void deleteAlbum(Integer albumId) throws JsonProcessingException {
        String url = albumsUrl + "/" + albumId;
        CacheLogDto cacheLogDto = new CacheLogDto()
                .setInternalRequest("DELETE /api/albums/" + albumId)
                .setRequestBody("null");
        if (cacheService.get(cacheLogDto) != null) {
            log.info("[DELETE /api/albums/ " + albumId + "] from cache");
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
