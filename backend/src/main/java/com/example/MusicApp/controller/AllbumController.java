package com.example.MusicApp.controller;

import com.example.MusicApp.dto.response.AlbumResponseDTO;
import com.example.MusicApp.service.AlbumService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/album")
public class AllbumController {
    private final AlbumService albumService;


//    @GetMapping("/get")
//    public List<AlbumResponseDTO> get10() {
//        List<AlbumResponseDTO> albumList =  albumService.get10();
//        return albumList;
//    }


    //get all album from database
    @GetMapping
    public List<AlbumResponseDTO> getAll() {
        return albumService.getAll();
    }


    //search by name
    @GetMapping("/search/{title}")
    public List<AlbumResponseDTO> getAlbumByTitle(@PathVariable String title) {
        List<AlbumResponseDTO> albumList =  albumService.getAlbumByTitle(title);
        return albumList;
    }



}
