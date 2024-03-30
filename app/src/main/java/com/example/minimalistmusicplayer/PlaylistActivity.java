package com.example.minimalistmusicplayer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class PlaylistActivity extends AppCompatActivity {
    BottomNavigationView bottomNav;
    RecyclerView recyclerView;
    Button createButton;

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    static ArrayList<PlaylistModel> playlists = new ArrayList<PlaylistModel>();
    //static ArrayList<PlaylistModel> playlists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        PlaylistActivity.context = getApplicationContext();

        recyclerView = findViewById(R.id.rv_playlists);
        createButton = findViewById(R.id.button_create);
        createButton.setOnClickListener(v -> goToActivity());

        bottomNav = findViewById(R.id.nav_bar_playlist);
        bottomNav.setOnItemSelectedListener(navListener);
        bottomNav.setSelectedItemId(R.id.playlists_menu);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new PlaylistListAdapter((ArrayList<PlaylistModel>) PrefConfig.readListFromPref(this), getApplicationContext()));

        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);
    }

    private void goToActivity(){
        Intent intent = new Intent(getApplicationContext(), PlaylistCreationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(intent);
    }

    public static Context getAppContext() {
        return PlaylistActivity.context;
    }

    public static void addPlaylist(PlaylistModel playlist){
        playlists = (ArrayList<PlaylistModel>) PrefConfig.readListFromPref(getAppContext());
        playlists.add(playlist);
        PrefConfig.writeListInPref(getAppContext(), playlists);
    }

    ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            Toast.makeText(PlaylistActivity.this, "Playlist Deleted", Toast.LENGTH_SHORT).show();
            playlists = (ArrayList<PlaylistModel>) PrefConfig.readListFromPref(getAppContext());
            playlists.remove(viewHolder.getAdapterPosition());
            PrefConfig.writeListInPref(getAppContext(), playlists);
            recyclerView.setAdapter(new PlaylistListAdapter(PrefConfig.readListFromPref(getAppContext()), getApplicationContext()));
        }
    };

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