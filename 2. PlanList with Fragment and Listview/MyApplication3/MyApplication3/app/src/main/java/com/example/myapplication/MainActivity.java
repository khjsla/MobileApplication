package com.example.myapplication;


import android.support.v4.app.FragmentTransaction;//새로추가
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

//implements로 MainActivity에 List_Fragment를 연결합니다.
//Listner 콜백 인터페이스를 구현(implement)합니다.
//여기에 OnItemClicked 매서드를 재정의 합니다.
public class MainActivity extends AppCompatActivity implements List_Fragment.Listner { //메인 액티비티는 AppCompatActivity를 상속받습니다.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void itemClicked(long id) { //FragmentTransaction 사용할것
        ContentFragment details = new ContentFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();  //지원되는 라이브러리에서 프래그먼트를 처리하는 관리자를 반환(getSupport..) //트랜스액션시작
        details.setContentId(id); //여기서 아이디 설정
        fragmentTransaction.replace(R.id.fragment_container, details); //내용 바꿔줍니다 (replace) 갱신
        fragmentTransaction.addToBackStack(null); //트랜스액션을 백스택에 추가. 대부분의 경우가 null
        fragmentTransaction.commit(); //바뀐것 적용되고, 트랜스액션 끝
    }
}
