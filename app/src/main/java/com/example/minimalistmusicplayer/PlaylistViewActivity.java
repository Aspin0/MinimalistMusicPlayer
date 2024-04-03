package com.example.minimalistmusicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PlaylistViewActivity extends AppCompatActivity {
    PlaylistModel playlistData;

    TextView titleText, descriptionText, noSongsText;
    Button playButton;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_view);

        titleText = findViewById(R.id.tv_playlist_name);
        descriptionText = findViewById(R.id.tv_playlist_description);
        noSongsText = findViewById(R.id.tv_empty_playlist);
        playButton = findViewById(R.id.button_play);
        recyclerView = findViewById(R.id.rv_playlist_songs);


        playlistData = (PlaylistModel) getIntent().getSerializableExtra("PLAYLIST");

        setResourcesWithPlaylist();
    }

    void setResourcesWithPlaylist() {
        titleText.setText(playlistData.getName());
        descriptionText.setText(playlistData.getDescription());

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MusicListAdapter(playlistData.getList(), getApplicationContext()));

    }

    public void goBackToPlaylists(View view) {
        Intent i = new Intent(getApplicationContext(), PlaylistActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(i);
    }

    public void playPlaylist(View view) {
        MyMediaPlayer.getInstance().reset();
        MyMediaPlayer.currentIndex = 0;
        Intent intent = new Intent(getApplicationContext(), MusicPlayerActivity.class);
        MyMediaPlayer.songQueue = playlistData.getList();
        MyMediaPlayer.fromNavBar = false;
        MyMediaPlayer.shuffle = false;
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(intent);
    }
}