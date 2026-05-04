package com.example.MusicApp.controller;

import com.example.MusicApp.dto.response.TrackResponseDTO;
import com.example.MusicApp.service.impl.TrackServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/track")
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





}