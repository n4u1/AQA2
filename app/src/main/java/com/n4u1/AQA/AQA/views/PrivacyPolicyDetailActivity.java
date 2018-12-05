package com.n4u1.AQA.AQA.views;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.n4u1.AQA.AQA.R;

public class PrivacyPolicyDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        setContentView(R.layout.activity_privacy_policy_detail);


//        WebView webView = findViewById(R.id.webView);
//        WebSettings webSettings;
//        webView.setWebViewClient(new WebViewClient());
//        webSettings = webView.getSettings();
//
//        webSettings.setJavaScriptEnabled(true);
//
//        webView.loadUrl("https://lkj840211.wixsite.com/aqacompany/blank");

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://lkj840211.wixsite.com/aqacompany/privacypolicy"));

        startActivity(intent);

        finish();



    }
}
