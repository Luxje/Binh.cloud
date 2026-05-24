package com.example.MusicApp.util;

import com.example.MusicApp.model.Track;
import com.example.MusicApp.repository.AlbumRepository;
import com.example.MusicApp.repository.TrackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class getImage {
    private final TrackRepository trackRepository;
    private final AlbumRepository albumRepository;

}
