package com.n4u1.AQA.AQA.views;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.n4u1.AQA.AQA.R;
import com.n4u1.AQA.AQA.dialog.AgainPasswordDialog;
import com.n4u1.AQA.AQA.dialog.BaseLoadingActivity;
import com.n4u1.AQA.AQA.dialog.ConfirmPasswordFailDialog;
import com.n4u1.AQA.AQA.dialog.CreateUserAgeDialog;
import com.n4u1.AQA.AQA.dialog.CreateUserGenderDialog;
import com.n4u1.AQA.AQA.dialog.FindPasswordFailDialog;
import com.n4u1.AQA.AQA.dialog.GUIDFailDialog;
import com.n4u1.AQA.AQA.dialog.NotEmailDialog;
import com.n4u1.AQA.AQA.dialog.NotGenderDialog;
import com.n4u1.AQA.AQA.dialog.NotIdlDialog;
import com.n4u1.AQA.AQA.dialog.NotInputDialog;
import com.n4u1.AQA.AQA.dialog.NullEmailDialog;
import com.n4u1.AQA.AQA.dialog.PrivacyPolicyActivity;
import com.n4u1.AQA.AQA.dialog.ShortIdDialog;
import com.n4u1.AQA.AQA.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateUserActivity extends AppCompatActivity implements CreateUserAgeDialog.CreateUserAgeDialogListener,
        CreateUserGenderDialog.CreateUserGenderDialogListener {

    private EditText createUser_editText_email, createUser_editText_password, createUser_editText_confirmPassword,
            createUser_editText_birth, createUser_editText_gender, createUser_editText_id;



    private String gender, email,  userId, password;
    private int age;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        ImageView createUser_imageView_start = findViewById(R.id.createUser_imageView_start);
        createUser_editText_id = findViewById(R.id.createUser_editText_id);
        createUser_editText_email = findViewById(R.id.createUser_editText_email);
        createUser_editText_password = findViewById(R.id.createUser_editText_password);
        createUser_editText_gender = findViewById(R.id.createUser_editText_gender);
        createUser_editText_birth = findViewById(R.id.createUser_editText_birth);
        createUser_editText_confirmPassword = findViewById(R.id.createUser_editText_confirmPassword);
        TextView createUser_textView_privacyPolicy = findViewById(R.id.createUser_textView_privacyPolicy);
//
//
        //나이 선택
        createUser_editText_birth.setFocusable(false);
        createUser_editText_birth.setClickable(false);
        createUser_editText_birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateUserAgeDialog createUserAgeDialog = new CreateUserAgeDialog();
                createUserAgeDialog.show(getSupportFragmentManager(), "createUserAgeDialog");
            }
        });

        //성별 선택
        createUser_editText_gender.setFocusable(false);
        createUser_editText_gender.setClickable(false);
        createUser_editText_gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateUserGenderDialog createUserGenderDialog = new CreateUserGenderDialog();
                createUserGenderDialog.show(getSupportFragmentManager(), "createUserGenderDialog");
            }
        });


        createUser_imageView_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkingCreateUser()){
                    gender = createUser_editText_gender.getText().toString();
                    userId = createUser_editText_id.getText().toString();
                    email = createUser_editText_email.getText().toString();
                    age = getYear() + 1 - Integer.parseInt(createUser_editText_birth.getText().toString());
                    password = createUser_editText_password.getText().toString();

                    Intent intent = new Intent(CreateUserActivity.this, SplashCreateUserActivity.class);
                    intent.putExtra("gender", gender);
                    intent.putExtra("userId", userId);
                    intent.putExtra("email", email);
                    intent.putExtra("age", age);
                    intent.putExtra("password", password);
                    startActivity(intent);

                }
            }
        });


        //개인정보처리방침
        String htmlString = "<u>개인정보처리방침</u>";
        createUser_textView_privacyPolicy.setText(Html.fromHtml(htmlString));
        createUser_textView_privacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateUserActivity.this, PrivacyPolicyActivity.class);
                startActivity(intent);
            }
        });


    }

    private boolean checkingCreateUser() {

        if (createUser_editText_email.getText().toString().isEmpty()) {
            NullEmailDialog nullEmailDialog = new NullEmailDialog();
            nullEmailDialog.show(getSupportFragmentManager(), "nullEmailDialog");
            return false;
        }

        if (!checkEmail(createUser_editText_email.getText().toString())) {
            NotEmailDialog notEmailDialog = new NotEmailDialog();
            notEmailDialog.show(getSupportFragmentManager(), "notEmailDialog");
            return false;
        }

        if(createUser_editText_id.getText().toString().isEmpty()) {
            NotIdlDialog notIdlDialog = new NotIdlDialog();
            notIdlDialog.show(getSupportFragmentManager(), "notIdlDialog");
            return false;
        }

        if (createUser_editText_id.getText().toString().length() < 4) {
            ShortIdDialog shortIdDialog = new ShortIdDialog();
            shortIdDialog.show(getSupportFragmentManager(), "shortIdDialog");
            return false;
        }

        if (createUser_editText_id.getText().toString().length() > 10) {
            ShortIdDialog shortIdDialog = new ShortIdDialog();
            shortIdDialog.show(getSupportFragmentManager(), "shortIdDialog");
            return false;
        }
        if (createUser_editText_password.getText().toString().length() < 6) {
            AgainPasswordDialog againPasswordDialog = new AgainPasswordDialog();
            againPasswordDialog.show(getSupportFragmentManager(), "againPasswordDialog");
            return false;
        }
        if (createUser_editText_birth.getText().toString().equals("")) {
            FindPasswordFailDialog findPasswordFailDialog = new FindPasswordFailDialog();
            findPasswordFailDialog.show(getSupportFragmentManager(), "findPasswordFailDialog");
            return false;
        }
        if (!createUser_editText_confirmPassword.getText().toString().equals(createUser_editText_password.getText().toString())) {
            ConfirmPasswordFailDialog confirmPasswordFailDialog = new ConfirmPasswordFailDialog();
            confirmPasswordFailDialog.show(getSupportFragmentManager(), "confirmPasswordFailDialog");
            return false;
        }
        if (createUser_editText_gender.getText().toString().isEmpty()) {
            NotGenderDialog notGenderDialog = new NotGenderDialog();
            notGenderDialog.show(getSupportFragmentManager(), "notGenderDialog");
            return false;
        }
        return true;
    }


    private boolean checkEmail(String inputUserEmail) {
        String regex = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(inputUserEmail);
        boolean isNormal = m.matches();
        return isNormal;
    }


    public int getYear() {
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
    public void choiceItemCallback(String string) {
        createUser_editText_birth.setText(string);
    }

    @Override
    public void choiceItemCallbackGender(String string) {
        createUser_editText_gender.setText(string);
    }
}
