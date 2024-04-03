package com.example.minimalistmusicplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
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
    SearchView searchView;
    private MusicListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.rv_songs);
        noSongsText = findViewById(R.id.tv_no_songs);
        searchView = findViewById(R.id.search_view);
        searchView.clearFocus();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });

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

        listAdapter = new MusicListAdapter(songsList, getApplicationContext());

        if(songsList.size() == 0){
            noSongsText.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(listAdapter);
        }

        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);
    }

    private void filterList(String text) {
        ArrayList<AudioModel> filteredList = new ArrayList<>();
        for (AudioModel audioModel: songsList){
            if (audioModel.getTitle().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(audioModel);
            }
        }

        if (filteredList.isEmpty()){
            Toast.makeText(this, "No Songs Found", Toast.LENGTH_SHORT).show();
        } else {
            listAdapter.setFilteredList(filteredList);
        }
    }

    ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            Toast.makeText(MainActivity.this, "Song Queued", Toast.LENGTH_SHORT).show();
            MyMediaPlayer.songQueue.add(songsList.get(viewHolder.getAdapterPosition()));
            recyclerView.setAdapter(new MusicListAdapter(songsList, getApplicationContext()));
        }
    };


    private final NavigationBarView.OnItemSelectedListener navListener = item -> {
        int itemId = item.getItemId();
        if (itemId == R.id.playback_menu){
            if (MyMediaPlayer.currentIndex == -1) {
                Toast.makeText(MainActivity.this, "no music is currently playing", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(getApplicationContext(), MusicPlayerActivity.class);
                MyMediaPlayer.fromNavBar = true;
                MyMediaPlayer.songsList = songsList;
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