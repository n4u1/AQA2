package com.n4u1.AQA.AQA.views;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
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

import com.n4u1.AQA.AQA.R;
import com.n4u1.AQA.AQA.models.ContentDTO;
import com.n4u1.AQA.AQA.recyclerview.PostAdapter;
import com.n4u1.AQA.AQA.util.OnLoadMoreListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;


public class HomeActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private FirebaseDatabase mDatabase;
    protected Handler handler;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView recyclerView_home;
    private PostAdapter postAdapter;
    final ArrayList<ContentDTO> contentDTOS = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(" ");
        }
        getSupportActionBar().setIcon(R.mipmap.ic_aqa_custom);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final SwipeRefreshLayout mSwipeRefreshLayout = findViewById(R.id.swipeRFL);

        recyclerView_home = findViewById(R.id.recyclerView_home);

        mDatabase = FirebaseDatabase.getInstance();
        handler = new Handler();
        FloatingActionButton fab_addContent = findViewById(R.id.fab_addContent);


        recyclerView_home.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);//20180730 전날꺼 보기 getApplicationContext()전에 this,?? 였음
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLayoutManager.isSmoothScrollbarEnabled();
        mLayoutManager.setStackFromEnd(true);
//        mLayoutManager.setReverseLayout(true);
        recyclerView_home.setLayoutManager(mLayoutManager);
        postAdapter = new PostAdapter(this, contentDTOS, recyclerView_home);
//        final PostAdapter postAdapter = new PostAdapter(getApplication(), contentDTOS); //20180730 전날꺼 보기 getApplication()전에 this,contentDTOS 였음

        recyclerView_home.setAdapter(postAdapter);
        postAdapter.notifyDataSetChanged();


        //onCreate시 액티비티 최초1회 바인딩
        mDatabase.getReference().child("user_contents").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                contentDTOS.clear();
                ArrayList<ContentDTO> contentDTOSTemp = new ArrayList<>();
                int temp = (int) dataSnapshot.getChildrenCount();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ContentDTO contentDTO = snapshot.getValue(ContentDTO.class);
                    contentDTOSTemp.add(contentDTO);
                }

//                Collections.reverse(contentDTOSTemp);

                Collections.reverse(contentDTOSTemp);

                for (int i = 0; i < 10; i++) {
                    contentDTOS.add(contentDTOSTemp.get(i));
                }

//                Collections.reverse(contentDTOS);


//                postAdapter.notifyItemInserted(contentDTOS.size());
                postAdapter.notifyDataSetChanged();
                recyclerView_home.scrollToPosition(0);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //맨 밑으로 내리면 더 보여주기
        postAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                //add null , so the adapter will check view_type and show progress bar at bottom
                contentDTOS.add(null);
                postAdapter.notifyItemInserted(contentDTOS.size() - 1);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //   remove progress item
                        contentDTOS.remove(contentDTOS.size() - 1);
                        postAdapter.notifyItemRemoved(contentDTOS.size());
                        //add items one by one
//                        final int start = contentDTOS.size();
//                        final int end = start + 5;

                        mDatabase.getReference().child("user_contents").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                ArrayList<ContentDTO> contentDTOSTemp = new ArrayList<>();
                                int start = contentDTOS.size();
                                int end = start + 5;

                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    ContentDTO contentDTO = snapshot.getValue(ContentDTO.class);
                                    contentDTOSTemp.add(contentDTO);
                                }

                                Collections.reverse(contentDTOSTemp);

                                for (int i = start; i <= end; i++) {
                                    try {
                                        if (contentDTOSTemp.get(i) != null) {
                                            contentDTOS.add(contentDTOSTemp.get(i));
                                        }
                                    } catch (Exception e) {
                                        Log.w("lkj exception", e);

                                    }
                                }
//                                if (contentDTOSTemp.size() == contentDTOS.size()) {
//                                    Toast toast = Toast.makeText(getApplicationContext(), "마지막 입니다.", Toast.LENGTH_SHORT);
//                                    toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
//                                    toast.show();
//                                }
                                postAdapter.notifyItemInserted(contentDTOS.size());
                                postAdapter.setLoaded();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                }, 2000);

            }
        });


        //Pull to Refresh 당겨서 새로고침
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mDatabase.getReference().child("user_contents").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        contentDTOS.clear();
                        ArrayList<ContentDTO> contentDTOSTemp = new ArrayList<>();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            ContentDTO contentDTO = snapshot.getValue(ContentDTO.class);
                            contentDTOSTemp.add(contentDTO);
                        }

                        Collections.reverse(contentDTOSTemp);

                        for (int i = 0; i < 10; i++) {
                            contentDTOS.add(contentDTOSTemp.get(i));
                        }


//                        postAdapter.notifyItemInserted(contentDTOS.size());

                        postAdapter.notifyDataSetChanged();
                        recyclerView_home.scrollToPosition(0);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        //글쓰기 FloatingActionButton
        fab_addContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, UserContentsUploadActivity.class);
                startActivity(intent);

            }
        });
    }

    //좋아요 클릭후 HomeActivity 새로고침
    public void resetActivity() {
        mDatabase.getReference().child("user_contents").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                contentDTOS.clear();
                ArrayList<ContentDTO> contentDTOSTemp = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ContentDTO contentDTO = snapshot.getValue(ContentDTO.class);
                    contentDTOSTemp.add(contentDTO);
                }

                Collections.reverse(contentDTOSTemp);

                for (int i = 0; i < 10; i++) {
                    contentDTOS.add(contentDTOSTemp.get(i));
                }

                postAdapter.notifyDataSetChanged();
                recyclerView_home.scrollToPosition(0);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public void onRefresh() {
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int curId = item.getItemId();
        switch (curId) {
            case R.id.menu_search:
                Intent intentSearch = new Intent(HomeActivity.this, SearchHomeActivity.class);
                overridePendingTransition(0, 0);
                startActivity(intentSearch);
                overridePendingTransition(0, 0);
                break;
            case R.id.menu_home:
                resetActivity();
                break;
            case R.id.menu_mine:
                Intent intentMine = new Intent(HomeActivity.this, MineActivity.class);
                startActivity(intentMine);
                break;


        }
//        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

}
