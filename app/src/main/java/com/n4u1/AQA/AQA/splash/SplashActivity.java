package com.n4u1.AQA.AQA.splash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.n4u1.AQA.AQA.R;
import com.n4u1.AQA.AQA.dialog.GUIDFailDialog;
import com.n4u1.AQA.AQA.views.HomeActivity;
import com.n4u1.AQA.AQA.views.LoginActivity;
import com.n4u1.AQA.AQA.views.PollRankingActivity;

import java.io.IOException;
import java.util.Map;

public class SplashActivity extends AppCompatActivity implements GUIDFailDialog.GUIDFailDialogListener {
    private FirebaseAuth mAuth;
    private String contentKey;
    private String pollMode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mAuth = FirebaseAuth.getInstance();


//        final Uri data = getIntent().getData();
//        if(data != null){
//            Log.d("lkj Data2 in intent2", data.toString());
//            Log.d("lkj Data2 in intent3", data.getLastPathSegment());
//            Log.d("lkj Data2 in intent4", data.getQueryParameter("contentKey"));
//            Log.d("lkj Data2 in intent5", data.getQueryParameter("pollMode"));
//            contentKey = data.getQueryParameter("contentKey");
//            pollMode = data.getQueryParameter("pollMode");
//        }


        SharedPreferences pref = getSharedPreferences("com.n4u1.AQA", MODE_PRIVATE);
        String spUserEmail = pref.getString("com.n4u1.AQA.fireBaseUserEmail", "a");
        String spUserPassword = pref.getString("com.n4u1.AQA.fireBaseUserPassword", "b");

        loginUser(spUserEmail, spUserPassword);


//        다이나믹링크 파라미터 받기
//        FirebaseDynamicLinks.getInstance().getDynamicLink(getIntent())
//                .addOnSuccessListener(new OnSuccessListener<PendingDynamicLinkData>() {
//                    @Override
//                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
//                        if (pendingDynamicLinkData == null) {
//                            return;
//                        }
//
//                        Uri deepLink = pendingDynamicLinkData.getLink();
//                        contentKey = deepLink.getQueryParameter("contentKey");
//                        Log.d("lkj Skey???", contentKey);
//
//                        if (!TextUtils.isEmpty(contentKey)) {
//                            Intent intent = new Intent(SplashActivity.this, PollRankingActivity.class);
//                            intent.putExtra("contentKey", contentKey);
//                            Log.d("lkj key?????????", contentKey);
//                            startActivity(intent);
//                        }
//                    }
//                });



    }

    private void loginUser(final String email, final String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            try {
//                                Log.d("lkj current uid", mAuth.getCurrentUser().getUid());
                                GUIDAsyncTask guidAsyncTask = new GUIDAsyncTask();
                                guidAsyncTask.execute();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            if (!TextUtils.isEmpty(contentKey) && TextUtils.isEmpty(pollMode)) {
//                                Log.d("lkj current uid2", mAuth.getCurrentUser().getUid());
                                GUIDAsyncTask guidAsyncTask = new GUIDAsyncTask();
                                guidAsyncTask.execute();
                            } else {
                                Handler hd = new Handler();
                                hd.postDelayed(new splashhandlerLogin(), 1000);
                            }
                            // If sign in fails, display a message to the user.

                        }
                    }
                });
    }


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
                DatabaseReference mDatabaseRef;
                mDatabaseRef = FirebaseDatabase.getInstance().getReference();
                mDatabaseRef.child("users").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Map<String, Object> user = (Map<String, Object>) dataSnapshot.getValue();
                        if (user.get("guid").toString().equals(advertId)) {
                            Handler hd = new Handler();
                            hd.postDelayed(new splashhandlerHome() , 1000);
                        } else {
                            GUIDFailDialog guidFailDialog = new GUIDFailDialog();
                            guidFailDialog.show(getSupportFragmentManager(), "guidFailDialog");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }
    }


    private class splashhandlerLogin implements Runnable {
        public void run() {
            startActivity(new Intent(getApplication(), LoginActivity.class)); // 로딩이 끝난후 이동할 Activity
            SplashActivity.this.finish(); // 로딩페이지 Activity Stack에서 제거
        }

    }


    private class splashhandlerHome implements Runnable {
        public void run() {
            startActivity(new Intent(getApplication(), HomeActivity.class)); // 로딩이 끝난후 이동할 Activity
            SplashActivity.this.finish(); // 로딩페이지 Activity Stack에서 제거
        }

    }

    @Override
    public void GUIDFailDialogCallback(String string) {
        if (string.equals("확인")) {
            startActivity(new Intent(getApplication(), LoginActivity.class)); // 로딩이 끝난후 이동할 Activity
            SplashActivity.this.finish(); // 로딩페이지 Activity Stack에서 제거
        }
    }



}
