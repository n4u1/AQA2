package com.n4u1.AQA.AQA.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.n4u1.AQA.AQA.R;

public class TestActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


    }

}


/*
ToDo
뒤로가기에서 온건지 확인ㅋㅋㅋ
https://stackoverflow.com/questions/24597085/android-how-to-detect-that-the-activity-is-back-from-another-activity
###################################################################################################################################################
공유하기 버튼 구현 필요 / 댓글 or 게시글 삭제 및 수정 기능 필요 / 동영상을 어떻게 하지.... / 비밀번호 변경 / 비밀번호 찾기 / uploadContentsActivity 제목부분 글자 밀림현상
###################################################################################################################################################
친구추가 기능? / 채팅 기능? / 사진 2개일경우 자동으로 단일 투표로 /  투표수에 따른 알람 / SharedPreferences 자동로그인 / 로그인>홈 스플래시 액티비티 / 로그아웃 / 회원 탈퇴
###################################################################################################################################################
광고 삽입 / 툴바에 각 화면 이름넣기 / fileChoiceUploadActivity 프레그먼트 삭제 / 공유 인증 페이지 / userPoint(userClass) 점수 카운팅, 점수 업로딩  / 광고ID
###################################################################################################################################################
개인정보보호처리 / 로그인광고ID / issueContent 로직수정
###################################################################################################################################################
###################################################################################################################################################
###################################################################################################################################################
###################################################################################################################################################
사진 2개일경우 자동으로 단일 투표로 / 리플달때만 리플수 변경됨 / 검색결과창, 마이업로드에 더보기 기능? / 알람 클릭이동
###################################################################################################################################################
mineActivity 에서 이메일 항목 n 글자 이상일경우 ... 표시  / 내가 올린게 아니어도 알람 받고싶을수있자나? / 액티비티 클릭시 디비에 카운팅해서 유저?(액티비티) 프로파일링 해보자
###################################################################################################################################################
/  딥링크 확인 / 자료출처 / 둘러보기 / 공지사항, AQA..., /  시간남으면 조회 /
###################################################################################################################################################
###################################################################################################################################################
###################################################################################################################################################
###################################################################################################################################################
둘러보기 / 로그인과정에서 개인정보관한사항넣기 / 로그인과정 한단계씩 하기 / 투표화면 가독성 높이기 / 개인정보보호방침 다시 검토 / 이용약관 다시 검토
###################################################################################################################################################

###################################################################################################################################################
###################################################################################################################################################
###################################################################################################################################################
###################################################################################################################################################
###################################################################################################################################################
관리자 페이지 : 이벤트,공지사항업로드,각종조회되도록,
###################################################################################################################################################
AQA 방향성
funny, transparently, confidently
재밌게 투명하게 자신있게
funny, trustly, purely
Funny Trustly Purely
###################################################################################################################################################
☆ 개발자한테 물어볼것
- 삭제후에 리플레시 되게 하는것
- 알람 클릭했을때 PollSingle or PollRanking 으로 이동되게 하는것
- implementation 'com.android.support:appcompat-v7:28.0.0' 이거 빨간줄..
- 안드로이드 질문 사이트..? 영어가 안되면..
- 크몽??
- 업데이트관리 및..... 공지사항이나...간단한 텍스트 변경하려면
###################################################################################################################################################
AQA 계획
###################################################################################################################################################
User Class
0~49	red_1
50~99	red_2
100~149	orange_1
150~199	orange_2
200~249	yellow_1
250~299	yellow_2
300~349	green_1
350~399	green_2
400~449	blue_1
450~500	blue_2

###################################################################################################################################################

to
String userDevice

from
userDevice = android.provider.Settings.Secure.getString(getBaseContext().getContentResolver(), Settings.Secure.ANDROID_ID);

why
사용자 디바이스 번호를 가져와서 디비에 저장하는데
만일 사용자가 핸드폰을 중고로 팔았을경우
구매자가 해당어플을 이용할경우에 대한 예외처리 및......
-> 아마 디바이스 번호로 고유성을 체크하지 않고 통신사 본인확인으로 해야할듯. (나이스 신용, 다날 등.. 같은것으로..)

###################################################################################################################################################

to
RegisterActivity.java

from
전체

why
기존에 저장되어있는 userDevice 라도 또 입력하면 덮어씌워지지 않음 ( 사실 예외처리는 필요없을것 같기는 하지만... )

###################################################################################################################################################

앱 최초 실행시 권한 요청하는 다이얼로그? 업로드할때 요청하는걸로 바꿔야함 - 이렇게하면 제목부분 editText가 밀림현상이 일어남..

###################################################################################################################################################

팔로우, 공유 버튼 필요 > 완료

###################################################################################################################################################

Activity Stack clear
Intent.FLAG_ACTIVITY_CLEAR_TOP

Intent intent = new Intent(getApplicationContext(),FlagTestActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);

###################################################################################################################################################

http://gogorchg.tistory.com/entry/Android-%EC%A7%80%EB%82%98%EC%B3%90-%EC%98%A8-Activity-%EC%A0%9C%EA%B1%B0-%EB%B0%A9%EB%B2%95

출처: http://gogorchg.tistory.com/entry/Android-지나쳐-온-Activity-제거-방법 [항상 초심으로]

################################################################################################################################################### 완료

댓글 DB구조를...

users/uid/reply/
user_contents/reply/
reply


###################################################################################################################################################

VideoFragment에서 back버튼누를때 호출됨
이때 비디오 올라간 리소스 제거 해줘야함
08-27 13:49:55.703 19131-19131/com.example.n4u1.test130 D/lkjFragment onPause: onPause
08-27 13:49:56.051 19131-19131/com.example.n4u1.test130 D/lkjFragment onStop: onStop
08-27 13:49:56.105 19131-19131/com.example.n4u1.test130 D/lkjFragment onDestroy: onDestroy


###################################################################################################################################################

FAQ 수정은 왜 없나요?
    동영상은 왜 업로드가 안되나요?
    사진을 꼭 업로드 해야 하나요?
    연동로그인 기능은 안하나요?
    순위투표의 점수 계산방식은 어떻게 되나요?
    중복투표는 어떻게? 일단은 1디바이스1투표 광고ID, 차후 본인인증절차를 걸쳐 1투표

###################################################################################################################################################
위너 김진우
엑소 세훈
하이라이트 용준형
비투비 육성재
엑소 찬열
위너원 황민현
위너원 강다니엘
방탄 지민
세븐틴 민규
빅뱅 약
방탄 정국
인피니티 엘
아스트로 차은우
방탄 뷔

###################################################################################################################################################


http://iw90.tistory.com/155
http://twinw.tistory.com/50



 */