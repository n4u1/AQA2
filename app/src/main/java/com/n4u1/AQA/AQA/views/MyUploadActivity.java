package com.n4u1.AQA.AQA.views;

import android.content.Intent;
import android.os.Handler;
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
import com.n4u1.AQA.AQA.util.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class MyUploadActivity extends AppCompatActivity {

    private FirebaseDatabase mDatabase;
    private FirebaseDatabase mDatabaseUser;
    private FirebaseUser mFireBaseUser;
    protected Handler handler;
    private PostAdapterMine postAdapterMine;
    private SwipeRefreshLayout swipeRFL;

    final ArrayList<ContentDTO> contentDTOS = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_upload);


        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(null);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
            getSupportActionBar().setSubtitle("내 투표");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseUser = FirebaseDatabase.getInstance();
        mFireBaseUser = FirebaseAuth.getInstance().getCurrentUser();

        swipeRFL = findViewById(R.id.swipeRFL);
        final RecyclerView recyclerView_myUpload = findViewById(R.id.recyclerView_myUpload);
        postAdapterMine = new PostAdapterMine(this, contentDTOS, recyclerView_myUpload);
        final TextView textView_myUpload = findViewById(R.id.textView_myUpload);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLayoutManager.isSmoothScrollbarEnabled();
        mLayoutManager.setStackFromEnd(true);
        mLayoutManager.setReverseLayout(true);
        recyclerView_myUpload.setLayoutManager(mLayoutManager);

        recyclerView_myUpload.setAdapter(postAdapterMine);
        postAdapterMine.notifyDataSetChanged();


        //onCreate시 업로드한 게시물 추려서 리스트업
         mDatabase.getReference().child("user_contents").addValueEventListener(new ValueEventListener() {
            ArrayList<String> key = new ArrayList<>();
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                mDatabaseUser.getReference().child("users").child(mFireBaseUser.getUid()).child("uploadContent").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshots) {
                        Map<String, Object> uploadContent = (Map<String, Object>) dataSnapshots.getValue();
                        int tempCount = 0;

                        if (uploadContent == null) {
                            textView_myUpload.setVisibility(View.VISIBLE);
                            recyclerView_myUpload.setVisibility(View.GONE);
//                            Toast toast = Toast.makeText(getApplicationContext(), "진행하신 투표가 아직 없습니다!", Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
//                            toast.show();
                        } else {
                            textView_myUpload.setVisibility(View.GONE);
                            recyclerView_myUpload.setVisibility(View.VISIBLE);
                            Set set = uploadContent.keySet();
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
                Intent intentHome = new Intent(MyUploadActivity.this, HomeActivity.class);
                intentHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentHome);
                break;

            case R.id.menu_refresh:
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }
//        onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
