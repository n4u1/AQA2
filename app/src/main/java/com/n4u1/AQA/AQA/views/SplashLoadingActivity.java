package com.n4u1.AQA.AQA.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.n4u1.AQA.AQA.R;

import java.util.Timer;
import java.util.TimerTask;

public class SplashLoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_loading);



        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                // 3초가 지나면
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(SplashLoadingActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                };

                Timer timer = new Timer();
                timer.schedule(task, 3000);
            }
        });
        thread.start();

    }
}
