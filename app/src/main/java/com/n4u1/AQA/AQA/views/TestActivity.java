package com.n4u1.AQA.AQA.views;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.n4u1.AQA.AQA.R;
import com.github.chrisbanes.photoview.PhotoView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TestActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);



    }


}


/*
ToDo
###################################################################################################################################################
공유하기 버튼 구현 필요 / 댓글 or 게시글 삭제 및 수정 기능 필요 / 동영상을 어떻게 하지.... / 비밀번호 변경 / 비밀번호 찾기 / 개인정보보호처리
###################################################################################################################################################
친구추가 기능? / 채팅 기능? / 사진 2개일경우 자동으로 단일 투표로 / 리플달때만 리플수 변경됨 / SharedPreferences 자동로그인 / 투표수에 따른 알람 /
###################################################################################################################################################
mineActivity 에서 이메일 항목 n 글자 이상일경우 ... 표시
###################################################################################################################################################
내정보에서 올린거 참여한거 좋아요 보여주는 리사이클러뷰 변경필요
###################################################################################################################################################
AQA 방향성
funny, transparently, confidently
재밌게 투명하게 자신있게


funny, trustly, purely
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

앱 최초 실행시 권한 요청하는 다이얼로그? 업로드할때 요청하는걸로 바꿔야함

###################################################################################################################################################

팔로우, 공유 버튼 필요

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


http://iw90.tistory.com/155
http://twinw.tistory.com/50



 */