package com.example.MusicApp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Album")
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int albumId;

    @Column(name = "Title", columnDefinition = "NVARCHAR(MAX)")
    private String title;

    @Column(name = "Genre", columnDefinition = "NVARCHAR(255)")
    private String genre;

    @Column(name = "ReleaseDate")
    private Date releaseDate;

    @Column(name = "ImagePath", columnDefinition = "NVARCHAR(MAX)")
    private String imagePath;

    @ManyToOne
    @JoinColumn(name = "ArtistID")
    private Artist artist;
}
