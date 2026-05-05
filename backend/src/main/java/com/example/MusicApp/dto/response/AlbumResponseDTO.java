package com.example.MusicApp.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class AlbumResponseDTO {
    @JsonProperty("album_genre")
    private String albumGenre;

    private String id;
    private String title;

    @JsonProperty("image_path")
    private String imagePath;

    @JsonProperty("release_date")
    private String releaseDate;

    private ArtistResponseDTO artist;
}
