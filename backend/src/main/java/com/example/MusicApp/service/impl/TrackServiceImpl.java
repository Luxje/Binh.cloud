package com.example.MusicApp.service.impl;

import com.example.MusicApp.dto.response.TrackResponseDTO;
import com.example.MusicApp.mapper.TrackMapper;
import com.example.MusicApp.model.Track;
import com.example.MusicApp.repository.TrackRepository;
import com.example.MusicApp.service.TrackService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrackServiceImpl implements TrackService {
    private final TrackRepository trackRepository;
    private final TrackMapper trackMapper;

    public List<TrackResponseDTO> getAll() {
        List<Track> tracks = trackRepository.findAll();
        return trackMapper.toDTO(tracks);
    }


    @Transactional
    public boolean uploadTrack(MultipartFile trackFile, MultipartFile imageFile , Track track) {
        try {
            //Track and image dir path
            Path trackDir = Paths.get("/mnt/userFiles/JavaFiles/Filenhac");
            Path imageDir = Paths.get("/mnt/userFiles/JavaFiles/TrackCover");

            if (!Files.exists(trackDir)) Files.createDirectories(trackDir);
            if (!Files.exists(imageDir)) Files.createDirectories(imageDir);




        }catch (Exception e){
            e.printStackTrace();
            return false;
        }



        return true;
    }

    public boolean removeTrack(int id) {
        trackRepository.removeTrackByTrackId(id);
        return true;
    }

    public List<TrackResponseDTO> searchTracksByTitle(String TrackTitle) {
        List<Track> tracks = trackRepository.findTracksByTitleContaining(TrackTitle);
        return trackMapper.toDTO(tracks);
    }


    public Resource getAudioFile(int id) {
        Track track = trackRepository.findTrackByTrackId(id);
        File file = new File(track.getAudioFileURL());

        if (!file.exists()) {
            throw new RuntimeException("File not found");
        }

        return new FileSystemResource(file);
    }

    public StreamingResponseBody play(int id) {
        Track currentTrack = trackRepository.findTrackByTrackId(id);
        return outputStream -> {
            try (InputStream is = new ClassPathResource(currentTrack.getAudioFileURL()).getInputStream()) {
                StreamUtils.copy(is, outputStream);
            } catch (IOException e) {
                throw new RuntimeException("Failed to play track", e);
            }
        };
    }



}
