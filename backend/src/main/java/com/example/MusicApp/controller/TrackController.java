package com.example.MusicApp.controller;

import com.example.MusicApp.dto.response.TrackResponseDTO;
import com.example.MusicApp.service.impl.TrackServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.hc.core5.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/track")
@CrossOrigin(origins = "https://localhost:5173") // Allow your Vue app
public class TrackController {

    private final TrackServiceImpl trackService;

    @GetMapping
    public List<TrackResponseDTO> getAllTrack() {
        return trackService.getAll();
    }

    @GetMapping("/search/{trackTitle}")
    public List<TrackResponseDTO> getTrackByTitle(@PathVariable String trackTitle) {
        return trackService.searchTracksByTitle(trackTitle);
    }


    @GetMapping("/{id}")
    public ResponseEntity<StreamingResponseBody> streamTrack(@PathVariable int id) {
        StreamingResponseBody responseBody = trackService.play(id);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("audo/mpeg"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"song.mp3\"")
                .body(responseBody);
    }



}