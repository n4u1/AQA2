package com.n4u1.AQA.AQA.splash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.n4u1.AQA.AQA.R;
import com.n4u1.AQA.AQA.views.CreateUserEmailActivity;
import com.n4u1.AQA.AQA.views.HomeActivity;
import com.n4u1.AQA.AQA.views.LoginActivity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SplashGuidActivity extends AppCompatActivity {

    private String userLoginFlag = "";
    private DatabaseReference mDatabaseReference;
    private boolean GUID_CHECK_FLAG = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_guid);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        userLoginFlag = getIntent().getStringExtra("userLoginFlag"); //처음사용자 확인용

        mAuth.signInAnonymously().addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    GUIDAsyncTask guidAsyncTask = new GUIDAsyncTask();
                    guidAsyncTask.execute();

                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "일시적 오류입니다. 다시 시도 해주세요.", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();
                    finish();
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
            return advertId;
        }

        @Override
        protected void onPostExecute(final String advertId) {
            Map<String, String> previewerMap = new HashMap<>();
            String previewerValue = "true";

            Log.d("lkj advertId", advertId);
            if (userLoginFlag.equals("preView")) {
                if (advertId != null && advertId.trim().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "다시 시도해주세요.", Toast.LENGTH_LONG).show();
                    onBackPressed();
                } else {
                    try {
                        SharedPreferences pref = getSharedPreferences("com.n4u1.AQA", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("com.n4u1.AQA.previewer", "previewerFirst");
                        editor.commit();

                        previewerMap.put(advertId, previewerValue);
                        mDatabaseReference.child("previewerGuid").child(advertId).setValue(previewerMap);
                        Intent intent = new Intent(SplashGuidActivity.this, HomeActivity.class);
                        intent.putExtra("guid", advertId);
                        SplashGuidActivity.this.finish();
                        startActivity(intent);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            } else {
                if (advertId != null && advertId.trim().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "다시 시도해주세요.", Toast.LENGTH_LONG).show();
                    onBackPressed();
                } else {
                    //guid 찾음
//                    mDatabaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            Iterator<DataSnapshot> UserIterator = dataSnapshot.getChildren().iterator();
//                            while (UserIterator.hasNext()) {
//                                Map<String, Object> userMap = (Map<String, Object>) UserIterator.next().getValue();
//                                if (String.valueOf(userMap.get("guid")).equals(advertId)) {
//
//                                }
//
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });

                    Intent intent = new Intent(SplashGuidActivity.this, CreateUserEmailActivity.class);
                    intent.putExtra("guid", advertId);
                    SplashGuidActivity.this.finish();
                    startActivity(intent);
                }
            }

        }


    }
}
