package com.example.MusicApp.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.core.io.Resource;

import java.time.LocalDate;
import java.util.Date;

@Data
public class TrackResponseDTO {

    private Integer trackId;

    private String title;

    private String artistName;

    // Matches the @ManyToOne relationship in entity
    private AlbumResponseDTO album;

    @JsonProperty("duration_seconds")
    private Double durationSeconds;

    @JsonProperty("release_date")
    private LocalDate releaseDate;

    @JsonProperty("audio_file_url")
    private String audioFileURL;

    @JsonProperty("image_path")
    private String imagePath;
}