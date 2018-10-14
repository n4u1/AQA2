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
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Trigger;
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
            getSupportActionBar().setTitle("");
        }

        mFireBaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("users").child(mFireBaseUser.getUid());

        TextView mineActivity_textView_account = findViewById(R.id.mineActivity_textView_account);
        final TextView mineActivity_textView_gender = findViewById(R.id.mineActivity_textView_gender);
        final TextView mineActivity_textView_age = findViewById(R.id.mineActivity_textView_age);
        TextView mineActivity_textView_like = findViewById(R.id.mineActivity_textView_like);
        TextView mineActivity_textView_like_ = findViewById(R.id.mineActivity_textView_like_);
        TextView mineActivity_textView_pickContent = findViewById(R.id.mineActivity_textView_pickContent);
        TextView mineActivity_textView_pickContent_ = findViewById(R.id.mineActivity_textView_pickContent_);
        TextView mineActivity_textView_reply = findViewById(R.id.mineActivity_textView_reply);
        TextView mineActivity_textView_reply_ = findViewById(R.id.mineActivity_textView_reply_);
        TextView mineActivity_textView_upload = findViewById(R.id.mineActivity_textView_upload);
        TextView mineActivity_textView_upload_ = findViewById(R.id.mineActivity_textView_upload_);
        LinearLayout mineActivity_linearLayout8 = findViewById(R.id.mineActivity_linearLayout8);

        //아이디 가져오기
        String tempId[] = mFireBaseUser.getEmail().split("@");
        mineActivity_textView_account.setText(tempId[0]);

        //백그라운드 노티 확인
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(MineActivity.this));
        dispatcher.mustSchedule(
                dispatcher.newJobBuilder()
                        .setService(NotificationJobService.class)
                        .setTag("NotificationJobService")
                        .setRecurring(true)
                        .setTrigger(Trigger.executionWindow(5, 30))
                        .build()
        );

        //성별 가져오기
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Object> users = (Map<String, Object>) dataSnapshot.getValue();
                mineActivity_textView_gender.setText(String.valueOf(users.get("sex")));
                mineActivity_textView_age.setText(String.valueOf(users.get("age")));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //notify test
        mineActivity_linearLayout8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificationShow();
            }

        });



        //좋아요 누른 게시물 모아보기
        mineActivity_textView_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MineActivity.this, MyLikeContentsActivity.class);
                startActivity(intent);
            }
        });
        mineActivity_textView_like_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MineActivity.this, MyLikeContentsActivity.class);
                startActivity(intent);
            }
        });

        //참여한 게시물 모아보기
        mineActivity_textView_pickContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MineActivity.this, MyPollActivity.class);
                startActivity(intent);
            }
        });
        mineActivity_textView_pickContent_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MineActivity.this, MyPollActivity.class);
                startActivity(intent);
            }
        });

        //댓글 남긴 게시물 모아보기
        mineActivity_textView_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MineActivity.this, MyReplyContentsActivity.class);
                startActivity(intent);
            }
        });
        mineActivity_textView_reply_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MineActivity.this, MyReplyContentsActivity.class);
                startActivity(intent);
            }
        });

        //내가 올린 게시물 모아보기
        mineActivity_textView_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MineActivity.this, MyUploadActivity.class);
                startActivity(intent);
            }
        });
        mineActivity_textView_upload_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MineActivity.this, MyUploadActivity.class);
                startActivity(intent);
            }
        });

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
