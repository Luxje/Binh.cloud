package com.example.MusicApp.repository;

import com.example.MusicApp.model.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TrackRepository extends JpaRepository<Track, Integer> {

    public List<Track> findTracksByTitleContaining(String title);

    void removeTrackByTrackId(Integer trackId);

    public Track findTrackByTrackId(Integer id);
}
