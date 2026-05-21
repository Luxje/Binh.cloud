package com.example.MusicApp.service.impl;

import com.example.MusicApp.dto.response.TrackResponseDTO;
import com.example.MusicApp.mapper.TrackMapper;
import com.example.MusicApp.model.Track;
import com.example.MusicApp.repository.TrackRepository;
import com.example.MusicApp.service.TrackService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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
            //Hard code, track and image dir path (need some relative path in future)
            Path trackDir = Paths.get("/mnt/userFiles/JavaFiles/Filenhac");
            Path imageDir = Paths.get("/mnt/userFiles/JavaFiles/TrackCover");

            //Check if the directory exists or not, if not create a new one
            if (!Files.exists(trackDir)) Files.createDirectories(trackDir);
            if (!Files.exists(imageDir)) Files.createDirectories(imageDir);

            //Clean path for security purpose (prevent overwrite system file attack)
            String trackFilePath = StringUtils.cleanPath(Objects.requireNonNull(trackFile.getOriginalFilename()));
            String imageFilePath = StringUtils.cleanPath(Objects.requireNonNull(imageFile.getOriginalFilename()));

            //Combine Folder and file path
            Path trackPath = trackDir.resolve(trackFilePath);
            Path imagePath = imageDir.resolve(imageFilePath);

            //Save track and image file to local drive
            Files.copy(trackFile.getInputStream(), trackPath, StandardCopyOption.REPLACE_EXISTING);
            Files.copy(imageFile.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);


            //Set track and image path for the current file
            track.setReleaseDate(LocalDate.now());
            track.setAudioFileURL(trackPath.toString());
            track.setImagePath(imagePath.toString());
            //Save to database
            trackRepository.save(track);
        return true;

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
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
