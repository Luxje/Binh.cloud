package com.example.MusicApp.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.core.io.Resource;

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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date releaseDate;

    @JsonProperty("audio_file_url")
    private String audioFileURL;

    @JsonProperty("track_cover")
    private String trackCoverUrl;
}