package com.example.afinal;

import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.message.template.ButtonObject;
import com.kakao.message.template.ContentObject;
import com.kakao.message.template.FeedTemplate;
import com.kakao.message.template.LinkObject;
import com.kakao.message.template.SocialObject;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;
import com.kakao.util.helper.log.Logger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,DialogInterface.OnDismissListener {
    private static String TAG = "key"; //맞나?? ㅠㅠ

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private  CustomDialog mCustomDialog;

    ListView mapListView;
    ArrayList<Map> mapArrayList;
    ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //위치 받아오기
        mFusedLocationProviderClient
                = LocationServices.getFusedLocationProviderClient(this);

//해쉬키 받아오기
        try {
            Log.d(TAG, "Key hash is " + getKeyHash(this));
        } catch (PackageManager.NameNotFoundException ex) {
// handle
        }

        //여기서 전역으로 선언했던 ListView 이어주기 _ adapter
        mapArrayList = new ArrayList<>();
        mapListView = (ListView)findViewById(R.id.maplist);
        adapter=new ListAdapter(this,mapArrayList);
        mapListView.setAdapter(adapter);
        //리스트뷰가 눌리게 되면 일어나는 상황 아래에 구현
        mapListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                               @Override
                                               public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                   int _position = position; //변수 선언
                                                   Map _Map = mapArrayList.get(_position); //어레이 리스트에서 그 위치가져와
                                                   Marker _Marker =_Map.getMapmarker(); //마커 만들은거
                                                   LatLng _LatLng = _Marker.getPosition(); //위치 가져와서 LatLng형식으로 mLatLng에 넣어주
                                                   mMap.moveCamera(CameraUpdateFactory.newLatLng(_LatLng)); //위치 옮겨준다
                                               }
                                           }
        );

    } //oncreate끝
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() { //마커 눌리면 : 카카오에 공유해야한다.
            @Override
            public boolean onMarkerClick(Marker marker) { //카카오톡에 마커의

    /**
                //아래는 실습예제 그대로
                FeedTemplate.Builder builder = FeedTemplate
                        .newBuilder(
                                ContentObject.newBuilder("디저트사진",
                                        "http://mud-kage.kakao.co.kr/dn/NTmhS/btqfEUdFAUf/FjKzkZsnoeE4o19klTOVI1/openlink_640x640s.jpg",
                                        LinkObject.newBuilder().setWebUrl("https://developers.kakao.com")
                                                .setMobileWebUrl("https://developers.kakao.com").build()
                                ).setDescrption("아메리카노, 빵, 케익")
                                        .build()
                        );
                builder.setSocial(
                        SocialObject.newBuilder().setLikeCount(10).setCommentCount(20)
                                .setSharedCount(30).setViewCount(40).build()
                );
                builder.addButton(
                        new ButtonObject("웹에서보기",
                                LinkObject.newBuilder()
                                        .setWebUrl("'https://developers.kakao.com")
                                        .setMobileWebUrl("'https://developers.kakao.com")
                                        .build()
                        )
                );
                builder.addButton(
                        new ButtonObject("앱에서보기", LinkObject.newBuilder()
                                .setWebUrl("'https://developers.kakao.com")
                                .setMobileWebUrl("'https://developers.kakao.com")
                                .setAndroidExecutionParams("key1=value1")
                                .setIosExecutionParams("key1=value1")
                                .build()
                        )
                );
                FeedTemplate params = builder.build();
                Map<String, String> serverCallbackArgs = new HashMap<String, String>();
                serverCallbackArgs.put("user_id", "${current_user_id}");
                serverCallbackArgs.put("product_id", "${shared_product_id}");
                KakaoLinkService.getInstance().sendDefault(this, params, serverCallbackArgs, new
                        ResponseCallback<KakaoLinkResponse>() {
                            @Override
                            public void onFailure(ErrorResult errorResult) {
                                Logger.e(errorResult.toString());
                            }

                            @Override
                            public void onSuccess(KakaoLinkResponse result) {
// 템플릿밸리데이션과쿼터체크가성공적으로끝남. 톡에서정상적으로보내졌는지보장은할수없다. 전송성공유무는서버콜백기능을이용하여야한다.
                            }
                        });
*/
                return false;
            }
        });

    //     final TextView Title = (TextView) findViewById(R.id.title); //다이얼로그의 값을 보내줄라고(?)
   //     final TextView Snippet = (TextView) findViewById(R.id.snippet);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() { //지도가 눌리면 일어나는 상황. dialog가 출력, title, snippet입력, 추가 버튼 입력시, 마커 추가. & listView에 추가
            @Override
            public void onMapClick(LatLng latLng) { //클릭된 위치 -latLng 이 변수
                //커스텀 다이얼로그 불러온다.
                mCustomDialog = new CustomDialog(MapsActivity.this); //지도가 눌리면 내가 정해줬던 새 다이얼로그 불러온다
                mCustomDialog.setOnDismissListener(MapsActivity.this); //implements에 Dialoginterface.onDismiss넣어준다
                mCustomDialog.setmapLatLng(latLng); //내가 클릭한곳의 경도위도 넣어줍니다
                mCustomDialog.show(); //보여줘요

                //CustomDialog customDialog = new CustomDialog(MapsActivity.this);
                }

        }); //onMapClick끝
    }

    @Override
    public void onDismiss(DialogInterface dialog) { //다이얼로그가 종료될떄 수행되는 작업을 허용하기위해 사용
        CustomDialog customDialog = (CustomDialog) dialog; //내가 만든 커스텀 다이얼로그

        LatLng latlng = customDialog.getmapLatLng(); //위치 가져와유
        Marker marker2 = mMap.addMarker(new MarkerOptions()
                .position(latlng)); //마커를 만듭니다.위치넣어주구
        Map map2 = new Map(customDialog.getmapTitle(), customDialog.getmapSnippet(), marker2); //Map form의 새 자료를 하나 만들어요
        mapArrayList.add(map2); //list에 추가해주빈다.

    }

    //키해시 가져오기
    public static String getKeyHash(final Context context) throws PackageManager.NameNotFoundException {
        PackageManager pm = context.getPackageManager();
        PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
        if (packageInfo == null) return null;
        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                return Base64.encodeToString(md.digest(), Base64.NO_WRAP);
            } catch (NoSuchAlgorithmException e) {
                Log.w(TAG, "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
        return null;
    }

}
