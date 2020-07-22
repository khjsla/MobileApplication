package com.example.miniproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Camera extends AppCompatActivity {
    private Button take;
    private Button choose;

    private static final int PICK_FROM_CAMERA = 44;
    private static final int PICK_FROM_ALBUM = 45;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        take =(Button)findViewById(R.id.takeButton);
        choose =(Button)findViewById(R.id.chooseButton);

        take.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(PICK_FROM_CAMERA); //사진찍는거는 REQUEST CODE 44_RESULT CODE
                finish();
            }
        });

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(PICK_FROM_ALBUM); //사진고르는거는 REQUEST CODE 45 _REQUEST CODE
                finish();
            }
        });

    }
}
