package com.example.MusicApp.repository;

import com.example.MusicApp.model.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AlbumRepository extends JpaRepository<Album, Integer> {

    public List<Album> findAlbumsByTitleContaining (String title);

    @Query(value = "SELECT TOP 10 * FROM Album ORDER BY albumId DESC", nativeQuery = true)
    public List<Album> findTop10Albums();
}
