package com.example.musictracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.musictracker.storageclasses.Song;

import org.json.JSONException;
import org.json.JSONObject;

public class AddSong extends AppCompatActivity {

    private EditText nameText;
    private Spinner locSpinner;
    private Spinner typeSpinner;
    private EditText tagsText;
    private EditText notesText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_song);
        nameText = findViewById(R.id.add_songNameEdit);
        locSpinner = findViewById(R.id.add_locSpinner);
        typeSpinner = findViewById(R.id.add_typeSpinner);
        tagsText = findViewById(R.id.add_tagEdit);
        notesText = findViewById(R.id.add_notesEdit);
        Intent activatorIntent = getIntent();
        if(activatorIntent.hasExtra("Song")) {
            populateFields(activatorIntent.getStringExtra("Song"));
        }
    }
    public void getData(View view) {
        SharedPreferences stored_data = getSharedPreferences("USER_PREFERENCES", MODE_PRIVATE);
        SharedPreferences.Editor stored_data_editor = stored_data.edit();
        String songName = nameText.getText().toString().trim();
        int loc = -1;
        switch(locSpinner.getSelectedItem().toString()) {
            case "Just Started":
                loc = 0;
                break;
            case "Learning":
                loc = 1;
                break;
            case "Halfway There":
                loc = 2;
                break;
            case "Almost Completed":
                loc = 3;
                break;
            case "Mastered":
                loc = 4;
                break;
            default:
                break;
        }
        int type = -1;
        switch(typeSpinner.getSelectedItem().toString()) {
            case "Riff":
                type = 0;
                break;
            case "Solo":
                type = 1;
                break;
            case "Chords":
                type = 2;
                break;
            case "Section":
                type = 3;
                break;
            case "Other":
                type = 4;
                break;
            default:
                break;
        }
        String[] tagsArray = tagsText.getText().toString().split(",");
        for (int i = 0; i < tagsArray.length; i++) {
            tagsArray[i] = tagsArray[i].trim();
        }
        String notes = notesText.getText().toString().trim();
        if(loc != -1 && type != -1 && !songName.trim().equals("")) {
            int newTag = stored_data.getInt("maxIndex", -1) + 1;
            Song addSong = new Song(songName, loc, tagsArray, type, notes, newTag);
            stored_data_editor.putInt("maxIndex", newTag);
            stored_data_editor.putString("Index: " + newTag, addSong.toString());
            Intent i = new Intent(this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            stored_data_editor.commit();
            startActivity(i);
            finish();
        } else {
            Toast alertToast = Toast.makeText(this, "Please give input to all required fields", Toast.LENGTH_SHORT);
            alertToast.show();
        }
    }
    public void cancelAdd(View view) {
        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(i);
        finish();
    }
    private void populateFields(String s) {
        try {
            JSONObject song = new JSONObject(s);
            nameText.setText(song.getString("songName"));
            typeSpinner.setSelection(song.getInt("type") + 1);
            locSpinner.setSelection(song.getInt("knowledgeLevel") + 1);
            tagsText.setText(song.getString("tags").substring(1, song.getString("tags").length()-1));
            notesText.setText(song.getString("notes"));
        } catch (JSONException e) {Log.d("EEE","failed");}
    }
}
