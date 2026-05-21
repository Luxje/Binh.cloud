package com.example.MusicApp.controller;

import com.example.MusicApp.dto.response.TrackResponseDTO;
import com.example.MusicApp.model.Track;
import com.example.MusicApp.service.TrackService;
import lombok.RequiredArgsConstructor;
import org.apache.hc.core5.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/track")
@CrossOrigin(origins = {"https://localhost:5173"}, allowCredentials = "true")
public class TrackController {

    private final TrackService trackService;

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

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadTrack(@RequestParam("trackFile") MultipartFile trackFile, @RequestParam("trackCoverFile") MultipartFile trackCoverFile , @ModelAttribute Track track) {
        trackService.uploadTrack(trackFile, trackCoverFile, track);
        return ResponseEntity.ok().build();

    }



}
