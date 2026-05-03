package com.example.MusicApp.mapper;

import com.example.MusicApp.dto.response.TrackResponseDTO;
import com.example.MusicApp.model.Track;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TrackMapper {

    // Add this! It's the "engine" for the list mapper
    TrackResponseDTO toDTO(Track track);

    // This will now work because it uses the method above internally
    List<TrackResponseDTO> toDTO(List<Track> tracks);

    Track toEntity(TrackResponseDTO trackResponseDTO);
}


