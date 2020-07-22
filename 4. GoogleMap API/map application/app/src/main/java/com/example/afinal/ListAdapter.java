package com.example.afinal;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends BaseAdapter { //리스트뷰를 사용하기위해선 어댑터가 필요하다

    private Context context; //왜하는건지 알아야한다 //Context를 받아와서 BaseAdapter의 함수들에 사용해야한다.
    private ArrayList<Map> maps = null; //listAdapter 대한 데이터소스

    public  ListAdapter(Activity _context, ArrayList<Map> _maps ){
        context = _context;
        maps=_maps;
    }

    @Override
    public int getCount() { //몇개 출력해야하는지 알게 해줌
        return maps.size();
    }

    @Override
    public Object getItem(int position) {
        return maps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) { //뷰가 보여주게된다
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_item,parent,false);
        }
        TextView textView = convertView.findViewById(R.id.mapTitle);
        TextView textView1 = convertView.findViewById(R.id.mapSnippet);

        textView.setText(maps.get(position).getMapTitle());
        textView1.setText(maps.get(position).getMapSnippet());

        return convertView;
    }
}
