package com.example.MusicApp.dto.response;

import com.example.MusicApp.dto.response.common.ImageResponseDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import se.michaelthelin.spotify.model_objects.miscellaneous.Restrictions;

import java.util.List;

@Data
public class AlbumResponseDTO {
    @JsonProperty("album_type")
    private String albumType;

    @JsonProperty("total_tracks")
    private Integer totalTracks;

    @JsonProperty("available_markets")
    private List<String> availableMarkets;

    private String href;
    private String id;
    private List<ImageResponseDTO> images;
    private String name;

    @JsonProperty("release_date")
    private String releaseDate;

    @JsonProperty("release_date_precision")
    private String releaseDatePrecision;

    private Restrictions restrictions;
    private String type;
    private String uri;
    private List<ArtistResponseDTO> artists;
}
