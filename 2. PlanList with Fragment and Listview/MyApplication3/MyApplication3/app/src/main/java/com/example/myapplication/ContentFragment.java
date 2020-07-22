package com.example.myapplication;


import android.os.Bundle; //onCreateView를 사용
import android.support.v4.app.Fragment; //extends Fragment 사용
import android.view.LayoutInflater;
import android.view.View; //onCreateView를 사용
import android.view.ViewGroup; //onCreateView를 사용
import android.widget.TextView; //onCreateView를 사용


/**A simple {@link Fragment} subclass.*/
public class ContentFragment extends Fragment { //Fragment를 상속받습니다
    private long contentId; //사용자 선택 주차내용ID 이를 이용해서 프래그먼트 뷰에 상세내용 설정

    public ContentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) { //onCreateView : 매서드 : 프래그먼트를 만들떄 안드로이드가 필요로하는 호출코드
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_content, container, false); //프래그먼트가 어떤 layout(:fragment_content)을 사용하는 지 return 해줍니다.
    }

    public void setContentId(long id){ //주차내용ID 설정하는 세터매서드 . MainActivity에서 이걸로 주차내용ID설정
        this.contentId=id;
    }

   @Override
    public void onStart() {
        super.onStart();
        View view = getView(); //getView() : Fragment 의 RootView를 반환, 이걸로 주차내용 뷰의 Reference를 얻을 수 있어요
        if (view != null){
            TextView menu = (TextView) view.findViewById(R.id.textTitle);
            Page page=Page.pages[(int)contentId];
            menu.setText(page.getTitle());
            TextView content=(TextView)view.findViewById(R.id.textContent);
            content.setText(page.getContent());
        }
    }

}
