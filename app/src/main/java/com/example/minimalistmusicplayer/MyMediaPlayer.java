package com.example.minimalistmusicplayer;

import android.media.MediaPlayer;

import java.util.ArrayList;

public class MyMediaPlayer {
    static MediaPlayer instance;
    public static int currentIndex = -1;
    public static ArrayList<AudioModel> songQueue;
    public static boolean fromNavBar;

    public static MediaPlayer getInstance(){
        if(instance == null){
            instance = new MediaPlayer();
        }
        return instance;
    }
}
