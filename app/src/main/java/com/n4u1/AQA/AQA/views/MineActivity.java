package com.n4u1.AQA.AQA.views;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
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
import com.n4u1.AQA.AQA.util.NotificationJobService;

import java.util.Map;

public class MineActivity extends AppCompatActivity {

    FirebaseUser mFireBaseUser;
    DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);


        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_aqa_custom);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle(null);
        }

        mFireBaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("users").child(mFireBaseUser.getUid());

        final TextView mineActivity_textView_noti = findViewById(R.id.mineActivity_textView_noti);
        final TextView mineActivity_textView_id = findViewById(R.id.mineActivity_textView_id);
        TextView mineActivity_textView_account = findViewById(R.id.mineActivity_textView_account);
        final TextView mineActivity_textView_gender = findViewById(R.id.mineActivity_textView_gender);
        final TextView mineActivity_textView_age = findViewById(R.id.mineActivity_textView_age);
        LinearLayout mineActivity_linearLayout_like = findViewById(R.id.mineActivity_linearLayout_like);
        LinearLayout mineActivity_linearLayout_pickContent = findViewById(R.id.mineActivity_linearLayout_pickContent);
        LinearLayout mineActivity_linearLayout_reply = findViewById(R.id.mineActivity_linearLayout_reply);
        LinearLayout mineActivity_linearLayout_upload = findViewById(R.id.mineActivity_linearLayout_upload);


        LinearLayout mineActivity_linearLayout_noti = findViewById(R.id.mineActivity_linearLayout_noti);
        LinearLayout mineActivity_linearLayout_password = findViewById(R.id.mineActivity_linearLayout_password);
        final SwitchCompat mineActivity_switch_noti = findViewById(R.id.mineActivity_switch_noti);



        //이메일 가져오기
        mineActivity_textView_account.setText(mFireBaseUser.getEmail());

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



        //비밀번호 변경하기
        mineActivity_linearLayout_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        });


        //notify test
        mineActivity_linearLayout_noti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backgroundNotify();

            }

        });

        //노티 스위치 상태
        if (mineActivity_switch_noti.getTextOn().toString().equals("on")) {
            mineActivity_textView_noti.setText("알람(사용중)");
        } else {
            mineActivity_textView_noti.setText("알람(미사용중)");
        }


        //노티 스위치 버튼
        mineActivity_switch_noti.setChecked(true);
        mineActivity_switch_noti.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    Log.d("lkj switch", "switch on");
                    Log.d("lkj switchlog", mineActivity_switch_noti.getTextOn().toString());
                    mineActivity_textView_noti.setText("알람 (사용중)");

                } else {
                    Log.d("lkj switch", "switch off");
                    Log.d("lkj switchlog", mineActivity_switch_noti.getTextOff().toString());
                    mineActivity_textView_noti.setText("알람 (미 사용중)");
                }
            }
        });




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

    }

    private void backgroundNotify() {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mine_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int curId = item.getItemId();
        switch (curId) {
            case R.id.menu_home:
                Intent intentHome = new Intent(MineActivity.this, HomeActivity.class);
                startActivity(intentHome);
                break;


            case android.R.id.home:
                Intent intentAqa = new Intent(MineActivity.this, HomeActivity.class);
                intentAqa.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentAqa);


        }
        return super.onOptionsItemSelected(item);
    }
}
