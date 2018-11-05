package com.n4u1.AQA.AQA.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.n4u1.AQA.AQA.R;
import com.n4u1.AQA.AQA.dialog.GUIDFailDialog;
import com.n4u1.AQA.AQA.util.GetAdIdAsyncTask;

import java.io.IOException;
import java.util.Map;

public class SplashActivity extends AppCompatActivity implements GUIDFailDialog.GUIDFailDialogListener {
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mAuth = FirebaseAuth.getInstance();


        SharedPreferences pref = getSharedPreferences("com.n4u1.AQA", MODE_PRIVATE);
        String spUserEmail = pref.getString("com.n4u1.AQA.fireBaseUserEmail", "a");
        String spUserPassword = pref.getString("com.n4u1.AQA.fireBaseUserPassword", "b");

        loginUser(spUserEmail, spUserPassword);


    }

    private void loginUser(final String email, final String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            try {
                                Log.d("lkj current uid", mAuth.getCurrentUser().getUid());
                                GUIDAsyncTask guidAsyncTask = new GUIDAsyncTask();
                                guidAsyncTask.execute();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            Handler hd = new Handler();
                            hd.postDelayed(new splashhandlerLogin(), 1000);

                        }
                    }
                });
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
                Toast toast = Toast.makeText(getApplicationContext(), "구글 광고ID를 찾을 수 없습니다.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
                toast.show();
            } else {
                DatabaseReference mDatabaseRef;
                mDatabaseRef = FirebaseDatabase.getInstance().getReference();
                mDatabaseRef.child("users").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Map<String, Object> user = (Map<String, Object>) dataSnapshot.getValue();
                        if (user.get("guid").toString().equals(advertId)) {
                            Handler hd = new Handler();
                            hd.postDelayed(new splashhandlerHome() , 500);
                        } else {
                            GUIDFailDialog guidFailDialog = new GUIDFailDialog();
                            guidFailDialog.show(getSupportFragmentManager(), "guidFailDialog");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }
    }


    private class splashhandlerLogin implements Runnable {
        public void run() {
            startActivity(new Intent(getApplication(), LoginActivity.class)); // 로딩이 끝난후 이동할 Activity
            SplashActivity.this.finish(); // 로딩페이지 Activity Stack에서 제거
        }

    }


    private class splashhandlerHome implements Runnable {
        public void run() {
            startActivity(new Intent(getApplication(), HomeActivity.class)); // 로딩이 끝난후 이동할 Activity
            SplashActivity.this.finish(); // 로딩페이지 Activity Stack에서 제거
        }

    }

    @Override
    public void GUIDFailDialogCallback(String string) {
        if (string.equals("확인")) {
            startActivity(new Intent(getApplication(), LoginActivity.class)); // 로딩이 끝난후 이동할 Activity
            SplashActivity.this.finish(); // 로딩페이지 Activity Stack에서 제거
        }
    }



}
