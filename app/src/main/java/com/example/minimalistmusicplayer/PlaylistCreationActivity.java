package com.example.minimalistmusicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PlaylistCreationActivity extends AppCompatActivity {

    EditText etName, etDescription;
    RecyclerView rvSongsList;
    ArrayList<AudioModel> songsForPl;
    PlaylistModel newPlaylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_creation);

        etName = findViewById(R.id.edit_text_name);
        etDescription = findViewById(R.id.edit_text_description);
        rvSongsList = findViewById(R.id.rv_playlist_creation);

        rvSongsList.setLayoutManager(new LinearLayoutManager(this));
        rvSongsList.setAdapter(new PlaylistCreationListAdapter(MyMediaPlayer.songsList, getApplicationContext()));

        songsForPl = new ArrayList<AudioModel>();

    }

    public void createPlaylist(View view) {
        songsForPl = PlaylistCreationListAdapter.getPlaylist();
        newPlaylist = new PlaylistModel(songsForPl, String.valueOf(etName.getText()), String.valueOf(etDescription.getText()));
        PlaylistActivity.addPlaylist(newPlaylist);
        Intent i = new Intent(getApplicationContext(), PlaylistActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(i);
    }
}