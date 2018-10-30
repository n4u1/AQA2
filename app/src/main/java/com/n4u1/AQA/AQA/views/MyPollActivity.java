package com.n4u1.AQA.AQA.views;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.n4u1.AQA.AQA.R;
import com.n4u1.AQA.AQA.models.ContentDTO;
import com.n4u1.AQA.AQA.recyclerview.PostAdapterMine;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class MyPollActivity extends AppCompatActivity {


    private FirebaseDatabase mDatabaseUser;
    private FirebaseUser mFireBaseUser;

    final ArrayList<ContentDTO> contentDTOS = new ArrayList<>();
    private SwipeRefreshLayout swipeRFL;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_poll);


        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(null);
        }


        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        mDatabaseUser = FirebaseDatabase.getInstance();
        mFireBaseUser = FirebaseAuth.getInstance().getCurrentUser();

        swipeRFL = findViewById(R.id.swipeRFL);
        final RecyclerView recyclerViewList = findViewById(R.id.recyclerView_home);
        final PostAdapterMine postAdapterMine = new PostAdapterMine(this, contentDTOS, recyclerViewList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        final TextView textView_myPoll = findViewById(R.id.textView_myPoll);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLayoutManager.isSmoothScrollbarEnabled();
        mLayoutManager.setStackFromEnd(true);
        mLayoutManager.setReverseLayout(true);
        recyclerViewList.setLayoutManager(mLayoutManager);

        recyclerViewList.setAdapter(postAdapterMine);
        postAdapterMine.notifyDataSetChanged();


        //onCreate시 참여한 게시물 추려서 최초1회 바인딩
        mDatabase.getReference().child("user_contents").addListenerForSingleValueEvent(new ValueEventListener() {
            ArrayList<String> key = new ArrayList<>();

            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                mDatabaseUser.getReference().child("users").child(mFireBaseUser.getUid()).child("pickContent").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshots) {
                        Map<String, Object> pickContent = (Map<String, Object>) dataSnapshots.getValue();
                        if (pickContent == null) {
                            textView_myPoll.setVisibility(View.VISIBLE);
                            recyclerViewList.setVisibility(View.GONE);
                        } else {
                            textView_myPoll.setVisibility(View.GONE);
                            recyclerViewList.setVisibility(View.VISIBLE);
                            int tempCount = 0;

                            try {
                                Set set = pickContent.keySet();
                                Iterator iterator = set.iterator();
                                while (iterator.hasNext()) {
                                    key.add((String) iterator.next());
                                    tempCount++;
                                }

                                contentDTOS.clear();
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    ContentDTO contentDTO = snapshot.getValue(ContentDTO.class);
                                    for (int i = 0; i < tempCount; i++) {
                                        if (contentDTO.getContentKey().contains(key.get(i))) {
                                            contentDTOS.add(contentDTO);
                                        }
                                    }
                                }
                                postAdapterMine.notifyDataSetChanged();
                            } catch (Exception e) {
                                Log.d("lkj exception", e.toString());
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


        swipeRFL.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
            }
        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mypoll_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int curId = item.getItemId();
        switch (curId) {
            case R.id.menu_home:
                Intent intentHome = new Intent(MyPollActivity.this, HomeActivity.class);
                intentHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentHome);
                break;

            case android.R.id.home:
                onBackPressed();
                break;
        }
//        onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
