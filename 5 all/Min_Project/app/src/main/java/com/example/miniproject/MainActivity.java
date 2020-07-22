package com.example.miniproject;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.ArrayList;

import static com.example.miniproject.detail.POSITION_TO_MAIN;

public class MainActivity extends AppCompatActivity implements FirstListFragment.FirstListener, SecondMapFragment.SecondListener ,ThirdMyFragment.ThirdListener{

    /** 키값 데이터 저장하기 _for영구저장
 *
    Context context = getActivity(); //context로 액티비티 받아와서

     공유참조를 다뤄보자
    SharedPreferences sharedPref = context.getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE); //셰어 참조를받아온다

     이를 써
    SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPref.edit();

    editor.putInt(getString(R.string.saved_high_score_key), newHighScore);
    editor.commit();

     공유 참조로부터 읽어오는 방법은 아래
    SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
    int defaultValue = getResources().getInteger(R.integer.saved_high_score_default_key);
    int highScore = sharedPref.getInt(getString(R.string.saved_high_score_key), defaultValue);
 Json 사용법
 **/

    private int REQUEST_ADD = 12;
    private int DELETE = 16;

    public static final String TITLE_TO_DETAIL = "TITLE_TO_DETAIL";
    public static final String SNIPPET_TO_DETAIL = "SNIPPET_TO_DETAIL";
    public static final String IMAGE_TO_DETAIL = "IMAGE_TO_DETAIL";
    public static final String LATITUDE_TO_DETAIL = "LATITUDE_TO_DETAIL";
    public static final String LONGITUDE_TO_DETAIL = "LONGITUDE_TO_DETAIL";
    public static final String POSITION = "POSITION";


    private FragmentManager fragmentManager = getSupportFragmentManager(); //FrameLayout에 각메뉴의 Fragment를 바꿔준다.

    private FirstListFragment firstListFragment = new FirstListFragment();
    private SecondMapFragment secondMapFragment = new SecondMapFragment();
    private ThirdMyFragment thirdMyFragment = new ThirdMyFragment();

    public ArrayList<Data> dataArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataArrayList=new ArrayList<Data>(); //데이터 넣어줄거야

