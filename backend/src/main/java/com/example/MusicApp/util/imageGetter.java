package com.example.MusicApp.util;

import com.example.MusicApp.model.Track;
import com.example.MusicApp.repository.TrackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;

@Service
@RequiredArgsConstructor
public class imageGetter {
    private final TrackRepository trackRepository;

    public ResponseEntity<Resource> getTrackCover(int trackId) {
            Track track = trackRepository.getReferenceById(trackId);
        try {
            //get locate of the image cover file
            File trackCoverFile = new File(track.getImagePath());

            //Check if the file exists or not
            if (!trackCoverFile.exists()) {
                return ResponseEntity.notFound().build();
            }
            //Wrap the trackCoverFile into Spring Resource
            Resource resource = new FileSystemResource(trackCoverFile);

            //Get the contentType of the file for the browser
            String contentType = Files.probeContentType(trackCoverFile.toPath());
            //Check if the contentType null or not
            if (contentType == null) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);

        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

}
