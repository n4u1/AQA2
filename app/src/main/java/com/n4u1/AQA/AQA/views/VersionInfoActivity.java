package com.n4u1.AQA.AQA.views;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.n4u1.AQA.AQA.BuildConfig;
import com.n4u1.AQA.AQA.R;
import com.n4u1.AQA.AQA.dialog.PrivacyPolicyActivity;

public class VersionInfoActivity extends AppCompatActivity {

    private LinearLayout versionInfoActivity_linearLayout_update;
    private TextView versionInfoActivity_textView_deviceVersion,
            versionInfoActivity_textView_playStoreVersion, versionInfoActivity_textView_update;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        setContentView(R.layout.activity_version_info);

        ImageView versionInfoActivity_imageView_back;
        versionInfoActivity_textView_update = findViewById(R.id.versionInfoActivity_textView_update);
        versionInfoActivity_textView_playStoreVersion = findViewById(R.id.versionInfoActivity_textView_playStoreVersion);
        versionInfoActivity_textView_deviceVersion = findViewById(R.id.versionInfoActivity_textView_deviceVersion);
        versionInfoActivity_linearLayout_update = findViewById(R.id.versionInfoActivity_linearLayout_update);
        versionInfoActivity_imageView_back = findViewById(R.id.versionInfoActivity_imageView_back);


        //뒤로가기 버튼
        versionInfoActivity_imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



        //현재버전, 스토어버전 비교해서 업데이트 버튼 보여주기
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        mFirebaseRemoteConfig.setConfigSettings(configSettings);
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);
        mFirebaseRemoteConfig.fetch(0)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            String device_version = null;
                            try {
                                device_version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
                            } catch (PackageManager.NameNotFoundException e) {
                                e.printStackTrace();
                            }
                            mFirebaseRemoteConfig.activateFetched();
                            String version = mFirebaseRemoteConfig.getString("aqa_version");
                            versionInfoActivity_textView_playStoreVersion.setText(version);
                            versionInfoActivity_textView_deviceVersion.setText(device_version);

                            float currentVersion = Float.parseFloat(device_version);
                            float storeVersion = Float.parseFloat(version);

                            if (storeVersion > currentVersion) {
                                versionInfoActivity_linearLayout_update.setVisibility(View.VISIBLE);
                            }

                        } else {
                            Toast.makeText(VersionInfoActivity.this, "버전 정보를 가져오는데 실패 했습니다.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });



        //업데이트 하러가기 클릭
        versionInfoActivity_linearLayout_update.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    versionInfoActivity_textView_update.setTextColor(0xFF88B6E7);
                }
                if (MotionEvent.ACTION_UP == event.getAction()) {
                    versionInfoActivity_textView_update.setTextColor(0xff4485c9);
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.n4u1.AQA.AQA"));
                    startActivity(intent);
                }
                return true;
            }
        });




    }
}
