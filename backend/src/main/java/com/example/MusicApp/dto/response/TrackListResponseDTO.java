package com.example.MusicApp.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

//If something happens to the tracks getting things this is the class that I have to look into a little bit
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TrackListResponseDTO {
    private List<TrackResponseDTO> tracks;

}
