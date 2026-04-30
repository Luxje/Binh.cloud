package com.example.MusicApp.controller;

import com.example.MusicApp.service.SpotifyService;
import com.example.MusicApp.service.TrackService;
import com.example.MusicApp.dto.response.TrackResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/track")
public class TrackController {

    private final TrackService trackService;
    private final SpotifyService spotifyService;

    @GetMapping("/{id}")
    public Mono<TrackResponseDTO> getTrack(@PathVariable String id, @RequestHeader("Authorization") String token) {
        return spotifyService.getTrackById(id, token);

    }

}