package com.example.MusicApp.util;

import com.example.MusicApp.model.Track;
import com.example.MusicApp.repository.TrackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@RequiredArgsConstructor
public class imageGetter {
    private final TrackRepository trackRepository;

//    public ResponseEntity<File> getTrackImage(int trackId) {
//        Track track  = trackRepository.getReferenceById(trackId);
//        String path = track.getImagePath();
//        File file = new File(path);
//
//
//    }
}
