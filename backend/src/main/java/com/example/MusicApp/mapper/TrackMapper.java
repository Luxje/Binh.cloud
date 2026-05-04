package com.example.MusicApp.mapper;

import com.example.MusicApp.dto.response.TrackResponseDTO;
import com.example.MusicApp.model.Track;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TrackMapper {


    TrackResponseDTO toDTO(Track track);

    //Base on the single Track return method(need the single toDTO method to work properly)
    List<TrackResponseDTO> toDTO(List<Track> tracks);

    Track toEntity(TrackResponseDTO trackResponseDTO);
}


