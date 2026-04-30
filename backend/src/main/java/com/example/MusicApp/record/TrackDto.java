package com.example.MusicApp.record;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TrackDto(
        String id,
        String name,
        String href,
        String uri,
        String type,

        @JsonProperty("is_local")
        boolean isLocal,

        @JsonProperty("is_playable")
        boolean isPlayable,

        @JsonProperty("track_number")
        int trackNumber
) {}
