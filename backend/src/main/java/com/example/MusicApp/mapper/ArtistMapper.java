package com.example.MusicApp.mapper;

import com.example.MusicApp.dto.response.ArtistResponseDTO;
import com.example.MusicApp.model.Artist;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ArtistMapper {

    ArtistResponseDTO toDTO(Artist artist);

    List<ArtistResponseDTO> toDTO(List<Artist> artists);




}
