package com.n4u1.AQA.AQA.views;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.n4u1.AQA.AQA.R;
import com.n4u1.AQA.AQA.models.ContentDTO;
import com.n4u1.AQA.AQA.recyclerview.PostAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class SearchResultActivity extends AppCompatActivity {

    private String searchCategory = "";
    private String searchTitle = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        setContentView(R.layout.activity_search_result);



        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
            getSupportActionBar().setTitle(null);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        FirebaseDatabase mSortingDatabase = FirebaseDatabase.getInstance();
        searchTitle = getIntent().getStringExtra("searchTitle");
        searchCategory = getIntent().getStringExtra("searchCategory");

        final RecyclerView recyclerView_result = findViewById(R.id.recyclerView_result);
        final ArrayList<ContentDTO> contentDTOS = new ArrayList<>();
//    final PostAdapterMyLike postAdapterMyLike = new PostAdapterMyLike(this, contentDTOSS);
//    final PostAdapterMine postAdapterMine = new PostAdapterMine(this, contentDTOSS);
        final PostAdapter postAdapter = new PostAdapter(this, contentDTOS, recyclerView_result);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLayoutManager.isSmoothScrollbarEnabled();
        mLayoutManager.setStackFromEnd(true);
        mLayoutManager.setReverseLayout(true);
        recyclerView_result.setLayoutManager(mLayoutManager);

        recyclerView_result.setAdapter(postAdapter);
        postAdapter.notifyDataSetChanged();

        if (searchTitle == null) {
            mSortingDatabase.getReference().child("user_contents").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    contentDTOS.clear();
                    Iterator<DataSnapshot> contentDTOIterator = dataSnapshot.getChildren().iterator();
                    while (contentDTOIterator.hasNext()) {
                        ContentDTO contentDTO = contentDTOIterator.next().getValue(ContentDTO.class);
                        if (contentDTO.contentType.contains(searchCategory)) {
                            contentDTOS.add(contentDTO);
                        }
                    }
                    Collections.reverse(contentDTOS);
                    postAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            mSortingDatabase.getReference().child("user_contents").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    contentDTOS.clear();
                    Iterator<DataSnapshot> contentDTOIterator = dataSnapshot.getChildren().iterator();
                    while (contentDTOIterator.hasNext()) {
                        ContentDTO contentDTO = contentDTOIterator.next().getValue(ContentDTO.class);
                        if (contentDTO.title.contains(searchTitle)) {
                            contentDTOS.add(contentDTO);
                        }
                    }
                    postAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_result_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int curId = item.getItemId();
        switch (curId) {
            case R.id.menu_search:
                Intent intentSearch = new Intent(SearchResultActivity.this, SearchHomeActivity.class);
                overridePendingTransition(0, 0);
                startActivity(intentSearch);
                overridePendingTransition(0, 0);
                break;
            case R.id.menu_home:
                Intent intentHome = new Intent(SearchResultActivity.this, HomeActivity.class);
                intentHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentHome);
                break;

            case android.R.id.home:
                onBackPressed();

        }
        return super.onOptionsItemSelected(item);
    }
}
