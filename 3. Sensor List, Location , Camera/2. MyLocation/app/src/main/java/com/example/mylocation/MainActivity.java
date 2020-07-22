package com.example.mylocation;
//2번째 과제 입니다. 퍼미션 구현과, 위치 얻어오기를 해야합니다.
import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.location.Location;
import android.location.LocationListener;
import android.os.Debug;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;




public class MainActivity extends AppCompatActivity {
    FusedLocationProviderClient mFusedLocationClient; //FusedLocationProvider 인스턴스를 생성해줍니다. _1

    static final int MY_PERMISSION_REQUEST=1111; //??

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermission(); //퍼미션 체크를 해줄겁니다.

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this); //FusedLocationProvider 인스턴스를 생성해줍니다. _2
    }

    private void checkPermission() { //퍼미션 확인 . activity가 로케이션에 대한 권한이 있는지 확인하는 코드
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSION_REQUEST);
            return;
        }
    }
/** 위에 세줄업애고 아래것도 ㅇㅇ
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)&&ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_COARSE_LOCATION)){
        } else{
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},MY_PERMISSION_REQUEST);
        } } else{
        }
    }
*/

//퍼미션 요청 결과처리 = 사용자가 퍼미션 요청에 응답할 경우, 시스템이 어플의 아래 함수에 결과를 전달한다.
   @Override
    public void onRequestPermissionsResult(int requestCode, String []permissions, int[] grantResults) {
       super.onRequestPermissionsResult(requestCode, permissions, grantResults);
       switch (requestCode) {
           case MY_PERMISSION_REQUEST:
               if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                   Toast.makeText(this,"권한체크거부됨",Toast.LENGTH_SHORT).show();
               }
               return;
       }
   }
                /**if부터 삭제하고 아래거 해도 가능
                {
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
// If request is cancelled, the result arrays are empty.
                }else{
// permission was granted, yay! Do the
// contacts-related task you need to do.
                }
                return;
            }
            // other 'case' lines to check for other
// permissions this app might request.
        }
    }
*/
    public void btnLastLocation(View view) { //마지막위치를 요청하기위한 . button에 연결.
        mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                Log.d("MainActivity",
                        "location: " + location.getLatitude() + ", " + location.getLongitude());
                TextView textView=findViewById(R.id.textView); //textView.setText(location.getLatitude())를 바로할 수 없다 왜냐하면, setText는 ()안에 String만 가져올 수 있기 때문이다. 따라서 String.valueOf를 사용해 값을 가져와야한다.
                textView.setText(String.valueOf(location.getLatitude()));
                textView =findViewById(R.id.textView2);
                textView.setText(String.valueOf(location.getLongitude()));
            }
        });
    }

    public void btnLocationUpdate(View view) {  //위치 업데이트를 위한 버튼, location Service에 연결한 후 위치요청을 해야합니다.
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(500);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        mFusedLocationClient.requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                for(Location location : locationResult.getLocations()) {
                    Log.d("MyLocation", "location: " + location.getLatitude() + ", " + location.getLongitude());
                }
            }
        }, null);

    }

    public void btnStopLocationUpdate(View view){ //위치갱신을 정지해주는 버튼
        mFusedLocationClient.removeLocationUpdates(new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                for(Location location : locationResult.getLocations()) {
                    Log.d("MyLocation", "location: " + location.getLatitude() + ", " + location.getLongitude());
                }
            }
        });
    }

}
