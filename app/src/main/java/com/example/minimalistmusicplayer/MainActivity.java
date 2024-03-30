package com.example.minimalistmusicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView noSongsText;
    ArrayList<AudioModel> songsList = new ArrayList<>();

    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.rv_songs);
        noSongsText = findViewById(R.id.tv_no_songs);

        bottomNav = findViewById(R.id.nav_bar);
        bottomNav.setOnItemSelectedListener(navListener);
        bottomNav.setSelectedItemId(R.id.songs_menu);

        if(!checkPermission()){
            requestPermission();
            return;
        }

        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION
                // can do artist and album here so look into that
        };

        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection,selection,null, null);
        while(true) {
            assert cursor != null;
            if (!cursor.moveToNext()) break;
            AudioModel songData = new AudioModel(cursor.getString(1), cursor.getString(0), cursor.getString(2));
            // checks if song file exists or not in case it has been deleted
            if(new File(songData.getPath()).exists()) {
                songsList.add(songData);
            }
        }

        if(songsList.size() == 0){
            noSongsText.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new MusicListAdapter(songsList, getApplicationContext()));
        }
    }


    private final NavigationBarView.OnItemSelectedListener navListener = item -> {
        int itemId = item.getItemId();
        if (itemId == R.id.playback_menu){
            if (MyMediaPlayer.currentIndex == -1) {
                Toast.makeText(MainActivity.this, "no music is currently playing", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(getApplicationContext(), MusicPlayerActivity.class);
                MyMediaPlayer.fromNavBar = true;
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
            }
        } else if (itemId == R.id.playlists_menu){
            Intent i = new Intent(getApplicationContext(), PlaylistActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            MyMediaPlayer.songsList = songsList;
            getApplicationContext().startActivity(i);
        }
        return true;
    };

    boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    void requestPermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)){
            Toast.makeText(MainActivity.this, "Permission is required to access songs. Please allow from settings", Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
        }
    }

}