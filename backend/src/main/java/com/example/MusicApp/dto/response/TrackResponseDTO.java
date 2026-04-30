package com.example.MusicApp.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import se.michaelthelin.spotify.model_objects.miscellaneous.Restrictions;

import java.util.List;

@Data
public class TrackResponseDTO {
    private AlbumResponseDTO album;
    private List<ArtistResponseDTO> artists;

    @JsonProperty("available_markets")
    private List<String> availableMarkets;

    @JsonProperty("disc_number")
    private Integer discNumber;

    @JsonProperty("duration_ms")
    private Long durationMs;

    private Boolean explicit;

    private String href;
    private String id;

    @JsonProperty("is_playable")
    private Boolean isPlayable;

    @JsonProperty("linked_from")
    private Object linkedFrom; // Typically an empty object or a simplified track reference

    private Restrictions restrictions;
    private String name;
    private Integer popularity;

    @JsonProperty("preview_url")
    private String previewUrl;

    @JsonProperty("track_number")
    private Integer trackNumber;

    private String type;
    private String uri;

    @JsonProperty("is_local")
    private Boolean isLocal;

}
