package com.n4u1.AQA.AQA.views;

import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

    ImageView findUser_ImageView_findUser;
    EditText findUser_editText_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_user);

        findUser_ImageView_findUser = findViewById(R.id.findUser_ImageView_findUser);
        findUser_editText_email = findViewById(R.id.findUser_editText_email);


        findUser_ImageView_findUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputUserEmail = findUser_editText_email.getText().toString();
                if (inputUserEmail.equals("")) {
//                    NullEmailDialog nullEmailDialog = new NullEmailDialog();
//                    nullEmailDialog.show(getSupportFragmentManager(), "nullEmailDialog");
                    NotInputDialog notInputDialog = new NotInputDialog();
                    notInputDialog.show(getSupportFragmentManager(), "notInputDialog");
                } else {
                    if (checkEmail(inputUserEmail)) {
                        FirebaseAuth auth = FirebaseAuth.getInstance();
                        String emailAddress = inputUserEmail;
                        auth.sendPasswordResetEmail(emailAddress)
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
