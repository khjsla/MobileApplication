package com.example.afinal;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.model.LatLng;

public class CustomDialog extends Dialog {

    private EditText _title;
    private EditText _snippet;
    private Button _okButton;
    private Button _cancelButton; //변수선언합니다 그래서 layout에서 값 저장해줍니다.

    private OnDismissListener mDismissListener; //다이얼로그 종료시 ondismiss작업을 수행할 수 있도록 할거에요
    private LatLng _latLng;

    public CustomDialog(Context context){
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_layout); //사용하는 레이아웃은 제가 만든 다이얼로그 레이아웃입니다.

        _title = (EditText) findViewById(R.id.title);
        _snippet =(EditText) findViewById(R.id.snippet);
        _okButton = (Button) findViewById(R.id.okButton);
        _cancelButton = (Button) findViewById(R.id.cancelButton); //앞서 지정한 변수안에 값을 저장해줍니다

        _okButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                mDismissListener.onDismiss(CustomDialog.this); //다이얼로그 종료되면서 onDismss 실행
                dismiss();
            }
        });

        _cancelButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                cancel();
            }
        });
    }
    public void setOnDismissListener(OnDismissListener listener){ //다이얼로그 종료시 작업수행
        mDismissListener = listener;
    }

    public String getmapTitle(){
        return _title.getText().toString();
    }
    public String getmapSnippet(){
        return _snippet.getText().toString();
    }
    public void setmapLatLng(LatLng latLng){
        _latLng = latLng;
    }
    public LatLng getmapLatLng(){
        return _latLng;
    }

}
