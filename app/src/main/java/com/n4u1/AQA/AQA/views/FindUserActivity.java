package com.n4u1.AQA.AQA.views;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.n4u1.AQA.AQA.R;
import com.n4u1.AQA.AQA.dialog.FindPasswordFailDialog;
import com.n4u1.AQA.AQA.dialog.NotEmailDialog;
import com.n4u1.AQA.AQA.dialog.NotInputDialog;
import com.n4u1.AQA.AQA.dialog.NullEmailDialog;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FindUserActivity extends AppCompatActivity {

    LinearLayout findUser_LinearLayout_findUser;
    EditText findUser_editText_email;
    ImageView findUser_imageView_back;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        setContentView(R.layout.activity_find_user);

        findUser_LinearLayout_findUser = findViewById(R.id.findUser_LinearLayout_findUser);
        findUser_editText_email = findViewById(R.id.findUser_editText_email);
        findUser_imageView_back = findViewById(R.id.findUser_imageView_back);

        findUser_imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        findUser_LinearLayout_findUser.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (MotionEvent.ACTION_UP == motionEvent.getAction()) {
                    findUser_LinearLayout_findUser.setBackgroundResource(R.drawable.shape);
                    String inputUserEmail = findUser_editText_email.getText().toString();
                    if (inputUserEmail.equals("")) {
                        NotInputDialog notInputDialog = new NotInputDialog();
                        notInputDialog.show(getSupportFragmentManager(), "notInputDialog");
                    } else {
                        if (checkEmail(inputUserEmail)) {
                            FirebaseAuth auth = FirebaseAuth.getInstance();
                            auth.sendPasswordResetEmail(inputUserEmail)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d("lkjpassword", "lkjpassword Success");
                                            } else {
                                                FindPasswordFailDialog findPasswordFailDialog = new FindPasswordFailDialog();
                                                findPasswordFailDialog.show(getSupportFragmentManager(), "findPasswordFailDialog");
                                            }
                                        }
                                    });
                        } else {
                            NotEmailDialog notEmailDialog = new NotEmailDialog();
                            notEmailDialog.show(getSupportFragmentManager(), "notEmailDialog");
                        }
                    }
                }
                if (MotionEvent.ACTION_DOWN == motionEvent.getAction()) {
                    findUser_LinearLayout_findUser.setBackgroundResource(R.drawable.shape_click);
                }

                return true;
            }
        });


    }

    //정규표현식으로 이메일 체킹
    private boolean checkEmail(String email) {
        String regex = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        boolean isNormal = m.matches();
        return isNormal;
    }
}
