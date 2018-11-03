package com.n4u1.AQA.AQA.views;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.n4u1.AQA.AQA.R;
import com.n4u1.AQA.AQA.models.ShareAuthDTO;
import com.n4u1.AQA.AQA.models.SuggestDTO;
import com.n4u1.AQA.AQA.recyclerview.ShareAuthAdapter;
import com.n4u1.AQA.AQA.recyclerview.SuggestAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class ShareAuthActivity extends AppCompatActivity {

    FirebaseUser mUser;
    DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;
    String userId = "";
    final ArrayList<ShareAuthDTO> shareAuthDTOS = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_auth);


        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(null);
            getSupportActionBar().setSubtitle("공유 인증 하기");
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        mDatabase = FirebaseDatabase.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        final SwipeRefreshLayout mSwipeRefreshLayout = findViewById(R.id.swipeRFL);
        final RecyclerView shareAuthActivity_recyclerView = findViewById(R.id.shareAuthActivity_recyclerView);
        FloatingActionButton shareAuthActivity_fab_addContent = findViewById(R.id.shareAuthActivity_fab_addContent);
        final ShareAuthAdapter shareAuthAdapter = new ShareAuthAdapter(this, shareAuthDTOS, shareAuthActivity_recyclerView);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLayoutManager.isSmoothScrollbarEnabled();
        mLayoutManager.setStackFromEnd(true);
        shareAuthActivity_recyclerView.setLayoutManager(mLayoutManager);

        shareAuthActivity_recyclerView.setAdapter(shareAuthAdapter);
        shareAuthAdapter.notifyDataSetChanged();



        //onCreate시 액티비티 최초1회 바인딩
        mDatabase.getReference().child("shareAuth").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                shareAuthDTOS.clear();
                ArrayList<ShareAuthDTO> shareAuthDTOTemp = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ShareAuthDTO shareAuthDTO = snapshot.getValue(ShareAuthDTO.class);
                    shareAuthDTOTemp.add(shareAuthDTO);
                }
                Collections.reverse(shareAuthDTOTemp);
                for (int i = 0; i < shareAuthDTOTemp.size(); i++) {
                    try {
                        shareAuthDTOS.add(shareAuthDTOTemp.get(i));
                    } catch (Exception e) {

                    }
                }
                shareAuthAdapter.notifyDataSetChanged();
                shareAuthActivity_recyclerView.scrollToPosition(0);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //userId 구하기
        mDatabaseReference.child("users").child(mUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Object> user = (Map<String, Object>) dataSnapshot.getValue();
                userId = user.get("userId").toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //Pull to Refresh 당겨서 새로고침
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mDatabase.getReference().child("shareAuth").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        shareAuthDTOS.clear();
                        ArrayList<ShareAuthDTO> shareAuthDTOSTemp = new ArrayList<>();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            ShareAuthDTO shareAuthDTO = snapshot.getValue(ShareAuthDTO.class);
                            shareAuthDTOSTemp.add(shareAuthDTO);
                        }

                        Collections.reverse(shareAuthDTOSTemp);

                        for (int i = 0; i < shareAuthDTOSTemp.size(); i++) {
                            try {
                                shareAuthDTOS.add(shareAuthDTOSTemp.get(i));
                            } catch (Exception e) {

                            }
                        }
                        shareAuthAdapter.notifyDataSetChanged();
                        shareAuthActivity_recyclerView.scrollToPosition(0);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });


        //공유인증하기 글쓰기 클릭 floating action bar
        shareAuthActivity_fab_addContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShareAuthActivity.this, UserShareAuthUploadActivity.class);
                intent.putExtra("shareAuthUserId", userId);
                startActivity(intent);
            }
        });




    }
}
