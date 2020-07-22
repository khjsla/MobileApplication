package com.example.myapplication;


import android.content.Context;
import android.os.Bundle; //@Overrid onCreateView사용
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter; //ArrayAdapter 사용
import android.widget.ListView;

/**
 * A simple {@link ListFragment} subclass.
 */
public class List_Fragment extends ListFragment { //Frag 가 아니라 ListFrag를 상속받았습니다. 다양한 이점 때문에 이를 선택하였고, 이는 보고서에 상세 설명 하도록 하겠습니다.

    static interface Listner{ //listner의 인터페이스 호출
        void itemClicked(long id); //Listner의 인터페이스를 이루는 모든 액션은 이걸 포함해야합니다. 그래야 응답가능
    }

    private Listner listner; //ListFrag를 액티비티에 연결할때 필요한 변수 추가

    public List_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        String[] titles=new String[Page.pages.length];
        for(int i=0;i<titles.length;i++){
            titles[i]=Page.pages[i].getTitle();
        } // 주차내용을 저장하는 for문 형성
        ArrayAdapter<String> adapter = new ArrayAdapter<>(inflater.getContext(),android.R.layout.simple_list_item_1,titles); //배열어댑터를 만들고, layout 인플레이터에서 Context얻는다
        setListAdapter(adapter); //정리된 어댑터를 리스트뷰에 연결
        //목록포함
        return super.onCreateView(inflater,container,savedInstanceState); //상위클래스의 onCreatView()가 실행되면, ListFrag의 기본 레이아웃 반환
    }
//onAttach 는 프래그먼트 생명주기에서 가장먼저 호출되는 콜백 매서드 입니다.
//프래그먼트에서 액티비티를 조작하려면 콜백을 사용합니다
    @Override
    public void onAttach(Context context) { //프래그먼트연결 하는 컨텍스트
        super.onAttach(context);
        this.listner=(Listner)context;
    }
//List를 클릭하면 일어나는 부분구현, 콜백매서드가되는 onListItemClick을 재정의 합니다.
//이는 ListFragment가 제공하는
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id); //리스트뷰에서 클릭된 것
        if(listner != null){
            listner.itemClicked(id); //사용자선택 ID를 전해 itemClicked사용  //클릭되면 Listener에 알림
        }
    }
}
