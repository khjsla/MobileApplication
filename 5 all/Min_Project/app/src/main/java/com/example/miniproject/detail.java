package com.example.miniproject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class detail extends FragmentActivity implements OnMapReadyCallback { //Content Add 에서 넣어진 Data 를 가져와야 한다. First Frag 에서, Second Frag에서 이어져야 한다.

    private GoogleMap mMap; //지도

    public static final String POSITION_TO_MAIN = "com.example.miniproject.POSITION_TO_MAIN";

    private int __position;
    private String __title;
    private String __snippet;
    private LatLng __latLng;
    private Uri  uri;
    private byte[] bytes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);//사용하는 레이아웃은 제가 만든 액티비티디테일 레이아웃입니다.

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this); //맵을 사용하기 위한 준비입니다.

        Intent intent = getIntent(); //값 넘겨받자
        __position = intent.getIntExtra(MainActivity.POSITION, 0);  //인덱스 값 받아온거

        __title = intent.getStringExtra(MainActivity.TITLE_TO_DETAIL);
        __snippet = intent.getStringExtra(MainActivity.SNIPPET_TO_DETAIL);

        Double latitude = intent.getDoubleExtra(MainActivity.LATITUDE_TO_DETAIL, 0);
        Double longitude = intent.getDoubleExtra(MainActivity.LONGITUDE_TO_DETAIL, 0);
        __latLng=new LatLng(latitude,longitude); //각 경도위도 더블로가져와서 LAtlang에 넣어줘 합쳐줌

        TextView TItle = findViewById(R.id.title);
        TextView SNippet = findViewById(R.id.snippet);

        TItle.setText(__title);
        SNippet.setText(__snippet);

       //ImageView layoutimageView = findViewById(R.id.imageView);
      // bytes=intent.getByteArrayExtra(MainActivity.IMAGE_TO_DETAIL);
        //byte[] 이미지 변환했던거;
        //Uri uri = 위에서 받아왔던 이미지를 uri로 변경
        //layoutimageView.setImageUri(uri);해줌
         //position 에 걸맞는 Uri를 getMapPicture로 받아와 detail.xml의 imageView에 넣어줍니다.


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;  //mMap으로 매소드들 사용해 줄 겁니다.
//d위에서 지정해준 latLng가져오기
        mMap.addMarker(new MarkerOptions().position(__latLng)); //그 Latlng으로 지도에 찍어줘요
        mMap.moveCamera(CameraUpdateFactory.newLatLng(__latLng)); //그 위치를 제가 보는 지도의 중앙으로 옮겨줍니다.
    }

    public void delete(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(POSITION_TO_MAIN, __position);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void kakao(View v) {

    }

}
