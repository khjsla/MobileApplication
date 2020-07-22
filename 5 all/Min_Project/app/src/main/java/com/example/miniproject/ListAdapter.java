package com.example.miniproject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends BaseAdapter { //리스트뷰를 사용하기위해선 어댑터가 필요하다

    private Context context; //Content를 Content Add로 바꾸ㅁ 왜하는건지 알아야한다 //Context를 받아와서 BaseAdapter의 함수들에 사용해야한다.
    public static List<Data> datas; //listAdapter 대한 데이터소스

    public  ListAdapter( Activity _context, List<Data> _datas){ //Activity를 Content로 바꿈 //Activity _context, ArrayList<Data> _datas 를 그냥 지ㅜ어줌
        context = _context;
        datas = _datas;
    }

    @Override
    public int getCount() { //몇개 출력해야하는지 알게 해줌
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) { //뷰가 보여주게된다
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_form,parent,false);
        } //넣을때 내가 지정한 listView Form 에 넣어준다

        TextView textView = convertView.findViewById(R.id.title_content); ////main xml에 listview 넣고 그의 아이디
        TextView textView1 = convertView.findViewById(R.id.snippet_content);
        ImageView imageView = convertView.findViewById(R.id.imageView);

        textView.setText(datas.get(position).getMapTitle());
        textView1.setText(datas.get(position).getMapSnippet());
        //imageView.setImageURI(datas.get(position).getMapPicture());

        return convertView;
    }  //저번 수업시간 내용그대로


}
