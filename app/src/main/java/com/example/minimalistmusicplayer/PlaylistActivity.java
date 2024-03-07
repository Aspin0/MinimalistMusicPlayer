package com.example.minimalistmusicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class PlaylistActivity extends AppCompatActivity {
    BottomNavigationView bottomNav;
    RecyclerView recyclerView;

    ArrayList<PlaylistModel> playlists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        recyclerView = findViewById(R.id.rv_playlists);

        bottomNav = findViewById(R.id.nav_bar_playlist);
        bottomNav.setOnItemSelectedListener(navListener);
        bottomNav.setSelectedItemId(R.id.playlists_menu);

        PlaylistModel tempPlaylist = new PlaylistModel(MyMediaPlayer.songQueue, "Test Playlist", "Test Description");
        playlists.add(tempPlaylist);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new PlaylistListAdapter(playlists, getApplicationContext()));

    }

    private final NavigationBarView.OnItemSelectedListener navListener = item -> {
        int itemId = item.getItemId();
        if (itemId == R.id.playback_menu){
            if (MyMediaPlayer.currentIndex == -1) {
                Toast.makeText(PlaylistActivity.this, "no music is currently playing", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(getApplicationContext(), MusicPlayerActivity.class);
                MyMediaPlayer.fromNavBar = true;
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
            }
        } else if (itemId == R.id.songs_menu){
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(i);
        }
        return true;
    };
}