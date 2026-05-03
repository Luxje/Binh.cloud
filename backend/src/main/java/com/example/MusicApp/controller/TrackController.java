package com.example.MusicApp.controller;

import com.example.MusicApp.dto.response.TrackListResponseDTO;
import com.example.MusicApp.model.Track;
import com.example.MusicApp.service.SpotifyService;
import com.example.MusicApp.service.TrackService;
import com.example.MusicApp.dto.response.TrackResponseDTO;
import com.example.MusicApp.service.impl.TrackServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/track")
public class TrackController {

    private final TrackServiceImpl trackService;
    private final SpotifyService spotifyService;

//    @GetMapping("/{id}")
//    public Mono<TrackResponseDTO> getTrack(@PathVariable String id, @RequestHeader("Authorization") String token) {
//        return spotifyService.getTrackById(id, token);
//
//    }

    @GetMapping("/{TrackTitle}")
    public List<TrackResponseDTO> getTrackByTitle(@PathVariable String TrackTitle) {
        return trackService.getTrack(TrackTitle);
    }



}