package com.n4u1.AQA.AQA.views;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.n4u1.AQA.AQA.R;
import com.n4u1.AQA.AQA.dialog.CreateUserAgeDialog;
import com.n4u1.AQA.AQA.dialog.CreateUserGenderDialog;
import com.n4u1.AQA.AQA.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateUserActivity extends AppCompatActivity implements CreateUserAgeDialog.CreateUserAgeDialogListener,
        CreateUserGenderDialog.CreateUserGenderDialogListener {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private EditText createUser_editText_email, createUser_editText_password,
            createUser_editText_sex, createUser_editText_age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        ImageView createUser_button_createUser;
        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        createUser_editText_email = findViewById(R.id.createUser_editText_email);
        createUser_editText_password = findViewById(R.id.createUser_editText_password);


        //나이 선택
        createUser_editText_age.setFocusable(false);
        createUser_editText_age.setClickable(false);
        createUser_editText_age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateUserAgeDialog createUserAgeDialog = new CreateUserAgeDialog();
                createUserAgeDialog.show(getSupportFragmentManager(), "createUserAgeDialog");
            }
        });

        //성별 선택
        createUser_editText_sex.setFocusable(false);
        createUser_editText_sex.setClickable(false);
        createUser_editText_sex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateUserGenderDialog createUserGenderDialog = new CreateUserGenderDialog();
                createUserGenderDialog.show(getSupportFragmentManager(), "createUserGenderDialog");
            }
        });


    }

    private void createUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(getApplicationContext(), "User Create Success", Toast.LENGTH_LONG).show();
                            String sex = createUser_editText_sex.getText().toString();
//                            String job = createUser_editText_job.getText().toString();
                            String email = mAuth.getCurrentUser().getEmail();
                            String uid = mAuth.getCurrentUser().getUid();
                            int ageTemp = Integer.parseInt(createUser_editText_age.getText().toString());
                            int age = 2018 + 1 - ageTemp;
                            writeNewUser(null, sex, uid, email, age);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(), "User Create Fail : " + task.getException(), Toast.LENGTH_LONG).show();
                            Log.d("getException", "createUserWithEmail:failure", task.getException());
                        }
                    }
                });
    }

    private boolean checkingCreateUser() {

        if (createUser_editText_email.getText().toString().isEmpty() || createUser_editText_password.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "아이디, 비밀번호를 입력해주세요!", Toast.LENGTH_LONG).show();
            return false;
        }

        if (createUser_editText_age.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "나이를 입력해주세요!", Toast.LENGTH_LONG).show();
            return false;
        }
//
//        if (createUser_editText_job.getText().toString().isEmpty()) {
//            Toast.makeText(getApplicationContext(), "직업을 입력해주세요!", Toast.LENGTH_LONG).show();
//            return false;
//        }

        if (createUser_editText_sex.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "성별을 입력해주세요!", Toast.LENGTH_LONG).show();
            return false;
        }
//
//        if (!checkEmail(createUser_editText_email.getText().toString())) {
//            Toast.makeText(getApplicationContext(), "이메일 형식으로 입력해주세요!!!!", Toast.LENGTH_LONG).show();
//            return false;
//        }

        if (createUser_editText_password.getText().toString().length() < 6) {
            Toast.makeText(getApplicationContext(), "비밀번호가 너무 짧아요ㅠ_ㅠ (6자 이상)", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void writeNewUser(String deviceName, String sex, String uid, String email, int age) {
        User user = new User();
        user.setAge(age);
        user.setSex(sex);
//        user.setJob(job);
        user.setUid(uid);
        user.setEmail(email);
        mDatabase.child("users").child(uid).setValue(user);
        Intent intent = new Intent(CreateUserActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();

    }

    private boolean checkEmail(String inputUserEmail) {
        String regex = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(inputUserEmail);
        boolean isNormal = m.matches();
        return isNormal;
    }


    //ContentTypeDialog choiceItemCallback
    @Override
    public void choiceItemCallback(String string) {
        createUser_editText_age.setText(string);
    }

    @Override
    public void choiceItemCallbackGender(String string) {
        createUser_editText_sex.setText(string);
    }
}
