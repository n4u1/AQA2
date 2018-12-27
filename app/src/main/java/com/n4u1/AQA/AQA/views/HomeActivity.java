package com.n4u1.AQA.AQA.views;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.n4u1.AQA.AQA.R;
import com.n4u1.AQA.AQA.dialog.MustLoginDialog;
import com.n4u1.AQA.AQA.dialog.NotEmailDialog;
import com.n4u1.AQA.AQA.dialog.NotFoundGUIDDialog;
import com.n4u1.AQA.AQA.dialog.NoticeHomeDialog;
import com.n4u1.AQA.AQA.dialog.ShareDialog;
import com.n4u1.AQA.AQA.models.ContentDTO;
import com.n4u1.AQA.AQA.recyclerview.PostAdapter;
import com.n4u1.AQA.AQA.util.NotificationJobService;
import com.n4u1.AQA.AQA.util.OnLoadMoreListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.n4u1.AQA.AQA.util.ShareContent;
import com.tomer.fadingtextview.BuildConfig;
import com.tomer.fadingtextview.FadingTextView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInDownAnimator;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;


public class HomeActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener,
        ShareDialog.ShareDialogListener, NoticeHomeDialog.NoticeHomeDialogListener {

    public static final int ACTIVITY_BACK_VERRIFICATION = 98765432;
    private FirebaseDatabase mDatabase;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth auth;
    protected Handler handler;
    private RecyclerView recyclerView_home;
    private PostAdapter postAdapter;
    final ArrayList<ContentDTO> contentDTOS = new ArrayList<>();
    final ArrayList<ContentDTO> issueContentDTOS = new ArrayList<>();
    private long backPressedTime = 0;
    final ArrayList<String> tempKey = new ArrayList<>();
    final ArrayList<String> issueContents_ = new ArrayList<>();
    private ShareContent shareContent = new ShareContent();
    private long issueRange;

    FadingTextView fadingTextView;
    String[] issueContents = new String[5];

    RelativeLayout relativeLayout_issue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        setContentView(R.layout.activity_home);


        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_aqa);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(null);
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
        auth = FirebaseAuth.getInstance();
        MobileAds.initialize(this, "ca-app-pub-1854873514128645/2926654312");

//        SlideInUpAnimator animatorUp = new SlideInUpAnimator(new OvershootInterpolator(1f));
        SlideInDownAnimator animatorDown = new SlideInDownAnimator(new OvershootInterpolator(1f));
        recyclerView_home.setHasFixedSize(true);
