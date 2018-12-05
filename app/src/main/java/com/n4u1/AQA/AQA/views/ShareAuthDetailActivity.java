package com.n4u1.AQA.AQA.views;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.n4u1.AQA.AQA.R;
import com.n4u1.AQA.AQA.models.ShareAuthDTO;
import com.n4u1.AQA.AQA.models.SuggestDTO;
import com.n4u1.AQA.AQA.util.GlideApp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class ShareAuthDetailActivity extends AppCompatActivity {

    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;


    TextView shareAuthDetailActivity_textView_title, shareAuthDetailActivity_textView_userId,
            shareAuthDetailActivity_textView_contentId, shareAuthDetailActivity_textView_date,
            shareAuthDetailActivity_textView_replyCount;
    ImageView shareAuthDetailActivity_imageView_userClass, 
            shareAuthDetailActivity_imageView_reply, shareAuthDetailActivity_imageView_userAddContent_1;
    
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        setContentView(R.layout.activity_share_auth_detail);


        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(null);
            getSupportActionBar().setSubtitle("공유 인증");
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        final String shareAuthKey = getIntent().getStringExtra("shareAuthKey");
        firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();


        shareAuthDetailActivity_textView_title = findViewById(R.id.shareAuthDetailActivity_textView_title);
        shareAuthDetailActivity_textView_userId = findViewById(R.id.shareAuthDetailActivity_textView_userId);
        shareAuthDetailActivity_textView_contentId = findViewById(R.id.shareAuthDetailActivity_textView_contentId);
        shareAuthDetailActivity_textView_date = findViewById(R.id.shareAuthDetailActivity_textView_date);

        shareAuthDetailActivity_imageView_userAddContent_1 = findViewById(R.id.shareAuthDetailActivity_imageView_userAddContent_1);
        shareAuthDetailActivity_imageView_userClass = findViewById(R.id.shareAuthDetailActivity_imageView_userClass);



        //shareAuthDTO 화면 초기세팅
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mDatabaseReference.child("shareAuth").child(shareAuthKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ShareAuthDTO shareAuthDTO = dataSnapshot.getValue(ShareAuthDTO.class);
                settingUserIcon(shareAuthDTO.getUid());
                shareAuthDetailActivity_textView_title.setText(shareAuthDTO.getTitle());
                shareAuthDetailActivity_textView_userId.setText(shareAuthDTO.getUserID());
                shareAuthDetailActivity_textView_contentId.setText(shareAuthDTO.getContentId());
                shareAuthDetailActivity_textView_date.setText(shareAuthDTO.getUploadDate());

                if (shareAuthDTO.getImageUrl() != null) {
                    shareAuthDetailActivity_imageView_userAddContent_1.setVisibility(View.VISIBLE);
                    GlideApp.with(getApplicationContext()).load(shareAuthDTO.getImageUrl()).centerCrop().thumbnail(Glide.with(getApplicationContext()).load(R.drawable.loadingicon)).into(shareAuthDetailActivity_imageView_userAddContent_1).getView();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    /*
    onCreate();
     */





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
                Intent intentHome = new Intent(this, HomeActivity.class);
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


    private void settingUserIcon(String uId) {
        mDatabaseReference.child("users").child(uId).child("userClass").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int userClass = Integer.parseInt(dataSnapshot.getValue().toString());
                if (userClass >= 0 && userClass < 50) {
                    shareAuthDetailActivity_imageView_userClass.setImageResource(R.drawable.q_class_red_2);
                } else if (userClass >= 50 && userClass < 100) {
                    shareAuthDetailActivity_imageView_userClass.setImageResource(R.drawable.q_class_red_1);
                } else if (userClass >= 100 && userClass < 150) {
                    shareAuthDetailActivity_imageView_userClass.setImageResource(R.drawable.q_class_orange_1);
                } else if (userClass >= 150 && userClass < 200) {
                    shareAuthDetailActivity_imageView_userClass.setImageResource(R.drawable.q_class_orange_2);
                } else if (userClass >= 200 && userClass < 250) {
                    shareAuthDetailActivity_imageView_userClass.setImageResource(R.drawable.q_class_yellow_1);
                } else if (userClass >= 250 && userClass < 300) {
                    shareAuthDetailActivity_imageView_userClass.setImageResource(R.drawable.q_class_yellow_2);
                } else if (userClass >= 300 && userClass < 350) {
                    shareAuthDetailActivity_imageView_userClass.setImageResource(R.drawable.q_class_green_1);
                } else if (userClass >= 350 && userClass < 400) {
                    shareAuthDetailActivity_imageView_userClass.setImageResource(R.drawable.q_class_green_2);
                } else if (userClass >= 400 && userClass < 450) {
                    shareAuthDetailActivity_imageView_userClass.setImageResource(R.drawable.q_class_blue_2);
                } else if (userClass >= 450 && userClass < 501) {
                    shareAuthDetailActivity_imageView_userClass.setImageResource(R.drawable.q_class_blue_2);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public String getDate() {
        TimeZone timeZone;
        timeZone = TimeZone.getTimeZone("Asia/Seoul");
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd(E)HH:mm:ss", Locale.KOREAN);
        df.setTimeZone(timeZone);
        String currentDate = df.format(date);
        return currentDate;
    }


    //댓글수 추가 (replyCount)
    private void onReplyClicked(final DatabaseReference postRef) {
        final String date = getDate();
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                SuggestDTO shareAuthDTO = mutableData.getValue(SuggestDTO.class);
                if (shareAuthDTO == null) {
                    return Transaction.success(mutableData);
                }
//                contentDTO.reply.put(date, pollActivity_editText_reply.getText().toString());
                mutableData.setValue(shareAuthDTO);
                firebaseDatabase.getReference().child("shareAuthReply").child(postRef.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        firebaseDatabase.getReference().child("shareAuth").child(postRef.getKey()).child("replyCount").setValue(dataSnapshot.getChildrenCount());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {


            }
        });
    }













}
