package com.example.minimalistmusicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class MusicPlayerActivity extends AppCompatActivity {

    TextView titleTv,currentTimeTv,totalTimeTv;
    SeekBar seekBar;
    ImageView pausePlay,nextBtn,previousBtn,queueBtn,shuffleBtn;

    ArrayList<AudioModel> songsList;
    AudioModel currentSong;
    MediaPlayer mediaPlayer = MyMediaPlayer.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        titleTv = findViewById(R.id.song_title);
        currentTimeTv = findViewById(R.id.current_time);
        totalTimeTv = findViewById(R.id.total_time);
        seekBar = findViewById(R.id.seek_bar);
        pausePlay = findViewById(R.id.pause_play);
        nextBtn = findViewById(R.id.next);
        previousBtn = findViewById(R.id.previous);
        queueBtn = findViewById(R.id.queue_button);
        shuffleBtn = findViewById(R.id.shuffle_button);
        if (!MyMediaPlayer.shuffle) {
            shuffleBtn.setImageResource(R.drawable.baseline_shuffle_24);
        } else {
            shuffleBtn.setImageResource(R.drawable.baseline_shuffle_on_24);
        }

        titleTv.setSelected(true);

        songsList = MyMediaPlayer.songQueue;
        
        setResourcesWithMusic();


        // method called every 100 milliseconds to constantly update seek bar and time
        // also changes play/pause button icon


        MusicPlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer!=null){
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    currentTimeTv.setText(convertTime(Integer.toString(mediaPlayer.getCurrentPosition())));

                    if (mediaPlayer.isPlaying()) {
                        pausePlay.setImageResource(R.drawable.baseline_pause_24);
                    } else {
                        pausePlay.setImageResource(R.drawable.baseline_play_arrow_24);
                    }
                }
                new Handler().postDelayed(this, 100);
            }
        });



        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer!=null && fromUser){
                    mediaPlayer.seekTo(progress);
                }
            }

            // dont need to put anything in these but getting rid of them causes a syntax error
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void goToQueueActivity(View view) {
        Intent i = new Intent(getApplicationContext(), QueueActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(i);
    }

    void setResourcesWithMusic(){
        currentSong = songsList.get(MyMediaPlayer.currentIndex);

        titleTv.setText(currentSong.getTitle());
        totalTimeTv.setText(convertTime(currentSong.getDuration()));

        pausePlay.setOnClickListener(v-> pausePlay());
        nextBtn.setOnClickListener(v-> playNextSong());
        previousBtn.setOnClickListener(v-> playPreviousSong());


        if (MyMediaPlayer.fromNavBar == false){
            playMusic();
        } else {
            Integer i = mediaPlayer.getCurrentPosition();
            mediaPlayer.reset();
            playMusic();
            mediaPlayer.seekTo(i);
        }
    }

    public void shuffleQueue(View view) {
        if (!MyMediaPlayer.shuffle) {
            shuffleBtn.setImageResource(R.drawable.baseline_shuffle_on_24);
            MyMediaPlayer.shuffle = true;
            MyMediaPlayer.beforeShuffle = new ArrayList<>();

            for (int i = MyMediaPlayer.currentIndex + 1; i < MyMediaPlayer.songQueue.size(); i++){
                MyMediaPlayer.beforeShuffle.add(MyMediaPlayer.songQueue.get(i));
            }

            ArrayList<AudioModel> list = new ArrayList<>(MyMediaPlayer.songQueue);
            list.remove(MyMediaPlayer.currentIndex);
            Collections.shuffle(list);
            ArrayList<AudioModel> shuffleList = new ArrayList<>();
            shuffleList.add(songsList.get(MyMediaPlayer.currentIndex));
            list.remove(songsList.get(MyMediaPlayer.currentIndex));
            shuffleList.addAll(list);
            MyMediaPlayer.currentIndex = 0;
            MyMediaPlayer.songQueue = shuffleList;
            songsList = shuffleList;


        } else {
            shuffleBtn.setImageResource(R.drawable.baseline_shuffle_24);
            MyMediaPlayer.shuffle = false;

            MyMediaPlayer.songQueue.subList(MyMediaPlayer.currentIndex + 1, MyMediaPlayer.songQueue.size()).clear();
            MyMediaPlayer.songQueue.addAll(MyMediaPlayer.beforeShuffle);
            songsList = MyMediaPlayer.songQueue;
        }
    }

    private void playMusic() {
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(currentSong.getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            seekBar.setProgress(0);
            seekBar.setMax(mediaPlayer.getDuration());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void playNextSong(){
        if(MyMediaPlayer.currentIndex == songsList.size()-1){
            return;
        }
        MyMediaPlayer.currentIndex += 1;
        mediaPlayer.reset();
        MyMediaPlayer.fromNavBar = false;
        setResourcesWithMusic();
    }

    private void playPreviousSong(){
        if(MyMediaPlayer.currentIndex == 0){
            return;
        }
        MyMediaPlayer.currentIndex -= 1;
        mediaPlayer.reset();
        MyMediaPlayer.fromNavBar = false;
        setResourcesWithMusic();
    }

    private void pausePlay(){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        } else {
            mediaPlayer.start();
        }
    }

    public void goBackToSongs(View view) {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(i);
    }

    public static String convertTime(String duration){
        Long millis = Long.parseLong(duration);
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
    }
}