        ArrayList<Data> arrayList=new ArrayList<Data>();

        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE); // Preference를 얻어오는데, Mode는 private다.
        Gson gson = new Gson(); //Gson사용해 줄거임 //Gson 자바객체를 JSON으로 변환하기도, JSON String을 자바객체로도 변환해주는
        String json = sharedPreferences.getString("Maplist", ""); //json안에 Maplist라는 키값으로 디포릍 밸류받아올거임
        Type myType = new TypeToken<ArrayList<Data>>() {}.getType();
        arrayList = gson.fromJson(json, myType);
       if(arrayList!=null){
            dataArrayList.addAll(arrayList);
       }
       //위의 코드는 convert from Json a typed ArrayList<> 방법으로부터 얻어왔습니다.

        //xml의 navigationbar의 정보와 여기와 이어줍니다.
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        //xml의 fab 정보와 여기와 이어줍니다.
        FloatingActionButton fab = findViewById(R.id.fab);

        //첫화면을 지정합니다.
        FragmentTransaction transaction =fragmentManager.beginTransaction();
        transaction.replace(R.id.Frame,firstListFragment).commitAllowingStateLoss();

        //navigation이 눌리게되면 위에 구현해 놓은 onNavigationonLtemClickListener가 작동하게 됩니다.
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                FragmentTransaction transaction =fragmentManager.beginTransaction();
                switch (menuItem.getItemId()){
                    case R.id.navigation_home: {
                        transaction.replace(R.id.Frame,firstListFragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.navigation_dashboard: {
                        transaction.replace(R.id.Frame,secondMapFragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.navigation_notifications: {
                        transaction.replace(R.id.Frame,thirdMyFragment).commitAllowingStateLoss();
                        break;
                    }
                }
                return true;
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ContentAdd.class);
                startActivityForResult(intent,REQUEST_ADD);
           //     Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
             //           .setAction("Action", null).show();
            }
        });
    }

    public ArrayList<Data> getData(){
        return dataArrayList;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent ddata) {
        super.onActivityResult(requestCode, resultCode, ddata);
        if (requestCode == REQUEST_ADD) {
            if (resultCode == RESULT_OK) {

                String title = ddata.getStringExtra(ContentAdd.TITLE);
                String snippet = ddata.getStringExtra(ContentAdd.SNIPPET);

                Double latitude = ddata.getDoubleExtra(ContentAdd.LATITUDE, 0);
                Double longitude = ddata.getDoubleExtra(ContentAdd.LONGITUDE, 0);
                LatLng mLatLng = new LatLng(latitude, longitude);
                //사진 정보 가져와서 넣어야

               // byte[] bytes = data.getByteArrayExtra(ContentAdd.IMAGE); //Content ADd에서 byteArray받아와 Uri로 변경해주자

               /** try{
                    String s = new String(bytes,"UTF-8");
                    Uri uri = Uri.parse(s);

                    Data data1;

                    data1 = new Data(title, snippet, mLatLng, uri); //사진가져와
                    dataArrayList.add(data1);
                }
                catch(UnsupportedEncodingException e) {
                    Log.e("Yourapp", "UnsupportedEncodingException");
                }**/

               Data mydata= new Data(title,snippet,mLatLng);
               dataArrayList.add(mydata);

            }
        }
        if (requestCode == DELETE) {
            if (resultCode == RESULT_OK) {
                int _position = ddata.getIntExtra(detail.POSITION_TO_MAIN, 0);
                dataArrayList.remove(_position);
            }

        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.Frame);
        transaction.detach(fragment).attach(fragment).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE); //키밸류 가져오기준비
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit(); //edit해줄거임 SharedPref 가져와서
        Gson gson = new Gson(); //Json _ Gson으로 사용해 줄거임임
        //Gson=Java 객체를 JSON표현식으로 변환해주는 API
       String json = gson.toJson(dataArrayList); //json에 gson의 toJson 함수를 이용해 dataArrayList값들을 json으로 설정해줘요
        prefsEditor.putString("Maplist", json); //Editor에  "Maplist" 라는 키값으로 json = dataArrayList 값들을 저장한다
        prefsEditor.commit(); //완료한다
    }


    @Override
    public void markClicked(int position) { //디테일로
        Intent intent = new Intent(this, detail.class);

        Data _data = dataArrayList.get(position);

     //   intent.putExtra(IMAGE_TO_DETAIL,_data.getMapPicture()); //이게되나..?

        intent.putExtra(TITLE_TO_DETAIL, _data.getMapTitle());
        intent.putExtra(SNIPPET_TO_DETAIL, _data.getMapSnippet());

        LatLng latLng = _data.getMapLatlngFormarker();
        double latitude = latLng.latitude;
        double longitude = latLng.longitude;
        intent.putExtra(LATITUDE_TO_DETAIL, latitude);
        intent.putExtra(LONGITUDE_TO_DETAIL, longitude);
        intent.putExtra(POSITION, position);

      startActivityForResult(intent, DELETE);
    }

    @Override
    public void ListClicked(int position) { //디테일로
        Intent in = new Intent(this, detail.class);

        Data _data = dataArrayList.get(position);

      // in.putExtra(IMAGE_TO_DETAIL,_data.getMapPicture()); //이것도,.,의문

        in.putExtra(TITLE_TO_DETAIL, _data.getMapTitle());
        in.putExtra(SNIPPET_TO_DETAIL, _data.getMapSnippet());

        LatLng latLng = _data.getMapLatlngFormarker();
        double latitude = latLng.latitude;
        double longitude = latLng.longitude;
        in.putExtra(LATITUDE_TO_DETAIL, latitude);
        in.putExtra(LONGITUDE_TO_DETAIL, longitude);
        in.putExtra(POSITION, position);

       startActivityForResult(in, DELETE);
    }

    @Override
    public void GridClicked(int position) { //디테일로
        Intent in = new Intent(this, detail.class);

        Data _data = dataArrayList.get(position);

        // in.putExtra(IMAGE_TO_DETAIL,_data.getMapPicture()); //이것도,.,의문

        in.putExtra(TITLE_TO_DETAIL, _data.getMapTitle());
        in.putExtra(SNIPPET_TO_DETAIL, _data.getMapSnippet());

        LatLng latLng = _data.getMapLatlngFormarker();
        double latitude = latLng.latitude;
        double longitude = latLng.longitude;
        in.putExtra(LATITUDE_TO_DETAIL, latitude);
        in.putExtra(LONGITUDE_TO_DETAIL, longitude);
        in.putExtra(POSITION, position);

        startActivityForResult(in, DELETE);
    }

}