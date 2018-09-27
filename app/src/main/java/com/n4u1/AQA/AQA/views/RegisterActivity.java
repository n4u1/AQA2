package com.n4u1.AQA.AQA.views;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.n4u1.AQA.AQA.R;
import com.n4u1.AQA.AQA.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class RegisterActivity extends AppCompatActivity {


    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        SetActionBarTitle(getSupportActionBar(), "AQA");
        setContentView(R.layout.activity_register);


        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
//        if (getSupportActionBar() != null){
//            getSupportActionBar().setTitle("  AQA");
//        }
        getSupportActionBar().setIcon(R.mipmap.ic_q_custom);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        TextView textView_uidTest = findViewById(R.id.textView_uidTest);
        final EditText editText_age = findViewById(R.id.editText_age);
        final EditText editText_job = findViewById(R.id.editText_job);
        final EditText editText_sex = findViewById(R.id.editText_sex);
        Button button_login = findViewById(R.id.button_confirm);

        //device id 말고 다른거 써야할듯
        final String USER_DEVICE_ID = android.provider.Settings.Secure.getString(getBaseContext().getContentResolver(), Settings.Secure.ANDROID_ID);



        textView_uidTest.setText(USER_DEVICE_ID);
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeNewUser(USER_DEVICE_ID, editText_sex.getText().toString(), editText_job.getText().toString(),
                        mUser.getUid(), mUser.getEmail(), Integer.parseInt(editText_age.getText().toString()));

                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }
        });



        

        
    }

    private void writeNewUser(String deviceName, String sex, String job, String uid, String email, int age) {
        User user = new User();
        user.setAge(age);
        user.setSex(sex);
        user.setJob(job);
        user.setUid(uid);
        user.setEmail(email);
        mDatabase.child("users").child(uid).setValue(user);

    }


}
