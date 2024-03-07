package com.example.minimalistmusicplayer;

import java.io.Serializable;
import java.util.ArrayList;

public class PlaylistModel implements Serializable {

    ArrayList<AudioModel> list;
    String name;
    String description;

    public PlaylistModel(ArrayList<AudioModel> list, String name, String description){
        this.list = list;
        this.name = name;
        this.description = description;
    }

    public ArrayList<AudioModel> getList() {
        return list;
    }

    public void setList(ArrayList<AudioModel> list) {
        this.list = list;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
