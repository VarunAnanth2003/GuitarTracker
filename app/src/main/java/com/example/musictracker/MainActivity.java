package com.example.musictracker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.JsonReader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.musictracker.adapters.SongListAdapter;
import com.example.musictracker.storageclasses.Song;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private ListView songListView;
    private JSONArray allSongs = new JSONArray();

    private String activeName = "";
    private String activeTag = "";
    private int activeLoc = -1;
    private int activeType = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(myToolbar);
        refreshInfo();
        final SharedPreferences stored_data = getSharedPreferences("USER_PREFERENCES", MODE_PRIVATE);
        songListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int pos, long l) {
                AlertDialog deleteDialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Are you sure you want to delete this item?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                try {
                                    SharedPreferences.Editor stored_data_editor = stored_data.edit();
                                    JSONObject currentObject = (JSONObject) allSongs.get(pos);
                                    stored_data_editor.putString("Index: " + currentObject.get("index"), "");
                                    stored_data_editor.apply();
                                    allSongs.remove(pos);
                                    refreshInfo();
                                } catch (JSONException e) {Log.d("EEE", "failed");}
                            }
                        })
                        .setNegativeButton("Keep", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) { }
                        })
                        .create();
                deleteDialog.show();
                return true;
            }
        });
        songListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int pos, long l) {
                JSONObject songJSON;
                Song currentSong;
                try {
                    songJSON = (JSONObject) allSongs.get(pos);
                    String songName = songJSON.getString("songName");
                    int knowledgeLevel = songJSON.getInt("knowledgeLevel");
                    String[] tags = songJSON.getString("tags").substring(1,songJSON.getString("tags").length()-1).split(",");
                    int type = songJSON.getInt("type");
                    String notes = songJSON.getString("notes");
                    int index = songJSON.getInt("index");
                    currentSong = new Song(songName,knowledgeLevel,tags,type,notes,index);
                    AlertDialog infoDialog = new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Song Name: " + currentSong.getSongName())
                            .setMessage(Html.fromHtml("<b>Tags: </b>" + Arrays.toString(currentSong.getTags()).substring(1, Arrays.toString(currentSong.getTags()).length()-1) + "<br><br><b>Notes: </b>" + currentSong.getNotes()))
                            .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    try {
                                        SharedPreferences.Editor stored_data_editor = stored_data.edit();
                                        JSONObject currentObject = (JSONObject) allSongs.get(pos);
                                        stored_data_editor.putString("Index: " + currentObject.get("index"), "");
                                        stored_data_editor.apply();
                                        String editableExtract = ((JSONObject) allSongs.get(pos)).toString();
                                        allSongs.remove(pos);
                                        refreshInfo();
                                        openEdit(editableExtract);
                                    } catch (JSONException e) {Log.d("EEE", "failed");}
                                }
                            })
                            .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) { }
                            })
                            .create();
                    infoDialog.show();
                } catch (JSONException e) {}
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshInfo();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_help:
                Intent i = new Intent(this, HelpScreen.class);
                startActivity(i);
                return true;
            case R.id.menu_search:
                Intent j = new Intent(this, FilterScreen.class);
                j.putExtra("activeName", activeName);
                j.putExtra("activeTag", activeTag);
                j.putExtra("activeLoc",activeLoc);
                j.putExtra("activeType", activeType);
                startActivity(j);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
    private void refreshInfo() {
        Intent prevIntent = getIntent();
        String activeFilters = "<b><u>Active Filters:</u></b><br>";
        if(prevIntent.hasExtra("activeName") && !prevIntent.getStringExtra("activeName").equals("")) {
            activeName = prevIntent.getStringExtra("activeName");
            activeFilters += "<b>Name contains: </b>" + activeName + "<br>";
        }
        if(prevIntent.hasExtra("activeTag") && !prevIntent.getStringExtra("activeTag").equals("")) {
            activeTag = prevIntent.getStringExtra("activeTag");
            activeFilters += "<b>Has tag: </b>" + activeTag + "<br>";
        }
        if(prevIntent.hasExtra("activeLoc")) {
            activeLoc = prevIntent.getIntExtra("activeLoc", -1);
            switch(activeLoc) {
                case 0:
                    activeFilters += "<b>Progress: </b>" + "Just Started" + "<br>";
                    break;
                case 1:
                    activeFilters += "<b>Progress: </b>" + "Learning" + "<br>";
                    break;
                case 2:
                    activeFilters += "<b>Progress: </b>" + "Halfway There" + "<br>";
                    break;
                case 3:
                    activeFilters += "<b>Progress: </b>" + "Almost Completed" + "<br>";
                    break;
                case 4:
                    activeFilters += "<b>Progress: </b>" + "Mastered" + "<br>";
                    break;
                default:
                    break;
            }
        }
        if(prevIntent.hasExtra("activeType")) {
            activeType = prevIntent.getIntExtra("activeType", -1);
            switch(activeType) {
                case 0:
                    activeFilters += "<b>Type: </b>" + "Riff" + "<br>";
                    break;
                case 1:
                    activeFilters += "<b>Type: </b>" + "Solo" + "<br>";
                    break;
                case 2:
                    activeFilters += "<b>Type: </b>" + "Chords" + "<br>";
                    break;
                case 3:
                    activeFilters += "<b>Type: </b>" + "Section" + "<br>";
                    break;
                case 4:
                    activeFilters += "<b>Type: </b>" + "Other" + "<br>";
                    break;
                default:
                    break;
            }
        }
        TextView filterText = findViewById(R.id.main_activeFilters);
        filterText.setText(activeFilters.equals("<b><u>Active Filters:</u></b><br>") ? "No filters active" : Html.fromHtml(activeFilters));
        SharedPreferences stored_data = getSharedPreferences("USER_PREFERENCES", MODE_PRIVATE);
        allSongs = new JSONArray();

        /*SharedPreferences.Editor stored_data_editor = stored_data.edit();
        stored_data_editor.clear();
        stored_data_editor.apply();*/

        int numTags = stored_data.getInt("maxIndex", 0);
        for (int i = 0; i <= numTags; i++) {
            try {
                if(!stored_data.getString("Index: " + i, "").equals("")) {
                    if(checkParams(stored_data.getString("Index: " + i, ""))) {
                        allSongs.put(new JSONObject(stored_data.getString("Index: " + i, "")));
                    }
                }
            } catch (JSONException e) {Log.d("EEE", "Unjsonify failed at index " + i);}
        }
        songListView = findViewById(R.id.main_songListView);
        SongListAdapter songListAdapter = new SongListAdapter(this, allSongs);
        songListView.setAdapter(songListAdapter);
        songListAdapter.notifyDataSetChanged();
    }

    public void openAddSong(View view) {
        Intent i = new Intent(this, AddSong.class);
        startActivity(i);
    }

    private void openEdit(String s) {
        Intent i = new Intent(this, AddSong.class);
        i.putExtra("Song", s);
        startActivity(i);
    }

    private boolean checkParams(String s) {
        boolean ret_val = true;
        try {
            JSONObject songJSON = new JSONObject(s);
            Song currentSong = new Song(
                    songJSON.getString("songName"),
                    songJSON.getInt("knowledgeLevel"),
                    songJSON.getString("tags").substring(1, songJSON.getString("tags").length()-1).split(","),
                    songJSON.getInt("type"),
                    songJSON.getString("notes"),
                    songJSON.getInt("index")
            );
            if(!currentSong.getSongName().toLowerCase().contains(activeName.toLowerCase()) && !activeName.equals("")) ret_val = false;
            if(!activeTag.equals("")) {
                for (int i = 0; i < currentSong.getTags().length; i++) {
                    ret_val = false;
                    if (currentSong.getTags()[i].trim().equalsIgnoreCase(activeTag)) {
                        ret_val = true;
                        break;
                    }
                }
            }
            if(currentSong.getKnowledgeLevel() != activeLoc && activeLoc != -1) ret_val = false;
            if(currentSong.getType() != activeType && activeType != -1) ret_val = false;
        } catch (JSONException e) { Log.d("EEE", "failed"); return true;}
        return ret_val;
    }
}
