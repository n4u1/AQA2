package com.n4u1.AQA.AQA.splash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.n4u1.AQA.AQA.R;
import com.n4u1.AQA.AQA.dialog.DeviceInitFailDialog;
import com.n4u1.AQA.AQA.dialog.GUIDFailDialog;
import com.n4u1.AQA.AQA.dialog.GUIDInitDialog;
import com.n4u1.AQA.AQA.dialog.LiveIdDialog;
import com.n4u1.AQA.AQA.dialog.NullEmailDialog;
import com.n4u1.AQA.AQA.views.HomeActivity;
import com.n4u1.AQA.AQA.views.LoginActivity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class SplashLoadingActivity extends AppCompatActivity
        implements GUIDFailDialog.GUIDFailDialogListener, GUIDInitDialog.GUIDInitDialogListener
, DeviceInitFailDialog.DeviceInitFailDialogListener {

    String email, password;
    FirebaseAuth mAuth;
    private String guid;
    private String userId = "nullId";
    private String uIdTemp = "nullIdTemp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_loading);

        mAuth = FirebaseAuth.getInstance();

        email = getIntent().getStringExtra("id");
        password = getIntent().getStringExtra("pw");


        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            try {
                                Log.d("lkj current uid", mAuth.getCurrentUser().getUid());
                                GUIDAsyncTask guidAsyncTask = new GUIDAsyncTask();
                                guidAsyncTask.execute();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

//                            Intent intent = new Intent(LoginActivity.this, SplashLoadingActivity.class);
//                            startActivity(intent);
//                            finish();
                        } else {
                            if (task.getException().toString().contains("There is no user record corresponding to this identifier")) {
//                                NullEmailDialog nullEmailDialog = new NullEmailDialog();
//                                nullEmailDialog.show(getSupportFragmentManager(), "nullEmailDialog");
                                Handler hd = new Handler();
                                hd.postDelayed(new splashhandlerLogin(), 100);
                                Toast.makeText(getApplicationContext(), "가입되어있는 이메일이 없습니다.", Toast.LENGTH_LONG).show();
                            } else {
                                Handler hd = new Handler();
                                hd.postDelayed(new splashhandlerLogin(), 100);
                                Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_LONG).show();
                            }


                        }
                    }
                });


    }


    /*
    onCreate();
     */


    private class GUIDAsyncTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            AdvertisingIdClient.Info idInfo = null;
            try {
                idInfo = AdvertisingIdClient.getAdvertisingIdInfo(getApplicationContext());
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            } catch (GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String advertId = null;
            try {
                advertId = idInfo.getId();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            Log.d("lkj advertId1", advertId);
            guid = advertId;
            return advertId;
        }

        @Override
        protected void onPostExecute(final String advertId) {
            Log.d("lkj advertId2", advertId);
            if (advertId != null && advertId.trim().isEmpty()) {
                Toast toast = Toast.makeText(getApplicationContext(), "구글 광고ID를 찾을 수 없습니다.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
                toast.show();
            } else {
                Log.d("lkj test1", "test1");
                DatabaseReference mDatabaseRef;
                mDatabaseRef = FirebaseDatabase.getInstance().getReference();
                mDatabaseRef.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.d("lkj test2", "test2");

                        int adIdFlag_1 = 0;
                        int adIdFlag_2 = 0;
                        Iterator<DataSnapshot> usersIterator = dataSnapshot.getChildren().iterator();
                        while (usersIterator.hasNext()) {
                            Log.d("lkj test3", "test3");
                            Map<String, Object> user = (Map<String, Object>) usersIterator.next().getValue();
                            if (advertId.equals(user.get("guid"))) { // 현재 구글ID가 디비에 있는지?
                                uIdTemp = String.valueOf(user.get("uid"));
                                userId = String.valueOf(user.get("userId"));
                                adIdFlag_1++;
                                Log.d("lkj user id1", String.valueOf(user.get("uid")));
                                Log.d("lkj user id2", mAuth.getCurrentUser().getUid());
                                if (String.valueOf(user.get("uid")).equals(mAuth.getCurrentUser().getUid())) {
                                    Log.d("lkj user id3", "????????????????");
                                    adIdFlag_1--;
                                    adIdFlag_2++;
                                }
                                Log.d("lkj adIdFlag", String.valueOf(adIdFlag_1));
                            }
                            if (String.valueOf(user.get("uid")).equals(mAuth.getCurrentUser().getUid())) { //현재로그인한 계정이 디비에 있는지?
                                adIdFlag_2++;
                            }
                        }
                        /*
                        adIdFlag_1	adIdFlag_2	결과
                            0       	2		정상 로그인 :
                            0       	0		이럴경우는 없겠지?
                            1       	0		구글ID가 디비에는 있는데 현재 로그인한 계정과 맞지 않을경우 : 다른 이메일로 로그인 시도
                            0       	1		처음 사용하는 다른 기기에서 기존 이메일로 로그인 시도는? : 다른 기기에서 로그인 시도 2
                            1           1       기존 사용했던 기록이 있는 기기에서 기존 이메일로 로그인시도는? : 다른 기기에서 로그인 시도 1
                            2	        1		구글아이디가 2개인데 현재 로그인하려는 계정과 연결 되어있지 않음
                            3       	1		구글아이디가 3개인데 현재 로그인하려는 계정과 연결 되어있지 않음
                            4       	1		구글아이디가 4개인데 현재 로그인하려는 계정과 연결 되어있지 않음
                         */

                        //확인한후에 로직
                        //정상 로그인
                        if (adIdFlag_1 == 0 && adIdFlag_2 == 2) {
                            SharedPreferences pref = getSharedPreferences("com.n4u1.AQA", MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("com.n4u1.AQA.fireBaseUserEmail", email);
                            editor.putString("com.n4u1.AQA.fireBaseUserPassword", password);
                            editor.commit();
                            Handler hd = new Handler();
                            hd.postDelayed(new splashhandlerHome(), 100);
                            finish();

                        }
                        //다른 이메일로 로그인 시도
                        else if (adIdFlag_1 == 1 && adIdFlag_2 == 0) {
                            //현재의 guid가 DB상에 존재할경우 초기화 진행
                            GUIDFailDialog guidFailDialog = new GUIDFailDialog();
                            guidFailDialog.show(getSupportFragmentManager(), "guidFailDialog");

                        }
                        //다른 기기에서 로그인 시도 1
                        else if (adIdFlag_1 == 1 && adIdFlag_2 == 1) {
                            GUIDInitDialog guidInitDialog = new GUIDInitDialog();
                            guidInitDialog.show(getSupportFragmentManager(), "guidInitDialog");
                        }
                        //다른 기기에서 로그인 시도 2
                        else if (adIdFlag_1 == 0 && adIdFlag_2 == 1) {
                            GUIDInitDialog guidInitDialog = new GUIDInitDialog();
                            guidInitDialog.show(getSupportFragmentManager(), "guidInitDialog");


                        } else if (adIdFlag_1 == 5 && adIdFlag_2 == 1) {
                            GUIDInitDialog guidInitDialog = new GUIDInitDialog();
                            guidInitDialog.show(getSupportFragmentManager(), "guidInitDialog");


                        }
                        //adminAuth에 포함되어있는 아이디일 경우 로그인 (관리자 및 특정 계정)
                        else {
                            DatabaseReference mDatabaseRefAdmin;
                            mDatabaseRefAdmin = FirebaseDatabase.getInstance().getReference();
                            mDatabaseRefAdmin.child("adminAuth").child("1").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (int i = 0; i < dataSnapshot.getChildrenCount(); i++) {
                                        Map<String, Boolean> adminMap = (Map<String, Boolean>) dataSnapshot.getValue();
//                                        Log.d("lkj adminId", adminMap.keySet().toString());
//                                        Log.d("lkj current userId", userId);
                                        if (adminMap.keySet().toString().contains(userId)) {
                                            SharedPreferences pref = getSharedPreferences("com.n4u1.AQA", MODE_PRIVATE);
                                            SharedPreferences.Editor editor = pref.edit();
                                            editor.putString("com.n4u1.AQA.fireBaseUserEmail", email);
                                            editor.putString("com.n4u1.AQA.fireBaseUserPassword", password);
                                            editor.commit();
                                            Handler hd = new Handler();
                                            hd.postDelayed(new splashhandlerHome(), 100);
                                            finish();
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            //이도 저도 아니면 로그인 실패
                            Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_LONG).show();
                            finish();
                        }
//                        Log.d("lkj flag1", String.valueOf(adIdFlag_1));
//                        Log.d("lkj flag2", String.valueOf(adIdFlag_2));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }
    }

    //다른 이메일로 로그인 시도
    @Override
    public void GUIDFailDialogCallback(String string) {
        if (string.equals("확인")) {
            Log.d("lkj lastTest111111", "lastTest11111111");
            final DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference();

            //초기화를 위해서 기존에 있던건 null로 넣어주고 지금 로그인한 게정에 guid를 넣어줌
            mDatabaseRef.child("users").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Map<String, Object> userMap = (Map<String, Object>) dataSnapshot.getValue();
                    long lastDate = Long.parseLong(String.valueOf(userMap.get("lastGuidDate")));
                    long currentDate = getCurrentDate();
//                    Log.d("lkj lastDate", String.valueOf(lastDate));
//                    Log.d("lkj currentDate", String.valueOf(currentDate));
                    if (lastDate + 86400000 < currentDate) {

                        DatabaseReference tmpRef = FirebaseDatabase.getInstance().getReference();
                        tmpRef.child("users").child(mAuth.getCurrentUser().getUid()).child("guid").setValue(guid);
                        tmpRef.child("users").child(mAuth.getCurrentUser().getUid()).child("lastGuidDate").setValue(currentDate);
                        tmpRef.child("users").child(mAuth.getCurrentUser().getUid()).child("lastGuidDateKr").setValue(getDate());
                        tmpRef.child("users").child(uIdTemp).child("guid").setValue("");


                        SharedPreferences pref = getSharedPreferences("com.n4u1.AQA", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("com.n4u1.AQA.fireBaseUserEmail", email);
                        editor.putString("com.n4u1.AQA.fireBaseUserPassword", password);
                        editor.commit();

                        Handler hd = new Handler();
                        hd.postDelayed(new splashhandlerHome(), 100);

                    } else {
                        long mustDate = 86400000 - (currentDate - lastDate);
                        String mustTime = changeDate(mustDate);
//                        Log.d("lkj must date", String.valueOf(mustDate));
//                        Log.d("lkj must date", mustTime);
                        DeviceInitFailDialog deviceInitFailDialog = DeviceInitFailDialog.newInstance(mustTime);
                        deviceInitFailDialog.show(getSupportFragmentManager(), "deviceInitFailDialog");
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } else if (string.equals("취소")) {
            finish();
        }
    }


    //다른 기기에서 로그인 시도
    @Override
    public void GUIDInitDialogCallback(String string) {
        if (string.equals("확인")) {
            Log.d("lkj lastTest22222", "lastTest222222");
            final DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference();

            //초기화를 위해서 현재 로그인한 계정의 guid에 현재의 guid로 변경
            mDatabaseRef.child("users").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Map<String, Object> userMap = (Map<String, Object>) dataSnapshot.getValue();
                    long lastDate = Long.parseLong(String.valueOf(userMap.get("lastGuidDate")));
                    long currentDate = getCurrentDate();
//                    Log.d("lkj lastDate", String.valueOf(lastDate));
//                    Log.d("lkj currentDate", String.valueOf(currentDate));
                    if (lastDate + 86400000 < currentDate) {


                        DatabaseReference tmpRef = FirebaseDatabase.getInstance().getReference();
                        tmpRef.child("users").child(mAuth.getCurrentUser().getUid()).child("guid").setValue(guid);
                        tmpRef.child("users").child(mAuth.getCurrentUser().getUid()).child("lastGuidDate").setValue(currentDate);
                        tmpRef.child("users").child(mAuth.getCurrentUser().getUid()).child("lastGuidDateKr").setValue(getDate());

                        SharedPreferences pref = getSharedPreferences("com.n4u1.AQA", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("com.n4u1.AQA.fireBaseUserEmail", email);
                        editor.putString("com.n4u1.AQA.fireBaseUserPassword", password);
                        editor.commit();

                        Handler hd = new Handler();
                        hd.postDelayed(new splashhandlerHome(), 100);
                    } else {
                        long mustDate = 86400000 - (currentDate - lastDate);
                        String mustTime = changeDate(mustDate);
//                        Log.d("lkj must date", String.valueOf(mustDate));
//                        Log.d("lkj must date", mustTime);

                        DeviceInitFailDialog deviceInitFailDialog = DeviceInitFailDialog.newInstance(mustTime);
                        deviceInitFailDialog.show(getSupportFragmentManager(), "deviceInitFailDialog");

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } else if (string.equals("취소")) {
            finish();
        }
    }


    @Override
    public void DeviceInitFailDialogCallback(String string) {
        if (string.equals("확인")) {
            finish();
        }
    }

    private String changeDate(long mustDate) {
        int num = Integer.parseInt(String.valueOf(mustDate)) / 1000;
        int hours, minute, second;
        //시간공식
        hours = num / 3600;//시 공식
        minute = num % 3600 / 60;//분을 구하기위해서 입력되고 남은값에서 또 60을 나눈다.
        second = num % 3600 % 60;//마지막 남은 시간에서 분을 뺀 나머지 시간을 초로 계산함
        String s = hours + "시간 " + minute + "분 " + second + "초";

        return s;
    }


    private long getCurrentDate() {
        long currentTimeMillis = System.currentTimeMillis();
        return currentTimeMillis;
    }


    public String getDate() {
        TimeZone timeZone;
        timeZone = TimeZone.getTimeZone("Asia/Seoul");
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd(E)HH:mm:ss", Locale.KOREAN);
        df.setTimeZone(timeZone);
        String currentDate = df.format(date);
        return currentDate;
    }


    private class splashhandlerLogin implements Runnable {
        public void run() {
            Intent intent = new Intent(SplashLoadingActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            startActivity(intent);
        }

    }


    private class splashhandlerHome implements Runnable {
        public void run() {
            Intent intent = new Intent(SplashLoadingActivity.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            startActivity(intent);

        }
    }
}
