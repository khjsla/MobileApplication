# MobileApplication
Mobile Application projects

  ## 5.all [Final Project]
  ### >> 어플리케이션은 세개의 탭으로 구성됨.
  #### 첫번째 탭은 컨텐츠의 목록
  #### 두번째 탭은 지도
  #### 세번째 탭은 저장한 데이터를 자유롭게 표현해보기
  
  각 탭에는 컨텐츠 추가 버튼이 있음
  컨텐츠 추가 버튼을 누르면 컨텐츠를 추가하는 activity가 실행되고 최소한 다음 4가지를 사용자에게 입력받아서 저장
  ( 마커 위치, 제목, 내용, 사진 )
  사진은 프리뷰말고 Full size이미지를 저장할 것
  사진 소스 선택시, 카메라 및 갤러리(또는 파일)중 선택할 수 있도록 구현할 것
  https://gist.github.com/TiagoGouvea/4739629db5f55d18cd72

  아래 설명을 참고하여 어플리케이션을 제작하시오.
  추가된 컨텐츠를 앱에 영구히 저장하기 위해서 아래 방법중 한가지를 참고하거나 기타 방법을 사용해도 됨

  1) Shared Preference + JSON
  https://developer.android.com/training/data-storage/shared- preferences#java
  https://github.com/google/gson
  2) 자바 객체 입출력 보조 스트림 - ObjectInputStream , ObjectOutputStream
  https://altongmon.tistory.com/276
  3) SQLite
  https://developer.android.com/training/data-storage/sqlite
  https://cocomo.tistory.com/409
  4) Room API
  https://developer.android.com/training/data-storage/room
  https://github.com/googlesamples/android-architecture- components/tree/master/BasicSample

  상세보기 페이지에서는 내용 확인 이외에 다음 두가지 기능을 제공 - 공유하기, 컨텐츠 삭제하기
  
  마커 위치의 위경도값을Geocoder를 이용해서 주소로 변경 후 공유
  https://dexx.tistory.com/78
  카카오링크 위치 템플릿 사용
  https://developers.kakao.com/docs/android/kakaotalk-link#위치-템플릿-보내기
