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
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.n4u1.AQA.AQA.R;
import com.n4u1.AQA.AQA.models.SuggestDTO;
import com.n4u1.AQA.AQA.recyclerview.SuggestAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;


public class SuggestActivity extends AppCompatActivity {

    FirebaseUser mUser;
    DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;
    String userId = "";
    final ArrayList<SuggestDTO> suggestDTOS = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(null);
            getSupportActionBar().setSubtitle("건의 사항");
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        mDatabase = FirebaseDatabase.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        final SwipeRefreshLayout mSwipeRefreshLayout = findViewById(R.id.swipeRFL);
        final RecyclerView suggestActivity_recyclerView = findViewById(R.id.suggestActivity_recyclerView);
        FloatingActionButton suggestActivity_fab_addContent = findViewById(R.id.suggestActivity_fab_addContent);
        final SuggestAdapter suggestAdapter = new SuggestAdapter(this, suggestDTOS, suggestActivity_recyclerView);
//        suggestActivity_recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLayoutManager.isSmoothScrollbarEnabled();
        mLayoutManager.setStackFromEnd(true);
        suggestActivity_recyclerView.setLayoutManager(mLayoutManager);


        suggestActivity_recyclerView.setAdapter(suggestAdapter);
        suggestAdapter.notifyDataSetChanged();


        //onCreate시 액티비티 최초1회 바인딩
        mDatabase.getReference().child("suggest").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                suggestDTOS.clear();
                ArrayList<SuggestDTO> suggestDTOSTemp = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    SuggestDTO suggestDTO = snapshot.getValue(SuggestDTO.class);
                    suggestDTOSTemp.add(suggestDTO);
                }
                Collections.reverse(suggestDTOSTemp);
                for (int i = 0; i < suggestDTOSTemp.size(); i++) {
                    try {
                        suggestDTOS.add(suggestDTOSTemp.get(i));
                    } catch (Exception e) {

                    }
                }
                suggestAdapter.notifyDataSetChanged();
                suggestActivity_recyclerView.scrollToPosition(0);
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
                mDatabase.getReference().child("suggest").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        suggestDTOS.clear();
                        ArrayList<SuggestDTO> suggestDTOSTemp = new ArrayList<>();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            SuggestDTO suggestDTO = snapshot.getValue(SuggestDTO.class);
                            suggestDTOSTemp.add(suggestDTO);
                        }

                        Collections.reverse(suggestDTOSTemp);

                        for (int i = 0; i < suggestDTOSTemp.size(); i++) {
                            try {
                                suggestDTOS.add(suggestDTOSTemp.get(i));
                            } catch (Exception e) {

                            }
                        }
                        suggestAdapter.notifyDataSetChanged();
                        suggestActivity_recyclerView.scrollToPosition(0);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        //건의하기 글쓰기 클릭 floating action bar
        suggestActivity_fab_addContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SuggestActivity.this, UserSuggestUploadActivity.class);
                intent.putExtra("suggestUserId", userId);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int curId = item.getItemId();
        switch (curId) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
