package com.example.miniproject;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.PixelCopy;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.internal.IAccountAccessor;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

//여기서 맵위치랑 (경도위도) title, snippet, Uri 다 넣을거에요 //우선 리스트?? 에 무조건 추가하는 걸로 하자.
//여기서 버튼을 누르면 First Frag 에선 리스트에 추가될거고,
//Second Frag에선 마커가 찍어진다.
//이 액티비티 시작하자마자 사진을 찍을건지 갤러리 에서 사진 가져올건지 정하고, 사진부터 처리르 해준다.

public class ContentAdd extends FragmentActivity implements OnMapReadyCallback {

    public static final String ITEM = "ITEM";
    public static final String TITLE = "TITLE";
    public static final String SNIPPET = "SNIPPET";
    public static final String IMAGE = "IMAGE";
    public static final String LATITUDE = "LATITUDE";
    public static final String LONGITUDE = "LONGITUDE";


    private LatLng _latLng;
    private byte[] byteArr;
    private Uri _uri;

    private GoogleMap mMap; //지도

    private Uri photoUri;
    private String currentPhotoPath; //실제 사진 파일 경로
    String mImageCaptureName; //이미지 이름

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_add);

        //아래 SupportMapFrag 그냥 Mapfrag도 되면 그냥 Suppport빼주라
        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this); //고정된 형식이다.

// ImageButton
        ImageButton imageButton = (ImageButton) findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //이미지 버튼 뷰만 가져오면 된다.
                Intent intent = new Intent(ContentAdd.this, Camera.class);
                startActivityForResult(intent,0);
            }
        });

 /**       Button button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //MainActivity화면 열어줌
                add(v);

                Intent intent = new Intent(ContentAdd.this, MainActivity.class);
                startActivityForResult(intent,0);
            }
        });**/

    }

        //여기서 맵의 경도,위도 가져와서 저장하면되니까
    @Override
    public void onMapReady (GoogleMap googleMap){ //다른 매소드에서 mMap을 사용하려고

        mMap=googleMap; //다른 매소드에서 mMap을 활용할거야 아래처럼

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() { //지도가 눌리면 일어나는 상황. 지도가 눌린곳의 위치를 가져와야한다.
            @Override
            public void onMapClick(LatLng latLng) { //클릭된 위치 -latLng 이 변수
                 final Marker marker = mMap.addMarker(new MarkerOptions().position(latLng));  // 마커 찍어주자자
                marker.setPosition(latLng);
                _latLng=latLng;
                //data에 넣어준다 map에서클릭한 lng값을 넣어주게
            }

        }); //onMapClick끝

    }
    //사진을 위한 코드들
    private static final int TAKE_PICTURE = 102;
    private static final int CHOOSE_PICTURE = 103;

    //Camera Activity에서는 어떻게 할지만 정하고, 사진 받아오기 여기서 하자
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //하고싶은거 여기에 적기
        if(resultCode==44){ //requestcode 1이면 사진찍기
            takePic();
        }
        else if(resultCode==45){ //requestcode 2이면 사진고르기
            choosePic();
        }
        else if(requestCode==TAKE_PICTURE) { //사진찍고나면
            getPictureForPhoto();
        }
        else if(requestCode==CHOOSE_PICTURE) { //갤러리열고나면
            sendPicture(data.getData());
        }
    }

    public void takePic() {
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {

                }
                if (photoFile != null) {
                    photoUri = FileProvider.getUriForFile(this, getPackageName(), photoFile);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    startActivityForResult(intent, TAKE_PICTURE);
                }
            }

        }
    }

    private File createImageFile() throws IOException {
        File dir = new File(Environment.getExternalStorageDirectory() + "/path/");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        mImageCaptureName = timeStamp + ".jpeg";

        File storageDir = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/path/"

                + mImageCaptureName);
        currentPhotoPath = storageDir.getAbsolutePath();

        return storageDir;
    }

    private void getPictureForPhoto() { //여기서 사진 루트 추출
        ImageButton imageButton = (ImageButton)findViewById(R.id.imageButton);
        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
        imageButton.setImageBitmap(bitmap);

        setmapPicture(this, bitmap); // 직접찍고 가져온 사진의 bitmap을 Uri형태로 바꿔서 _uri로 저장
    //    byteArr = PictureToByteArray(this,bitmap);
    }

    public void choosePic(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PICTURE);
    }

    private void sendPicture(Uri imgUri) { //여기서 갤러리 루트 추출

        String imagePath = getRealPathFromURI(imgUri); // path 경로

        ImageButton imageButton = (ImageButton)findViewById(R.id.imageButton);
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);//경로를 통해 비트맵으로 전환
        imageButton.setImageBitmap(bitmap);

        setmapPicture(this, bitmap); //갤러리열고난 후 가져온 사진의 bitmap을 Uri형태로 바꿔서 _uri로 저장
  //      byteArr = PictureToByteArray(this,bitmap);
    }

    private String getRealPathFromURI(Uri contentUri) {
        int column_index=0;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if(cursor.moveToFirst()){
            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        }
        return cursor.getString(column_index);
    }

    public void setmapPicture(Context context, Bitmap inImage){ //imageButton에 들어온 이미지를 uri로 변환해서 저장해주는 함수
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        _uri= Uri.parse(path);
    }

    public byte[] PictureToByteArray(Context context,Bitmap inImage){
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        byte[] byteArray = bytes.toByteArray();
        return byteArray;
    }//??

    public void add(View v){
        EditText _title = findViewById(R.id.Title_content); //_title 에 내용저장했어
        EditText _snippet = findViewById(R.id.Snippet_content); //_snippet 에 내용저장했어

        String title = _title.getText().toString();
        String snippet = _snippet.getText().toString();

        Double latitude = _latLng.latitude;
        Double longitude = _latLng.longitude;

        //사진 String이든 바이트어레이든 변환해서 puEXTRA에 넣어줘야함
      //  byte[] bytearray = byteArr; //바이트 어레이로 가져갈거임
        Intent intent = new Intent();

        intent.putExtra(TITLE, title);
        intent.putExtra(SNIPPET, snippet);
       // intent.putExtra(IMAGE,bytearray); //Uri 혹은 Bitmap String으로 변경시켜서 넣어준
        intent.putExtra(LATITUDE, latitude);
        intent.putExtra(LONGITUDE, longitude);

        setResult(RESULT_OK, intent);
        finish();

    }

}
