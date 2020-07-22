package com.example.mycamera;
//url이나 이미지 위치 경로를 토대로 접근하여 input output 스트림을 이용하여 이미지를 변환하여 가져오는
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class MainActivity extends AppCompatActivity {

    static final int MY_PERMISSION_REQUEST = 1111; //??

    CameraPreview mPreview;
    Camera mCamera;

    ListView List;
    ArrayList<String> mList;
    ArrayAdapter<String> adapter;

    public static String IMAGE_FILE="capture.jpg"; //캡쳐한 사진 저장할 이름

    Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() { //추가
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File file = getOutputMediaFile(MEDIA_TYPE_IMAGE);

            if (file == null) {
                return;
            }
            try {
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(data);
                fos.close();

                Toast.makeText(getApplicationContext(),"카메라로 찍은 사진을 앨범에 저장했습니다.",Toast.LENGTH_LONG).show();

            } catch (IOException e) {
                Log.d("MainActivity", e.getMessage());
            }
        } //여기까지
    };

    private File getOutputMediaFile(int type) {

        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(),
                "MyCamera");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("CameraApp", "failed to create directory");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            String pathName = mediaStorageDir.getPath() + File.separator + "IMG" + timeStamp + ".jpg"; //pathName
            mediaFile = new File(pathName);
            //mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_"+ timeStamp + ".jpg");

            mList.add(pathName);
            adapter.notifyDataSetChanged();

        } else {
            return null;
        }
        return mediaFile;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermission(); //퍼미션체크

        List = findViewById(R.id.pictureList);

        mList = new ArrayList();

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, mList);
        List.setAdapter(adapter);

        List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) { //리스트뷰에 있는 사진 누르면 _ 리스트 누르면
                ImageView imageView;

                imageView=(ImageView) findViewById(R.id.imageView);

                try{
                    //Uri로 사진을 받아와서
                    Uri uri = Uri.parse(mList.get(position)); //보여주고싶어요
                    imageView.setImageURI(uri);
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void checkPermission() { //퍼미션 확인 . activity가 로케이션에 대한 권한이 있는지 확인하는 코드
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            return;
        }
    }

    //퍼미션 요청 결과처리 = 사용자가 퍼미션 요청에 응답할 경우, 시스템이 어플의 아래 함수에 결과를 전달한다.
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSION_REQUEST:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "권한체크거부됨", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        ConstraintLayout rootView = findViewById(R.id.rootView);
        if (openCameraSafe(0)) { //카메라 프리뷰 보여주기
            mPreview = new CameraPreview(this, mCamera);
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(400, 400);
            rootView.addView(mPreview, 0, params);
        }
    }

    public void btnCamera(View view) {
        mCamera.takePicture(null, null, mPictureCallback);
    }

    private boolean openCameraSafe(int id) {
        boolean qOpen = false;
        try {
            releaseCameraAndPreview();
            mCamera = Camera.open(id);
            qOpen = (mCamera != null);
        } catch (Exception e) {
            Log.d("MainActivity", e.getMessage());
        }
        return qOpen;
    }

    private void releaseCameraAndPreview() {
        if (mPreview != null) {
            mPreview.setCamera(null);
        }
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }
}



/**
 Uri providerURI = FileProvider.getUriForFile(this,getPackageName(),photoFile);

 imgUri = providerURI;

 intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, providerURI);

 startActivityForResult(intent, FROM_CAMERA);
 카메라로 찍은 사진 uri로 저장
*/

/**
 위의 startActivity에 대한 처리

 @Override

 protected void onActivityResult(int requestCode, int resultCode, Intent data) {

 super.onActivityResult(requestCode, resultCode, data);


 if(resultCode != RESULT_OK){

 return;

 }

 switch (requestCode){

 case FROM_ALBUM : {

 //앨범에서 가져오기

 if(data.getData()!=null){

 try{

 File albumFile = null;

 albumFile = createImageFile();


 photoURI = data.getData();

 albumURI = Uri.fromFile(albumFile);


 galleryAddPic();

 //이미지뷰에 이미지 셋팅

 img1.setImageURI(photoURI);

 //cropImage();

 }catch (Exception e){

 e.printStackTrace();

 Log.v("알림","앨범에서 가져오기 에러");

 }

 }

 break;

 }
 */

/**
 Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

 Uri providerURI = FileProvider.getUriForFile(this,getPackageName(),photoFile);

 Uri imgUri = providerURI;

 intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, providerURI);

 startActivityForResult(intent, FROM_CAMERA);
 */

/**
 private ImageView imageView02;

 imageView02 = (ImageView) findViewById(R.id.imageView02);

 try {
 Uri uri = Uri.parse("file:///" + Environment.getExternalStorageDirectory() + "/572/내그림/image_sample.jpg");
 imageView02.setImageURI(uri);
 }catch (Exception e){
 e.printStackTrace();
 }
 */