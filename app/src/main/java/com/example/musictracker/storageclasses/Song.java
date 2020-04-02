package com.example.musictracker.storageclasses;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class Song {
    private String songName;
    private int knowledgeLevel;
    private String[] tags;
    private int type;
    private String notes;
    private int index;

    public Song(String songName, int knowledgeLevel, String[] tags, int type, String notes, int index) {
        this.songName = songName;
        this.knowledgeLevel = knowledgeLevel;
        this.tags = tags;
        this.type = type;
        this.notes = notes;
        this.index = index;
    }

    @Override
    public String toString() {
        try {
            JSONObject ret_val = new JSONObject();
            ret_val.put("songName", songName);
            ret_val.put("knowledgeLevel", knowledgeLevel);
            ret_val.put("tags", Arrays.toString(tags));
            ret_val.put("type", type);
            ret_val.put("notes", notes);
            ret_val.put("index", index);
            return ret_val.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "FAIL";
        }
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public int getKnowledgeLevel() {
        return knowledgeLevel;
    }

    public void setKnowledgeLevel(int knowledgeLevel) {
        this.knowledgeLevel = knowledgeLevel;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
