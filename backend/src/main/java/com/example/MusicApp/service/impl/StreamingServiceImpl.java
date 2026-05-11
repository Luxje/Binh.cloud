package com.example.MusicApp.service.impl;

import com.example.MusicApp.model.Track;
import com.example.MusicApp.repository.TrackRepository;
import com.example.MusicApp.service.StreamingService;
import javazoom.jl.player.Player;
import javazoom.jl.player.advanced.AdvancedPlayer;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;


@Service
@RequiredArgsConstructor
public class StreamingServiceImpl implements StreamingService {

    private final TrackRepository trackRepository;
    private volatile Thread playerThread;
    private volatile AdvancedPlayer player;


    public void playTrack(int id) {
        stopTrack();

        Track currentTrack = trackRepository.findById(id).orElse(null);
        String filePath = currentTrack.getAudioFileURL();

        playerThread = new Thread(() -> {
            try (FileInputStream fis = new FileInputStream(filePath)) {
                player = new AdvancedPlayer(fis);
                player.play();
            }catch (Exception e){
                e.printStackTrace();
            }
        });
        playerThread.start();

    }

    public void stopTrack() {
        if (player != null) {
            player.close();
            player = null;
        }
        if (player != null && playerThread != null) {
            playerThread.interrupt();
            playerThread = null;
        }
    }

}
