package com.example.MusicApp.service.impl;

import com.example.MusicApp.service.AuthService;
import org.apache.hc.core5.http.ContentType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import tools.jackson.databind.ObjectMapper;

@Service
public class AuthServiceImpl implements AuthService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;


    public AuthServiceImpl(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        this.webClient = webClientBuilder.baseUrl("https://accounts.spotify.com/api/token").build();
        this.objectMapper = objectMapper;
    }

//
//    public String token(String clientId, String clientSecret) {
//        return this.webClient.post()
//                .uri("")
//                .header(ContentType.APPLICATION_FORM_URLENCODED)
//
//
//    }
//

}
