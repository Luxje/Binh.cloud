package com.example.MusicApp.repository;

import com.example.MusicApp.dto.response.TrackListResponseDTO;
import com.example.MusicApp.dto.response.TrackResponseDTO;
import com.example.MusicApp.model.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import reactor.core.publisher.Mono;

import java.util.List;

public interface TrackRepository extends JpaRepository<Track, Integer> {
    public List<Track> findByArtistName(String artistName);

    public Track findTrackByTrackId(int id);

    public List<Track> findTracksByTitleContaining(String title);

    public List<Track> findTracksByTitle(String title);

    public List<Track> findTracksByArtistName(String artistName);

}
