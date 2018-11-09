package com.n4u1.AQA.AQA.views;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
//

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.n4u1.AQA.AQA.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.n4u1.AQA.AQA.dialog.AgainPasswordDialog;
import com.n4u1.AQA.AQA.dialog.GUIDFailDialog;
import com.n4u1.AQA.AQA.dialog.NotEmailDialog;
import com.n4u1.AQA.AQA.dialog.NotInputDialog;
import com.n4u1.AQA.AQA.dialog.PreviewDialog;
import com.n4u1.AQA.AQA.dialog.PrivacyPolicyActivity;
import com.n4u1.AQA.AQA.splash.SplashGuidActivity;
import com.n4u1.AQA.AQA.splash.SplashLoadingActivity;


import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LoginActivity extends AppCompatActivity implements PreviewDialog.PreviewDialogListener,
        GUIDFailDialog.GUIDFailDialogListener {
    private FirebaseAuth mAuth;

    private long backPressedTime = 0;
    EditText editTextEmail;
    EditText editTextPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView textView_hidden = findViewById(R.id.textView_hidden);
        TextView textView_privacy_policy = findViewById(R.id.textView_privacy_policy);
        LinearLayout linearLayout_createUser = findViewById(R.id.linearLayout_createUser);
        LinearLayout linearLayout_loginUser = findViewById(R.id.linearLayout_loginUser);
        LinearLayout linearLayout_findUser = findViewById(R.id.linearLayout_findUser);
        TextView textView_preview = findViewById(R.id.textView_preview);
//        String htmlString = "<u>둘러보기</u>";


        mAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.editText_email);
        editTextPassword = findViewById(R.id.editText_password);


        //둘러보기
//        textView_preview.setText(Html.fromHtml(htmlString));
        textView_preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PreviewDialog previewDialog = new PreviewDialog();
                previewDialog.show(getSupportFragmentManager(), "previewDialog");
            }
        });


        //개인정보 처리방침
        textView_privacy_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, PrivacyPolicyActivity.class);
                startActivity(intent);
            }
        });

        //히든
        textView_hidden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(), "Hidden Quest Complete!\n오늘 좋은일이 생길거에요!", Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), "히든 퀘스트!!\n잘생긴 경준이 오빠라고 하면 커피 쏩니다ㅋㅋㅋㅋㅋㅋㅋㅋㅋ선착순1명ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ", Toast.LENGTH_LONG).show();

            }
        });


        //계정 만들기
        linearLayout_createUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SplashGuidActivity.class);
                intent.putExtra("userLoginFlag", "createUser");
                startActivity(intent);
            }
        });


        //계정 찾기
        linearLayout_findUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent findIntent = new Intent(LoginActivity.this, FindUserActivity.class);
                startActivity(findIntent);
            }
        });



        //로그인하기
        linearLayout_loginUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId;
                userId = editTextEmail.getText().toString();
                if (editTextEmail.getText().toString().equals("") || editTextPassword.getText().toString().equals("")) {
                    NotInputDialog notInputDialog = new NotInputDialog();
                    notInputDialog.show(getSupportFragmentManager(), "notInputDialog");
                } else if (!checkEmail(editTextEmail.getText().toString())) {
                    NotEmailDialog notEmailDialog = new NotEmailDialog();
                    notEmailDialog.show(getSupportFragmentManager(), "notEmailDialog");
                } else if (editTextPassword.getText().toString().length() < 6) {
                    AgainPasswordDialog againPasswordDialog = new AgainPasswordDialog();
                    againPasswordDialog.show(getSupportFragmentManager(), "againPasswordDialog");
                } else {
                    loginUser(userId, editTextPassword.getText().toString());
                }


            }
        });

    }


    private void loginUser(final String email, final String password) {
        Intent loadingIntent = new Intent(LoginActivity.this, SplashLoadingActivity.class);
        loadingIntent.putExtra("id", email);
        loadingIntent.putExtra("pw", password);
        startActivity(loadingIntent);
    }


    private boolean checkEmail(String inputUserEmail) {
        String regex = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(inputUserEmail);
        boolean isNormal = m.matches();
        return isNormal;
    }


    @Override
    public void PreviewDialogCallback(String string) {
        if (string.equals("확인")) {
            Intent intent = new Intent(LoginActivity.this, SplashGuidActivity.class);
            intent.putExtra("userLoginFlag", "preView");
            startActivity(intent);
//            Toast toast = Toast.makeText(this, "준비중 입니다.", Toast.LENGTH_SHORT);
//            toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
//            toast.show();
        }
    }


    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        long FINISH_INTERVAL_TIME = 2000;
        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
//            super.onBackPressed();
            Activity activity = this;
            activity.finish();
        } else {
            backPressedTime = tempTime;
            Toast.makeText(this, " '뒤로' 버튼을 한번더 누르시면 종료 됩니다.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void GUIDFailDialogCallback(String string) {
    }

}
