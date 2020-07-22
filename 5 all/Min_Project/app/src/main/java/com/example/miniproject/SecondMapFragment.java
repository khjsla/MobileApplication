package com.example.miniproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;


public class SecondMapFragment extends Fragment implements OnMapReadyCallback ,View.OnClickListener {

    private GoogleMap mMap; //지도
    MapView mapView; //프래그먼트 위에서 구글 맵을 사용할 때는 activity때와 달리, mapFragment가 아닌 mapView로 사용해줍니다.

    private ArrayList<Data> dataArrayList;
    SecondListener secondListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) { //지우면 안됨
        setHasOptionsMenu(true); //? 지워도 되면 지워
        super.onCreate(savedInstanceState);
    }

    //내용이 보여지게 되는
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_second_map, container, false); //layout xml의 요소들을 더 효율적으로 사용하기 위해서

        ///Fragment내에서는 mapView로 지도를 실행
        mapView = (MapView)view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState); //?
        mapView.getMapAsync(this); // 비동기적 방식으로 구글 맵 실행

        return view; //view를 View view로 해주었기 때문에 이를 마지막에 리턴해주어야 원하는 화면이 출력되게 됩니다.
    }

    //여기서 마커 찍어주고, 마커 누르면 detail Activity로 이어지게 만들어준다.
    @Override
    public void onMapReady (GoogleMap googleMap){ //다른 매소드에서 mMap을 사용하려고

        mMap=googleMap; //다른 매소드에서 mMap을 활용할거야 아래처럼

        //Data에들어있는 모든 getMapLatlngForMArker값을 가져와 marker로 만들고싶다.

        //Content Add에서 넣어준 Latlng들 모두의 마커를 찍어줘야한다.

        if(dataArrayList.size()>0){ //데이터가 있으면(속이 0이 아니면)
            ArrayList<Marker> markers = new ArrayList<Marker>(); //마커 다찍어줄거야
            for(int i=0;i<dataArrayList.size();i++){
                Data data = dataArrayList.get(i); //위치 순서대로 가져와
                LatLng latLng = data.getMapLatlngFormarker();

                mMap.addMarker(new MarkerOptions().position(latLng)); //마커 다찍어줘
            }
        }
        //다찍었고
        //이제 마커 누르면
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                //마커 눌리면 어떻게 되는지 알려줘라
                //1. 마커위치를 통해 동일한 것으 position으로 어떤 내용인지 찾아 이어줘서 detail Activity열어준다.
                LatLng markerll=marker.getPosition();

                for (int i=0;i<dataArrayList.size();i++){
                    Data data=dataArrayList.get(i);
                    LatLng latLng = data.getMapLatlngFormarker();

                    if(markerll.toString().equals(latLng.toString())){ //같은 부분 찾아와서 ==로 판단할 수 없다. 꼭 equal 이어야함!!
                        secondListener.markClicked(i);
                        return false;
                    }
                }
                return false;
            }
        });
    }
//?
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity = (Activity)context;

        try{
            secondListener = (SecondListener) activity;
        }
        catch(ClassCastException e){
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }

    }
//?

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onLowMemory();
    }

    public  interface SecondListener{ //메인으로 넘기기위한 리스너
        void markClicked(int i);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

 /**       if(mapView != null)
        {
            mapView.onCreate(savedInstanceState);
        } //map 실행하려면 이거 꼭있어야함
**/
        dataArrayList = (ArrayList<Data>) ((MainActivity)getActivity()).getData();//데이터 가져오기
    }

    @Override //onClick쓰기위해 필수 override
    public void onClick(View v) {

    }
}