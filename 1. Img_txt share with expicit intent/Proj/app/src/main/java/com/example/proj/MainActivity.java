package com.example.proj;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

import static android.os.Environment.DIRECTORY_PICTURES;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE) { //REQUEST_VOTE였음
            if (resultCode == RESULT_OK) {
                /**String result=data.getStringExtra(VoteActivity.EXTRA_RESULT);
                 TextView textView=findViewById(R.id.textView2);
                 textView.setText(result); - 이건 텍스트 공유 */
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                ImageView imageView = findViewById(R.id.imageView2);
                imageView.setImageBitmap(imageBitmap); //사진찍은거 imageView로 가져와서 보여줘요

                //여기에 String.. 찍은 사진을 파일로 저장하자
                String path = getExternalFilesDir(DIRECTORY_PICTURES) + "/Share.png";
                File file = new File(path);
                FileOutputStream out; //경로지정

                try {
                    out = new FileOutputStream(file); //파일로 저장해요
                    imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        }


    }


    /**사진 찍*/
    public static final int REQUEST_IMAGE_CAPTURE = 2;

    public void takePicture(View view) { //사진직기
        Intent takePicIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //사진을 찍어서 보내는 인텐트
        if (takePicIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePicIntent, REQUEST_IMAGE_CAPTURE); //activity 실행
        }
    }

    /**TEXT 공유*/
    /**IMAGE 공유*/
    public void Share(View view) {
//image
//text
        String path = getExternalFilesDir(DIRECTORY_PICTURES) + "/Share.png";
        File file = new File(path);
        Uri bmpUri = FileProvider.getUriForFile(MainActivity.this, "com.example.proj.fileprovider", file); //찍었던 사진 저장한 경로 가져오기

        Intent shareIntent = new Intent(); //인텐트 설정
        shareIntent.setAction(Intent.ACTION_SEND); //보내기인텐트를

        EditText editText = findViewById(R.id.editText);
        String textMessage = editText.getText().toString();
        shareIntent.putExtra(Intent.EXTRA_TEXT, textMessage);
        shareIntent.setType("text/plain");

        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri); //bmpUri붙여서
        shareIntent.setType("image/png"); //타입은이거야
        startActivity(Intent.createChooser(shareIntent, "send"));

    }
}

