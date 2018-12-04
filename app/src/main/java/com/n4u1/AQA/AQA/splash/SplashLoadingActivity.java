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
import com.n4u1.AQA.AQA.dialog.GUIDFailDialog;
import com.n4u1.AQA.AQA.dialog.GUIDInitDialog;
import com.n4u1.AQA.AQA.dialog.LiveIdDialog;
import com.n4u1.AQA.AQA.dialog.NullEmailDialog;
import com.n4u1.AQA.AQA.views.HomeActivity;
import com.n4u1.AQA.AQA.views.LoginActivity;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class SplashLoadingActivity extends AppCompatActivity
        implements GUIDFailDialog.GUIDFailDialogListener, GUIDInitDialog.GUIDInitDialogListener {

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
                                adIdFlag_1++;
//                                Log.d("lkj user ad id", String.valueOf(user.get("userId")));
                                if (String.valueOf(user.get("uid")).equals(mAuth.getCurrentUser().getUid())) {
                                    userId = String.valueOf(user.get("userId"));
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
                            0       	1		처음 사용하는 다른 기기에서 기존 이메일로 로그인 시도는? : 다른 기기에서 로그인시도
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
                        //다른 기기에서 로그인 시도
                        else if (adIdFlag_1 == 0 && adIdFlag_2 == 1) {
                            GUIDInitDialog guidInitDialog = new GUIDInitDialog();
                            guidInitDialog.show(getSupportFragmentManager(), "guidInitDialog");

                        }
                        //adminAuth에 포함되어있는 아이디일 경우 로그인
                        else {
                            DatabaseReference mDatabaseRefAdmin;
                            mDatabaseRefAdmin = FirebaseDatabase.getInstance().getReference();
                            mDatabaseRefAdmin.child("adminAuth").child("1").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (int i = 0; i < dataSnapshot.getChildrenCount(); i++) {
                                        Map<String, Boolean> adminMap = (Map<String, Boolean>) dataSnapshot.getValue();
                                        Log.d("lkj adminId", adminMap.keySet().toString());
                                        Log.d("lkj current userId", userId);
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
                        Log.d("lkj flag1", String.valueOf(adIdFlag_1));
                        Log.d("lkj flag2", String.valueOf(adIdFlag_2));
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
            DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference();
            //초기화를 위해서 기존에 있던건 null로 넣어주고 지금 로그인한 게정에 guid를 넣어줌
            mDatabaseRef.child("users").child(mAuth.getCurrentUser().getUid()).child("guid").setValue(guid);
            mDatabaseRef.child("users").child(uIdTemp).child("guid").setValue("");
            SharedPreferences pref = getSharedPreferences("com.n4u1.AQA", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("com.n4u1.AQA.fireBaseUserEmail", email);
            editor.putString("com.n4u1.AQA.fireBaseUserPassword", password);
            editor.commit();

            Handler hd = new Handler();
            hd.postDelayed(new splashhandlerHome(), 100);
        }
    }


    //다른 기기에서 로그인 시도
    @Override
    public void GUIDInitDialogCallback(String string) {
        if (string.equals("확인")) {
            DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference();
            //초기화를 위해서 현재 로그인한 계정의 guid에 현재의 guid로 변경
            mDatabaseRef.child("users").child(mAuth.getCurrentUser().getUid()).child("guid").setValue(guid);
            SharedPreferences pref = getSharedPreferences("com.n4u1.AQA", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("com.n4u1.AQA.fireBaseUserEmail", email);
            editor.putString("com.n4u1.AQA.fireBaseUserPassword", password);
            editor.commit();

            Handler hd = new Handler();
            hd.postDelayed(new splashhandlerHome(), 100);
        }
    }


    private class splashhandlerLogin implements Runnable {
        public void run() {
            startActivity(new Intent(getApplication(), LoginActivity.class)); // 로딩이 끝난후 이동할 Activity
            SplashLoadingActivity.this.finish(); // 로딩페이지 Activity Stack에서 제거
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
