package com.n4u1.AQA.AQA.views;

import android.content.Intent;
import android.graphics.Paint;
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
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.n4u1.AQA.AQA.R;
import com.n4u1.AQA.AQA.models.ContentDTO;
import com.n4u1.AQA.AQA.recyclerview.PostAdapter;
import com.n4u1.AQA.AQA.util.OnLoadMoreListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tomer.fadingtextview.BuildConfig;
import com.tomer.fadingtextview.FadingTextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;


public class HomeActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private FirebaseDatabase mDatabase;
    private FirebaseDatabase firebaseDatabase;
    protected Handler handler;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView recyclerView_home;
    private PostAdapter postAdapter;
    final ArrayList<ContentDTO> contentDTOS = new ArrayList<>();
    final ArrayList<ContentDTO> issueContentDTOS = new ArrayList<>();
    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;
    final ArrayList<String> tempKey = new ArrayList<>();
    final ArrayList<String> issueContents_ = new ArrayList<>();
    FadingTextView fadingTextView;
    String[] issueContents = new String[5];

    RelativeLayout relativeLayout_issue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_aqa_custom);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
        }

        final SwipeRefreshLayout mSwipeRefreshLayout = findViewById(R.id.swipeRFL);
        recyclerView_home = findViewById(R.id.recyclerView_home);
        relativeLayout_issue = findViewById(R.id.relativeLayout_issue);
        mDatabase = FirebaseDatabase.getInstance();
        handler = new Handler();
        FloatingActionButton fab_addContent = findViewById(R.id.fab_addContent);
//        final TextView testText = findViewById(R.id.testText);
        fadingTextView = findViewById(R.id.fadingTextView);
        firebaseDatabase = FirebaseDatabase.getInstance();


        recyclerView_home.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);//20180730 전날꺼 보기 getApplicationContext()전에 this,?? 였음
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLayoutManager.isSmoothScrollbarEnabled();
        mLayoutManager.setStackFromEnd(true);
        recyclerView_home.setLayoutManager(mLayoutManager);
        postAdapter = new PostAdapter(this, contentDTOS, recyclerView_home);
        recyclerView_home.setAdapter(postAdapter);
        postAdapter.notifyDataSetChanged();
        //실시간 투표순위 5개


        //fadingTextview init
        String[] tempString = {" ", "2", "3", "4", "5"};
        fadingTextView.setTimeout(4, TimeUnit.SECONDS);
        if (!BuildConfig.DEBUG) {

            fadingTextView.setTexts(tempString);
        }

        //실시간 투표 목록 가져오기 (issueContents 용)
        firebaseDatabase.getReference().child("issueContents").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> strings = new ArrayList<>();
                ArrayList<String> stringsTemp = new ArrayList<>();
                ArrayList<String> stringsTemp_ = new ArrayList<>();
                ArrayList<String> issueString = new ArrayList<>();
                ArrayList<String> filteringIssueString = new ArrayList<>();
                ArrayList<Long> issueLong = new ArrayList<>();
                HashMap<String, String> issueMap = new HashMap<>();
//                        Map<String, String> resultMap = new HashMap<>();
                LinkedHashMap<String, Integer> resultMap = new LinkedHashMap<>();
                LinkedHashMap<String, Integer> resultMap_ = new LinkedHashMap<>();

                String[] stringsTemp__;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    strings.add(String.valueOf(snapshot.getValue()));
                }
                for (int i = 0; i < dataSnapshot.getChildrenCount(); i++) {

                    stringsTemp_.add(i, strings.get(i).replace("{", ""));
                    stringsTemp.add(i, stringsTemp_.get(i).replace("}", ""));

                }

                stringsTemp__ = stringsTemp.get(0).split("=");

                for (int i = 0; i < dataSnapshot.getChildrenCount(); i++) {
                    stringsTemp__ = stringsTemp.get(i).split("=");
                    issueMap.put(stringsTemp__[0], stringsTemp__[1]);
                    issueString.add(stringsTemp__[0]);

                }

                for (int i = 0; i < dataSnapshot.getChildrenCount(); i++) {
                    issueLong.add(Long.parseLong(issueString.get(i)));
                }


                long issueDate_ = getCurrentDate();
                int filteringCount = 0;
                ArrayList<String> filterIssueDate = new ArrayList<>();

