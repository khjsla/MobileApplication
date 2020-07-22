package com.example.miniproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

//getActivity로 받자
public class FirstListFragment extends Fragment implements View.OnClickListener{ //우선 여기의 list 라는 listview 아이템에 ContentAdd를 통해 저장된 Data 정보를 가져와야 한다.

    ListAdapter adapter;
    ListView mapListView;
    ArrayList<Data> dataArrayList;

    FirstListener firstListener; //detail activity로 position보내줄라구


      public interface FirstListener{ //activity로 position보내줄라구
        void ListClicked(int i);
    }


    @Override
    public void onAttach(Context context) { //detail activity로 position보내줄라구
        super.onAttach(context);

        Activity activity = (Activity)context; //?

        try{
            firstListener=(FirstListener) activity;
        }
        catch(ClassCastException e){
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_first_list, container, false);
        mapListView = view.findViewById(R.id.list);

        //리스트뷰가 눌리게 되면 일어나는 상황 아래에 구현
        mapListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //detail 화면으로 옮겨지게 되어야한다
                int _position = position; //변수 선언
                firstListener.ListClicked(_position); //activity로 position보내준다.
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        dataArrayList = (ArrayList<Data>)((MainActivity)getActivity()).getData(); //mainActivity의 매소드 써준다
        adapter = new ListAdapter(getActivity(),dataArrayList);

        mapListView.setAdapter(adapter);
        adapter.notifyDataSetChanged(); //이거 뺴도 되면 뺴
    }

    @Override //implements View.onClickcListener랑 이거 꼭있어야 함>?? //ㅇㅇ 있어야함
    public void onClick(View v) {

    }
}
