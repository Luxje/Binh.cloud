package com.example.MusicApp.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrackRequestDTO {
    @NotBlank
    private String TrackId;
    @NotBlank
    private String RequestType;
    @NotBlank
    private String token;
}
