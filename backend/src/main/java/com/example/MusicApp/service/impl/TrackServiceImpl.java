package com.example.MusicApp.service.impl;

import com.example.MusicApp.dto.response.TrackResponseDTO;
import com.example.MusicApp.mapper.TrackMapper;
import com.example.MusicApp.model.Track;
import com.example.MusicApp.repository.TrackRepository;
import com.example.MusicApp.service.TrackService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public boolean removeTrack(Track track) {
        trackRepository.delete(track);
        return true;
    }

    public List<TrackResponseDTO> searchTracksByTitle(String TrackTitle) {
        List<Track> tracks = trackRepository.findByTitleIgnoreCase(TrackTitle);
        return trackMapper.toDTO(tracks);
    }
}
