package com.example.MusicApp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Track")
public class Track {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TrackID")
    private Integer trackId;

    @Column(name = "Title", columnDefinition = "NVARCHAR(MAX)")
    private String title;

    @Column(name = "ArtistName", columnDefinition = "NVARCHAR(255)")
    private String artistName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AlbumID")
    private Album album;
    // ------------------------------

    @Column(name = "DurationSeconds")
    private Double durationSeconds;

    @Column(name = "ReleaseDate")
    private LocalDate releaseDate;

    @Column(name = "AudioFileURL", columnDefinition = "NVARCHAR(MAX)")
    private String audioFileURL;

    @Column(name = "ImagePath", columnDefinition = "NVARCHAR(MAX)")
    private String imagePath;




}