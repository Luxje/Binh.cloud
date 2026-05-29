package com.example.MusicApp.controller;

import com.example.MusicApp.util.imageGetter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/image")
@RequiredArgsConstructor
@CrossOrigin(origins = {"https://localhost:5173"}, allowCredentials = "true")
public class ImageController {
    private final imageGetter imageGetterService;

    @GetMapping("/{trackId}")
    public ResponseEntity<Resource> getTrackCover(@PathVariable int trackId) {
        return imageGetterService.getTrackCover(trackId);
    }
}
