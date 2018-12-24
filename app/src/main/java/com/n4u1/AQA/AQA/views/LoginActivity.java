package com.n4u1.AQA.AQA.views;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
//

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.n4u1.AQA.AQA.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.n4u1.AQA.AQA.dialog.AgainPasswordDialog;
import com.n4u1.AQA.AQA.dialog.GUIDFailDialog;
import com.n4u1.AQA.AQA.dialog.InitDeviceDialog;
import com.n4u1.AQA.AQA.dialog.NotEmailDialog;
import com.n4u1.AQA.AQA.dialog.NotInputDialog;
import com.n4u1.AQA.AQA.dialog.PreviewDialog;
import com.n4u1.AQA.AQA.dialog.PrivacyPolicyActivity;
import com.n4u1.AQA.AQA.models.User;
import com.n4u1.AQA.AQA.splash.LoadingDialog;
import com.n4u1.AQA.AQA.splash.SplashGuidActivity;
import com.n4u1.AQA.AQA.splash.SplashLoadingActivity;


import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LoginActivity extends AppCompatActivity
        implements PreviewDialog.PreviewDialogListener, InitDeviceDialog.InitDeviceDialogListener, GUIDFailDialog.GUIDFailDialogListener {

    private FirebaseAuth mAuth;

    private long backPressedTime = 0;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private DatabaseReference mDatabaseReference;
    private LoadingDialog loadingDialog;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        setContentView(R.layout.activity_login);

        TextView textView_hidden = findViewById(R.id.textView_hidden);
        final TextView textView_privacy_policy = findViewById(R.id.textView_privacy_policy);
        final TextView textView_servicePolicy = findViewById(R.id.textView_servicePolicy);
        final LinearLayout linearLayout_createUser = findViewById(R.id.linearLayout_createUser);
        final LinearLayout linearLayout_loginUser = findViewById(R.id.linearLayout_loginUser);
        final LinearLayout linearLayout_findUser = findViewById(R.id.linearLayout_findUser);
        final LinearLayout linearLayout_preView = findViewById(R.id.linearLayout_preView);
//        final TextView textView_initDevice = findViewById(R.id.textView_initDevice);
//        String htmlString = "<u>둘러보기</u>";

        loadingDialog = new LoadingDialog(LoginActivity.this);


        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.editText_email);
        editTextPassword = findViewById(R.id.editText_password);


        //로그인하기
        linearLayout_loginUser.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_UP == event.getAction()) {
                    linearLayout_loginUser.setBackgroundResource(R.drawable.shape);
                    String userId;
                    userId = editTextEmail.getText().toString();
                    if (editTextEmail.getText().toString().equals("") || editTextPassword.getText().toString().equals("")) {
                        NotInputDialog notInputDialog = new NotInputDialog();
                        notInputDialog.show(getSupportFragmentManager(), "notInputDialog");
                    } else if (!checkEmail(editTextEmail.getText().toString())) {
                        NotEmailDialog notEmailDialog = new NotEmailDialog();
                        notEmailDialog.show(getSupportFragmentManager(), "notEmailDialog");
                    } else if (editTextPassword.getText().toString().length() < 6) {
                        AgainPasswordDialog againPasswordDialog = new AgainPasswordDialog();
                        againPasswordDialog.show(getSupportFragmentManager(), "againPasswordDialog");
                    } else {
                        loginUser(userId, editTextPassword.getText().toString());
                    }
                }
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    linearLayout_loginUser.setBackgroundResource(R.drawable.shape_click);
                }
                return true;
            }
        });


        //둘러보기
        linearLayout_preView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    linearLayout_preView.setBackgroundResource(R.drawable.shape_click);
                }
                if (MotionEvent.ACTION_UP == event.getAction()) {
                    linearLayout_preView.setBackgroundResource(R.drawable.shape);
                    PreviewDialog previewDialog = new PreviewDialog();
                    previewDialog.show(getSupportFragmentManager(), "previewDialog");
                }
                return true;
            }

        });


//        //기기 초기화, Google Device Id Init
//        textView_initDevice.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (MotionEvent.ACTION_UP == event.getAction()) {
//                    textView_initDevice.setTextColor(0xff4485c9);
//                    InitDeviceDialog initDeviceDialog = new InitDeviceDialog();
//                    initDeviceDialog.show(getSupportFragmentManager(), "initDeviceDialog");
//                }
//                if (MotionEvent.ACTION_DOWN == event.getAction()) {
//                    textView_initDevice.setTextColor(0xFF88B6E7);
//                }
//                return true;
//            }
//        });


        //이용약관
        textView_servicePolicy.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    textView_servicePolicy.setTextColor(0xFF88B6E7);
                }
                if (MotionEvent.ACTION_UP == event.getAction()) {
                    textView_servicePolicy.setTextColor(0xff4485c9);
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://lkj840211.wixsite.com/aqacompany/servicepolicy"));
                    startActivity(intent);
                }
                return true;
            }
        });


        //개인정보 처리방침
        textView_privacy_policy.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    textView_privacy_policy.setTextColor(0xFF88B6E7);
                }
                if (MotionEvent.ACTION_UP == event.getAction()) {
                    textView_privacy_policy.setTextColor(0xff4485c9);
                    Intent intent = new Intent(LoginActivity.this, PrivacyPolicyActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        });


        //히든
        textView_hidden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "행복한 하루 되세요!", Toast.LENGTH_LONG).show();

            }
        });


        //아이디 만들기
        linearLayout_createUser.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_UP == event.getAction()) {
                    linearLayout_createUser.setBackgroundResource(R.drawable.shape);
                    Intent intent = new Intent(LoginActivity.this, SplashGuidActivity.class);
                    intent.putExtra("userLoginFlag", "createUser");
                    startActivity(intent);
                }
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    linearLayout_createUser.setBackgroundResource(R.drawable.shape_click);
                }
                return true;
            }
        });

        //비밀번호 찾기
        linearLayout_findUser.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_UP == event.getAction()) {
                    linearLayout_findUser.setBackgroundResource(R.drawable.shape);
                    Intent findIntent = new Intent(LoginActivity.this, FindUserActivity.class);
                    startActivity(findIntent);
                }
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    linearLayout_findUser.setBackgroundResource(R.drawable.shape_click);
                }
                return true;
            }
        });


        //testButton -> TestActivity
        findViewById(R.id.buttonTest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, TestActivity.class);
                startActivity(intent);

