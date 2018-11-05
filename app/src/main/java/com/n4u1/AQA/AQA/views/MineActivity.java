package com.n4u1.AQA.AQA.views;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.n4u1.AQA.AQA.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.n4u1.AQA.AQA.dialog.FindEmailDialog;
import com.n4u1.AQA.AQA.dialog.PrivacyPolicyActivity;
import com.n4u1.AQA.AQA.dialog.SignOutDialog;
import com.n4u1.AQA.AQA.dialog.LogOutDialog;
import com.n4u1.AQA.AQA.models.ContentDTO;
import com.n4u1.AQA.AQA.util.NotificationJobService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MineActivity extends AppCompatActivity implements LogOutDialog.LogOutDialogListener,
        SignOutDialog.SignOutDialogListener, FindEmailDialog.FindEmailDialogListener {

    FirebaseUser mFireBaseUser;
    DatabaseReference mDatabaseReference;

    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(null);
            getSupportActionBar().setSubtitle("설 정");
        }

        mFireBaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("users").child(mFireBaseUser.getUid());

//        AdView adView = findViewById(R.id.adView);
        final TextView mineActivity_textView_id = findViewById(R.id.mineActivity_textView_id);
        final TextView mineActivity_textView_userClass = findViewById(R.id.mineActivity_textView_userClass);
        TextView mineActivity_textView_account = findViewById(R.id.mineActivity_textView_account);
        final TextView mineActivity_textView_gender = findViewById(R.id.mineActivity_textView_gender);
        final TextView mineActivity_textView_age = findViewById(R.id.mineActivity_textView_age);
        LinearLayout mineActivity_linearLayout_email = findViewById(R.id.mineActivity_linearLayout_email);
        LinearLayout mineActivity_linearLayout_like = findViewById(R.id.mineActivity_linearLayout_like);
        LinearLayout mineActivity_linearLayout_pickContent = findViewById(R.id.mineActivity_linearLayout_pickContent);
        LinearLayout mineActivity_linearLayout_reply = findViewById(R.id.mineActivity_linearLayout_reply);
        LinearLayout mineActivity_linearLayout_upload = findViewById(R.id.mineActivity_linearLayout_upload);
        LinearLayout mineActivity_linearLayout_logOut = findViewById(R.id.mineActivity_linearLayout_logOut);
        LinearLayout mineActivity_linearLayout_authOut = findViewById(R.id.mineActivity_linearLayout_authOut);
        LinearLayout mineActivity_linearLayout_suggest = findViewById(R.id.mineActivity_linearLayout_suggest);
        LinearLayout mineActivity_linearLayout_servicePolicy = findViewById(R.id.mineActivity_linearLayout_servicePolicy);
        LinearLayout mineActivity_linearLayout_privacyPolicy = findViewById(R.id.mineActivity_linearLayout_privacyPolicy);
        LinearLayout mineActivity_linearLayout_version = findViewById(R.id.mineActivity_linearLayout_version);
        LinearLayout mineActivity_linearLayout_userClass = findViewById(R.id.mineActivity_linearLayout_userClass);
        final ImageView mineActivity_imageView_userClass = findViewById(R.id.mineActivity_imageView_userClass);
        LinearLayout mineActivity_linearLayout_password = findViewById(R.id.mineActivity_linearLayout_password);


        //adView
