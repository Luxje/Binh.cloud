package com.example.MusicApp.service.impl;

import com.example.MusicApp.dto.response.TrackResponseDTO;
import com.example.MusicApp.mapper.TrackMapper;
import com.example.MusicApp.model.Track;
import com.example.MusicApp.repository.TrackRepository;
import com.example.MusicApp.service.TrackService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.*;
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


    public File getAudioFile(int id) {
        Track track = trackRepository.findTrackByTrackId(id);
        File file = new File(track.getAudioFileURL());

        if (!file.exists()) {
            throw new RuntimeException("File not found");
        }

        return file;
    }


    //stream by chunk of the file
   public ResponseEntity<ResourceRegion> streamByChunk(int id, HttpHeaders header) throws IOException {
        //prepare for metadata
        Track currentTrack = trackRepository.findTrackByTrackId(id);
        
        Resource audioResource = new FileSystemResource(this.getAudioFile(id));
        
        //define the chunk size 
        long chunkSize = 1024 * 1024;

        long contentLength = audioResource.contentLength();
        //get range from the HttpHeaders
        HttpRange range = header.getRange().stream().findFirst().orElse(null);

       ResourceRegion region;

       if (range != null) {
           //get the start and end range of the file
           long start = range.getRangeStart(contentLength);
           long end = range.getRangeEnd(contentLength);
           long rangeLength = Math.min(chunkSize, end - start + 1);

           region = new ResourceRegion(audioResource, start, rangeLength);
       }else {
           long rangeLength = Math.min(chunkSize, contentLength);
           region = new ResourceRegion(audioResource, 0, rangeLength);
       }
       return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
               .contentType(MediaType.parseMediaType("audio/mpeg"))
               .body(region);
   }



}
