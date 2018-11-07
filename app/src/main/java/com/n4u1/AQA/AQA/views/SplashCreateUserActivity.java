package com.n4u1.AQA.AQA.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.n4u1.AQA.AQA.R;
import com.n4u1.AQA.AQA.dialog.GUIDFailDialog;
import com.n4u1.AQA.AQA.models.User;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SplashCreateUserActivity extends AppCompatActivity implements GUIDFailDialog.GUIDFailDialogListener {


    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private String gender, email, uid, userId, password;
    private int age;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_create_user);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        gender = getIntent().getStringExtra("gender");
        email = getIntent().getStringExtra("email");
        uid = getIntent().getStringExtra("uid");
        userId = getIntent().getStringExtra("userId");
        password = getIntent().getStringExtra("password");
        age = getIntent().getIntExtra("age", 0);

        createUser(email, password);


    }


    private void createUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
//                            Toast.makeText(getApplicationContext(), "User Create Success", Toast.LENGTH_LONG).show();
                            String email = mAuth.getCurrentUser().getEmail();
                            String uid = mAuth.getCurrentUser().getUid();
                            GUIDAsyncTask guidAsyncTask = new GUIDAsyncTask();
                            guidAsyncTask.execute();
                            writeNewUser(null, gender, uid, email, age, userId);

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(), "User Create Fail : " + task.getException(), Toast.LENGTH_LONG).show();
                            Log.d("getException", "createUserWithEmail:failure", task.getException());
                        }
                    }
                });
    }


    private void writeNewUser(String deviceName, String sex, String uid, String email, int age, String userId) {
        User user = new User();
        user.setUserId(userId);
        user.setAge(age);
        user.setSex(sex);
//        user.setJob(job);
        user.setUid(uid);
        user.setEmail(email);
        user.setUserClass(0);
        mDatabase.child("users").child(uid).setValue(user);

        SharedPreferences pref = getSharedPreferences("com.n4u1.AQA", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("com.n4u1.AQA.fireBaseUserEmail", email);
        editor.putString("com.n4u1.AQA.fireBaseUserPassword", password);
        editor.commit();

        Intent intent = new Intent(SplashCreateUserActivity.this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

        finish();

    }




    private class GUIDAsyncTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            AdvertisingIdClient.Info idInfo = null;
            try {
                idInfo = AdvertisingIdClient.getAdvertisingIdInfo(getApplicationContext());
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            } catch (GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String advertId = null;
            try {
                advertId = idInfo.getId();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            Log.d("lkj advertId1", advertId);
            return advertId;
        }

        @Override
        protected void onPostExecute(final String advertId) {
            Log.d("lkj advertId2", advertId);
            if (advertId != null && advertId.trim().isEmpty()) {
                //guid 못찾음
                HomeActivity homeActivity = new HomeActivity();
                homeActivity.testDialog();
            } else {
                //guid 찾음
                mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child("guid").setValue(advertId);
            }
        }
    }


    @Override
    public void GUIDFailDialogCallback(String string) {

    }
}
