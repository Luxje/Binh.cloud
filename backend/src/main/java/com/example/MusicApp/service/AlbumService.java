package com.example.MusicApp.service;

import com.example.MusicApp.dto.response.AlbumResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AlbumService {
    public List<AlbumResponseDTO> getAll();

    public List<AlbumResponseDTO> get10();

    public List<AlbumResponseDTO> getAlbumByTitle(String title);




}
