package com.example.MusicApp.dto.response;

import com.example.MusicApp.dto.response.common.ImageResponseDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ArtistResponseDTO {

    private FollowersResponseDTO followers;

    private List<String> genres;
    private String href;
    private String id;
    private List<ImageResponseDTO> images;
    private String name;
    private Integer popularity;
    private String type;
    private String uri;
}