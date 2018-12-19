package com.n4u1.AQA.AQA.views;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.n4u1.AQA.AQA.R;
import com.n4u1.AQA.AQA.models.NoticeDTO;

public class NoticeDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_detail);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(null);
            getSupportActionBar().setSubtitle("공지 사항");
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        String noticeKey = getIntent().getStringExtra("noticeKey");
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
        final TextView noticeDetailActivity_textView_title, noticeDetailActivity_textView_description;
        noticeDetailActivity_textView_description = findViewById(R.id.noticeDetailActivity_textView_description);
        noticeDetailActivity_textView_title = findViewById(R.id.noticeDetailActivity_textView_title);

        mRef.child("notice").child(noticeKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    NoticeDTO noticeDTO = dataSnapshot.getValue(NoticeDTO.class);
                    noticeDetailActivity_textView_title.setText(noticeDTO.getTitle());
                    noticeDetailActivity_textView_description.setText(noticeDTO.getDescription());
                } catch (Exception e) {

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
