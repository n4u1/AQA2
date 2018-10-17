package com.n4u1.AQA.AQA.views;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.n4u1.AQA.AQA.R;

public class FindUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_user);

        Button findUser_button_findUser = findViewById(R.id.findUser_button_findUser);
        EditText findUser_editText_password = findViewById(R.id.findUser_editText_password);


        findUser_button_findUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                String emailAddress = "lkj84021@gmail.com";

                auth.sendPasswordResetEmail(emailAddress)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d("lkjpassword", "lkjpassword Success");
                                } else {
                                    Log.d("lkjpassword", "lkjpassword Fail");
                                }
                            }
                        });
            }
        });


    }
}
