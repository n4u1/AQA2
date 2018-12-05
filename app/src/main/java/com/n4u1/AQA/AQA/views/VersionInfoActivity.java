package com.n4u1.AQA.AQA.views;

import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.n4u1.AQA.AQA.BuildConfig;
import com.n4u1.AQA.AQA.R;

public class VersionInfoActivity extends AppCompatActivity {

    TextView versionInfoActivity_textView_version2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        setContentView(R.layout.activity_version_info);


        versionInfoActivity_textView_version2 = findViewById(R.id.versionInfoActivity_textView_version2);



        String versionName = BuildConfig.VERSION_NAME;


        versionInfoActivity_textView_version2.setText(versionName);

    }
}
