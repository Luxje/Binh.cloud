package com.example.MusicApp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Artist")
public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer artistId;

    @Column(name = "Name", columnDefinition = "NVARCHAR(255)")
    private String name;

    @Column(name = "Email", columnDefinition = "NVARCHAR(255)")
    private String email;

    @Column(name = "Genre", columnDefinition = "NVARCHAR(255)")
    private String genre;

    @OneToOne
    @JoinColumn(name = "UserID")
    private Users user;
}
