package com.example.musictracker.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.musictracker.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SongListAdapter extends BaseAdapter {

    private Context context;
    private JSONArray allSongs;

    public SongListAdapter(Context context, JSONArray allSongs) {
        this.context = context;
        this.allSongs = allSongs;
    }

    @Override
    public int getCount() {
        return allSongs.length();
    }

    @Override
    public Object getItem(int i) {
        try {
            return allSongs.get(i);
        } catch(JSONException e) {
            return new Object();
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).
                    inflate(R.layout.listview_song, viewGroup, false);
        }
        JSONObject currentObject = (JSONObject) getItem(i);
        TextView songNameField = view.findViewById(R.id.songlistview_name);
        TextView typeField = view.findViewById(R.id.songlistview_type);
        TextView tagField = view.findViewById(R.id.songlistview_tags);
        ImageView locImage = view.findViewById(R.id.songlistview_image);
        try {
            songNameField.setText(Html.fromHtml("<b>Song: </b>" + currentObject.getString("songName")));
            String type;
            switch(Integer.parseInt(currentObject.getString("type"))) {
                case 0:
                    type = "Riff";
                    break;
                case 1:
                    type = "Solo";
                    break;
                case 2:
                    type = "Chords";
                    break;
                case 3:
                    type = "Section";
                    break;
                case 4:
                    type = "Other";
                    break;
                default:
                    type = "N/A";
                    break;
            }
            typeField.setText(Html.fromHtml("<b>Type: </b>" + type));
            int loc;
            switch(Integer.parseInt(currentObject.getString("knowledgeLevel"))) {
                case 0:
                    loc = R.drawable.locimagea;
                    break;
                case 1:
                    loc = R.drawable.locimageb;
                    break;
                case 2:
                    loc = R.drawable.locimagec;
                    break;
                case 3:
                    loc = R.drawable.locimaged;
                    break;
                case 4:
                    loc = R.drawable.locimagee;
                    break;
                default:
                    loc = R.drawable.locimagef;
                    break;
            }
            locImage.setImageResource(loc);
            tagField.setText(Html.fromHtml("<b>Tags: </b><i>" + currentObject.getString("tags").substring(1,currentObject.getString("tags").length()-1) + "</i>"));
            songNameField.setTag(currentObject.get("index"));
        } catch (JSONException e) {}
        return view;
    }
}
