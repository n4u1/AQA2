package com.n4u1.AQA.AQA.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
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

public class SplashActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mAuth = FirebaseAuth.getInstance();



        SharedPreferences pref = getSharedPreferences("com.n4u1.AQA", MODE_PRIVATE);
        String spUserEmail = pref.getString("com.n4u1.AQA.fireBaseUserEmail", null);
        String spUserPassword = pref.getString("com.n4u1.AQA.fireBaseUserPassword", null);


        if (spUserEmail != null || spUserPassword != null) {
            Log.d("lkj SharedPreferences", spUserEmail + spUserPassword);
            loginUser(spUserEmail, spUserPassword);
        } else {
            Handler hd = new Handler();
            hd.postDelayed(new splashhandlerLogin() , 1000);
        }


    }

    private void loginUser(final String email, final String password) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Handler hd = new Handler();
                            hd.postDelayed(new splashhandlerHome() , 1000);
//
//                            SharedPreferences pref = getSharedPreferences("com.n4u1.AQA", MODE_PRIVATE);
//                            SharedPreferences.Editor editor = pref.edit();
//                            editor.putString("com.n4u1.AQA.fireBaseUserEmail", email);
//                            editor.putString("com.n4u1.AQA.fireBaseUserPassword", password);
//                            editor.commit();
//
//
//
//                            Intent intent = new Intent(SplashActivity.this, SplashLoadingActivity.class);
//                            startActivity(intent);
//                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(), "User Login Fail", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    private class splashhandlerLogin implements Runnable{
        public void run() {
            startActivity(new Intent(getApplication(), LoginActivity.class)); // 로딩이 끝난후 이동할 Activity
            SplashActivity.this.finish(); // 로딩페이지 Activity Stack에서 제거
        }

    }



    private class splashhandlerHome implements Runnable{
        public void run() {
            startActivity(new Intent(getApplication(), HomeActivity.class)); // 로딩이 끝난후 이동할 Activity
            SplashActivity.this.finish(); // 로딩페이지 Activity Stack에서 제거
        }

    }
}
