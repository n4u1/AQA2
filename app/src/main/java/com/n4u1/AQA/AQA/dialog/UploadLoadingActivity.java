package com.n4u1.AQA.AQA.dialog;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.n4u1.AQA.AQA.R;

import io.netopen.hotbitmapgg.library.view.RingProgressBar;

public class UploadLoadingActivity extends AppCompatActivity {

    RingProgressBar ringProgressBar;
    int progress = 0;
    Handler mHandler;

    long uploadingTime = 10; //프로그레스바 10당 1초

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_loading);

        //1000 = 1MB
        int sec = getIntent().getIntExtra("sec", 1000);
        if (sec < 2000) {
            uploadingTime = 10;
        } else if (sec < 4000) {
            uploadingTime = 20;
        } else if (sec < 6000) {
            uploadingTime = 30;
        } else if (sec < 8000) {
            uploadingTime = 40;
        } else if (sec < 10000) {
            uploadingTime = 50;
        } else if (sec < 12000) {
            uploadingTime = 65;
        } else if (sec < 14000) {
            uploadingTime = 80;
        } else if (sec < 16000) {
            uploadingTime = 95;
        } else if (sec < 18000) {
            uploadingTime = 110;
        } else if (sec < 20000) {
            uploadingTime = 125;
        } else if (sec < 25000) {
            uploadingTime = 150;
        } else if (sec < 30000) {
            uploadingTime = 175;
        } else uploadingTime = 200;



        ringProgressBar = findViewById(R.id.ringProgressBar);
        ringProgress();

    }


    @SuppressLint("HandlerLeak")
    public void ringProgress() {
        ringProgressBar.setOnProgressListener(new RingProgressBar.OnProgressListener() {
            @Override
            public void progressToComplete() {
                Toast.makeText(getApplicationContext(), "complete!!!", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0) {
                    if (progress < 100) {
                        progress++;
                        ringProgressBar.setProgress(progress);
                    }
                }
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("lkjloadingsec", String.valueOf(uploadingTime));
                for (int i = 0; i < 100; i++) {
                    try {
                        //uploadingTime 대략 10당 1초
                        Thread.sleep(uploadingTime);
                        mHandler.sendEmptyMessage(0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
