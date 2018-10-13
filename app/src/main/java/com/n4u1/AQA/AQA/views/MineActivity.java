package com.n4u1.AQA.AQA.views;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.n4u1.AQA.AQA.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

        //아이디 가져오기
        String tempId[] = mFireBaseUser.getEmail().split("@");
        mineActivity_textView_account.setText(tempId[0]);

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
