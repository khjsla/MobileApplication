package com.example.afinal;

import com.google.android.gms.maps.model.Marker;

public class Map {
    public String title;
    public  String snippet;
    public Marker marker;

    public  Map(String _title, String _snippet, Marker _marker){
        title = _title;
        snippet = _snippet;
        marker = _marker;
    }

    public String getMapTitle(){
        return title;
    }
    public String getMapSnippet(){
        return snippet;
    }
    public Marker getMapmarker(){
        return marker;
    }
}
