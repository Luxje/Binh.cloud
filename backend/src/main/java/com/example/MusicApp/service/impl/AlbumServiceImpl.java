package com.example.MusicApp.service.impl;

import com.example.MusicApp.dto.response.AlbumResponseDTO;
import com.example.MusicApp.mapper.AlbumMapper;
import com.example.MusicApp.model.Album;
import com.example.MusicApp.repository.AlbumRepository;
import com.example.MusicApp.service.AlbumService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AlbumServiceImpl implements AlbumService {
    private final AlbumRepository albumRepository;
    private final AlbumMapper albumMapper;

    //get all albums exist in database
    public List<AlbumResponseDTO> getAll() {
        List<Album> albums = albumRepository.findAll();
        return albumMapper.toDTO(albums);
    }

    //get 10 album
    public List<AlbumResponseDTO> get10() {
        List<Album> albums = albumRepository.findTop10Albums();
        return albumMapper.toDTO(albums);
    }

    //search album by title
    public List<AlbumResponseDTO> getAlbumByTitle(String title) {
        List<Album> albums = albumRepository.findAlbumsByTitleContaining(title);
        return albumMapper.toDTO(albums);
    }

}
