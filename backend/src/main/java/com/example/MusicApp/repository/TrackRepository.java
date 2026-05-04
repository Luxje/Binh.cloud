package com.example.MusicApp.repository;

import com.example.MusicApp.model.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TrackRepository extends JpaRepository<Track, Integer> {

    public List<Track> findTracksByTitleContainingIgnoreCase(String title);


    @Query("SELECT tr FROM Track tr WHERE tr.artistName like :artistName")
    public List<Track> findTracksByArtistName(String artistName);

    @Query("SELECT tr FROM Track tr WHERE LOWER(tr.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<Track> findByTitleIgnoreCase(@Param("title") String title);
}
