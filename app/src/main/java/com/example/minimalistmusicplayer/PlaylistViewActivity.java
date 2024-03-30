package com.example.minimalistmusicplayer;

import android.os.Bundle;
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
    Button playButton, shuffleButton;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_view);

        titleText = findViewById(R.id.tv_playlist_name);
        descriptionText = findViewById(R.id.tv_playlist_description);
        noSongsText = findViewById(R.id.tv_empty_playlist);
        playButton = findViewById(R.id.button_play);
        shuffleButton = findViewById(R.id.button_shuffle);
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
}