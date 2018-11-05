package com.n4u1.AQA.AQA.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.n4u1.AQA.AQA.R;

public class PrivacyPolicyDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy_detail);




        PhotoView photoView = findViewById(R.id.photoView);

        Glide.with(this).load(R.drawable.privacy_policy).into(photoView);


    }
}
