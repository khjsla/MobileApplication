package com.example.miniproject;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;


public class ThirdMyFragment extends Fragment implements View.OnClickListener {  //갤러리뷰로 사용하고싶다

    ListAdapter adapter;
    GridView _gridView;
    //내용이 보여지게 되는

    ThirdListener thirdListener; //리스너로 메인에서 값 받아옴

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_third_my, container, false);

        _gridView = view.findViewById(R.id.gridview); //내가 앞서 선언한 그리드뷰랑이어준다 이제 이 그리드 뷰에 사진을 넣어줄거임

        _gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() { //그리드 뷰 눌리면 일어나는일
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        return view;
    }

    public interface ThirdListener{ //activity로 position보내줄라구
        void GridClicked(int i);
    }

    @Override
    public void onAttach(Context context) { //detail activity로 position보내줄라구
        super.onAttach(context);

        Activity activity = (Activity)context; //?

        try{
            thirdListener=(ThirdListener) activity;
        }
        catch(ClassCastException e){
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }



    @Override
    public void onClick(View v) {

    }
}