//        AdRequest adRequest = new AdRequest.Builder()
////                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
//                .addTestDevice("C39C4F095E193D0C5E7BBCB91B89B469")  // Galaxy Nexus-4 device ID
//
//                .build();
//        adView.loadAd(adRequest);

        //이메일 가져오기
        mineActivity_textView_account.setText(mFireBaseUser.getEmail());


        //이메일 변경하기
        mineActivity_linearLayout_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast toast = Toast.makeText(getApplicationContext(), "만들고 있어요", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
                toast.show();

            }
        });

        //성별 나이 아이디 가져오기
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Object> users = (Map<String, Object>) dataSnapshot.getValue();

                mineActivity_textView_gender.setText(String.valueOf(users.get("sex")));
                mineActivity_textView_age.setText(String.valueOf(users.get("age")));
                mineActivity_textView_id.setText(String.valueOf(users.get("userId")));

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Q포인트 점수,
        mDatabaseReference.child("userClass").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int userClass = Integer.parseInt(dataSnapshot.getValue().toString());
                mineActivity_textView_userClass.setText(String.valueOf(userClass));
                if (userClass >= 0 && userClass < 50) {
                    mineActivity_imageView_userClass.setImageResource(R.drawable.q_class_red_1);
                } else if (userClass >= 50 && userClass < 100) {
                    mineActivity_imageView_userClass.setImageResource(R.drawable.q_class_red_2);
                } else if (userClass >= 100 && userClass < 150) {
                    mineActivity_imageView_userClass.setImageResource(R.drawable.q_class_orange_1);
                } else if (userClass >= 150 && userClass < 200) {
                    mineActivity_imageView_userClass.setImageResource(R.drawable.q_class_orange_2);
                } else if (userClass >= 200 && userClass < 250) {
                    mineActivity_imageView_userClass.setImageResource(R.drawable.q_class_yellow_1);
                } else if (userClass >= 250 && userClass < 300) {
                    mineActivity_imageView_userClass.setImageResource(R.drawable.q_class_yellow_2);
                } else if (userClass >= 300 && userClass < 350) {
                    mineActivity_imageView_userClass.setImageResource(R.drawable.q_class_green_1);
                } else if (userClass >= 350 && userClass < 400) {
                    mineActivity_imageView_userClass.setImageResource(R.drawable.q_class_green_2);
                } else if (userClass >= 400 && userClass < 450) {
                    mineActivity_imageView_userClass.setImageResource(R.drawable.q_class_blue_1);
                } else if (userClass >= 450 && userClass < 501) {
                    mineActivity_imageView_userClass.setImageResource(R.drawable.q_class_blue_2);
                } else if (userClass >= 501) {
                    mineActivity_imageView_userClass.setImageResource(R.drawable.q_class_black);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Q포인트 점수 알아보기
        mineActivity_linearLayout_userClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MineActivity.this, UserClassInfoActivity.class);
                startActivity(intent);
            }
        });


        //개발자욕하러가기
        mineActivity_linearLayout_suggest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MineActivity.this, SuggestActivity.class);
                startActivity(intent);
            }
        });


        //버전 확인
        mineActivity_linearLayout_version.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MineActivity.this, VersionInfoActivity.class);
                startActivity(intent);
            }
        });



        //비밀번호 변경하기
        mineActivity_linearLayout_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                String emailAddress = auth.getCurrentUser().getEmail();
                FindEmailDialog findEmailDialog = FindEmailDialog.newInstance(emailAddress);
                findEmailDialog.show(getSupportFragmentManager(), "findEmailDialog");


            }
        });


        //노티 스위치 상태
