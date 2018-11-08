package com.n4u1.AQA.AQA.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.n4u1.AQA.AQA.R;


public class CreateUserIdActivity extends AppCompatActivity {

    TextView createUserId_textView_next;
    ImageView createUserId_imageView_back;
    EditText createUserId_editText_id, createUserId_editText_pw, createUserId_editText_pwComfirm;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user_id);

        email = getIntent().getStringExtra("email");
        createUserId_textView_next = findViewById(R.id.createUserId_textView_next);
        createUserId_imageView_back = findViewById(R.id.createUserId_imageView_back);
        createUserId_editText_id = findViewById(R.id.createUserId_editText_id);
        createUserId_editText_pw = findViewById(R.id.createUserId_editText_pw);
        createUserId_editText_pwComfirm = findViewById(R.id.createUser_editText_confirmPassword);



        createUserId_textView_next.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    createUserId_textView_next.setTextColor(0xFF88B6E7);
                }
                if (MotionEvent.ACTION_UP == event.getAction()) {
                    createUserId_textView_next.setTextColor(0xff4485c9);
                    String id = createUserId_editText_id.getText().toString();
                    Intent intent = new Intent(CreateUserIdActivity.this, CreateUserAGActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("email", email);
                    startActivity(intent);
                }
                return true;

            }
        });

        createUserId_imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



    }
}
