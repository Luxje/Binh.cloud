package com.example.MusicApp.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class AlbumResponseDTO {
    @JsonProperty("album_genre")
    private String albumType;

    private String id;
    private String title;

    @JsonProperty("release_date")
    private String releaseDate;

    private List<ArtistResponseDTO> artists;
}
