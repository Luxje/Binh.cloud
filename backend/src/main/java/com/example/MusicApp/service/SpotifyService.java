package com.example.MusicApp.service;


import com.example.MusicApp.dto.response.TrackResponseDTO;
import reactor.core.publisher.Mono;

import java.util.List;

public interface SpotifyService {
    Mono<TrackResponseDTO>  getTrackById(String trackId, String accessToken);

}
