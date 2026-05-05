package com.example.MusicApp.mapper;

import com.example.MusicApp.dto.response.AlbumResponseDTO;
import com.example.MusicApp.model.Album;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AlbumMapper {

    @Mapping(source = "genre", target = "albumGenre") // Replace 'genre' with the actual field name in Album.java
    @Mapping(source = "releaseDate", target = "releaseDate") // Replace 'publishedDate' with the actual name
    @Mapping(source = "albumId", target = "id")
    @Mapping(source = "imagePath", target = "imagePath")

    AlbumResponseDTO toDTO(Album album);

    List<AlbumResponseDTO> toDTO(List<Album> albums);

    @InheritInverseConfiguration
    Album toEntity(AlbumResponseDTO albumResponseDTO);
}
