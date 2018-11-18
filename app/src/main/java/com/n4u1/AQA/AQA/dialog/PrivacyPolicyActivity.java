package com.n4u1.AQA.AQA.dialog;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.n4u1.AQA.AQA.R;
import com.n4u1.AQA.AQA.views.PrivacyPolicyDetailActivity;

public class PrivacyPolicyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        TextView privacyPolicy_textView_detail = findViewById(R.id.privacyPolicy_textView_detail);
        TextView privacyPolicy_textView_done = findViewById(R.id.privacyPolicy_textView_done);

        String htmlString = "<u>자세히보기</u>";
        privacyPolicy_textView_detail.setText(Html.fromHtml(htmlString));
        privacyPolicy_textView_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://lkj840211.wixsite.com/aqacompany/privacypolicy"));
                startActivity(intent);
                finish();
            }
        });


        privacyPolicy_textView_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
