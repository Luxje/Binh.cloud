package com.example.MusicApp.controller;

import com.example.MusicApp.dto.response.TrackResponseDTO;
import com.example.MusicApp.model.Track;
import com.example.MusicApp.service.TrackService;
import com.example.MusicApp.util.imageGetter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/track")
@CrossOrigin(origins = {"https://localhost:5173"}, allowCredentials = "true")
public class TrackController {

    private final TrackService trackService;

    @GetMapping
    public List<TrackResponseDTO> getTracks() {
        return trackService.getAll(0,50);
    }

    @GetMapping("/search/{trackTitle}")
    public List<TrackResponseDTO> getTrackByTitle(@PathVariable String trackTitle) {
        return trackService.searchTracksByTitle(trackTitle);
    }


    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadTrack(@RequestParam("trackFile") MultipartFile trackFile,
                                              @RequestParam("trackCoverFile") MultipartFile trackCoverFile,
                                              @ModelAttribute Track track) {
        trackService.uploadTrack(trackFile, trackCoverFile, track);
        return ResponseEntity.ok().build();
    }

    // pass stream audio by chunk when trigger by id of the track/song
    @GetMapping(value = "/{id}/stream", produces = "audio/mpeg")
    public ResponseEntity<ResourceRegion> streamAudio(@PathVariable("id") int trackId,
                                                      @RequestHeader HttpHeaders header) throws IOException {
        return trackService.streamByChunk(trackId, header);
    }



}
