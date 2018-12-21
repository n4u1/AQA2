package com.n4u1.AQA.AQA.splash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.n4u1.AQA.AQA.R;
import com.n4u1.AQA.AQA.views.PollRankingActivity;
import com.n4u1.AQA.AQA.views.PollSingleActivity;

public class SplashDynamicLinkActivity extends AppCompatActivity {

    private String contentKey;
    private String pollMode;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_dynamic_link);
        mAuth = FirebaseAuth.getInstance();

//
//        final Uri data = getIntent().getData();
//        if(data != null){
//            Log.d("lkj Data2 in intent2", data.toString());
//            Log.d("lkj Data2 in intent3", data.getLastPathSegment());
//            Log.d("lkj Data2 in intent4", data.getQueryParameter("contentKey"));
//            Log.d("lkj Data2 in intent5", data.getQueryParameter("pollMode"));
//            contentKey = data.getQueryParameter("contentKey");
//            pollMode = data.getQueryParameter("pollMode");
//        }


        //다이나믹링크 파라미터 받기
        FirebaseDynamicLinks.getInstance().getDynamicLink(getIntent())
                .addOnSuccessListener(new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        if (pendingDynamicLinkData == null) {
                            return;
                        }

                        Uri deepLink = pendingDynamicLinkData.getLink();
                        contentKey = deepLink.getQueryParameter("contentKey");
                        pollMode = deepLink.getQueryParameter("pollMode");
//                        Log.d("lkj Skey???", contentKey);
//                        Log.d("lkj Spmode???", pollMode);

//                        if (!TextUtils.isEmpty(contentKey)) {
//                            Intent intent = new Intent(SplashActivity.this, PollRankingActivity.class);
//                            intent.putExtra("contentKey", contentKey);
//                            Log.d("lkj key?????????", contentKey);
//                            startActivity(intent);
//                        }
                    }
                });


        SharedPreferences pref = getSharedPreferences("com.n4u1.AQA", MODE_PRIVATE);
        String spUserEmail = pref.getString("com.n4u1.AQA.fireBaseUserEmail", "a");
        String spUserPassword = pref.getString("com.n4u1.AQA.fireBaseUserPassword", "b");

        loginUser(spUserEmail, spUserPassword);



    }


    private void loginUser(final String email, final String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (pollMode.equals("single")) {
                                Intent intentSingle = new Intent(SplashDynamicLinkActivity.this, PollSingleActivity.class);
                                intentSingle.putExtra("contentKey", contentKey);
                                startActivity(intentSingle);
                                SplashDynamicLinkActivity.this.finish();
                            } else {
                                Intent intentRanking = new Intent(SplashDynamicLinkActivity.this, PollRankingActivity.class);
                                intentRanking.putExtra("contentKey", contentKey);
                                startActivity(intentRanking);
                                SplashDynamicLinkActivity.this.finish();
                            }

                        } else {
                            mAuth.signInAnonymously()
                                    .addOnCompleteListener(SplashDynamicLinkActivity.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                if (pollMode.equals("single")) {
                                                    Intent intentSingle = new Intent(SplashDynamicLinkActivity.this, PollSingleActivity.class);
                                                    intentSingle.putExtra("contentKey", contentKey);
                                                    startActivity(intentSingle);
                                                    SplashDynamicLinkActivity.this.finish();
                                                } else {
                                                    Intent intentRanking = new Intent(SplashDynamicLinkActivity.this, PollRankingActivity.class);
                                                    intentRanking.putExtra("contentKey", contentKey);
                                                    startActivity(intentRanking);
                                                    SplashDynamicLinkActivity.this.finish();
                                                }
                                            } else {
                                                Toast toast = Toast.makeText(getApplicationContext(), "일시적 오류입니다. 다시 시도 해주세요.", Toast.LENGTH_SHORT);
                                                toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
                                                toast.show();
                                                finish();

                                            }

                                        }
                                    });
                        }
                    }
                });
    }
}