//        if (mineActivity_switch_noti.getTextOn().toString().equals("on")) {
//            mineActivity_textView_noti.setText("알람(사용중)");
//        } else {
//            mineActivity_textView_noti.setText("알람(미사용중)");
//        }





        //좋아요 누른 게시물 모아보기
        mineActivity_linearLayout_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MineActivity.this, MyLikeContentsActivity.class);
                startActivity(intent);
            }
        });

        //참여한 게시물 모아보기
        mineActivity_linearLayout_pickContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MineActivity.this, MyPollActivity.class);
                startActivity(intent);
            }
        });

        //댓글 남긴 게시물 모아보기
        mineActivity_linearLayout_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MineActivity.this, MyReplyContentsActivity.class);
                startActivity(intent);
            }
        });

        //내가 올린 게시물 모아보기
        mineActivity_linearLayout_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MineActivity.this, MyUploadActivity.class);
                startActivity(intent);
            }
        });

        //로그 아웃
        mineActivity_linearLayout_logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogOutDialog logOutDialog = new LogOutDialog();
                logOutDialog.show(getSupportFragmentManager(), "logOutDialog");

            }
        });

        //이용약관
        mineActivity_linearLayout_servicePolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MineActivity.this, ServicePolicyActivity.class);
                startActivity(intent);
            }
        });


        //개인정보처리방침
        mineActivity_linearLayout_privacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MineActivity.this, PrivacyPolicyActivity.class);
                startActivity(intent);
            }
        });

        //회원 탈퇴
        mineActivity_linearLayout_authOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignOutDialog authOutDialog = new SignOutDialog();
                authOutDialog.show(getSupportFragmentManager(), "authOutDialog");

            }
        });

    }

    private void backgroundNotify() {
        String id = "aa";
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        Job myJob = dispatcher.newJobBuilder()
                // the JobService that will be called
                .setService(NotificationJobService.class)
                // uniquely identifies the job
                .setTag("NotificationJobService")
                // one-off job
                .setRecurring(true)
                // don't persist past a device reboot
                .setLifetime(Lifetime.FOREVER)
                // start between 0 and 60 seconds from now
                .setTrigger(Trigger.executionWindow(10, 20))
                // don't overwrite an existing job with the same tag
                .setReplaceCurrent(true)
                // retry with exponential backoff
                .setRetryStrategy(RetryStrategy.DEFAULT_LINEAR)
                // constraints that need to be satisfied for the job to run


                .build();

        dispatcher.mustSchedule(myJob);
    }

    private void notificationShow() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");
        builder.setSmallIcon(R.mipmap.ic_aqa_custom);
        builder.setContentTitle("100명이 투표했어요!");
        builder.setContentText("클릭해서 확인하기!");
        Intent intentHome = new Intent(this, HomeActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intentHome, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_aqa_custom);
        builder.setLargeIcon(largeIcon);

        Uri ringtoneUri = RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(ringtoneUri);

        long[] vibrate = {0, 100, 200, 300};
        builder.setVibrate(vibrate);
        builder.setAutoCancel(true);

        NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(new NotificationChannel("default", "기본채널", NotificationManager.IMPORTANCE_DEFAULT));
        }
        manager.notify(1, builder.build());


    }


    private Map<String, String> getAlarmList(final ArrayList<String> contentList) {
//        ArrayList<String> alarmList = new ArrayList<>();

        mDatabase.child("user_contents").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                ArrayList<String> tmp = new ArrayList<>();
//
//                Iterator<DataSnapshot> contentDTOIterator = dataSnapshot.getChildren().iterator();
//
//                while (contentDTOIterator.hasNext()) {
//                    final ContentDTO contentDTO = contentDTOIterator.next().getValue(ContentDTO.class);
//                    tmp.add(contentDTO.title);
//                }
                final HashMap<String, String> list = new HashMap<>();
                Iterator<DataSnapshot> listIterator = dataSnapshot.getChildren().iterator();
                while (listIterator.hasNext()) {
                    ContentDTO contentDTO = listIterator.next().getValue(ContentDTO.class);

                    for (int i = 0; i < contentList.size(); i++) {
                        if (contentDTO.contentKey.equals(contentList.get(i))) {
                            Log.d("lkj", String.valueOf(i) + " : " + contentDTO.contentKey);
                            Log.d("lkj", String.valueOf(i) + " : " + contentList.get(i));
                            list.put(contentDTO.contentKey, contentDTO.title);
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return null;

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mine_menu, menu);
        return true;

    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int curId = item.getItemId();
        switch (curId) {
            case R.id.menu_home:
                Intent intentHome = new Intent(MineActivity.this, HomeActivity.class);
                intentHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentHome);
                finish();
                break;


            case android.R.id.home:
                onBackPressed();
                break;


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void LogOutDialogCallback(String string) {
        if (string.equals("확인")) {
            FirebaseAuth.getInstance().signOut();
            SharedPreferences pref = getSharedPreferences("com.n4u1.AQA", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("com.n4u1.AQA.fireBaseUserEmail", "a");
            editor.putString("com.n4u1.AQA.fireBaseUserPassword", "b");
            editor.commit();
            Intent intent = new Intent(MineActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        }
    }

    @Override
    public void SignOutDialogCallback(String string) {
        if (string.equals("탈퇴하기")) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            DatabaseReference authDatabaseRef = FirebaseDatabase.getInstance().getReference();

            authDatabaseRef.child("users").child(user.getUid()).removeValue();

            SharedPreferences pref = getSharedPreferences("com.n4u1.AQA", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("com.n4u1.AQA.fireBaseUserEmail", null);
            editor.putString("com.n4u1.AQA.fireBaseUserPassword", null);
            editor.commit();




            user.delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("lkj authout", "User account deleted.");
                            }
                        }
                    });



            Intent intent = new Intent(MineActivity.this, LoginActivity.class);
            startActivity(intent);
        }

    }

    @Override
    public void FindEmailDialogCallback(String string) {
        if (string.equals("확인")) {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            String emailAddress = auth.getCurrentUser().getEmail();
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
    }
}
