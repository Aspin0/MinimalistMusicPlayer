package com.example.minimalistmusicplayer;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PlaylistCreationListAdapter extends RecyclerView.Adapter<PlaylistCreationListAdapter.ViewHolder> {

    ArrayList<AudioModel> songsList;
    private static ArrayList<AudioModel> playlist;
    Context context;
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;
        ImageView iconView;
        boolean checked;


        public ViewHolder(View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.tv_creation_title_text);
            iconView = itemView.findViewById(R.id.creation_icon_view);
            checked = false;
        }
    }

    public PlaylistCreationListAdapter(ArrayList<AudioModel> songsList, Context applicationContext) {
        this.songsList = songsList;
        this.context = applicationContext;
        playlist = new ArrayList<AudioModel>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item2, parent, false);
        return new PlaylistCreationListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AudioModel songData = songsList.get(position);
        holder.titleTextView.setText(songData.getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.checked == false) {
                    holder.iconView.setImageResource(R.drawable.baseline_check_box_24);
                    holder.checked = true;
                    // add to playlist
                    playlist.add(songData);
                } else {
                    holder.iconView.setImageResource(R.drawable.baseline_check_box_outline_blank_24);
                    holder.checked = false;
                    // remove from playlist
                    playlist.remove(songData);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return songsList.size();
    }

    public static ArrayList<AudioModel> getPlaylist() {
        return playlist;
    }

}
