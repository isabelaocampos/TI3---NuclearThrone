package com.example.nuclearthrone.model.menus;

import java.io.File;
import java.util.HashMap;

import com.example.nuclearthrone.MainMenu;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Soundtrack {

    public static HashMap<String, MediaPlayer> sounds;
    public static HashMap<String, Boolean> isPlaying;

    private Soundtrack() {
    }

    private static Soundtrack instance;

    MediaPlayer mediaPlayer;
    public void reproduceSound(String name){
        if(isPlaying.containsKey(name) && !isPlaying.get(name)){
            MediaPlayer mediaPlayer = sounds.get(name);
            mediaPlayer.play();
            isPlaying.put(name,true);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        }
    }

    public void reproduceSound(String name, boolean loop){
        if(isPlaying.containsKey(name) && !isPlaying.get(name)){
            MediaPlayer mediaPlayer = sounds.get(name);
            mediaPlayer.play();
            isPlaying.put(name,true);
            if(loop) {
                mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            }else{
                mediaPlayer.setOnEndOfMedia(()->{
                    sounds.get(name).stop();
                    isPlaying.put(name,false);
                });
            }
        }
    }

    public void setVolumneOf(String name,double volume){
        if(sounds.containsKey(name)){
            MediaPlayer mediaPlayer = sounds.get(name);
            mediaPlayer.setVolume(volume);
        }
    }

    public void stopSound(String name){
        if(isPlaying.containsKey(name)){
            MediaPlayer mediaPlayer = sounds.get(name);
            mediaPlayer.stop();
            isPlaying.put(name, false);
        }
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public static Soundtrack getInstance() {
        if (instance == null) {
            instance = new Soundtrack();

        }
        return instance;
    }


}
