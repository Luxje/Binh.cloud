package com.example.MusicApp.service.impl;

import com.example.MusicApp.dto.response.TrackListResponseDTO;
import com.example.MusicApp.dto.response.TrackResponseDTO;
import com.example.MusicApp.service.SpotifyService;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Service
public class SpotifyServiceImpl implements SpotifyService {
    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public SpotifyServiceImpl(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        this.webClient = webClientBuilder.baseUrl("https://api.spotify.com/v1").build();
        this.objectMapper = objectMapper;
    }


    public Mono<TrackResponseDTO> getTrackById(String id, String accessToken) {
        return this.webClient.get()
                .uri("/tracks/{id}", id)
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .retrieve()
                .bodyToMono(TrackResponseDTO.class);


    }

    public List<TrackResponseDTO> parseTracks(String jsonResponseBody) {
        try {
            TrackListResponseDTO wrapper = objectMapper.readValue(
                    jsonResponseBody,
                    TrackListResponseDTO.class
            );

            return wrapper.getTracks();

        } catch (Exception e) {
            throw new RuntimeException("Failed to map Spotify tracks", e);
        }
    }
}
