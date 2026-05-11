package com.example.MusicApp.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data

public class LoginRequestDTO {
    @JsonProperty("user_email")
    private String userEmail;



    @JsonProperty("user_password")
    private String userPassword;
}
