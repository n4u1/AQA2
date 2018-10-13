package com.n4u1.AQA.AQA.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

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
import java.util.Iterator;

public class MyReplyContentsActivity extends AppCompatActivity {

    private FirebaseDatabase mDatabase;
    private FirebaseDatabase mDatabaseUser;
    private FirebaseUser mFireBaseUser;

    final ArrayList<ContentDTO> contentDTOS = new ArrayList<>();
    final PostAdapterMine postAdapterMine = new PostAdapterMine(this, contentDTOS);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reply_contents);


        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_aqa_custom);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle("");
        }

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseUser = FirebaseDatabase.getInstance();
        mFireBaseUser = FirebaseAuth.getInstance().getCurrentUser();

        final RecyclerView recyclerViewList = findViewById(R.id.recyclerView_home);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLayoutManager.isSmoothScrollbarEnabled();
        mLayoutManager.setStackFromEnd(true);
        mLayoutManager.setReverseLayout(true);
        recyclerViewList.setLayoutManager(mLayoutManager);

        recyclerViewList.setAdapter(postAdapterMine);
        postAdapterMine.notifyDataSetChanged();



        //onCreate시 리플단 게시글 추려서 리스트업
        mDatabase.getReference().child("user_contents").addListenerForSingleValueEvent(new ValueEventListener() {
            int tempCount = 0;
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                mDatabaseUser.getReference().child("users").child(mFireBaseUser.getUid()).child("reply").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshots) {
                        ArrayList<String> replyKey = new ArrayList<>();
                        Iterator<DataSnapshot> replyKeyIterator = dataSnapshots.getChildren().iterator();
                        while (replyKeyIterator.hasNext()) {
                            replyKey.add(replyKeyIterator.next().getKey());
                            tempCount++;
                        }

                        contentDTOS.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            ContentDTO contentDTO = snapshot.getValue(ContentDTO.class);
                            for(int i = 0; i < tempCount; i++) {
                                if (contentDTO.getContentKey().contains(replyKey.get(i))) {
                                    contentDTOS.add(contentDTO);
                                }
                            }
                        }
                        postAdapterMine.notifyDataSetChanged();
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




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mylike_menu, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int curId = item.getItemId();
        switch (curId) {
            case R.id.menu_home:
                Intent intentHome = new Intent(MyReplyContentsActivity.this, HomeActivity.class);
                startActivity(intentHome);
                break;
            case R.id.menu_back:
                break;
            case android.R.id.home:
                Intent intentAqa = new Intent(MyReplyContentsActivity.this, HomeActivity.class);
                intentAqa.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentAqa);


        }
//        onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
