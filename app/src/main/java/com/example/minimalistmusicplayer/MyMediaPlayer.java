package com.example.minimalistmusicplayer;

import android.media.MediaPlayer;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MyMediaPlayer {
    static MediaPlayer instance;
    public static int currentIndex = -1;
    public static ArrayList<AudioModel> songsList; // List of all songs read from files
    public static ArrayList<AudioModel> songQueue;

    public static ArrayList<AudioModel> beforeShuffle;
    public static boolean fromNavBar;

    public static boolean shuffle = false;

    public static MediaPlayer getInstance(){
        if(instance == null){
            instance = new MediaPlayer();
        }
        return instance;
    }
}
