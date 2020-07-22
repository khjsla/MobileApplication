package com.example.myapplication;

public class Page{ //여기에 주차와, 주차설명내용을 저장 할 것입니다.
//주차와, 상세설명으로 이루어집니다.
    private String title;
    private String content;

    public static final Page[] pages={ //네개의 배열을 저장합니다
            new Page("1주차","1주차에는 안드로이드에 대해서 소개합니다."),new Page("2주차","2주차에는 안드로이드 UI에 대해서 학습합니다."),new Page("3주차","3주차에는 안드로이드 센서 프레임워크에 대해서 학습합니다."),new Page("4주차","4주차에는 지도와 오픈API에 대해서 학습합니다.")
    };

    private Page(String title,String content){
        this.title=title;
        this.content=content;
    }
//private 변수는 get으로 가져옵니다.
    public String getTitle() {
        return title;
    }

    public String getContent(){
        return content;
    }
}
