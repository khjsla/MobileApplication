package com.example.miniproject;

import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

//내가 사용할 데이터들을 저장해줄 폼입니다. - title, snippet, marker(위치), 사진 데이터를 저장해줍니다.
public class Data {
    private String title;
    private String snippet;
    private LatLng latLng;
    private Uri picture; //사진이름(경로) 가져온다 - 사진은 Uri형식


    public  Data(String _title, String _snippet, LatLng _latLng/**, Uri _picture*/){
        this.title = _title;
        this.snippet = _snippet;
        this.latLng = _latLng;
    }
        //this.picture = _picture; }//사진 uri가져온다 이거 나중에 bitmap으로 전환해서 보여줄 수 있는거같음
 //이거 ListAdapter의 addItem 때문에 일단 주석 처리


    public String getMapTitle(){
        return this.title;
    } //title과
    public String getMapSnippet(){
        return this.snippet;
    } //snippet과
    public LatLng getMapLatlngFormarker(){
        return this.latLng;
    } //마커위치와
    public Uri getMapPicture(){
        return this.picture;
    } //사진을 가져옵니다.



}