//        recyclerView_home.setItemAnimator(animatorUp);
        recyclerView_home.setItemAnimator(animatorDown);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLayoutManager.isSmoothScrollbarEnabled();
        mLayoutManager.setStackFromEnd(true);
        recyclerView_home.setLayoutManager(mLayoutManager);
        postAdapter = new PostAdapter(this, contentDTOS, recyclerView_home);
        recyclerView_home.setAdapter(new AlphaInAnimationAdapter(postAdapter));
        postAdapter.notifyDataSetChanged();
        AdView adView = findViewById(R.id.adView);


        //시작시 권한요청
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (!user.isAnonymous()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 0);
            }
        }


        //실시간 이슈 체크하는 시간범위를 db에서 가져오기
        mDatabase.getReference().child("issueRange").child("inputRange").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                issueRange = Long.parseLong(dataSnapshot.getValue().toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //노티 백그라운드 자동 실행
        backgroundNotify();


        //광고넣기
        AdRequest adRequest = new AdRequest.Builder()
//                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
//                .addTestDevice("C39C4F095E193D0C5E7BBCB91B89B469")  // TestDeviceId
                .build();
        adView.loadAd(adRequest);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                Log.d("lkj onAdLoaded", "onAdLoaded");
            }

            @Override
            public void onAdClosed() {
                Log.d("lkj onAdClosed", "onAdClosed");
            }
        });


        //실시간 투표순위 X개
        //fadingTextView init
        String[] tempString = {" ", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
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
//                Log.d("lkj issueData", String.valueOf(issueDate_));
                int filteringCount = 0;
                ArrayList<String> filterIssueDate = new ArrayList<>();


//                Log.d("lkj IssueRange2", String.valueOf(issueRange));
//                600000 = 600초 = 10분
//                6000000 = 6000초 = 1시간40분
//                3600000 = 3600초 = 1시간
//                10800000 = 10800 = 3시간
//                21600000 = 21600 = 6시간
//                43200000 = 43200 = 12시간
//                86400000 = 86400 = 1일
//                259200000 = 259200 = 3일
//                1909600000 = 1909600 = 22일2시간
                for (int i = 0; i < dataSnapshot.getChildrenCount(); i++) {
                    if (issueLong.get(i) > issueDate_ - issueRange) {
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
                    for (int i = resultMap_.size() - 1; i > resultMap_.size() - 11; i--) {
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

                            try {
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
                            } catch (Exception e) {

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
                            bundle.putString("previewerGuid", getIntent().getStringExtra("guid"));
                            bundle.putInt("itemViewType", issueContentDTOS.get(i).itemViewType);
                            bundle.putInt("contentHit", issueContentDTOS.get(i).contentHit);
                            intent.putExtras(bundle);
                            getApplicationContext().startActivity(intent);
                        }
                        if (issueContentDTOS.get(i).getPollMode().equals("단일 투표")) {
                            Intent intent = new Intent(getApplicationContext(), PollSingleActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("contentKey", issueContentDTOS.get(i).contentKey);
                            bundle.putString("previewerGuid", getIntent().getStringExtra("guid"));
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
        //init recyclerView
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
                    try {
                        contentDTOS.add(contentDTOSTemp.get(i));
                    } catch (Exception e) {

                    }
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
                            try {
                                contentDTOS.add(contentDTOSTemp.get(i));
                            } catch (Exception e) {

                            }
                        }
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
                if (user.isAnonymous()) {
                    MustLoginDialog mustLoginDialog = new MustLoginDialog();
                    mustLoginDialog.show(getSupportFragmentManager(), "mustLoginDialog");
                } else {
                    int permissionCheck_storage = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
                    int permissionCheck_camera = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);
                    if (permissionCheck_camera == PackageManager.PERMISSION_DENIED || permissionCheck_storage == PackageManager.PERMISSION_DENIED) {
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 0);
                    } else {
                        Intent intent = new Intent(HomeActivity.this, UserContentsUploadActivity.class);
                        startActivity(intent);
                    }
                }

            }
        });


        //다이나믹 링크 받기
        FirebaseDynamicLinks.getInstance().getDynamicLink(getIntent())
                .addOnSuccessListener(new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        if (pendingDynamicLinkData == null) {
                            return;
                        }

                        Uri deepLink = pendingDynamicLinkData.getLink();
                        String contentKey = deepLink.getQueryParameter("contentKey");
                        String pollMode = deepLink.getQueryParameter("pollMode");
//                        Log.d("lkj key???", contentKey);

                        if (!TextUtils.isEmpty(contentKey)) {
                            if (pollMode.equals("single")) {
                                Intent intent = new Intent(HomeActivity.this, PollSingleActivity.class);
                                intent.putExtra("contentKey", contentKey);
//                                Log.d("lkj key?????", contentKey);
//                                Log.d("lkj mode?????", pollMode);
                                startActivity(intent);
                            }
                            if (pollMode.equals("ranking")) {
                                Intent intent = new Intent(HomeActivity.this, PollRankingActivity.class);
                                intent.putExtra("contentKey", contentKey);
//                                Log.d("lkj key???", contentKey);
//                                Log.d("lkj mode???", pollMode);
                                startActivity(intent);
                            }

                        }
                    }
                });

        //노티 데이터 받기
        String notiDataContentKey = getIntent().getStringExtra("contentKey");
        String notiDataPollMode = getIntent().getStringExtra("mode");
        if (notiDataContentKey != null | notiDataPollMode != null) {
            Log.d("Data in intent", notiDataContentKey);
            Log.d("Data in intent", notiDataPollMode);


            if (notiDataPollMode.equals("single")) {
                Intent intent = new Intent(HomeActivity.this, PollSingleActivity.class);
                intent.putExtra("contentKey", notiDataContentKey);
                startActivity(intent);
            }
            if (notiDataPollMode.equals("ranking")) {
                Intent intent = new Intent(HomeActivity.this, PollRankingActivity.class);
                intent.putExtra("contentKey", notiDataContentKey);
//                Log.d("lkj key???", notiDataContentKey);
//                Log.d("lkj mode???", notiDataPollMode);
                startActivity(intent);
            }

        }

    }


    //실시간투표 fadingTextView 세팅
    private void onIssueContents(String[] issueContents) {
        deleteCache(getApplicationContext());
        fadingTextView.forceRefresh();
        fadingTextView.setTimeout(4, TimeUnit.SECONDS);
        if (!BuildConfig.DEBUG) {
            fadingTextView.setTexts(issueContents);
        }
    }

    public void backRefresh() {
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
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

        LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<>();

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

        long FINISH_INTERVAL_TIME = 2000;
        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
//            super.onBackPressed();
            Activity activity = this;
            activity.finish();
        } else {
            backPressedTime = tempTime;
            Toast.makeText(this, " '뒤로' 버튼을 한번더 누르시면 종료 됩니다.", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onResume() {
        Log.d("lkj onResume", "onResume()");

        super.onResume();

    }

    @Override
    public void onRefresh() {

        Log.d("lkj onRefresh", "onRefresh()");

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        onRefresh();

        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
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
//
//            case R.id.menu_home:
//                resetActivity();
//                finish();
//                overridePendingTransition(0, 0);
//                startActivity(getIntent());
//                overridePendingTransition(0, 0);
//                break;

            case R.id.menu_mine:
                Intent intentMine = new Intent(HomeActivity.this, MineActivity.class);
                startActivity(intentMine);
                break;

            case R.id.menu_share:
                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                intent.setType("text/plain");
//                     Set default text message
//                     카톡, 이메일, MMS 다 이걸로 설정 가능
                String subject = "하나만 선택해 어서! AQA!\n";
                String text = shareContent.getShareUrl();
                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                intent.putExtra(Intent.EXTRA_TEXT, text);
//                     Title of intent
                Intent chooser = Intent.createChooser(intent, "공유하기");
                startActivity(chooser);
//                firebaseDatabase.getReference().child("users").child(auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        Map<String, Object> users = (Map<String, Object>) dataSnapshot.getValue();
//                        String id = String.valueOf(users.get("userId"));
//                        ShareDialog shareDialog = ShareDialog.newInstance(id);
//                        shareDialog.show(getSupportFragmentManager(), "shareDialog");
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
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


    //공유하기 콜백
    @Override
    public void ShareDialogCallback(String string) {
        switch (string) {
            case "공유하기":
                try {
//                    test 버전
//                    Toast toast = Toast.makeText(this, "정식 버전 되면 많이 해주세요!!", Toast.LENGTH_SHORT);
//                    toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
//                    toast.show();


//                    real 버전
                    Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                    intent.setType("text/plain");
//                     Set default text message
//                     카톡, 이메일, MMS 다 이걸로 설정 가능
                    String subject = "하나만 선택해 어서! AQA!";
                    String text = shareContent.getShareUrl();
                    intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                    intent.putExtra(Intent.EXTRA_TEXT, text);
//                     Title of intent
                    Intent chooser = Intent.createChooser(intent, "공유하기");
                    startActivity(chooser);


//                    AQA이미지 공유기능
//                    String type = "image/*";
//                    Bitmap shareBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.aqacustom2);
//                    File outputFile = new File(getApplicationContext().getCacheDir(), "AQA" + ".png");
//                    FileOutputStream outPutStream = new FileOutputStream(outputFile);
//                    shareBitmap.compress(Bitmap.CompressFormat.PNG, 100, outPutStream);
//                    outPutStream.flush();
//                    outPutStream.close();
//                    outputFile.setReadable(true, false);
//                    Uri outputUri = FileProvider.getUriForFile(getApplicationContext(), "com.n4u1.AQA.AQA.fileprovider", outputFile);
//                    Intent share = new Intent(Intent.ACTION_SEND);
//                    share.putExtra(Intent.EXTRA_STREAM, outputUri);
//                    share.setType(type);
//                    startActivity(Intent.createChooser(share, "공유하기"));

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
                    Log.d("lkj shareErr", e.toString());
                }
                Log.d("lkj share", "share");


                break;

            case "인증하기":
                Intent intentShareAuthIntent = new Intent(this, ShareAuthActivity.class);
                startActivity(intentShareAuthIntent);
                break;
        }
    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }


    //test dialog
    public void testDialog() {
        NotFoundGUIDDialog notFoundGUIDDialog = new NotFoundGUIDDialog();
        notFoundGUIDDialog.show(getSupportFragmentManager(), "notFoundGUIDDialog");
    }


    //알람켜기
    private void backgroundNotify() {
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        Job myJob = dispatcher.newJobBuilder()
                // the JobService that will be called
                .setService(NotificationJobService.class)
                // uniquely identifies the job
                .setTag("NotificationJobService")
                // one-off job
                .setRecurring(true)
                // don't persist past a device reboot
                .setLifetime(Lifetime.FOREVER)
                // start between 0 and 60 seconds from now
                .setTrigger(Trigger.executionWindow(10, 20))
                // don't overwrite an existing job with the same tag
                .setReplaceCurrent(true)
                // retry with exponential backoff
                .setRetryStrategy(RetryStrategy.DEFAULT_LINEAR)
                // constraints that need to be satisfied for the job to run


                .build();

        dispatcher.mustSchedule(myJob);
    }

    @Override
    public void NoticeHomeDialogCallback(String string) {
        if (string.equals("확인")) {

        }
    }
}
