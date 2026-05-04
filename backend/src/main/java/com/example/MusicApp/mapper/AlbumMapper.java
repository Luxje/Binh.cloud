package com.example.MusicApp.mapper;

import com.example.MusicApp.dto.response.AlbumResponseDTO;
import com.example.MusicApp.model.Album;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AlbumMapper {

    AlbumResponseDTO toDTO(Album album);

    List<AlbumResponseDTO> toDTO(List<Album> albums);

    Album toEntity(AlbumResponseDTO albumResponseDTO);
}
