package com.example.minimalistmusicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class QueueActivity extends AppCompatActivity {

    RecyclerView nowPlaying, upNext;
    ArrayList<AudioModel> queue = new ArrayList<>();
    ArrayList<AudioModel> npSong = new ArrayList<>();
    ArrayList<AudioModel> unSongs = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue);
        nowPlaying = findViewById(R.id.rv_now_playing);
        upNext = findViewById(R.id.rv_up_next);

        queue = MyMediaPlayer.songQueue;
        npSong.add(queue.get(MyMediaPlayer.currentIndex));
        for (int i = MyMediaPlayer.currentIndex + 1; i < queue.size(); i++){
            unSongs.add(queue.get(i));
        }

        nowPlaying.setLayoutManager(new LinearLayoutManager(this));
        nowPlaying.setAdapter(new MusicListAdapter(npSong, getApplicationContext()));

        upNext.setLayoutManager(new LinearLayoutManager(this));
        upNext.setAdapter(new MusicListAdapter(unSongs, getApplicationContext()));
    }

    public void goBackToPlayer(View view) {
        Intent i = new Intent(getApplicationContext(), MusicPlayerActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        MyMediaPlayer.fromNavBar = true;
        getApplicationContext().startActivity(i);
    }
}