//                Log.d("lkj currentDate", String.valueOf(getCurrentDate()));



            }
        });


        //test login
        Button buttona = findViewById(R.id.buttona);
        Button buttonb = findViewById(R.id.buttonb);
        Button buttonc = findViewById(R.id.buttonc);
        Button buttond = findViewById(R.id.buttond);
        Button buttonl = findViewById(R.id.buttonl);
        buttona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser("a@a.com", "aaaaaa");
            }
        });
        buttonb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser("b@b.com", "bbbbbb");
            }
        });
        buttonc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser("c@c.com", "cccccc");
            }
        });
        buttond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser("d@d.com", "dddddd");
            }
        });
        buttonl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser("lkj840211@gmail.com", "dltjsdn2@");
            }
        });


    }
    /*
    onCreate();
     */




    private void loginUser(final String email, final String password) {
        Intent loadingIntent = new Intent(LoginActivity.this, SplashLoadingActivity.class);
        loadingIntent.putExtra("id", email);
        loadingIntent.putExtra("pw", password);
        startActivity(loadingIntent);
    }


    private boolean checkEmail(String inputUserEmail) {
        String regex = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(inputUserEmail);
        boolean isNormal = m.matches();
        return isNormal;
    }


    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        long FINISH_INTERVAL_TIME = 2000;
        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
//            super.onBackPressed();
            Activity activity = this;
            activity.finish();
        } else {
            backPressedTime = tempTime;
            Toast.makeText(this, " '뒤로' 버튼을 한번더 누르시면 종료 됩니다.", Toast.LENGTH_SHORT).show();
        }
    }


    //사용안함
    @Override
    public void InitDeviceDialogCallback(String string) {
        if (string.equals("확인")) {
            loadingDialog.show();
            mAuth.signInAnonymously().addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        GUIDAsyncTask guidAsyncTask = new GUIDAsyncTask();
                        guidAsyncTask.execute();
                    }
                }
            });


        }
    }


    @Override
    public void PreviewDialogCallback(String string) {
        if (string.equals("확인")) {

            SharedPreferences pref = getSharedPreferences("com.n4u1.AQA", MODE_PRIVATE);
            String spUserEmail = pref.getString("com.n4u1.AQA.fireBaseUserEmail", null);
            String spUserPassword = pref.getString("com.n4u1.AQA.fireBaseUserPassword", null);


            Intent intent = new Intent(LoginActivity.this, SplashGuidActivity.class);
            intent.putExtra("userLoginFlag", "preView");
            startActivity(intent);
//            Toast toast = Toast.makeText(this, "준비중 입니다.", Toast.LENGTH_SHORT);
//            toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
//            toast.show();
        }
    }

    @Override
    public void GUIDFailDialogCallback(String string) {
        if (string.equals("확인")) {

            mDatabaseReference.child("users").child("k3iL4rW9xpNxAG6UQtVf5iLbu0h1").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d("lkj lastguiddate1", dataSnapshot.toString());
                    Map<String, Object> userMap = (Map<String, Object>) dataSnapshot.getValue();

                    Log.d("lkj lastguiddate2", String.valueOf(userMap.get("lastGuidDate")));
                    long lastDate = Long.parseLong(String.valueOf(userMap.get("lastGuidDate")));

                    Log.d("lkj lastguiddate3", String.valueOf(lastDate));

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
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
            mDatabaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    User user = dataSnapshot.getValue(User.class);
                    Iterator<DataSnapshot> usersIterator = dataSnapshot.getChildren().iterator();
                    while (usersIterator.hasNext()) {
                        Map<String, Object> user = (Map<String, Object>) usersIterator.next().getValue();
                        String guid = String.valueOf(user.get("guid"));
                        if (guid.equals(advertId)) {
                            Log.d("lkj user ad id", String.valueOf(user.get("userId")));
//                            mDatabaseReference.child("users").child(String.valueOf(user.get("uid"))).child("guid").setValue("");
                        }
                    }
                    loadingDialog.dismiss();
                    mAuth.signOut();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
