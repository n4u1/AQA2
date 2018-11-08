package com.n4u1.AQA.AQA.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.n4u1.AQA.AQA.R;

public class CreateUserAGActivity extends AppCompatActivity {

    EditText createUserAG_editText_age, createUserAG_editText_gender;
    TextView createUserAG_textView_start;
    ImageView createUserAG_imageView_back;
    String email, id, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user_ag);


        email = getIntent().getStringExtra("email");
        id = getIntent().getStringExtra("id");
        password = getIntent().getStringExtra("password");

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


        //시작하기
        createUserAG_textView_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
            }
        });


    }


}
