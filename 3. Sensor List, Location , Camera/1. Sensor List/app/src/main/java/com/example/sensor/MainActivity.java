package com.example.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {


    ListView SensorListView;

    private SensorManager sensorManager;
    private Sensor sensorAcc; //엑셀센서 인자

    private ArrayList SensorList; //알맞게 ListView의 사이즈가 맞춰진다

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensorAcc, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause(){
     super.onPause();
     sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent){
        TextView textView=findViewById(R.id.textView);
        textView.setText(String.valueOf(sensorEvent.values[0])); //x값
        textView =findViewById(R.id.textView2);
        textView.setText(String.valueOf(sensorEvent.values[1])); //y값
        textView =findViewById(R.id.textView3);
        textView.setText(String.valueOf(sensorEvent.values[2])); //z값
    }

    @Override
    public void onAccuracyChanged(Sensor sensor,int i){
        //?
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //디폴트

        SensorListView=(ListView)findViewById(R.id.sensors);

        sensorManager=(SensorManager)this.getSystemService(SENSOR_SERVICE); //센서매니저 인스턴스(인자)형서

        SensorList=new ArrayList(); //센서의 리스트 가져오기
        List<Sensor> sensors=sensorManager.getSensorList(Sensor.TYPE_ALL);

        for (Sensor s:sensors){ //센서들 이름가져오기
            SensorList.add(s.getName()); //getName함수를 이용해서.
        }

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,SensorList);
        SensorListView.setAdapter(adapter); //adapter 세팅 _ 안드로이드 제공 어답터 사용, simple list item 은 한 텍스트뷰로 구성된 레이아웃 형태를 말한다

        //list누르면 일어나게되는 상황
        SensorListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) { //클릭하게 되면 일어나는 상황//디폴트 센서를
                    sensorAcc = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            }
        });

        if(sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!=null){
            sensorAcc=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }

    }
}
