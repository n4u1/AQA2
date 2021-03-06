package com.n4u1.AQA.AQA.views;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.n4u1.AQA.AQA.BuildConfig;
import com.n4u1.AQA.AQA.R;
import com.n4u1.AQA.AQA.util.MarketVersionChecker;

public class TestActivity extends AppCompatActivity {
    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        final EditText editText_appVersion = findViewById(R.id.editText_appVersion);
        final EditText editText_playStoreVersion = findViewById(R.id.editText_playStoreVersion);

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        mFirebaseRemoteConfig.setConfigSettings(configSettings);


        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);

// cacheExpirationSeconds is set to cacheExpiration here, indicating the next fetch request
// will use fetch data from the Remote Config service, rather than cached parameter values,
// if cached parameter values are more than cacheExpiration seconds old.
// See Best Practices in the README for more information.
        mFirebaseRemoteConfig.fetch(0)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            String device_version = null;
                            try {
                                device_version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
                            } catch (PackageManager.NameNotFoundException e) {
                                e.printStackTrace();
                            }
                            // After config data is successfully fetched, it must be activated before newly fetched
                            // values are returned.
                            mFirebaseRemoteConfig.activateFetched();
                            String version = mFirebaseRemoteConfig.getString("aqa_version");
                            editText_playStoreVersion.setText(version);
                            editText_appVersion.setText(device_version);
                        } else {
                            Toast.makeText(TestActivity.this, "Fetch Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });





//        String store_version = MarketVersionChecker.getMarketVersion(getPackageName());
//        String device_version;
//        try {
//            device_version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
//            editText_appVersion.setText(device_version);
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        editText_playStoreVersion.setText(store_version);



    }


}


/*
ToDo
https://stackoverflow.com/questions/24597085/android-how-to-detect-that-the-activity-is-back-from-another-activity
###################################################################################################################################################
공유하기 버튼 구현 필요 / 댓글 or 게시글 삭제 및 수정 기능 필요 / 동영상을 어떻게 하지.... / 비밀번호 변경 / 비밀번호 찾기 / uploadContentsActivity 제목부분 글자 밀림현상
###################################################################################################################################################
친구추가 기능? / 채팅 기능? / 사진 2개일경우 자동으로 단일 투표로 /  투표수에 따른 알람 / SharedPreferences 자동로그인 / 로그인>홈 스플래시 액티비티 / 로그아웃 / 회원 탈퇴
###################################################################################################################################################
광고 삽입 / 툴바에 각 화면 이름넣기 / fileChoiceUploadActivity 프레그먼트 삭제 / 공유 인증 페이지 / userPoint(userClass) 점수 카운팅, 점수 업로딩  / 광고ID
###################################################################################################################################################
개인정보보호처리 / 로그인광고ID / issueContent 로직수정 / 둘러보기 / 투표화면 가독성 높이기 / 로그인과정 한단계씩 하기  /
###################################################################################################################################################
사용자 탈퇴하면 PostAdapter userClass가져올때 uid없어서 null 떨어짐 / 둘러보기(정상)접속시 처음 사용자 안내팝업(투표방법)
###################################################################################################################################################
개인정보보호방침 다시 검토 / 이용약관 다시 검토 / 로그인과정(마지막)에서 개인정보관한사항넣기(체크박스) / AQA소개 / 사진 2개일경우 자동으로 단일 투표로 /
###################################################################################################################################################
딥링크 확인 / Q포인트 처음에 한번 안올라감 /
###################################################################################################################################################
리플달때만 리플수 변경됨 / 검색결과창, 마이업로드에 더보기 기능? / 알람 클릭이동 / 공유하기,공유인증하기
###################################################################################################################################################
mineActivity 에서 이메일 항목 n 글자 이상일경우 ... 표시  / 내가 올린게 아니어도 알람 받고싶을수있자나? /  메일변경하기 / Q포인트 아이콘 변경 /
###################################################################################################################################################
둘러보기 예외처리  /  기기 초기화(guid) /  공지사항 / 자료출처 / 공지사항 / 액티비티 클릭시 디비에 카운팅해서 유저?(액티비티) 프로파일링 해보자
###################################################################################################################################################
게시글 삭제한다음에 오는 액티비티에 삭제된 게시글 남아있음 / 컨텐츠삭제시 스토리지도 삭제 / 더해지다가 1000점 되면?? / 차트색깔.. / 이메일변경 UI 수정 /
###################################################################################################################################################
/ 순위투표선택후 2개 이미지면 토스트를 하나 띄울까? / 광고 추가 / 출시전 CreateUserEmailActivity.java 에서 GUID_CHECK_FLAG 변경필요
###################################################################################################################################################
관리자 페이지 : 이벤트,공지사항업로드,각종조회되도록,런칭후 1년뒤 미사용자 체크후 개인정보 별도 보관필요 /  AQA는... 수정 /
###################################################################################################################################################
###################################################################################################################################################
###################################################################################################################################################
공유인증 이벤트 진행방식? / 마지막에 구글 그..광고 아이디 변경해야함. /
###################################################################################################################################################
공지사항만들기 / 버전 체크 / 지난 이슈 투표 확인  / 통계 세부 결과 초중후반 확인필요
###################################################################################################################################################
런칭전에 할것 : 구글 광고 아이디 변경
###################################################################################################################################################
###################################################################################################################################################
###################################################################################################################################################
AQA 방향성
funny, transparently, confidently
재밌게 투명하게 자신있게1909600000
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

 */