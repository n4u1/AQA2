package com.n4u1.AQA.AQA.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.n4u1.AQA.AQA.R;
import com.n4u1.AQA.AQA.dialog.AgainPasswordDialog;
import com.n4u1.AQA.AQA.dialog.BirthInputFailDialog;
import com.n4u1.AQA.AQA.dialog.ConfirmPasswordFailDialog;
import com.n4u1.AQA.AQA.dialog.CreateUserAgeDialog;
import com.n4u1.AQA.AQA.dialog.CreateUserGenderDialog;
import com.n4u1.AQA.AQA.dialog.NotEmailDialog;
import com.n4u1.AQA.AQA.dialog.NotGenderDialog;
import com.n4u1.AQA.AQA.dialog.NotIdlDialog;
import com.n4u1.AQA.AQA.dialog.NullEmailDialog;
import com.n4u1.AQA.AQA.dialog.ShortIdDialog;
import com.n4u1.AQA.AQA.splash.SplashCreateUserActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class CreateUserAGActivity extends AppCompatActivity
        implements CreateUserGenderDialog.CreateUserGenderDialogListener,
        CreateUserAgeDialog.CreateUserAgeDialogListener {

    EditText createUserAG_editText_age, createUserAG_editText_gender;
    TextView createUserAG_textView_start;
    ImageView createUserAG_imageView_back;
    String email, password, gender, userId;
    int age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user_ag);


        email = getIntent().getStringExtra("email");
        userId = getIntent().getStringExtra("id");
        password = getIntent().getStringExtra("pw");

        createUserAG_imageView_back = findViewById(R.id.createUserAG_imageView_back);
        createUserAG_textView_start = findViewById(R.id.createUserAG_textView_start);
        createUserAG_editText_gender = findViewById(R.id.createUserAG_editText_gender);
        createUserAG_editText_age = findViewById(R.id.createUserAG_editText_age);

        //뒤로가기
        createUserAG_imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        //성별 선택
        createUserAG_editText_gender.setFocusable(false);
        createUserAG_editText_gender.setClickable(false);
        createUserAG_editText_gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateUserGenderDialog createUserGenderDialog = new CreateUserGenderDialog();
                createUserGenderDialog.show(getSupportFragmentManager(), "createUserGenderDialog");
            }
        });



        //나이 선택
        createUserAG_editText_age.setFocusable(false);
        createUserAG_editText_age.setClickable(false);
        createUserAG_editText_age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateUserAgeDialog createUserAgeDialog = new CreateUserAgeDialog();
                createUserAgeDialog.show(getSupportFragmentManager(), "createUserAgeDialog");
            }
        });


        //시작하기
        createUserAG_textView_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkingCreateUser()) {
                    gender = createUserAG_editText_gender.getText().toString();
                    age = getYear() + 1 - Integer.parseInt(createUserAG_editText_age.getText().toString());

                    Intent intent = new Intent(CreateUserAGActivity.this, SplashCreateUserActivity.class);
                    intent.putExtra("gender", gender);
                    intent.putExtra("userId", userId);
                    intent.putExtra("email", email);
                    intent.putExtra("age", age);
                    intent.putExtra("password", password);
                    startActivity(intent);
                }
//
            }
        });
    }



    private boolean checkingCreateUser() {
        if (createUserAG_editText_age.getText().toString().equals("")) {
            BirthInputFailDialog birthInputFailDialog = new BirthInputFailDialog();
            birthInputFailDialog.show(getSupportFragmentManager(), "birthInputFailDialog");
            return false;
        }
        if (createUserAG_editText_gender.getText().toString().isEmpty()) {
            NotGenderDialog notGenderDialog = new NotGenderDialog();
            notGenderDialog.show(getSupportFragmentManager(), "notGenderDialog");
            return false;
        }
        return true;
    }



    private int getYear() {
        TimeZone timeZone;
        timeZone = TimeZone.getTimeZone("Asia/Seoul");
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd(E)HH:mm:ss", Locale.KOREAN);
        df.setTimeZone(timeZone);
        String currentDate = df.format(date);
        String currentYear = currentDate.substring(0,4);
        return Integer.parseInt(currentYear);
    }


    @Override
    public void choiceItemCallbackGender(String string) {
        createUserAG_editText_gender.setText(string);
    }

    @Override
    public void choiceItemCallback(String string) {
        createUserAG_editText_age.setText(String.valueOf(string));
    }
}
