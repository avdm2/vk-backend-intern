package com.vk.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vk.dto.albums.CreateAlbumRequest;
import com.vk.dto.albums.CreateAlbumResponse;
import com.vk.dto.albums.GetAlbumCommentsResponse;
import com.vk.dto.albums.GetAlbumResponse;
import com.vk.dto.albums.UpdateAlbumRequest;
import com.vk.dto.albums.UpdateAlbumResponse;
import com.vk.services.AlbumService;
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
@RequestMapping("/api/albums")
@PreAuthorize("hasRole('ROLE_ALBUMS_VIEWER')")
public class AlbumController {

    private final AlbumService albumService;

    public AlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }

    @GetMapping
    public ResponseEntity<GetAlbumResponse[]> getAllAlbums() throws JsonProcessingException {
        return ResponseEntity.ok(albumService.getAllAlbums());
    }

    @GetMapping("/{albumId}")
    public ResponseEntity<GetAlbumResponse> getAlbum(@PathVariable("albumId") Integer albumId)
            throws JsonProcessingException {
        return ResponseEntity.ok(albumService.getAlbum(albumId));
    }

    @GetMapping("/{albumId}/comments")
    public ResponseEntity<GetAlbumCommentsResponse[]> getAlbumComments(@PathVariable("albumId") Integer albumId)
            throws JsonProcessingException {
        return ResponseEntity.ok(albumService.getPostComments(albumId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ALBUMS')")
    public ResponseEntity<CreateAlbumResponse> createAlbum(@RequestBody CreateAlbumRequest request)
            throws JsonProcessingException {
        return ResponseEntity.ok(albumService.createAlbum(request));
    }

    @PutMapping("/{albumId}")
    @PreAuthorize("hasRole('ROLE_ALBUMS')")
    public ResponseEntity<UpdateAlbumResponse> updateAlbum(@PathVariable("albumId") Integer albumId,
                                                           @RequestBody UpdateAlbumRequest request)
            throws JsonProcessingException {
        return ResponseEntity.ok(albumService.updateAlbum(albumId, request));
    }

    @DeleteMapping("/{albumId}")
    @PreAuthorize("hasRole('ROLE_ALBUMS')")
    public ResponseEntity<?> deleteAlbum(@PathVariable("albumId") Integer albumId) throws JsonProcessingException {
        albumService.deleteAlbum(albumId);
        return ResponseEntity.ok().build();
    }
}