//                6000000 = 600 초 = 10 분
//                60000000 = 6000 초 = 100 분 = 1 시간40분
//                6000000
//                999999999 = 약278시간 = 약11일13시간46분
                for (int i = 0; i < dataSnapshot.getChildrenCount(); i++) {
                    if (issueLong.get(i) > issueDate_ - 999999999) {
                        filterIssueDate.add(String.valueOf(issueLong.get(i)));
                        filteringCount++;
                    }
                }

                for (int i = 0; i < filteringCount; i++) {
                    filteringIssueString.add(filterIssueDate.get(i));

                }

                //해당시간안에 컨텐츠당 투표한 인원 구하기
                for (int i = 0; i < filteringIssueString.size(); i++) {
                    int tmpCount = 0;
                    for (int j = 0; j < filteringIssueString.size(); j++) {
                        if (issueMap.get(filteringIssueString.get(i)).equals(issueMap.get(filteringIssueString.get(j)))) {
                            tmpCount++;
                        }
                    }
                    resultMap.put(issueMap.get(filteringIssueString.get(i)), tmpCount);
                }
                resultMap_ = sortHashMapByValues(resultMap);
                Set key = resultMap_.keySet();

                //실시간 투표수 최상위 x개 가져오기
                //resultMap_.size()-n 이면 n-1개 가져옴
                //resultMap_.size()-3 이면 2개 가져옴
                //resultMap_.size()-4 이면 3개 가져옴
                //resultMap_.size()-5 이면 4개 가져옴
                //tempKey에는 해당 컨텐츠의 key 가 들어감
                try {
                    for (int i = resultMap_.size() - 1; i > resultMap_.size() - 6; i--) {
                        tempKey.add(key.toArray()[i].toString());
                    }
                } catch (Exception e) {
                    Log.w("lkj obr exti", e);
                }

                //homeActivity 툴바 바로 아래 이슈컨텐츠 제목표시
                issueContentDTOS.clear();
                mDatabase.getReference().child("user_contents").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Iterator<DataSnapshot> contentDTOIterator = dataSnapshot.getChildren().iterator();
                        while (contentDTOIterator.hasNext()) {
                            ContentDTO contentDTO = contentDTOIterator.next().getValue(ContentDTO.class);
                            if (contentDTO.contentKey.contains(tempKey.get(0))) {
                                issueContentDTOS.add(contentDTO);
                                issueContents_.add(contentDTO.title);
                            }
                            if (contentDTO.contentKey.contains(tempKey.get(1))) {
                                issueContentDTOS.add(contentDTO);
                                issueContents_.add(contentDTO.title);
                            }
                            if (contentDTO.contentKey.contains(tempKey.get(2))) {
                                issueContentDTOS.add(contentDTO);
                                issueContents_.add(contentDTO.title);
                            }
                            if (contentDTO.contentKey.contains(tempKey.get(3))) {
                                issueContentDTOS.add(contentDTO);
                                issueContents_.add(contentDTO.title);
                            }
                            if (contentDTO.contentKey.contains(tempKey.get(4))) {
                                issueContentDTOS.add(contentDTO);
                                issueContents_.add(contentDTO.title);
                            }
                        }
                        issueContents = issueContents_.toArray(new String[issueContents_.size()]);
                        onIssueContents(issueContents);
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


        //실시간 이슈 클릭시 해당 게시물로 이동
        fadingTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < issueContentDTOS.size(); i++) {
                    if (fadingTextView.getText().equals(issueContentDTOS.get(i).title)) {
                        //PostAdapter 의 movePoll(int position) 내용만 가져옴
                        if (issueContentDTOS.get(i).getPollMode().equals("순위 투표")) {
                            Intent intent = new Intent(getApplicationContext(), PollRankingActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("contentKey", issueContentDTOS.get(i).contentKey);
                            bundle.putInt("itemViewType", issueContentDTOS.get(i).itemViewType);
                            bundle.putInt("contentHit", issueContentDTOS.get(i).contentHit);
                            intent.putExtras(bundle);
                            getApplicationContext().startActivity(intent);
                        }
                        if (issueContentDTOS.get(i).getPollMode().equals("단일 투표")) {
                            Intent intent = new Intent(getApplicationContext(), PollSingleActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("contentKey", issueContentDTOS.get(i).contentKey);
                            bundle.putInt("itemViewType", issueContentDTOS.get(i).itemViewType);
                            bundle.putInt("contentHit", issueContentDTOS.get(i).contentHit);
                            intent.putExtras(bundle);
                            getApplicationContext().startActivity(intent);
                        }
                    }
                }
            }
        });


        //onCreate시 액티비티 최초1회 바인딩
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

    //실시간투표 fadingTextView 세팅
    private void onIssueContents(String[] issueContents) {
        fadingTextView.forceRefresh();
        fadingTextView.setTimeout(4, TimeUnit.SECONDS);
        if (!BuildConfig.DEBUG) {
            fadingTextView.setTexts(issueContents);
        }
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


    private long getCurrentDate() {
        long currentTimeMillis = System.currentTimeMillis();
        return currentTimeMillis;
    }

    //실시간 투표 목록 파싱용
    public LinkedHashMap<String, Integer> sortHashMapByValues(
            HashMap<String, Integer> passedMap) {
        List<String> mapKeys = new ArrayList<>(passedMap.keySet());
        List<Integer> mapValues = new ArrayList<>(passedMap.values());
        Collections.sort(mapValues);
        Collections.sort(mapKeys);

        LinkedHashMap<String, Integer> sortedMap =
                new LinkedHashMap<>();

        Iterator<Integer> valueIt = mapValues.iterator();
        while (valueIt.hasNext()) {
            int val = valueIt.next();
            Iterator<String> keyIt = mapKeys.iterator();

            while (keyIt.hasNext()) {
                String key = keyIt.next();
                int comp1 = passedMap.get(key);
                int comp2 = val;

                if (comp1 == comp2) {
                    keyIt.remove();
                    sortedMap.put(key, val);
                    break;
                }
            }
        }
        return sortedMap;
    }

    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
            super.onBackPressed();
        } else {
            backPressedTime = tempTime;
            Toast.makeText(this, " '뒤로' 버튼을 한번더 누르시면 종료 됩니다.", Toast.LENGTH_SHORT).show();
        }
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
//                resetActivity();
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
                break;

            case R.id.menu_mine:
                Intent intentMine = new Intent(HomeActivity.this, MineActivity.class);
                startActivity(intentMine);
                break;

            case android.R.id.home:
                Intent intentAqa = new Intent(HomeActivity.this, HomeActivity.class);
                overridePendingTransition(0, 0);
                intentAqa.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                overridePendingTransition(0, 0);
                startActivity(intentAqa);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

}
