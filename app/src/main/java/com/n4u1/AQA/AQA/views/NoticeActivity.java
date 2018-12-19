package com.n4u1.AQA.AQA.views;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.n4u1.AQA.AQA.R;
import com.n4u1.AQA.AQA.models.NoticeDTO;
import com.n4u1.AQA.AQA.models.SuggestDTO;
import com.n4u1.AQA.AQA.models.User;
import com.n4u1.AQA.AQA.recyclerview.NoticeAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class NoticeActivity extends AppCompatActivity {

    private RecyclerView noticeActivity_recyclerView;
    private FloatingActionButton noticeActivity_fab_addContent;
    private ArrayList<NoticeDTO> noticeDTOS = new ArrayList<>();
    private FirebaseDatabase mDatabase;
    private String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        setContentView(R.layout.activity_notice);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(null);
            getSupportActionBar().setSubtitle("공지 사항");
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        final FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();

        final NoticeAdapter noticeAdapter = new NoticeAdapter(this, noticeDTOS);
        noticeActivity_recyclerView = findViewById(R.id.noticeActivity_recyclerView);
        noticeActivity_fab_addContent = findViewById(R.id.noticeActivity_fab_addContent);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLayoutManager.isSmoothScrollbarEnabled();
        mLayoutManager.setStackFromEnd(true);
        noticeActivity_recyclerView.setLayoutManager(mLayoutManager);

        noticeActivity_recyclerView.setAdapter(noticeAdapter);
        noticeAdapter.notifyDataSetChanged();


        //공지사항 뿌리기
        mDatabase.getReference().child("notice").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                noticeDTOS.clear();
                try {
                    ArrayList<NoticeDTO> noticeDTOSTemp = new ArrayList<>();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        NoticeDTO noticeDTO = snapshot.getValue(NoticeDTO.class);
                        noticeDTOSTemp.add(noticeDTO);
                    }
                    Collections.reverse(noticeDTOSTemp);
                    for (int i = 0; i < noticeDTOSTemp.size(); i++) {
                        try {
                            noticeDTOS.add(noticeDTOSTemp.get(i));
                        } catch (Exception e) {

                        }
                    }
                    noticeAdapter.notifyDataSetChanged();
                    noticeActivity_recyclerView.scrollToPosition(0);
                } catch (Exception e) {

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //userId구해서 DB/adminAuth/ 에 등록되어있는 아이디일경우 공지사항 글쓰기 버튼 보이기
        if (!mUser.isAnonymous()) {
            mDatabase.getReference().child("users").child(mUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    Map<String, Object> userMap = (Map<String, Object>) dataSnapshot.getValue();
                    userId = String.valueOf(userMap.get("userId"));
                    Log.d("lkj userId", userId);

                    mDatabase.getReference().child("adminAuth").child("1").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (int i = 0; i < dataSnapshot.getChildrenCount(); i++) {
                                Map<String, Boolean> adminMap = (Map<String, Boolean>) dataSnapshot.getValue();
                                if (adminMap.keySet().toString().contains(userId)) {
                                    noticeActivity_fab_addContent.show();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


        noticeActivity_fab_addContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NoticeActivity.this, NoticeUploadActivity.class);
                startActivity(intent);
            }
        });


        //공지사항 글쓰기


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
                Intent intentHome = new Intent(NoticeActivity.this, HomeActivity.class);
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
}
