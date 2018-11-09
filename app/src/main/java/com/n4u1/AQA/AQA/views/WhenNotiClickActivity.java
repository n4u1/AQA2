package com.n4u1.AQA.AQA.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.n4u1.AQA.AQA.R;

import java.util.Timer;
import java.util.TimerTask;

public class WhenNotiClickActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_when_noti_click);

        mAuth = FirebaseAuth.getInstance();
        final int itemViewType = getIntent().getIntExtra("notify_itemViewType", 0);
        final int contentHit = getIntent().getIntExtra("notify_contentHit", 999999);
        final String contentKey = getIntent().getStringExtra("notify_contentKey");
        final String mode = getIntent().getStringExtra("notify_mode");

//
//
        SharedPreferences pref = getSharedPreferences("com.n4u1.AQA", MODE_PRIVATE);
        String spUserEmail = pref.getString("com.n4u1.AQA.fireBaseUserEmail", null);
        String spUserPassword = pref.getString("com.n4u1.AQA.fireBaseUserPassword", null);


        if (spUserEmail != null || spUserPassword != null) {
            Log.d("lkj SharedPreferences", spUserEmail + spUserPassword);
            loginUser(spUserEmail, spUserPassword);
        }




        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                // 3초가 지나면 다이얼로그 닫기
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(WhenNotiClickActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();

//                        if (mode.equals("순위 투표")) {
//                            Intent intent = new Intent(WhenNotiClickActivity.this, PollRankingActivity.class);
//                            Bundle bundle = new Bundle();
//                            bundle.putString("contentKey", contentKey);
//                            bundle.putInt("itemViewType", itemViewType);
//                            bundle.putInt("contentHit", contentHit);
//                            intent.putExtras(bundle);
//                            startActivity(intent);
//                            finish();
//                        } else {
//                            Intent intent = new Intent(WhenNotiClickActivity.this, PollSingleActivity.class);
//                            Bundle bundle = new Bundle();
//                            bundle.putString("contentKey", contentKey);
//                            bundle.putInt("itemViewType", itemViewType);
//                            bundle.putInt("contentHit", contentHit);
//                            intent.putExtras(bundle);
//                            startActivity(intent);
//                            finish();
//                        }
                    }
                };

                Timer timer = new Timer();
                timer.schedule(task, 4000);
            }
        });
        thread.start();


    }

    private void loginUser(final String email, final String password) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("lkj auth successsssss", "login successssssssssss  ");
//
//                            SharedPreferences.Editor editor = sharedPref.edit();
//                            editor.putString("com.n4u1.AQA.fireBaseUid", email);
//                            editor.putString("com.n4u1.AQA.fireBasePassword", password);
//                            editor.commit();

                            SharedPreferences pref = getSharedPreferences("com.n4u1.AQA", MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("com.n4u1.AQA.fireBaseUserEmail", email);
                            editor.putString("com.n4u1.AQA.fireBaseUserPassword", password);
                            editor.commit();

                            // Sign in success, update UI with the signed-in user's information
                            //Toast.makeText(getApplicationContext(), "User Login Success", Toast.LENGTH_LONG).show();//

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(), "User Login Faillllllllllll", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


}
