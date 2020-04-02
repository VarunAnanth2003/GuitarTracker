package com.example.musictracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

public class FilterScreen extends Activity {

    EditText aNameField;
    EditText aTagField;
    Spinner locSpinner;
    Spinner typeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_screen);
        aNameField = findViewById(R.id.filter_name);
        aTagField = findViewById(R.id.filter_tag);
        locSpinner = findViewById(R.id.filter_loc);
        typeSpinner = findViewById(R.id.filter_type);
        Intent i = getIntent();
        if(i.hasExtra("activeName")) aNameField.setText(i.getStringExtra("activeName"));
        if(i.hasExtra("activeTag")) aTagField.setText(i.getStringExtra("activeTag"));
        if(i.hasExtra("activeLoc")) locSpinner.setSelection(i.getIntExtra("activeLoc", 0) + 1);
        if(i.hasExtra("activeType")) typeSpinner.setSelection(i.getIntExtra("activeType", 0) + 1);
    }
    public void setFilter(View view) {
        String activeName;
        String activeTag;
        int activeLoc;
        int activeType;
        activeName = aNameField.getText().toString().trim();
        activeTag = aTagField.getText().toString().trim();
        activeLoc = -1;
        switch(locSpinner.getSelectedItem().toString()) {
            case "Just Started":
                activeLoc = 0;
                break;
            case "Learning":
                activeLoc = 1;
                break;
            case "Halfway There":
                activeLoc = 2;
                break;
            case "Almost Completed":
                activeLoc = 3;
                break;
            case "Mastered":
                activeLoc = 4;
                break;
            default:
                break;
        }
        activeType = -1;
        switch(typeSpinner.getSelectedItem().toString()) {
            case "Riff":
                activeType = 0;
                break;
            case "Solo":
                activeType = 1;
                break;
            case "Chords":
                activeType = 2;
                break;
            case "Section":
                activeType = 3;
                break;
            case "Other":
                activeType = 4;
                break;
            default:
                break;
        }
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("activeName", activeName);
        i.putExtra("activeTag", activeTag);
        i.putExtra("activeLoc",activeLoc);
        i.putExtra("activeType", activeType);
        startActivity(i);
        finish();
    }
    public void clearFilter (View view) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
