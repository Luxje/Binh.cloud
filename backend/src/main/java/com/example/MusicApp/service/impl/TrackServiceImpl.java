package com.example.MusicApp.service.impl;

import com.example.MusicApp.dto.response.TrackResponseDTO;
import com.example.MusicApp.mapper.TrackMapper;
import com.example.MusicApp.model.Track;
import com.example.MusicApp.repository.TrackRepository;
import com.example.MusicApp.service.TrackService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

import java.io.File;
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

    public boolean addTrack(Track track) {
        trackRepository.save(track);
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




}
