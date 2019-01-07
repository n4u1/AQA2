package com.n4u1.AQA.AQA.views;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.n4u1.AQA.AQA.R;
import com.n4u1.AQA.AQA.dialog.AlarmDoneDialog;
import com.n4u1.AQA.AQA.dialog.CannotDeleteDialog;
import com.n4u1.AQA.AQA.dialog.ContentDeleteDialog;
import com.n4u1.AQA.AQA.dialog.DeleteModificationActivity;
import com.n4u1.AQA.AQA.dialog.MustLoginDialog;
import com.n4u1.AQA.AQA.dialog.MustPollDialog;
import com.n4u1.AQA.AQA.dialog.PollButtonInfoDialog;
import com.n4u1.AQA.AQA.dialog.PollInitInfoDialog;
import com.n4u1.AQA.AQA.dialog.PollResultAnonymousDialog;
import com.n4u1.AQA.AQA.dialog.PollResultRankingDialog;
import com.n4u1.AQA.AQA.dialog.PollRankingChoiceActivity;
import com.n4u1.AQA.AQA.dialog.ShareDialog;
import com.n4u1.AQA.AQA.dialog.UserAlarmDialog;
import com.n4u1.AQA.AQA.models.ContentDTO;
import com.n4u1.AQA.AQA.models.ReplyDTO;
import com.n4u1.AQA.AQA.recyclerview.ReplyAdapter;
import com.n4u1.AQA.AQA.util.GlideApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.n4u1.AQA.AQA.util.ImageSaver;
import com.n4u1.AQA.AQA.util.ShareContent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class PollRankingActivity extends AppCompatActivity implements View.OnClickListener,
        ContentDeleteDialog.ContentDeleteDialogListener, PollButtonInfoDialog.PollButtonInfoDialogListener,
        ShareDialog.ShareDialogListener {


    private boolean ACTIVITY_REPLY_FLAG;
    private boolean ACTIVITY_BESTREPLY_FLAG;
    private int pickCandidate = 0;
    private int contentAmount;

    private FirebaseAuth auth;
    private DatabaseReference mDatabaseReference;


    private DatabaseReference mDatabaseReferencePicker;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseDatabase likeFirebaseDatabase;
    private String replyKey;
    private String contentKey;


    final ArrayList<ReplyDTO> replyDTOS = new ArrayList<>();
    private HashMap<String, String> issueMap = new HashMap<>();
    private HashMap<ImageView, String> userChoiceMap = new HashMap<>();
    boolean checkUserHitContent = false;
    int contentHit;
    String tmpContentKey, tmpPollMode;

    RelativeLayout pollActivity_relativeLayout_addImage_1;
    FloatingActionButton pollActivity_fab_result;
    TextView pollActivity_textView_title, pollActivity_textView_description,
            pollActivity_textView_pollMode, pollActivity_textView_contentType, pollActivity_textView_date;
    ImageView pollActivity_imageView_userAddContent_1, pollActivity_imageView_userAddContent_2,
            pollActivity_imageView_userAddContent_3, pollActivity_imageView_userAddContent_4,
            pollActivity_imageView_userAddContent_5, pollActivity_imageView_userAddContent_6,
            pollActivity_imageView_userAddContent_7, pollActivity_imageView_userAddContent_8,
            pollActivity_imageView_userAddContent_9, pollActivity_imageView_userAddContent_10;

    ImageView pollActivity_imageView_userAddContent_n1, pollActivity_imageView_userAddContent_n2,
            pollActivity_imageView_userAddContent_n3, pollActivity_imageView_userAddContent_n4,
            pollActivity_imageView_userAddContent_n5, pollActivity_imageView_userAddContent_n6,
            pollActivity_imageView_userAddContent_n7, pollActivity_imageView_userAddContent_n8,
            pollActivity_imageView_userAddContent_n9, pollActivity_imageView_userAddContent_n10;

    ImageView pollActivity_imageView_choice_1, pollActivity_imageView_choice_2,
            pollActivity_imageView_choice_3, pollActivity_imageView_choice_4,
            pollActivity_imageView_choice_5, pollActivity_imageView_choice_6,
            pollActivity_imageView_choice_7, pollActivity_imageView_choice_8,
            pollActivity_imageView_choice_9, pollActivity_imageView_choice_10,
            pollActivity_imageView_replyView_1, pollActivity_imageView_replyView_2;

    ImageView pollActivity_imageView_userClass, imageView_userClass0, imageView_userClass1, imageView_userClass2;
    ImageView pollActivity_button_replySend, pollActivity_imageView_share;
    EditText pollActivity_editText_reply;
    RecyclerView pollActivity_recyclerView_reply;
    RelativeLayout pollActivity_relativeLayout_reply;

    LinearLayout linearLayout_bestReply0, linearLayout_bestReply1, linearLayout_bestReply2, pollActivity_linearLayout_reply;
    TextView bestReply_id0, bestReply_id1, bestReply_id2, bestReply_reply0, bestReply_reply1, bestReply_reply2,
            bestReply_date0, bestReply_date1, bestReply_date2, bestReply_likeCount0, bestReply_likeCount1, bestReply_likeCount2;
    ImageView bestReply_thumbImg0, bestReply_thumbImg1, bestReply_thumbImg2;

    TextView pollActivity_textView_check_1, pollActivity_textView_check_2,
            pollActivity_textView_check_3, pollActivity_textView_check_4,
            pollActivity_textView_check_5, pollActivity_textView_check_6,
            pollActivity_textView_check_7, pollActivity_textView_check_8,
            pollActivity_textView_check_9, pollActivity_textView_check_10,
            pollActivity_textView_userId, pollActivity_textView_state;

    TextView pollActivity_textView_hitCount, pollActivity_textView_likeCount, pollActivity_textView_contentId, pollActivity_textView_replyCount;
    ImageView pollActivity_imageView_state, pollActivity_imageView_like, pollActivity_imageView_alarm;

    String shareUrl;
    AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        setContentView(R.layout.activity_poll_ranking);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(null);
        }


        final Uri data = getIntent().getData();
        if (data != null) {
            contentKey = data.getQueryParameter("contentKey");
            Log.d("lkj intent Test1", data.getQueryParameter("contentKey"));
        } else {
            contentKey = getIntent().getStringExtra("contentKey");
            Log.d("lkj intent Test2", getIntent().getStringExtra("contentKey"));
        }

        final ShareContent shareContent = new ShareContent(contentKey, "ranking");
        MobileAds.initialize(this, "ca-app-pub-1854873514128645/6589856710");
        adView = findViewById(R.id.adView);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("user_contents").child(contentKey);
        DatabaseReference mDatabaseReferenceAlarm;
        mDatabaseReferencePicker = FirebaseDatabase.getInstance().getReference("users");
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        likeFirebaseDatabase = FirebaseDatabase.getInstance();
        pollActivity_relativeLayout_addImage_1 = findViewById(R.id.pollActivity_relativeLayout_addImage_1);
        pollActivity_imageView_alarm = findViewById(R.id.pollActivity_imageView_alarm);
        pollActivity_imageView_replyView_1 = findViewById(R.id.pollActivity_imageView_replyView_1);
        pollActivity_imageView_replyView_2 = findViewById(R.id.pollActivity_imageView_replyView_2);

        pollActivity_textView_state = findViewById(R.id.pollActivity_textView_state);
        pollActivity_imageView_userClass = findViewById(R.id.pollActivity_imageView_userClass);
        imageView_userClass0 = findViewById(R.id.imageView_userClass0);
        imageView_userClass1 = findViewById(R.id.imageView_userClass1);
        imageView_userClass2 = findViewById(R.id.imageView_userClass2);
        pollActivity_textView_userId = findViewById(R.id.pollActivity_textView_userId);
        pollActivity_fab_result = findViewById(R.id.pollActivity_fab_result);
        pollActivity_textView_title = findViewById(R.id.pollActivity_textView_title);
        pollActivity_textView_description = findViewById(R.id.pollActivity_textView_description);
        pollActivity_textView_contentType = findViewById(R.id.pollActivity_textView_contentType);
        pollActivity_textView_pollMode = findViewById(R.id.pollActivity_textView_pollMode);
        pollActivity_textView_date = findViewById(R.id.pollActivity_textView_date);
        pollActivity_textView_contentId = findViewById(R.id.pollActivity_textView_contentId);
        pollActivity_textView_replyCount = findViewById(R.id.pollActivity_textView_replyCount);

        pollActivity_imageView_userAddContent_1 = findViewById(R.id.pollActivity_imageView_userAddContent_1);
        pollActivity_imageView_userAddContent_2 = findViewById(R.id.pollActivity_imageView_userAddContent_2);
        pollActivity_imageView_userAddContent_3 = findViewById(R.id.pollActivity_imageView_userAddContent_3);
        pollActivity_imageView_userAddContent_4 = findViewById(R.id.pollActivity_imageView_userAddContent_4);
        pollActivity_imageView_userAddContent_5 = findViewById(R.id.pollActivity_imageView_userAddContent_5);
        pollActivity_imageView_userAddContent_6 = findViewById(R.id.pollActivity_imageView_userAddContent_6);
        pollActivity_imageView_userAddContent_7 = findViewById(R.id.pollActivity_imageView_userAddContent_7);
        pollActivity_imageView_userAddContent_8 = findViewById(R.id.pollActivity_imageView_userAddContent_8);
        pollActivity_imageView_userAddContent_9 = findViewById(R.id.pollActivity_imageView_userAddContent_9);
        pollActivity_imageView_userAddContent_10 = findViewById(R.id.pollActivity_imageView_userAddContent_10);
        pollActivity_relativeLayout_reply = findViewById(R.id.pollActivity_relativeLayout_reply);
        pollActivity_recyclerView_reply = findViewById(R.id.pollActivity_recyclerView_reply);
        pollActivity_editText_reply = findViewById(R.id.pollActivity_editText_reply);
        pollActivity_button_replySend = findViewById(R.id.pollActivity_button_replySend);

        pollActivity_linearLayout_reply = findViewById(R.id.pollActivity_linearLayout_reply);
        linearLayout_bestReply0 = findViewById(R.id.linearLayout_bestReply0);
        linearLayout_bestReply1 = findViewById(R.id.linearLayout_bestReply1);
        linearLayout_bestReply2 = findViewById(R.id.linearLayout_bestReply2);
        bestReply_thumbImg0 = findViewById(R.id.bestReply_thumbImg0);
        bestReply_thumbImg1 = findViewById(R.id.bestReply_thumbImg1);
        bestReply_thumbImg2 = findViewById(R.id.bestReply_thumbImg2);
        bestReply_id0 = findViewById(R.id.bestReply_id0);
        bestReply_id1 = findViewById(R.id.bestReply_id1);
        bestReply_id2 = findViewById(R.id.bestReply_id2);
        bestReply_reply0 = findViewById(R.id.bestReply_reply0);
        bestReply_reply1 = findViewById(R.id.bestReply_reply1);
        bestReply_reply2 = findViewById(R.id.bestReply_reply2);
        bestReply_date0 = findViewById(R.id.bestReply_date0);
        bestReply_date1 = findViewById(R.id.bestReply_date1);
        bestReply_date2 = findViewById(R.id.bestReply_date2);
        bestReply_likeCount0 = findViewById(R.id.bestReply_likeCount0);
        bestReply_likeCount1 = findViewById(R.id.bestReply_likeCount1);
        bestReply_likeCount2 = findViewById(R.id.bestReply_likeCount2);


        pollActivity_textView_check_1 = findViewById(R.id.pollActivity_textView_check_1);
        pollActivity_textView_check_2 = findViewById(R.id.pollActivity_textView_check_2);
        pollActivity_textView_check_3 = findViewById(R.id.pollActivity_textView_check_3);
        pollActivity_textView_check_4 = findViewById(R.id.pollActivity_textView_check_4);
        pollActivity_textView_check_5 = findViewById(R.id.pollActivity_textView_check_5);
        pollActivity_textView_check_6 = findViewById(R.id.pollActivity_textView_check_6);
        pollActivity_textView_check_7 = findViewById(R.id.pollActivity_textView_check_7);
        pollActivity_textView_check_8 = findViewById(R.id.pollActivity_textView_check_8);
        pollActivity_textView_check_9 = findViewById(R.id.pollActivity_textView_check_9);
        pollActivity_textView_check_10 = findViewById(R.id.pollActivity_textView_check_10);


        pollActivity_textView_hitCount = findViewById(R.id.pollActivity_textView_hitCount);
        pollActivity_textView_likeCount = findViewById(R.id.pollActivity_textView_likeCount);
        pollActivity_imageView_state = findViewById(R.id.pollActivity_imageView_state);
        pollActivity_imageView_like = findViewById(R.id.pollActivity_imageView_like);


        pollActivity_imageView_userAddContent_n1 = findViewById(R.id.pollActivity_imageView_userAddContent_n1);
        pollActivity_imageView_userAddContent_n2 = findViewById(R.id.pollActivity_imageView_userAddContent_n2);
        pollActivity_imageView_userAddContent_n3 = findViewById(R.id.pollActivity_imageView_userAddContent_n3);
        pollActivity_imageView_userAddContent_n4 = findViewById(R.id.pollActivity_imageView_userAddContent_n4);
        pollActivity_imageView_userAddContent_n5 = findViewById(R.id.pollActivity_imageView_userAddContent_n5);
        pollActivity_imageView_userAddContent_n6 = findViewById(R.id.pollActivity_imageView_userAddContent_n6);
        pollActivity_imageView_userAddContent_n7 = findViewById(R.id.pollActivity_imageView_userAddContent_n7);
        pollActivity_imageView_userAddContent_n8 = findViewById(R.id.pollActivity_imageView_userAddContent_n8);
        pollActivity_imageView_userAddContent_n9 = findViewById(R.id.pollActivity_imageView_userAddContent_n9);
        pollActivity_imageView_userAddContent_n10 = findViewById(R.id.pollActivity_imageView_userAddContent_n10);


        pollActivity_imageView_userAddContent_1.setOnClickListener(this);
        pollActivity_imageView_userAddContent_2.setOnClickListener(this);
        pollActivity_imageView_userAddContent_3.setOnClickListener(this);
        pollActivity_imageView_userAddContent_4.setOnClickListener(this);
        pollActivity_imageView_userAddContent_5.setOnClickListener(this);
        pollActivity_imageView_userAddContent_6.setOnClickListener(this);
        pollActivity_imageView_userAddContent_7.setOnClickListener(this);
        pollActivity_imageView_userAddContent_8.setOnClickListener(this);
        pollActivity_imageView_userAddContent_9.setOnClickListener(this);
        pollActivity_imageView_userAddContent_10.setOnClickListener(this);
        pollActivity_textView_check_1.setOnClickListener(this);
        pollActivity_textView_check_2.setOnClickListener(this);
        pollActivity_textView_check_3.setOnClickListener(this);
        pollActivity_textView_check_4.setOnClickListener(this);
        pollActivity_textView_check_5.setOnClickListener(this);
        pollActivity_textView_check_6.setOnClickListener(this);
        pollActivity_textView_check_7.setOnClickListener(this);
        pollActivity_textView_check_8.setOnClickListener(this);
        pollActivity_textView_check_9.setOnClickListener(this);
        pollActivity_textView_check_10.setOnClickListener(this);



        //처음사용자 Q 버튼 알려주기
        if (user.isAnonymous()) {
            SharedPreferences pref = getSharedPreferences("com.n4u1.AQA", MODE_PRIVATE);
            String previewer = pref.getString("com.n4u1.AQA.previewer", null);
            try {
                if (previewer.equals("previewerFirst")) {
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("com.n4u1.AQA.previewer", "previewer");
                    editor.commit();
                    PollInitInfoDialog pollInitInfoDialog = new PollInitInfoDialog();
                    pollInitInfoDialog.show(getSupportFragmentManager(), "pollInitInfoDialog");
                }
            } catch (Exception e) {

            }

        } else {
            firebaseDatabase.getReference().child("users").child(auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        Map<String, Object> user = (Map<String, Object>) dataSnapshot.getValue();
                        String flag = String.valueOf(user.get("pollInitInfo"));
                        if (flag.equals("true")) {
                            PollInitInfoDialog pollInitInfoDialog = new PollInitInfoDialog();
                            pollInitInfoDialog.show(getSupportFragmentManager(), "pollInitInfoDialog");
                            firebaseDatabase.getReference().child("users").child(auth.getCurrentUser().getUid()).child("pollInitInfo").setValue("false");
                        }
                    } catch (Exception e) {

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }



        //광고넣기
        AdRequest adRequest = new AdRequest.Builder()
//                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
//                .addTestDevice("C39C4F095E193D0C5E7BBCB91B89B469")  // TestDeviceId
                .build();
        adView.loadAd(adRequest);
        adView.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded() {
                Log.d("lkj onAdLoaded", "onAdLoaded");
            }

            @Override
            public void onAdClosed() {
                Log.d("lkj onAdClosed", "onAdClosed");
            }
        });


        //알람설정 클릭
        pollActivity_imageView_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (data != null) {
                    contentKey = data.getQueryParameter("contentKey");
                } else {
                    contentKey = getIntent().getStringExtra("contentKey");
                }
                Intent intentShowMore = new Intent(PollRankingActivity.this, UserAlarmDialog.class);
                intentShowMore.putExtra("pollKey", contentKey);
                intentShowMore.putExtra("hitCount", contentHit);
                startActivityForResult(intentShowMore, 20000);
            }
        });


        //reply item click listener 댓글 클릭 리스너
        final ReplyAdapter replyAdapter = new ReplyAdapter(getApplicationContext(), replyDTOS, new ReplyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final View view, final int position) {
                if (!user.isAnonymous()) {
                    FirebaseDatabase replyDatabase;
                    replyDatabase = FirebaseDatabase.getInstance();
                    replyDatabase.getReference().child("users").child(auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Map<String, Object> users = (Map<String, Object>) dataSnapshot.getValue();
                            if (replyDTOS.get(position).getId().equals(users.get("userId").toString())) {
                                if (view.getTag().equals("replyAdapter_relativeLayout_like")) { //댓글 좋아요 클릭
                                    Toast.makeText(getApplicationContext(), users.get("userId").toString() + "님 댓글 입니다.", Toast.LENGTH_SHORT).show();
                                } else if (view.getTag().equals("replyAdapter_relativeLayout_main")) { //댓글 클릭, 삭제or수정
                                    firebaseDatabase.getReference().child("reply").child(contentKey).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            replyDTOS.clear();
                                            ArrayList<ReplyDTO> replyDTOTemp = new ArrayList<>();

                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                ReplyDTO replyDTO = snapshot.getValue(ReplyDTO.class);
                                                replyDTOTemp.add(replyDTO);
                                            }
//                                Collections.reverse(replyDTOTemp);
                                            replyDTOS.addAll(replyDTOTemp);
//                                int adminAuthId = replyDTOS.size() - position - 1;
                                            String replyKey = replyDTOTemp.get(position).getReplyKey();

                                            //수정하기, 선택하기 액티비티(다이얼로그)띄우기
                                            Intent intent = new Intent(PollRankingActivity.this, DeleteModificationActivity.class);
                                            intent.putExtra("replyKey", replyKey);
                                            startActivityForResult(intent, 10000);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }
                            //본인이 아닌 댓글,좋아요 클릭시
                            else {
                                if (view.getTag().equals("replyAdapter_relativeLayout_like")) { //댓글 좋아요 클릭
                                    firebaseDatabase.getReference().child("reply").child(contentKey).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            replyDTOS.clear();
                                            ArrayList<ReplyDTO> replyDTOTemp = new ArrayList<>();

                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                ReplyDTO replyDTO = snapshot.getValue(ReplyDTO.class);
                                                replyDTOTemp.add(replyDTO);
                                            }
                                            Collections.reverse(replyDTOTemp);
                                            replyDTOS.addAll(replyDTOTemp);
                                            int temp = replyDTOS.size() - position - 1;
                                            onReplyLikeClicked(firebaseDatabase.getReference().child("reply").child(contentKey).child(replyDTOTemp.get(temp).getReplyKey()));
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                } else {
                    MustLoginDialog mustLoginDialog = new MustLoginDialog();
                    mustLoginDialog.show(getSupportFragmentManager(), "mustLoginDialog");

                }

            }
        });


        //이미투표했는지 여부 확인해서 floating action button 색 넣기
        fabCheck(firebaseDatabase.getReference().child("user_contents").child(contentKey));


        //투표하고 결과보기
        pollActivity_imageView_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.isAnonymous()) {
                    startAnonymousPollResult(contentKey);
//                        issueContents 테스트 디비 입력용
//                        long issueDate = getCurrentDate();
//                        issueMap.put(String.valueOf(issueDate), contentKey);
//                        firebaseDatabase.getReference().child("issueContents").child(String.valueOf(issueDate)).setValue(issueMap);
                } else {
                    mDatabaseReferencePicker.child(auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Map<String, Object> user = (Map<String, Object>) dataSnapshot.getValue();
                            Object object = user.get("age");
                            int currentAge = Integer.parseInt(object.toString());
                            String currentGender = user.get("sex").toString();
                            onResultClicked(firebaseDatabase.getReference().child("user_contents").child(contentKey), currentAge, currentGender);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

        //투표하고 결과보기
        pollActivity_fab_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.isAnonymous()) {
                    startAnonymousPollResult(contentKey);
//                        issueContents 테스트 디비 입력용
                        long issueDate = getCurrentDate();
                        issueMap.put(String.valueOf(issueDate), contentKey);
                        firebaseDatabase.getReference().child("issueContents").child(String.valueOf(issueDate)).setValue(issueMap);
                } else {
                    mDatabaseReferencePicker.child(auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Map<String, Object> user = (Map<String, Object>) dataSnapshot.getValue();
                            Object object = user.get("age");
                            int currentAge = Integer.parseInt(object.toString());
                            String currentGender = user.get("sex").toString();
                            onResultClicked(firebaseDatabase.getReference().child("user_contents").child(contentKey), currentAge, currentGender);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }


            }
        });

        if (user.isAnonymous()) {
            pollActivity_editText_reply.setHint("댓글을 작성하시려면 로그인해주세요.");
        }

        //댓글 등록 버튼 색 변경
        pollActivity_editText_reply.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() > 0) {
                    pollActivity_button_replySend.setImageResource(R.drawable.ic_play_triangle_2);
                } else {
                    pollActivity_button_replySend.setImageResource(R.drawable.ic_play_triangle_1);
                }
            }
        });


        //댓글 펼치기
        pollActivity_relativeLayout_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseDatabase.getReference().child("user_contents").child(contentKey).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ContentDTO contentDTO = dataSnapshot.getValue(ContentDTO.class);
                        try {
                            int replyCount = contentDTO.getReplyCount();
                            openReply(replyCount);
                        } catch (Exception e) {
                            pollActivity_imageView_replyView_1.setVisibility(View.GONE);
                            pollActivity_imageView_replyView_2.setVisibility(View.GONE);
                            pollActivity_recyclerView_reply.setVisibility(View.GONE);
                            pollActivity_editText_reply.setVisibility(View.GONE);
                            pollActivity_button_replySend.setVisibility(View.GONE);
                            pollActivity_linearLayout_reply.setVisibility(View.GONE);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                //(베댓을위한)댓글의 좋아요 갯수 정렬
                firebaseDatabase.getReference().child("reply").child(contentKey).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<ReplyDTO> replyDTOS = new ArrayList<>();
                        Iterator<DataSnapshot> replyDTOSIterator = dataSnapshot.getChildren().iterator();
                        while (replyDTOSIterator.hasNext()) {
                            final ReplyDTO replyDTO_ = replyDTOSIterator.next().getValue(ReplyDTO.class);
                            replyDTOS.add(replyDTO_);
                        }

                        Collections.sort(replyDTOS, new Comparator<ReplyDTO>() {
                            @Override
                            public int compare(ReplyDTO o1, ReplyDTO o2) {

                                if (o1.getLikeCount() < o2.getLikeCount()) {
                                    return -1;

                                } else if (o1.getLikeCount() > o2.getLikeCount()) {
                                    return 1;

                                } else {
                                    return 0;
                                }
                            }
                        });

                        Collections.reverse(replyDTOS);//요3개를 댓글 베스트3로 넘겨줌
//                        Log.d("lkj replyLike0", String.valueOf(replyDTOS.get(0).getLikeCount()));
//                        Log.d("lkj replyLike1", String.valueOf(replyDTOS.get(1).getLikeCount()));
//                        Log.d("lkj replyLike2", String.valueOf(replyDTOS.get(2).getLikeCount()));

                        if (replyDTOS.isEmpty()) {
                            return;
                        } else {
                            openBestReply(replyDTOS);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                firebaseDatabase.getReference().child("reply").child(contentKey).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //댓글의 좋아요 갯수 확인
                        ArrayList<Integer> tmp_ = new ArrayList<>();
                        Iterator<DataSnapshot> replyDTOSIterator = dataSnapshot.getChildren().iterator();
                        while (replyDTOSIterator.hasNext()) {
                            final ReplyDTO replyDTO_ = replyDTOSIterator.next().getValue(ReplyDTO.class);
                            tmp_.add(replyDTO_.likeCount);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                firebaseDatabase.getReference().child("reply").child(contentKey).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        replyDTOS.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            ReplyDTO replyDTO = snapshot.getValue(ReplyDTO.class);
                            replyDTOS.add(replyDTO);
                        }
                        replyAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });


        //따봉버튼 클릭리스너,  좋아요(따봉) 이미지 클릭
        pollActivity_imageView_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("lkj like click test", "test1111111");
                if (user.isAnonymous()) {
                    Log.d("lkj like click test", "test");
                    Toast.makeText(getApplicationContext(), "로그인 해야 합니다.", Toast.LENGTH_LONG).show();
                } else {
                    likeClick();
                }

            }
        });

        //따봉버튼 클릭리스너,  숫자 클릭
        pollActivity_textView_likeCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("lkj like click test", "test1111111");
                if (user.isAnonymous()) {
                    Log.d("lkj like click test", "test");
                    MustLoginDialog mustLoginDialog = new MustLoginDialog();
                    mustLoginDialog.show(getSupportFragmentManager(), "mustLoginDialog");
                } else {
                    likeClick();
                }
            }
        });


        //댓글 리사이클러뷰 스크롤은 PollSingleActivity에 포함되도록
        pollActivity_recyclerView_reply.setNestedScrollingEnabled(false);
        pollActivity_recyclerView_reply.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });


        //댓글 리사이클러뷰
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());//getApplicationContext()전에 this,?? 였음
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLayoutManager.isSmoothScrollbarEnabled();
        mLayoutManager.setStackFromEnd(true);
        mLayoutManager.setReverseLayout(false);
        pollActivity_recyclerView_reply.setLayoutManager(mLayoutManager);
        pollActivity_recyclerView_reply.setAdapter(replyAdapter);


        //reply button click, 댓글달기버튼
        pollActivity_button_replySend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.isAnonymous()) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); //키보드 숨기기
                    inputMethodManager.hideSoftInputFromWindow(pollActivity_editText_reply.getWindowToken(), 0); //키보드 숨기기
                } else {
                    if (pollActivity_editText_reply.getText().length() > 0) {
                        FirebaseDatabase replyDatabase;
                        replyDatabase = FirebaseDatabase.getInstance();
                        replyDatabase.getReference().child("users").child(auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            User user = dataSnapshot.getValue(User.class);
                                Map<String, Object> users = (Map<String, Object>) dataSnapshot.getValue();
                                String date = getDate();
                                onReplyClicked(firebaseDatabase.getReference().child("user_contents").child(contentKey));
                                replyKey = firebaseDatabase.getReference().child("reply").push().getKey();
                                ReplyDTO replyDTO = new ReplyDTO();
                                replyDTO.setReplyKey(replyKey);
                                replyDTO.setDate(date);
                                replyDTO.setId(users.get("userId").toString());
                                replyDTO.setuId(auth.getCurrentUser().getUid());
                                replyDTO.setReply(pollActivity_editText_reply.getText().toString());
                                replyDTO.setqPoint(Integer.parseInt(users.get("userClass").toString()));
                                replyDTO.setContentKey(contentKey);
                                firebaseDatabase.getReference().child("reply").child(contentKey).child(replyKey).setValue(replyDTO);
                                firebaseDatabase.getReference().child("users").child(auth.getCurrentUser().getUid()).child("reply").child(contentKey).push().setValue(replyDTO);
                                pollActivity_editText_reply.setText(null);//editText 초기화
                                pollActivity_editText_reply.setHint("댓글...");
                                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); //키보드 숨기기
                                inputMethodManager.hideSoftInputFromWindow(pollActivity_editText_reply.getWindowToken(), 0); //키보드 숨기기
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }

            }
        });


        //contentDTO 화면 초기세팅
        mDatabaseReferenceAlarm = FirebaseDatabase.getInstance().getReference();
        mDatabaseReferenceAlarm.child("user_contents").child(contentKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ContentDTO contentDTO = dataSnapshot.getValue(ContentDTO.class);
                try {
//                    tmpContentKey = contentDTO.getContentKey();
//                    tmpPollMode = contentDTO.getPollMode();
//                    tmpPollMode = tmpPollMode.replaceAll(" ", "");
                    pollActivity_textView_date.setText(contentDTO.getUploadDate());
                    pollActivity_textView_title.setText(contentDTO.getTitle());
                    pollActivity_textView_contentId.setText(contentDTO.getContentId());
                    pollActivity_textView_contentType.setText(contentDTO.getContentType());
                    pollActivity_textView_description.setText(contentDTO.getDescription());
                    pollActivity_textView_pollMode.setText(contentDTO.getPollMode());
                    pollActivity_textView_hitCount.setText(String.valueOf(contentDTO.getContentHit()));
                    pollActivity_textView_likeCount.setText(String.valueOf(contentDTO.getLikeCount()));
                    pollActivity_textView_replyCount.setText(String.valueOf(contentDTO.getReplyCount()));
                    pollActivity_textView_userId.setText(contentDTO.getUserID());
                    settingUserIcon(contentDTO.getUid());
                    settingUserAlarm(contentKey);
                    contentHit = contentDTO.getContentHit();
                    contentAmount = contentDTO.getItemViewType();

                    if (user.isAnonymous()) {
                        pollActivity_textView_state.setText("투표에 참여하시려면 로그인 해주세요.");
                    } else if (contentDTO.contentPicker.get(auth.getCurrentUser().getUid()) == null) {
                        pollActivity_textView_state.setText("투표 전 입니다");
                    } else {
                        int picked = contentDTO.contentPicker.get(auth.getCurrentUser().getUid());
                        if (picked == 9999) {
                            pollActivity_textView_state.setText(contentDTO.getUserID() + "님의 투표입니다.");
                        } else {
                            pollActivity_textView_state.setText(picked + 1 + "번에 1위 투표 하셧습니다.");
                        }
                    }


                    if (contentDTO.likes.containsKey(auth.getCurrentUser().getUid())) {
                        pollActivity_imageView_like.setImageResource(R.drawable.ic_thumb_up_blue);
                    } else {
                        pollActivity_imageView_like.setImageResource(R.drawable.ic_outline_thumb_up_24px);
                    }

                    switch (contentDTO.getItemViewType()) {
                        case 2:
                            pollActivity_imageView_userAddContent_1.setVisibility(View.VISIBLE);
                            pollActivity_imageView_userAddContent_2.setVisibility(View.VISIBLE);
                            GlideApp.with(getApplicationContext()).load(contentDTO.getImageUrl_0()).centerCrop().thumbnail(Glide.with(getApplicationContext()).load(R.drawable.loadingicon)).into(pollActivity_imageView_userAddContent_1).getView();
                            GlideApp.with(getApplicationContext()).load(contentDTO.getImageUrl_1()).centerCrop().thumbnail(Glide.with(getApplicationContext()).load(R.drawable.loadingicon)).into(pollActivity_imageView_userAddContent_2).getView();
                            break;
                        case 3:
                            pollActivity_imageView_userAddContent_1.setVisibility(View.VISIBLE);
                            pollActivity_imageView_userAddContent_2.setVisibility(View.VISIBLE);
                            pollActivity_imageView_userAddContent_3.setVisibility(View.VISIBLE);
                            GlideApp.with(getApplicationContext()).load(contentDTO.getImageUrl_0()).centerCrop().thumbnail(Glide.with(getApplicationContext()).load(R.drawable.loadingicon)).into(pollActivity_imageView_userAddContent_1).getView();
                            GlideApp.with(getApplicationContext()).load(contentDTO.getImageUrl_1()).centerCrop().thumbnail(Glide.with(getApplicationContext()).load(R.drawable.loadingicon)).into(pollActivity_imageView_userAddContent_2).getView();
                            GlideApp.with(getApplicationContext()).load(contentDTO.getImageUrl_2()).centerCrop().thumbnail(Glide.with(getApplicationContext()).load(R.drawable.loadingicon)).into(pollActivity_imageView_userAddContent_3).getView();
                            break;
                        case 4:
                            pollActivity_imageView_userAddContent_1.setVisibility(View.VISIBLE);
                            pollActivity_imageView_userAddContent_2.setVisibility(View.VISIBLE);
                            pollActivity_imageView_userAddContent_3.setVisibility(View.VISIBLE);
                            pollActivity_imageView_userAddContent_4.setVisibility(View.VISIBLE);
                            GlideApp.with(getApplicationContext()).load(contentDTO.getImageUrl_0()).centerCrop().thumbnail(Glide.with(getApplicationContext()).load(R.drawable.loadingicon)).into(pollActivity_imageView_userAddContent_1).getView();
                            GlideApp.with(getApplicationContext()).load(contentDTO.getImageUrl_1()).centerCrop().thumbnail(Glide.with(getApplicationContext()).load(R.drawable.loadingicon)).into(pollActivity_imageView_userAddContent_2).getView();
                            GlideApp.with(getApplicationContext()).load(contentDTO.getImageUrl_2()).centerCrop().thumbnail(Glide.with(getApplicationContext()).load(R.drawable.loadingicon)).into(pollActivity_imageView_userAddContent_3).getView();
                            GlideApp.with(getApplicationContext()).load(contentDTO.getImageUrl_3()).centerCrop().thumbnail(Glide.with(getApplicationContext()).load(R.drawable.loadingicon)).into(pollActivity_imageView_userAddContent_4).getView();
                            break;
                        case 5:
                            pollActivity_imageView_userAddContent_1.setVisibility(View.VISIBLE);
                            pollActivity_imageView_userAddContent_2.setVisibility(View.VISIBLE);
                            pollActivity_imageView_userAddContent_3.setVisibility(View.VISIBLE);
                            pollActivity_imageView_userAddContent_4.setVisibility(View.VISIBLE);
                            pollActivity_imageView_userAddContent_5.setVisibility(View.VISIBLE);
                            GlideApp.with(getApplicationContext()).load(contentDTO.getImageUrl_0()).centerCrop().thumbnail(Glide.with(getApplicationContext()).load(R.drawable.loadingicon)).into(pollActivity_imageView_userAddContent_1).getView();
                            GlideApp.with(getApplicationContext()).load(contentDTO.getImageUrl_1()).centerCrop().thumbnail(Glide.with(getApplicationContext()).load(R.drawable.loadingicon)).into(pollActivity_imageView_userAddContent_2).getView();
                            GlideApp.with(getApplicationContext()).load(contentDTO.getImageUrl_2()).centerCrop().thumbnail(Glide.with(getApplicationContext()).load(R.drawable.loadingicon)).into(pollActivity_imageView_userAddContent_3).getView();
                            GlideApp.with(getApplicationContext()).load(contentDTO.getImageUrl_3()).centerCrop().thumbnail(Glide.with(getApplicationContext()).load(R.drawable.loadingicon)).into(pollActivity_imageView_userAddContent_4).getView();
                            GlideApp.with(getApplicationContext()).load(contentDTO.getImageUrl_4()).centerCrop().thumbnail(Glide.with(getApplicationContext()).load(R.drawable.loadingicon)).into(pollActivity_imageView_userAddContent_5).getView();
                            break;
                        case 6:
                            pollActivity_imageView_userAddContent_1.setVisibility(View.VISIBLE);
                            pollActivity_imageView_userAddContent_2.setVisibility(View.VISIBLE);
                            pollActivity_imageView_userAddContent_3.setVisibility(View.VISIBLE);
                            pollActivity_imageView_userAddContent_4.setVisibility(View.VISIBLE);
                            pollActivity_imageView_userAddContent_5.setVisibility(View.VISIBLE);
                            pollActivity_imageView_userAddContent_6.setVisibility(View.VISIBLE);
                            GlideApp.with(getApplicationContext()).load(contentDTO.getImageUrl_0()).centerCrop().thumbnail(Glide.with(getApplicationContext()).load(R.drawable.loadingicon)).into(pollActivity_imageView_userAddContent_1).getView();
                            GlideApp.with(getApplicationContext()).load(contentDTO.getImageUrl_1()).centerCrop().thumbnail(Glide.with(getApplicationContext()).load(R.drawable.loadingicon)).into(pollActivity_imageView_userAddContent_2).getView();
                            GlideApp.with(getApplicationContext()).load(contentDTO.getImageUrl_2()).centerCrop().thumbnail(Glide.with(getApplicationContext()).load(R.drawable.loadingicon)).into(pollActivity_imageView_userAddContent_3).getView();
                            GlideApp.with(getApplicationContext()).load(contentDTO.getImageUrl_3()).centerCrop().thumbnail(Glide.with(getApplicationContext()).load(R.drawable.loadingicon)).into(pollActivity_imageView_userAddContent_4).getView();
                            GlideApp.with(getApplicationContext()).load(contentDTO.getImageUrl_4()).centerCrop().thumbnail(Glide.with(getApplicationContext()).load(R.drawable.loadingicon)).into(pollActivity_imageView_userAddContent_5).getView();
                            GlideApp.with(getApplicationContext()).load(contentDTO.getImageUrl_5()).centerCrop().thumbnail(Glide.with(getApplicationContext()).load(R.drawable.loadingicon)).into(pollActivity_imageView_userAddContent_6).getView();
                            break;
                        case 7:
                            pollActivity_imageView_userAddContent_1.setVisibility(View.VISIBLE);
                            pollActivity_imageView_userAddContent_2.setVisibility(View.VISIBLE);
                            pollActivity_imageView_userAddContent_3.setVisibility(View.VISIBLE);
                            pollActivity_imageView_userAddContent_4.setVisibility(View.VISIBLE);
                            pollActivity_imageView_userAddContent_5.setVisibility(View.VISIBLE);
                            pollActivity_imageView_userAddContent_6.setVisibility(View.VISIBLE);
                            pollActivity_imageView_userAddContent_7.setVisibility(View.VISIBLE);
                            GlideApp.with(getApplicationContext()).load(contentDTO.getImageUrl_0()).centerCrop().thumbnail(Glide.with(getApplicationContext()).load(R.drawable.loadingicon)).into(pollActivity_imageView_userAddContent_1).getView();
                            GlideApp.with(getApplicationContext()).load(contentDTO.getImageUrl_1()).centerCrop().thumbnail(Glide.with(getApplicationContext()).load(R.drawable.loadingicon)).into(pollActivity_imageView_userAddContent_2).getView();
                            GlideApp.with(getApplicationContext()).load(contentDTO.getImageUrl_2()).centerCrop().thumbnail(Glide.with(getApplicationContext()).load(R.drawable.loadingicon)).into(pollActivity_imageView_userAddContent_3).getView();
                            GlideApp.with(getApplicationContext()).load(contentDTO.getImageUrl_3()).centerCrop().thumbnail(Glide.with(getApplicationContext()).load(R.drawable.loadingicon)).into(pollActivity_imageView_userAddContent_4).getView();
                            GlideApp.with(getApplicationContext()).load(contentDTO.getImageUrl_4()).centerCrop().thumbnail(Glide.with(getApplicationContext()).load(R.drawable.loadingicon)).into(pollActivity_imageView_userAddContent_5).getView();
                            GlideApp.with(getApplicationContext()).load(contentDTO.getImageUrl_5()).centerCrop().thumbnail(Glide.with(getApplicationContext()).load(R.drawable.loadingicon)).into(pollActivity_imageView_userAddContent_6).getView();
                            GlideApp.with(getApplicationContext()).load(contentDTO.getImageUrl_6()).centerCrop().thumbnail(Glide.with(getApplicationContext()).load(R.drawable.loadingicon)).into(pollActivity_imageView_userAddContent_7).getView();
                            break;
                        case 8:
                            pollActivity_imageView_userAddContent_1.setVisibility(View.VISIBLE);
                            pollActivity_imageView_userAddContent_2.setVisibility(View.VISIBLE);
                            pollActivity_imageView_userAddContent_3.setVisibility(View.VISIBLE);
                            pollActivity_imageView_userAddContent_4.setVisibility(View.VISIBLE);
                            pollActivity_imageView_userAddContent_5.setVisibility(View.VISIBLE);
                            pollActivity_imageView_userAddContent_6.setVisibility(View.VISIBLE);
                            pollActivity_imageView_userAddContent_7.setVisibility(View.VISIBLE);
                            pollActivity_imageView_userAddContent_8.setVisibility(View.VISIBLE);
                            GlideApp.with(getApplicationContext()).load(contentDTO.getImageUrl_0()).centerCrop().thumbnail(Glide.with(getApplicationContext()).load(R.drawable.loadingicon)).into(pollActivity_imageView_userAddContent_1).getView();
                            GlideApp.with(getApplicationContext()).load(contentDTO.getImageUrl_1()).centerCrop().thumbnail(Glide.with(getApplicationContext()).load(R.drawable.loadingicon)).into(pollActivity_imageView_userAddContent_2).getView();
                            GlideApp.with(getApplicationContext()).load(contentDTO.getImageUrl_2()).centerCrop().thumbnail(Glide.with(getApplicationContext()).load(R.drawable.loadingicon)).into(pollActivity_imageView_userAddContent_3).getView();
                            GlideApp.with(getApplicationContext()).load(contentDTO.getImageUrl_3()).centerCrop().thumbnail(Glide.with(getApplicationContext()).load(R.drawable.loadingicon)).into(pollActivity_imageView_userAddContent_4).getView();
                            GlideApp.with(getApplicationContext()).load(contentDTO.getImageUrl_4()).centerCrop().thumbnail(Glide.with(getApplicationContext()).load(R.drawable.loadingicon)).into(pollActivity_imageView_userAddContent_5).getView();
                            GlideApp.with(getApplicationContext()).load(contentDTO.getImageUrl_5()).centerCrop().thumbnail(Glide.with(getApplicationContext()).load(R.drawable.loadingicon)).into(pollActivity_imageView_userAddContent_6).getView();
                            GlideApp.with(getApplicationContext()).load(contentDTO.getImageUrl_6()).centerCrop().thumbnail(Glide.with(getApplicationContext()).load(R.drawable.loadingicon)).into(pollActivity_imageView_userAddContent_7).getView();
                            GlideApp.with(getApplicationContext()).load(contentDTO.getImageUrl_7()).centerCrop().thumbnail(Glide.with(getApplicationContext()).load(R.drawable.loadingicon)).into(pollActivity_imageView_userAddContent_8).getView();
                            break;
                        case 9:
                            pollActivity_imageView_userAddContent_1.setVisibility(View.VISIBLE);
                            pollActivity_imageView_userAddContent_2.setVisibility(View.VISIBLE);
                            pollActivity_imageView_userAddContent_3.setVisibility(View.VISIBLE);
                            pollActivity_imageView_userAddContent_4.setVisibility(View.VISIBLE);
                            pollActivity_imageView_userAddContent_5.setVisibility(View.VISIBLE);
                            pollActivity_imageView_userAddContent_6.setVisibility(View.VISIBLE);
                            pollActivity_imageView_userAddContent_7.setVisibility(View.VISIBLE);
                            pollActivity_imageView_userAddContent_8.setVisibility(View.VISIBLE);
                            pollActivity_imageView_userAddContent_9.setVisibility(View.VISIBLE);
                            GlideApp.with(getApplicationContext()).load(contentDTO.getImageUrl_0()).centerCrop().thumbnail(Glide.with(getApplicationContext()).load(R.drawable.loadingicon)).into(pollActivity_imageView_userAddContent_1).getView();
                            GlideApp.with(getApplicationContext()).load(contentDTO.getImageUrl_1()).centerCrop().thumbnail(Glide.with(getApplicationContext()).load(R.drawable.loadingicon)).into(pollActivity_imageView_userAddContent_2).getView();
                            GlideApp.with(getApplicationContext()).load(contentDTO.getImageUrl_2()).centerCrop().thumbnail(Glide.with(getApplicationContext()).load(R.drawable.loadingicon)).into(pollActivity_imageView_userAddContent_3).getView();
                            GlideApp.with(getApplicationContext()).load(contentDTO.getImageUrl_3()).centerCrop().thumbnail(Glide.with(getApplicationContext()).load(R.drawable.loadingicon)).into(pollActivity_imageView_userAddContent_4).getView();
                            GlideApp.with(getApplicationContext()).load(contentDTO.getImageUrl_4()).centerCrop().thumbnail(Glide.with(getApplicationContext()).load(R.drawable.loadingicon)).into(pollActivity_imageView_userAddContent_5).getView();
                            GlideApp.with(getApplicationContext()).load(contentDTO.getImageUrl_5()).centerCrop().thumbnail(Glide.with(getApplicationContext()).load(R.drawable.loadingicon)).into(pollActivity_imageView_userAddContent_6).getView();
                            GlideApp.with(getApplicationContext()).load(contentDTO.getImageUrl_6()).centerCrop().thumbnail(Glide.with(getApplicationContext()).load(R.drawable.loadingicon)).into(pollActivity_imageView_userAddContent_7).getView();
                            GlideApp.with(getApplicationContext()).load(contentDTO.getImageUrl_7()).centerCrop().thumbnail(Glide.with(getApplicationContext()).load(R.drawable.loadingicon)).into(pollActivity_imageView_userAddContent_8).getView();
                            GlideApp.with(getApplicationContext()).load(contentDTO.getImageUrl_8()).centerCrop().thumbnail(Glide.with(getApplicationContext()).load(R.drawable.loadingicon)).into(pollActivity_imageView_userAddContent_9).getView();
                            break;
                        case 10:
                            pollActivity_imageView_userAddContent_1.setVisibility(View.VISIBLE);
                            pollActivity_imageView_userAddContent_2.setVisibility(View.VISIBLE);
                            pollActivity_imageView_userAddContent_3.setVisibility(View.VISIBLE);
                            pollActivity_imageView_userAddContent_4.setVisibility(View.VISIBLE);
                            pollActivity_imageView_userAddContent_5.setVisibility(View.VISIBLE);
                            pollActivity_imageView_userAddContent_6.setVisibility(View.VISIBLE);
                            pollActivity_imageView_userAddContent_7.setVisibility(View.VISIBLE);
                            pollActivity_imageView_userAddContent_8.setVisibility(View.VISIBLE);
                            pollActivity_imageView_userAddContent_9.setVisibility(View.VISIBLE);
                            pollActivity_imageView_userAddContent_10.setVisibility(View.VISIBLE);
                            GlideApp.with(getApplicationContext()).load(contentDTO.getImageUrl_0()).centerCrop().thumbnail(Glide.with(getApplicationContext()).load(R.drawable.loadingicon)).into(pollActivity_imageView_userAddContent_1).getView();
                            GlideApp.with(getApplicationContext()).load(contentDTO.getImageUrl_1()).centerCrop().thumbnail(Glide.with(getApplicationContext()).load(R.drawable.loadingicon)).into(pollActivity_imageView_userAddContent_2).getView();
                            GlideApp.with(getApplicationContext()).load(contentDTO.getImageUrl_2()).centerCrop().thumbnail(Glide.with(getApplicationContext()).load(R.drawable.loadingicon)).into(pollActivity_imageView_userAddContent_3).getView();
                            GlideApp.with(getApplicationContext()).load(contentDTO.getImageUrl_3()).centerCrop().thumbnail(Glide.with(getApplicationContext()).load(R.drawable.loadingicon)).into(pollActivity_imageView_userAddContent_4).getView();
                            GlideApp.with(getApplicationContext()).load(contentDTO.getImageUrl_4()).centerCrop().thumbnail(Glide.with(getApplicationContext()).load(R.drawable.loadingicon)).into(pollActivity_imageView_userAddContent_5).getView();
                            GlideApp.with(getApplicationContext()).load(contentDTO.getImageUrl_5()).centerCrop().thumbnail(Glide.with(getApplicationContext()).load(R.drawable.loadingicon)).into(pollActivity_imageView_userAddContent_6).getView();
                            GlideApp.with(getApplicationContext()).load(contentDTO.getImageUrl_6()).centerCrop().thumbnail(Glide.with(getApplicationContext()).load(R.drawable.loadingicon)).into(pollActivity_imageView_userAddContent_7).getView();
                            GlideApp.with(getApplicationContext()).load(contentDTO.getImageUrl_7()).centerCrop().thumbnail(Glide.with(getApplicationContext()).load(R.drawable.loadingicon)).into(pollActivity_imageView_userAddContent_8).getView();
                            GlideApp.with(getApplicationContext()).load(contentDTO.getImageUrl_8()).centerCrop().thumbnail(Glide.with(getApplicationContext()).load(R.drawable.loadingicon)).into(pollActivity_imageView_userAddContent_9).getView();
                            GlideApp.with(getApplicationContext()).load(contentDTO.getImageUrl_9()).centerCrop().thumbnail(Glide.with(getApplicationContext()).load(R.drawable.loadingicon)).into(pollActivity_imageView_userAddContent_10).getView();
                            break;
                    }
                } catch (Exception e) {

                }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });

        //비트맵 만들고 blur효과 이후에 캐시에 저장
        BlurAsyncTask blurAsyncTask = new BlurAsyncTask();
        blurAsyncTask.execute();


        //공유 클릭
        findViewById(R.id.pollActivity_imageView_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    shareUrl = shareContent.getShareUrl();
                } catch (Exception e) {

                }

                String id = pollActivity_textView_userId.getText().toString();
                if (shareUrl == null) {
                    Toast.makeText(PollRankingActivity.this, "공유 URL을 생성중입니다. 다시 시도해주세요.", Toast.LENGTH_LONG).show();
                } else {
                    DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
                    mRef.child("user_contents").child(contentKey).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            ContentDTO contentDTO = dataSnapshot.getValue(ContentDTO.class);
                            Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                            intent.setType("text/plain");
                            String subject = contentDTO.getTitle();
                            String text = shareUrl;
                            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                            intent.putExtra(Intent.EXTRA_TEXT, text);
                            Intent chooser = Intent.createChooser(intent, "공유하기");
                            startActivity(chooser);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

            }
        });
    }

    private void startAnonymousPollResult(final String c_Key) {
        DatabaseReference tmpRef = FirebaseDatabase.getInstance().getReference();
        tmpRef.child("user_contents").child(c_Key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Object> contentMap = (Map<String, Object>) dataSnapshot.getValue();
                PollResultAnonymousDialog pollResultAnonymousDialog = new PollResultAnonymousDialog();
                Bundle bundle = new Bundle();
                bundle.putInt("imagePick", currentPick());
                bundle.putInt("imageN", contentAmount);
                bundle.putInt("contentHits", contentHit);
                bundle.putString("currentContent", c_Key);
                bundle.putString("statisticsCode", String.valueOf(contentMap.get("statistics_code")));
                pollResultAnonymousDialog.setArguments(bundle);
                pollResultAnonymousDialog.show(getSupportFragmentManager(), "pollResultAnonymousDialog");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /*
    onCreate();
     */

    private void settingUserIcon(String userId) {
        firebaseDatabase.getReference().child("users").child(userId).child("userClass").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    int userClass = Integer.parseInt(dataSnapshot.getValue().toString());
                    if (userClass >= 0 && userClass < 50) {
                        pollActivity_imageView_userClass.setImageResource(R.drawable.q_class_red_2);
                    } else if (userClass >= 50 && userClass < 100) {
                        pollActivity_imageView_userClass.setImageResource(R.drawable.q_class_red_1);
                    } else if (userClass >= 100 && userClass < 150) {
                        pollActivity_imageView_userClass.setImageResource(R.drawable.q_class_orange_1);
                    } else if (userClass >= 150 && userClass < 200) {
                        pollActivity_imageView_userClass.setImageResource(R.drawable.q_class_orange_2);
                    } else if (userClass >= 200 && userClass < 250) {
                        pollActivity_imageView_userClass.setImageResource(R.drawable.q_class_yellow_1);
                    } else if (userClass >= 250 && userClass < 300) {
                        pollActivity_imageView_userClass.setImageResource(R.drawable.q_class_yellow_2);
                    } else if (userClass >= 300 && userClass < 350) {
                        pollActivity_imageView_userClass.setImageResource(R.drawable.q_class_green_1);
                    } else if (userClass >= 350 && userClass < 400) {
                        pollActivity_imageView_userClass.setImageResource(R.drawable.q_class_green_2);
                    } else if (userClass >= 400 && userClass < 450) {
                        pollActivity_imageView_userClass.setImageResource(R.drawable.q_class_blue_2);
                    } else if (userClass >= 450 && userClass < 501) {
                        pollActivity_imageView_userClass.setImageResource(R.drawable.q_class_blue_2);
                    } else if (userClass == 1000) {
                        pollActivity_imageView_userClass.setImageResource(R.drawable.q_class_black);
                    }
                } catch (Exception e) {
                    pollActivity_imageView_userClass.setImageResource(R.drawable.q_class_null);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    /*
     * onCreate()
     */


    //알람 이미지 파란색 or 비어있는
    private void settingUserAlarm(String key) {
        firebaseDatabase.getReference().child("user_contents").child(key).child("alarm").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    String tmp = dataSnapshot.toString();
                    int index = tmp.indexOf("C");//ContinueCount
                    String temp = tmp.substring(index, tmp.length() - 2);
                    int index_ = temp.indexOf("O");//OneCount
//                                                        int continueCount = Integer.parseInt(adminAuthId.substring(1, index_));
                    int oneCount = Integer.parseInt(temp.substring(index_ + 1, temp.length()));
//
                    if (oneCount == 0) {
                        pollActivity_imageView_alarm.setImageResource(R.drawable.ic_noti_outline);
                    } else {
                        pollActivity_imageView_alarm.setImageResource(R.drawable.ic_noti_fill);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    private void likeClick() {
        FirebaseUser muUser = FirebaseAuth.getInstance().getCurrentUser();
        if (muUser.isAnonymous()) {
            MustLoginDialog mustLoginDialog = new MustLoginDialog();
            mustLoginDialog.show(getSupportFragmentManager(), "mustLoginDialog");
        } else {
            final Uri data = getIntent().getData();
            if (data != null) {
                contentKey = data.getQueryParameter("contentKey");
            } else {
                contentKey = getIntent().getStringExtra("contentKey");
            }
            firebaseDatabase.getReference().child("user_contents").child(contentKey).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ContentDTO contentDTO = dataSnapshot.getValue(ContentDTO.class);
                    try {
                        if (contentDTO.uid.equals(auth.getCurrentUser().getUid())) {
                            Toast toast = Toast.makeText(getApplicationContext(), contentDTO.userID + "님의 투표입니다!", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
                            toast.show();
                        } else {
                            onLikeClicked(firebaseDatabase.getReference().child("user_contents").child(contentKey));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    likeFirebaseDatabase.getReference().child("user_contents").child(contentKey).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            ContentDTO contentDTO_ = dataSnapshot.getValue(ContentDTO.class);
                            try {
                                if (contentDTO_.likes.containsKey(auth.getCurrentUser().getUid())) {
                                    pollActivity_imageView_like.setImageResource(R.drawable.ic_thumb_up_blue);
                                    pollActivity_textView_likeCount.setText(String.valueOf(contentDTO_.likeCount));
                                } else {
                                    pollActivity_imageView_like.setImageResource(R.drawable.ic_outline_thumb_up_24px);
                                    pollActivity_textView_likeCount.setText(String.valueOf(contentDTO_.likeCount));
                                }
                            } catch (Exception e) {

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

    }


    //베스트댓글 보여주기
    private void openBestReply(ArrayList<ReplyDTO> replyDTOS) {
        if (!ACTIVITY_BESTREPLY_FLAG) {
            //베스트3를 보여주는데 좋아요가 눌린 댓글수가 3개 미만일수도있으니

            try {
                if (replyDTOS.get(0) != null && replyDTOS.get(0).likeCount > 0) {
                    int userClass = replyDTOS.get(0).getqPoint();
                    if (userClass >= 0 && userClass < 50) {
                        imageView_userClass0.setImageResource(R.drawable.q_class_red_2);
                    } else if (userClass >= 50 && userClass < 100) {
                        imageView_userClass0.setImageResource(R.drawable.q_class_red_1);
                    } else if (userClass >= 100 && userClass < 150) {
                        imageView_userClass0.setImageResource(R.drawable.q_class_orange_1);
                    } else if (userClass >= 150 && userClass < 200) {
                        imageView_userClass0.setImageResource(R.drawable.q_class_orange_2);
                    } else if (userClass >= 200 && userClass < 250) {
                        imageView_userClass0.setImageResource(R.drawable.q_class_yellow_1);
                    } else if (userClass >= 250 && userClass < 300) {
                        imageView_userClass0.setImageResource(R.drawable.q_class_yellow_2);
                    } else if (userClass >= 300 && userClass < 350) {
                        imageView_userClass0.setImageResource(R.drawable.q_class_green_1);
                    } else if (userClass >= 350 && userClass < 400) {
                        imageView_userClass0.setImageResource(R.drawable.q_class_green_2);
                    } else if (userClass >= 400 && userClass < 450) {
                        imageView_userClass0.setImageResource(R.drawable.q_class_blue_2);
                    } else if (userClass >= 450 && userClass < 501) {
                        imageView_userClass0.setImageResource(R.drawable.q_class_blue_2);
                    }
                    linearLayout_bestReply0.setVisibility(View.VISIBLE);
                    bestReply_id0.setText(replyDTOS.get(0).getId());
                    bestReply_reply0.setText(replyDTOS.get(0).getReply());
                    bestReply_date0.setText(replyDTOS.get(0).getDate());
                    bestReply_likeCount0.setText(String.valueOf(replyDTOS.get(0).getLikeCount()));
                }

                if (replyDTOS.get(1) != null && replyDTOS.get(1).likeCount > 0) {
                    int userClass = replyDTOS.get(1).getqPoint();
                    if (userClass >= 0 && userClass < 50) {
                        imageView_userClass1.setImageResource(R.drawable.q_class_red_2);
                    } else if (userClass >= 50 && userClass < 100) {
                        imageView_userClass1.setImageResource(R.drawable.q_class_red_1);
                    } else if (userClass >= 100 && userClass < 150) {
                        imageView_userClass1.setImageResource(R.drawable.q_class_orange_1);
                    } else if (userClass >= 150 && userClass < 200) {
                        imageView_userClass1.setImageResource(R.drawable.q_class_orange_2);
                    } else if (userClass >= 200 && userClass < 250) {
                        imageView_userClass1.setImageResource(R.drawable.q_class_yellow_1);
                    } else if (userClass >= 250 && userClass < 300) {
                        imageView_userClass1.setImageResource(R.drawable.q_class_yellow_2);
                    } else if (userClass >= 300 && userClass < 350) {
                        imageView_userClass1.setImageResource(R.drawable.q_class_green_1);
                    } else if (userClass >= 350 && userClass < 400) {
                        imageView_userClass1.setImageResource(R.drawable.q_class_green_2);
                    } else if (userClass >= 400 && userClass < 450) {
                        imageView_userClass1.setImageResource(R.drawable.q_class_blue_2);
                    } else if (userClass >= 450 && userClass < 501) {
                        imageView_userClass1.setImageResource(R.drawable.q_class_blue_2);
                    }
                    linearLayout_bestReply1.setVisibility(View.VISIBLE);
                    bestReply_id1.setText(replyDTOS.get(1).getId());
                    bestReply_reply1.setText(replyDTOS.get(1).getReply());
                    bestReply_date1.setText(replyDTOS.get(1).getDate());
                    bestReply_likeCount1.setText(String.valueOf(replyDTOS.get(1).getLikeCount()));
                }

                if (replyDTOS.get(2) != null && replyDTOS.get(2).likeCount > 0) {
                    int userClass = replyDTOS.get(2).getqPoint();
                    if (userClass >= 0 && userClass < 50) {
                        imageView_userClass2.setImageResource(R.drawable.q_class_red_2);
                    } else if (userClass >= 50 && userClass < 100) {
                        imageView_userClass2.setImageResource(R.drawable.q_class_red_1);
                    } else if (userClass >= 100 && userClass < 150) {
                        imageView_userClass2.setImageResource(R.drawable.q_class_orange_1);
                    } else if (userClass >= 150 && userClass < 200) {
                        imageView_userClass2.setImageResource(R.drawable.q_class_orange_2);
                    } else if (userClass >= 200 && userClass < 250) {
                        imageView_userClass2.setImageResource(R.drawable.q_class_yellow_1);
                    } else if (userClass >= 250 && userClass < 300) {
                        imageView_userClass2.setImageResource(R.drawable.q_class_yellow_2);
                    } else if (userClass >= 300 && userClass < 350) {
                        imageView_userClass2.setImageResource(R.drawable.q_class_green_1);
                    } else if (userClass >= 350 && userClass < 400) {
                        imageView_userClass2.setImageResource(R.drawable.q_class_green_2);
                    } else if (userClass >= 400 && userClass < 450) {
                        imageView_userClass2.setImageResource(R.drawable.q_class_blue_2);
                    } else if (userClass >= 450 && userClass < 501) {
                        imageView_userClass2.setImageResource(R.drawable.q_class_blue_2);
                    }
                    linearLayout_bestReply2.setVisibility(View.VISIBLE);
                    bestReply_id2.setText(replyDTOS.get(2).getId());
                    bestReply_reply2.setText(replyDTOS.get(2).getReply());
                    bestReply_date2.setText(replyDTOS.get(2).getDate());
                    bestReply_likeCount2.setText(String.valueOf(replyDTOS.get(2).getLikeCount()));
                }
            } catch (Exception e) {
                Log.w("lkj obr exti", e);
            }

            ACTIVITY_BESTREPLY_FLAG = true;

        } else {
            linearLayout_bestReply0.setVisibility(View.GONE);
            linearLayout_bestReply1.setVisibility(View.GONE);
            linearLayout_bestReply2.setVisibility(View.GONE);
            ACTIVITY_BESTREPLY_FLAG = false;
        }

    }


    //댓글펼치기
    private void openReply(int replyCount) {
        if (!ACTIVITY_REPLY_FLAG) {
            if (replyCount == 0) {
                pollActivity_editText_reply.setHint("아직 댓글이 없습니다. 댓글을 달아보세요!");
            }
            pollActivity_imageView_replyView_1.setVisibility(View.GONE);
            pollActivity_imageView_replyView_2.setVisibility(View.VISIBLE);
            pollActivity_relativeLayout_reply.setFocusableInTouchMode(true);
            pollActivity_relativeLayout_reply.requestFocus();
            pollActivity_recyclerView_reply.setNestedScrollingEnabled(false);
            pollActivity_recyclerView_reply.setVisibility(View.VISIBLE);
            pollActivity_editText_reply.setVisibility(View.VISIBLE);
            pollActivity_button_replySend.setVisibility(View.VISIBLE);
            pollActivity_linearLayout_reply.setVisibility(View.VISIBLE);
            pollActivity_fab_result.hide();
            ACTIVITY_REPLY_FLAG = true;
        } else {
            pollActivity_imageView_replyView_1.setVisibility(View.VISIBLE);
            pollActivity_imageView_replyView_2.setVisibility(View.GONE);
            pollActivity_editText_reply.setText(null);//editText 초기화
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); //키보드 숨기기
            inputMethodManager.hideSoftInputFromWindow(pollActivity_editText_reply.getWindowToken(), 0); //키보드 숨기기
            pollActivity_recyclerView_reply.setVisibility(View.GONE);
            pollActivity_editText_reply.setVisibility(View.GONE);
            pollActivity_button_replySend.setVisibility(View.GONE);
            pollActivity_linearLayout_reply.setVisibility(View.GONE);
            pollActivity_fab_result.show();
            ACTIVITY_REPLY_FLAG = false;
        }
    }


    //댓글수 추가 (replyCount)
    private void onReplyClicked(final DatabaseReference postRef) {
        final String date = getDate();
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                ContentDTO contentDTO = mutableData.getValue(ContentDTO.class);
                if (contentDTO == null) {
                    return Transaction.success(mutableData);
                }
//                contentDTO.reply.put(date, pollActivity_editText_reply.getText().toString());
                mutableData.setValue(contentDTO);
                firebaseDatabase.getReference().child("reply").child(postRef.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        firebaseDatabase.getReference().child("user_contents").child(postRef.getKey()).child("replyCount").setValue(dataSnapshot.getChildrenCount());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {


                // Transaction completed

            }
        });
    }


    //투표 시작
    private void onResultClicked(final DatabaseReference postRef, final int currentAge, final String currentGender) {

        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                final ContentDTO contentDTO = mutableData.getValue(ContentDTO.class);
                if (contentDTO == null) {
                    return Transaction.success(mutableData);
                }
                if (contentDTO.contentPicker.containsKey(auth.getCurrentUser().getUid())) {
                    //투표가 되어있으면 PollResultRankingDialog
                    final Uri data = getIntent().getData();
                    if (data != null) {
                        contentKey = data.getQueryParameter("contentKey");
                    } else {
                        contentKey = getIntent().getStringExtra("contentKey");
                    }

                    PollResultRankingDialog pollResultRankingDialog = new PollResultRankingDialog();
                    Bundle bundle = new Bundle();

                    bundle.putInt("imagePick", currentPick());
                    bundle.putInt("imageN", contentAmount);
                    bundle.putInt("contentHits", contentHit);
                    bundle.putString("currentContent", contentKey);
                    bundle.putString("statisticsCode", contentDTO.statistics_code);

                    pollResultRankingDialog.setArguments(bundle);
                    pollResultRankingDialog.show(getSupportFragmentManager(), "PollResultRankingDialog");


                    //투표가 안되어있으면 투표하고 PollResultRankingDialog
                } else {
                    //선택한 후보의 순위를 각 후보에 더해줌
                    if (pollChecking() == contentAmount) {
                        //true면 투표수 추가하고 PollResultRankingDialog
                        contentDTO.contentHit = contentDTO.contentHit + 1;
                        if (currentRankingPick().size() == 2) {
                            contentDTO.candidateScore_0 = contentDTO.candidateScore_0 + currentRankingPick().get(0);
                            contentDTO.candidateScore_1 = contentDTO.candidateScore_1 + currentRankingPick().get(1);
                        }
                        if (currentRankingPick().size() == 3) {
                            contentDTO.candidateScore_0 = contentDTO.candidateScore_0 + currentRankingPick().get(0);
                            contentDTO.candidateScore_1 = contentDTO.candidateScore_1 + currentRankingPick().get(1);
                            contentDTO.candidateScore_2 = contentDTO.candidateScore_2 + currentRankingPick().get(2);
                        }
                        if (currentRankingPick().size() == 4) {
                            contentDTO.candidateScore_0 = contentDTO.candidateScore_0 + currentRankingPick().get(0);
                            contentDTO.candidateScore_1 = contentDTO.candidateScore_1 + currentRankingPick().get(1);
                            contentDTO.candidateScore_2 = contentDTO.candidateScore_2 + currentRankingPick().get(2);
                            contentDTO.candidateScore_3 = contentDTO.candidateScore_3 + currentRankingPick().get(3);
                        }
                        if (currentRankingPick().size() == 5) {
                            contentDTO.candidateScore_0 = contentDTO.candidateScore_0 + currentRankingPick().get(0);
                            contentDTO.candidateScore_1 = contentDTO.candidateScore_1 + currentRankingPick().get(1);
                            contentDTO.candidateScore_2 = contentDTO.candidateScore_2 + currentRankingPick().get(2);
                            contentDTO.candidateScore_3 = contentDTO.candidateScore_3 + currentRankingPick().get(3);
                            contentDTO.candidateScore_4 = contentDTO.candidateScore_4 + currentRankingPick().get(4);
                        }
                        if (currentRankingPick().size() == 6) {
                            contentDTO.candidateScore_0 = contentDTO.candidateScore_0 + currentRankingPick().get(0);
                            contentDTO.candidateScore_1 = contentDTO.candidateScore_1 + currentRankingPick().get(1);
                            contentDTO.candidateScore_2 = contentDTO.candidateScore_2 + currentRankingPick().get(2);
                            contentDTO.candidateScore_3 = contentDTO.candidateScore_3 + currentRankingPick().get(3);
                            contentDTO.candidateScore_4 = contentDTO.candidateScore_4 + currentRankingPick().get(4);
                            contentDTO.candidateScore_5 = contentDTO.candidateScore_5 + currentRankingPick().get(5);
                        }
                        if (currentRankingPick().size() == 7) {
                            contentDTO.candidateScore_0 = contentDTO.candidateScore_0 + currentRankingPick().get(0);
                            contentDTO.candidateScore_1 = contentDTO.candidateScore_1 + currentRankingPick().get(1);
                            contentDTO.candidateScore_2 = contentDTO.candidateScore_2 + currentRankingPick().get(2);
                            contentDTO.candidateScore_3 = contentDTO.candidateScore_3 + currentRankingPick().get(3);
                            contentDTO.candidateScore_4 = contentDTO.candidateScore_4 + currentRankingPick().get(4);
                            contentDTO.candidateScore_5 = contentDTO.candidateScore_5 + currentRankingPick().get(5);
                            contentDTO.candidateScore_6 = contentDTO.candidateScore_6 + currentRankingPick().get(6);
                        }
                        if (currentRankingPick().size() == 8) {
                            contentDTO.candidateScore_0 = contentDTO.candidateScore_0 + currentRankingPick().get(0);
                            contentDTO.candidateScore_1 = contentDTO.candidateScore_1 + currentRankingPick().get(1);
                            contentDTO.candidateScore_2 = contentDTO.candidateScore_2 + currentRankingPick().get(2);
                            contentDTO.candidateScore_3 = contentDTO.candidateScore_3 + currentRankingPick().get(3);
                            contentDTO.candidateScore_4 = contentDTO.candidateScore_4 + currentRankingPick().get(4);
                            contentDTO.candidateScore_5 = contentDTO.candidateScore_5 + currentRankingPick().get(5);
                            contentDTO.candidateScore_6 = contentDTO.candidateScore_6 + currentRankingPick().get(6);
                            contentDTO.candidateScore_7 = contentDTO.candidateScore_7 + currentRankingPick().get(7);
                        }
                        if (currentRankingPick().size() == 9) {
                            contentDTO.candidateScore_0 = contentDTO.candidateScore_0 + currentRankingPick().get(0);
                            contentDTO.candidateScore_1 = contentDTO.candidateScore_1 + currentRankingPick().get(1);
                            contentDTO.candidateScore_2 = contentDTO.candidateScore_2 + currentRankingPick().get(2);
                            contentDTO.candidateScore_3 = contentDTO.candidateScore_3 + currentRankingPick().get(3);
                            contentDTO.candidateScore_4 = contentDTO.candidateScore_4 + currentRankingPick().get(4);
                            contentDTO.candidateScore_5 = contentDTO.candidateScore_5 + currentRankingPick().get(5);
                            contentDTO.candidateScore_6 = contentDTO.candidateScore_6 + currentRankingPick().get(6);
                            contentDTO.candidateScore_7 = contentDTO.candidateScore_7 + currentRankingPick().get(7);
                            contentDTO.candidateScore_8 = contentDTO.candidateScore_8 + currentRankingPick().get(8);
                        }
                        if (currentRankingPick().size() == 10) {
                            contentDTO.candidateScore_0 = contentDTO.candidateScore_0 + currentRankingPick().get(0);
                            contentDTO.candidateScore_1 = contentDTO.candidateScore_1 + currentRankingPick().get(1);
                            contentDTO.candidateScore_2 = contentDTO.candidateScore_2 + currentRankingPick().get(2);
                            contentDTO.candidateScore_3 = contentDTO.candidateScore_3 + currentRankingPick().get(3);
                            contentDTO.candidateScore_4 = contentDTO.candidateScore_4 + currentRankingPick().get(4);
                            contentDTO.candidateScore_5 = contentDTO.candidateScore_5 + currentRankingPick().get(5);
                            contentDTO.candidateScore_6 = contentDTO.candidateScore_6 + currentRankingPick().get(6);
                            contentDTO.candidateScore_7 = contentDTO.candidateScore_7 + currentRankingPick().get(7);
                            contentDTO.candidateScore_8 = contentDTO.candidateScore_8 + currentRankingPick().get(8);
                            contentDTO.candidateScore_9 = contentDTO.candidateScore_9 + currentRankingPick().get(9);
                        }


                        contentDTO.statistics_code = addStatistics(contentDTO.statistics_code, currentRankingPick(), currentGender, currentAge);


                        //몇변에 투표했는지 users/pickContent:100
                        contentDTO.contentPicker.put(auth.getCurrentUser().getUid(), currentPick());
                        final Uri data = getIntent().getData();
                        String key;
                        if (data != null) {
                            Log.d("lkj Data2 in intent2", data.toString());
                            Log.d("lkj Data2 in intent3", data.getLastPathSegment());
                            Log.d("lkj Data2 in intent4", data.getQueryParameter("contentKey"));
                            key = data.getQueryParameter("contentKey");
                        } else {
                            key = getIntent().getStringExtra("contentKey");
                        }

                        firebaseDatabase.getReference()
                                .child("users")
                                .child(auth.getCurrentUser().getUid())
                                .child("pickContent")
                                .child(key)
                                .setValue("Ranking");

                        //투표했을때 DB/issueContents 에 시간 : contentKey 입력
                        long issueDate = getCurrentDate();
                        issueMap.put(String.valueOf(issueDate), key);
                        firebaseDatabase.getReference().child("issueContents").child(String.valueOf(issueDate)).setValue(issueMap);


                        PollResultRankingDialog pollResultRankingDialog = new PollResultRankingDialog();
                        Bundle bundle = new Bundle();
                        bundle.putInt("imagePick", currentPick());
                        bundle.putInt("imageN", contentAmount);
                        bundle.putString("currentContent", key);
                        bundle.putString("statisticsCode", contentDTO.statistics_code);
                        pollResultRankingDialog.setArguments(bundle);
                        pollResultRankingDialog.show(getSupportFragmentManager(), "pollResultDialog");

                        //포인트1점 추가
                        userPointAdd(1);


                        //투표 선택 안되있으면
                    } else if (pollChecking() == 0) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                Toast.makeText(getApplicationContext(), "투표하면 알랴쥼 ^ㅠ^", Toast.LENGTH_SHORT).show();
                                MustPollDialog mustPollDialog = new MustPollDialog();
                                mustPollDialog.show(getSupportFragmentManager(), "mustPollDialog");
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "순위 투표입니다. 순위를 모두 정해주세요~!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                // Set value and report transaction success
                mutableData.setValue(contentDTO);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                // Transaction completed
                Log.d("lkjlkj", "postTransaction:onComplete:" + databaseError);
            }
        });
    }


    public String getDate() {
        TimeZone timeZone;
        timeZone = TimeZone.getTimeZone("Asia/Seoul");
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd(E)HH:mm:ss", Locale.KOREAN);
        df.setTimeZone(timeZone);
        String currentDate = df.format(date);

        return currentDate;
    }


    private long getCurrentDate() {
        long currentTimeMillis = System.currentTimeMillis();
        return currentTimeMillis;
    }

    private ArrayList<String> rankingTextChecking() {
        ArrayList<String> stringArrayList = new ArrayList<>();

        try {

            if (pollActivity_imageView_userAddContent_n1.getDrawable() != null) {
                stringArrayList.add(pollActivity_textView_check_1.getText().toString());
            }
            if (pollActivity_imageView_userAddContent_n2.getDrawable() != null) {
                stringArrayList.add(pollActivity_textView_check_2.getText().toString());
            }
            if (pollActivity_imageView_userAddContent_n3.getDrawable() != null) {
                stringArrayList.add(pollActivity_textView_check_3.getText().toString());
            }
            if (pollActivity_imageView_userAddContent_n4.getDrawable() != null) {
                stringArrayList.add(pollActivity_textView_check_4.getText().toString());
            }
            if (pollActivity_imageView_userAddContent_n5.getDrawable() != null) {
                stringArrayList.add(pollActivity_textView_check_5.getText().toString());
            }
            if (pollActivity_imageView_userAddContent_n6.getDrawable() != null) {
                stringArrayList.add(pollActivity_textView_check_6.getText().toString());
            }
            if (pollActivity_imageView_userAddContent_n7.getDrawable() != null) {
                stringArrayList.add(pollActivity_textView_check_7.getText().toString());
            }
            if (pollActivity_imageView_userAddContent_n8.getDrawable() != null) {
                stringArrayList.add(pollActivity_textView_check_8.getText().toString());
            }
            if (pollActivity_imageView_userAddContent_n9.getDrawable() != null) {
                stringArrayList.add(pollActivity_textView_check_9.getText().toString());
            }
            if (pollActivity_imageView_userAddContent_n10.getDrawable() != null) {
                stringArrayList.add(pollActivity_textView_check_10.getText().toString());
            }

        } catch (Exception e) {

        }

        return stringArrayList;


    }


    //순위투표 여부 체크
    private int rankingChecking() {
        int rCheck = 0;
        if (((ColorDrawable) pollActivity_imageView_choice_1.getBackground()).getColor() == 0xff4485c9) {
            rCheck++;
        }
        if (((ColorDrawable) pollActivity_imageView_choice_2.getBackground()).getColor() == 0xff4485c9) {
            rCheck++;
        }
        if (((ColorDrawable) pollActivity_imageView_choice_3.getBackground()).getColor() == 0xff4485c9) {
            rCheck++;
        }
        if (((ColorDrawable) pollActivity_imageView_choice_4.getBackground()).getColor() == 0xff4485c9) {
            rCheck++;
        }
        if (((ColorDrawable) pollActivity_imageView_choice_5.getBackground()).getColor() == 0xff4485c9) {
            rCheck++;
        }
        if (((ColorDrawable) pollActivity_imageView_choice_6.getBackground()).getColor() == 0xff4485c9) {
            rCheck++;
        }
        if (((ColorDrawable) pollActivity_imageView_choice_7.getBackground()).getColor() == 0xff4485c9) {
            rCheck++;
        }
        if (((ColorDrawable) pollActivity_imageView_choice_8.getBackground()).getColor() == 0xff4485c9) {
            rCheck++;
        }
        if (((ColorDrawable) pollActivity_imageView_choice_9.getBackground()).getColor() == 0xff4485c9) {
            rCheck++;
        }
        if (((ColorDrawable) pollActivity_imageView_choice_10.getBackground()).getColor() == 0xff4485c9) {
            rCheck++;
        }
        return rCheck;
    }


    //현재 Activity 에서의 투표 체크 유무
    private int pollChecking() {
        int pickCount = 0;
        if (pollActivity_imageView_userAddContent_n1.getVisibility() == View.VISIBLE) pickCount++;
        if (pollActivity_imageView_userAddContent_n2.getVisibility() == View.VISIBLE) pickCount++;
        if (pollActivity_imageView_userAddContent_n3.getVisibility() == View.VISIBLE) pickCount++;
        if (pollActivity_imageView_userAddContent_n4.getVisibility() == View.VISIBLE) pickCount++;
        if (pollActivity_imageView_userAddContent_n5.getVisibility() == View.VISIBLE) pickCount++;
        if (pollActivity_imageView_userAddContent_n6.getVisibility() == View.VISIBLE) pickCount++;
        if (pollActivity_imageView_userAddContent_n7.getVisibility() == View.VISIBLE) pickCount++;
        if (pollActivity_imageView_userAddContent_n8.getVisibility() == View.VISIBLE) pickCount++;
        if (pollActivity_imageView_userAddContent_n9.getVisibility() == View.VISIBLE) pickCount++;
        if (pollActivity_imageView_userAddContent_n10.getVisibility() == View.VISIBLE) pickCount++;
        return pickCount;
    }

    //1위선택한것 찾기
    private int currentPick() {
        if (pollActivity_textView_check_1.getText().toString().equals("1 위")) return 0;
        if (pollActivity_textView_check_2.getText().toString().equals("1 위")) return 1;
        if (pollActivity_textView_check_3.getText().toString().equals("1 위")) return 2;
        if (pollActivity_textView_check_4.getText().toString().equals("1 위")) return 3;
        if (pollActivity_textView_check_5.getText().toString().equals("1 위")) return 4;
        if (pollActivity_textView_check_6.getText().toString().equals("1 위")) return 5;
        if (pollActivity_textView_check_7.getText().toString().equals("1 위")) return 6;
        if (pollActivity_textView_check_8.getText().toString().equals("1 위")) return 7;
        if (pollActivity_textView_check_9.getText().toString().equals("1 위")) return 8;
        if (pollActivity_textView_check_10.getText().toString().equals("1 위")) return 9;
        else return 100;
    }


    private ArrayList<Integer> currentRankingPick() {
        ArrayList<Integer> integerArrayList = new ArrayList<>();
        if (pollActivity_textView_check_1.getText().toString().contains("위")) {
            integerArrayList.add(contentAmount - Integer.parseInt(pollActivity_textView_check_1.getText().toString().replace(" 위", "")));
        }
        if (pollActivity_textView_check_2.getText().toString().contains("위")) {
            integerArrayList.add(contentAmount - Integer.parseInt(pollActivity_textView_check_2.getText().toString().replace(" 위", "")));
        }
        if (pollActivity_textView_check_3.getText().toString().contains("위")) {
            integerArrayList.add(contentAmount - Integer.parseInt(pollActivity_textView_check_3.getText().toString().replace(" 위", "")));
        }
        if (pollActivity_textView_check_4.getText().toString().contains("위")) {
            integerArrayList.add(contentAmount - Integer.parseInt(pollActivity_textView_check_4.getText().toString().replace(" 위", "")));
        }
        if (pollActivity_textView_check_5.getText().toString().contains("위")) {
            integerArrayList.add(contentAmount - Integer.parseInt(pollActivity_textView_check_5.getText().toString().replace(" 위", "")));
        }
        if (pollActivity_textView_check_6.getText().toString().contains("위")) {
            integerArrayList.add(contentAmount - Integer.parseInt(pollActivity_textView_check_6.getText().toString().replace(" 위", "")));
        }
        if (pollActivity_textView_check_7.getText().toString().contains("위")) {
            integerArrayList.add(contentAmount - Integer.parseInt(pollActivity_textView_check_7.getText().toString().replace(" 위", "")));
        }
        if (pollActivity_textView_check_8.getText().toString().contains("위")) {
            integerArrayList.add(contentAmount - Integer.parseInt(pollActivity_textView_check_8.getText().toString().replace(" 위", "")));
        }
        if (pollActivity_textView_check_9.getText().toString().contains("위")) {
            integerArrayList.add(contentAmount - Integer.parseInt(pollActivity_textView_check_9.getText().toString().replace(" 위", "")));
        }
        if (pollActivity_textView_check_10.getText().toString().contains("위")) {
            integerArrayList.add(contentAmount - Integer.parseInt(pollActivity_textView_check_10.getText().toString().replace(" 위", "")));
        }
        return integerArrayList;

    }


    //이미지 선택시 : 순위선택, 다시선택, 원본 보기
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(PollRankingActivity.this, PollRankingChoiceActivity.class);
        if (checkUserHitContent) {
            intent.putExtra("contentsCount", 1);
        } else {
            intent.putExtra("contentsCount", contentAmount);
        }
        switch (v.getId()) {
            case R.id.pollActivity_imageView_userAddContent_1:
                intent.putStringArrayListExtra("rankingTextCheck", rankingTextChecking());
                startActivityForResult(intent, 100);
                break;
            case R.id.pollActivity_imageView_userAddContent_2:
                intent.putStringArrayListExtra("rankingTextCheck", rankingTextChecking());
                startActivityForResult(intent, 200);
                break;
            case R.id.pollActivity_imageView_userAddContent_3:
                intent.putStringArrayListExtra("rankingTextCheck", rankingTextChecking());
                startActivityForResult(intent, 300);
                break;
            case R.id.pollActivity_imageView_userAddContent_4:
                intent.putStringArrayListExtra("rankingTextCheck", rankingTextChecking());
                startActivityForResult(intent, 400);
                break;
            case R.id.pollActivity_imageView_userAddContent_5:
                intent.putStringArrayListExtra("rankingTextCheck", rankingTextChecking());
                startActivityForResult(intent, 500);
                break;
            case R.id.pollActivity_imageView_userAddContent_6:
                intent.putStringArrayListExtra("rankingTextCheck", rankingTextChecking());
                startActivityForResult(intent, 600);
                break;
            case R.id.pollActivity_imageView_userAddContent_7:
                intent.putStringArrayListExtra("rankingTextCheck", rankingTextChecking());
                startActivityForResult(intent, 700);
                break;
            case R.id.pollActivity_imageView_userAddContent_8:
                intent.putStringArrayListExtra("rankingTextCheck", rankingTextChecking());
                startActivityForResult(intent, 800);
                break;
            case R.id.pollActivity_imageView_userAddContent_9:
                intent.putStringArrayListExtra("rankingTextCheck", rankingTextChecking());
                startActivityForResult(intent, 900);
                break;
            case R.id.pollActivity_imageView_userAddContent_10:
                intent.putStringArrayListExtra("rankingTextCheck", rankingTextChecking());
                startActivityForResult(intent, 1000);
                break;

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 100:
                    if (data.getStringExtra("result").equals("refresh")) {
                        refreshActivity();
                    } else if (data.getStringExtra("result").equals("원본 보기")) {
                        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Map<String, Object> contentInfo = (Map<String, Object>) dataSnapshot.getValue();
                                String url = contentInfo.get("imageUrl_0").toString();
                                Intent intent = new Intent(PollRankingActivity.this, FullImageActivity.class);
                                intent.putExtra("imgUrl", url);
                                startActivity(intent);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    } else {
                        if (checkUserHitContent) {
                            Toast.makeText(getApplicationContext(), "투표하셨거나 자신의 투표입니다.!", Toast.LENGTH_SHORT).show();
                        } else {
                            pollActivity_textView_check_1.setText(data.getStringExtra("result"));
                            String userChoice = data.getStringExtra("result");
                            pollActivity_imageView_userAddContent_n1.setVisibility(View.VISIBLE);
                            imageChoiceNumber(userChoice, pollActivity_imageView_userAddContent_n1);
                            Bitmap bitmap = new ImageSaver(PollRankingActivity.this).setFileName(contentKey + "_1")
                                    .setDirectoryName("AQA").load();
                            GlideApp.with(PollRankingActivity.this).load(bitmap).centerCrop().into(pollActivity_imageView_userAddContent_1);
                        }

                    }
                    break;
                case 200:
                    if (data.getStringExtra("result").equals("refresh")) {
                        refreshActivity();
                    } else if (data.getStringExtra("result").equals("원본 보기")) {
                        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Map<String, Object> contentInfo = (Map<String, Object>) dataSnapshot.getValue();
                                String url = contentInfo.get("imageUrl_1").toString();
                                Intent intent = new Intent(PollRankingActivity.this, FullImageActivity.class);
                                intent.putExtra("imgUrl", url);
                                startActivity(intent);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    } else {
                        if (checkUserHitContent) {
                            Toast.makeText(getApplicationContext(), "투표하셨거나 자신의 투표입니다.!", Toast.LENGTH_SHORT).show();
                        } else {
                            pollActivity_textView_check_2.setText(data.getStringExtra("result"));
                            String userChoice = data.getStringExtra("result");
                            pollActivity_imageView_userAddContent_n2.setVisibility(View.VISIBLE);
                            imageChoiceNumber(userChoice, pollActivity_imageView_userAddContent_n2);
                            Bitmap bitmap = new ImageSaver(PollRankingActivity.this).setFileName(contentKey + "_2")
                                    .setDirectoryName("AQA").load();
                            GlideApp.with(PollRankingActivity.this).load(bitmap).centerCrop().into(pollActivity_imageView_userAddContent_2);
                        }
                    }
                    break;
                case 300:
                    if (data.getStringExtra("result").equals("refresh")) {
                        refreshActivity();
                    } else if (data.getStringExtra("result").equals("원본 보기")) {
                        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Map<String, Object> contentInfo = (Map<String, Object>) dataSnapshot.getValue();
                                String url = contentInfo.get("imageUrl_2").toString();
                                Intent intent = new Intent(PollRankingActivity.this, FullImageActivity.class);
                                intent.putExtra("imgUrl", url);
                                startActivity(intent);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    } else {
                        if (checkUserHitContent) {
                            Toast.makeText(getApplicationContext(), "투표하셨거나 자신의 투표입니다.!", Toast.LENGTH_SHORT).show();
                        } else {
                            pollActivity_textView_check_3.setText(data.getStringExtra("result"));
                            String userChoice = data.getStringExtra("result");
                            userChoiceMap.put(pollActivity_imageView_userAddContent_3, userChoice);
                            pollActivity_imageView_userAddContent_n3.setVisibility(View.VISIBLE);
                            imageChoiceNumber(userChoice, pollActivity_imageView_userAddContent_n3);
                            Bitmap bitmap = new ImageSaver(PollRankingActivity.this).setFileName(contentKey + "_3")
                                    .setDirectoryName("AQA").load();
                            GlideApp.with(PollRankingActivity.this).load(bitmap).centerCrop().into(pollActivity_imageView_userAddContent_3);
                        }

                    }
                    break;
                case 400:
                    if (data.getStringExtra("result").equals("refresh")) {
                        refreshActivity();
                    } else if (data.getStringExtra("result").equals("원본 보기")) {
                        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Map<String, Object> contentInfo = (Map<String, Object>) dataSnapshot.getValue();
                                String url = contentInfo.get("imageUrl_3").toString();
                                Intent intent = new Intent(PollRankingActivity.this, FullImageActivity.class);
                                intent.putExtra("imgUrl", url);
                                startActivity(intent);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    } else {
                        if (checkUserHitContent) {
                            Toast.makeText(getApplicationContext(), "투표하셨거나 자신의 투표입니다.!", Toast.LENGTH_SHORT).show();
                        } else {
                            pollActivity_textView_check_4.setText(data.getStringExtra("result"));
                            String userChoice = data.getStringExtra("result");
                            userChoiceMap.put(pollActivity_imageView_userAddContent_4, userChoice);
                            pollActivity_imageView_userAddContent_n4.setVisibility(View.VISIBLE);
                            imageChoiceNumber(userChoice, pollActivity_imageView_userAddContent_n4);
                            Bitmap bitmap = new ImageSaver(PollRankingActivity.this).setFileName(contentKey + "_4")
                                    .setDirectoryName("AQA").load();
                            GlideApp.with(PollRankingActivity.this).load(bitmap).centerCrop().into(pollActivity_imageView_userAddContent_4);
                        }

                    }
                    break;
                case 500:
                    if (data.getStringExtra("result").equals("refresh")) {
                        refreshActivity();
                    } else if (data.getStringExtra("result").equals("원본 보기")) {
                        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Map<String, Object> contentInfo = (Map<String, Object>) dataSnapshot.getValue();
                                String url = contentInfo.get("imageUrl_4").toString();
                                Intent intent = new Intent(PollRankingActivity.this, FullImageActivity.class);
                                intent.putExtra("imgUrl", url);
                                startActivity(intent);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    } else {
                        if (checkUserHitContent) {
                            Toast.makeText(getApplicationContext(), "투표하셨거나 자신의 투표입니다.!", Toast.LENGTH_SHORT).show();
                        } else {
                            pollActivity_textView_check_5.setText(data.getStringExtra("result"));
                            String userChoice = data.getStringExtra("result");
                            userChoiceMap.put(pollActivity_imageView_userAddContent_5, userChoice);
                            pollActivity_imageView_userAddContent_n5.setVisibility(View.VISIBLE);
                            imageChoiceNumber(userChoice, pollActivity_imageView_userAddContent_n5);
                            Bitmap bitmap = new ImageSaver(PollRankingActivity.this).setFileName(contentKey + "_5")
                                    .setDirectoryName("AQA").load();
                            GlideApp.with(PollRankingActivity.this).load(bitmap).centerCrop().into(pollActivity_imageView_userAddContent_5);
                        }

                    }
                    break;
                case 600:
                    if (data.getStringExtra("result").equals("refresh")) {
                        refreshActivity();
                    } else if (data.getStringExtra("result").equals("원본 보기")) {
                        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Map<String, Object> contentInfo = (Map<String, Object>) dataSnapshot.getValue();
                                String url = contentInfo.get("imageUrl_5").toString();
                                Intent intent = new Intent(PollRankingActivity.this, FullImageActivity.class);
                                intent.putExtra("imgUrl", url);
                                startActivity(intent);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    } else {
                        if (checkUserHitContent) {
                            Toast.makeText(getApplicationContext(), "투표하셨거나 자신의 투표입니다.!", Toast.LENGTH_SHORT).show();
                        } else {
                            pollActivity_textView_check_6.setText(data.getStringExtra("result"));
                            String userChoice = data.getStringExtra("result");
                            userChoiceMap.put(pollActivity_imageView_userAddContent_6, userChoice);
                            pollActivity_imageView_userAddContent_n6.setVisibility(View.VISIBLE);
                            imageChoiceNumber(userChoice, pollActivity_imageView_userAddContent_n6);
                            Bitmap bitmap = new ImageSaver(PollRankingActivity.this).setFileName(contentKey + "_6")
                                    .setDirectoryName("AQA").load();
                            GlideApp.with(PollRankingActivity.this).load(bitmap).centerCrop().into(pollActivity_imageView_userAddContent_6);
                        }

                    }
                    break;
                case 700:
                    if (data.getStringExtra("result").equals("refresh")) {
                        refreshActivity();
                    } else if (data.getStringExtra("result").equals("원본 보기")) {
                        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Map<String, Object> contentInfo = (Map<String, Object>) dataSnapshot.getValue();
                                String url = contentInfo.get("imageUrl_6").toString();
                                Intent intent = new Intent(PollRankingActivity.this, FullImageActivity.class);
                                intent.putExtra("imgUrl", url);
                                startActivity(intent);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    } else {
                        if (checkUserHitContent) {
                            Toast.makeText(getApplicationContext(), "투표하셨거나 자신의 투표입니다.!", Toast.LENGTH_SHORT).show();
                        } else {
                            pollActivity_textView_check_7.setText(data.getStringExtra("result"));
                            String userChoice = data.getStringExtra("result");
                            userChoiceMap.put(pollActivity_imageView_userAddContent_7, userChoice);
                            pollActivity_imageView_userAddContent_n7.setVisibility(View.VISIBLE);
                            imageChoiceNumber(userChoice, pollActivity_imageView_userAddContent_n7);
                            Bitmap bitmap = new ImageSaver(PollRankingActivity.this).setFileName(contentKey + "_7")
                                    .setDirectoryName("AQA").load();
                            GlideApp.with(PollRankingActivity.this).load(bitmap).centerCrop().into(pollActivity_imageView_userAddContent_7);
                        }

                    }
                    break;
                case 800:
                    if (data.getStringExtra("result").equals("refresh")) {
                        refreshActivity();
                    } else if (data.getStringExtra("result").equals("원본 보기")) {
                        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Map<String, Object> contentInfo = (Map<String, Object>) dataSnapshot.getValue();
                                String url = contentInfo.get("imageUrl_7").toString();
                                Intent intent = new Intent(PollRankingActivity.this, FullImageActivity.class);
                                intent.putExtra("imgUrl", url);
                                startActivity(intent);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    } else {
                        if (checkUserHitContent) {
                            Toast.makeText(getApplicationContext(), "투표하셨거나 자신의 투표입니다.!", Toast.LENGTH_SHORT).show();
                        } else {
                            pollActivity_textView_check_8.setText(data.getStringExtra("result"));
                            String userChoice = data.getStringExtra("result");
                            userChoiceMap.put(pollActivity_imageView_userAddContent_8, userChoice);
                            pollActivity_imageView_userAddContent_n8.setVisibility(View.VISIBLE);
                            imageChoiceNumber(userChoice, pollActivity_imageView_userAddContent_n8);
                            Bitmap bitmap = new ImageSaver(PollRankingActivity.this).setFileName(contentKey + "_8")
                                    .setDirectoryName("AQA").load();
                            GlideApp.with(PollRankingActivity.this).load(bitmap).centerCrop().into(pollActivity_imageView_userAddContent_8);
                        }

                    }
                    break;
                case 900:
                    if (data.getStringExtra("result").equals("refresh")) {
                        refreshActivity();
                    } else if (data.getStringExtra("result").equals("원본 보기")) {
                        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Map<String, Object> contentInfo = (Map<String, Object>) dataSnapshot.getValue();
                                String url = contentInfo.get("imageUrl_8").toString();
                                Intent intent = new Intent(PollRankingActivity.this, FullImageActivity.class);
                                intent.putExtra("imgUrl", url);
                                startActivity(intent);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    } else {
                        if (checkUserHitContent) {
                            Toast.makeText(getApplicationContext(), "투표하셨거나 자신의 투표입니다.!", Toast.LENGTH_SHORT).show();
                        } else {
                            pollActivity_textView_check_9.setText(data.getStringExtra("result"));
                            String userChoice = data.getStringExtra("result");
                            userChoiceMap.put(pollActivity_imageView_userAddContent_9, userChoice);
                            pollActivity_imageView_userAddContent_n9.setVisibility(View.VISIBLE);
                            imageChoiceNumber(userChoice, pollActivity_imageView_userAddContent_n9);
                            Bitmap bitmap = new ImageSaver(PollRankingActivity.this).setFileName(contentKey + "_9")
                                    .setDirectoryName("AQA").load();
                            GlideApp.with(PollRankingActivity.this).load(bitmap).centerCrop().into(pollActivity_imageView_userAddContent_9);
                        }

                    }
                    break;
                case 1000:
                    if (data.getStringExtra("result").equals("refresh")) {
                        refreshActivity();
                    } else if (data.getStringExtra("result").equals("원본 보기")) {
                        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Map<String, Object> contentInfo = (Map<String, Object>) dataSnapshot.getValue();
                                String url = contentInfo.get("imageUrl_9").toString();
                                Intent intent = new Intent(PollRankingActivity.this, FullImageActivity.class);
                                intent.putExtra("imgUrl", url);
                                startActivity(intent);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    } else {
                        if (checkUserHitContent) {
                            Toast.makeText(getApplicationContext(), "투표하셨거나 자신의 투표입니다.!", Toast.LENGTH_SHORT).show();
                        } else {
                            pollActivity_textView_check_10.setText(data.getStringExtra("result"));
                            String userChoice = data.getStringExtra("result");
                            userChoiceMap.put(pollActivity_imageView_userAddContent_10, userChoice);
                            pollActivity_imageView_userAddContent_n10.setVisibility(View.VISIBLE);
                            imageChoiceNumber(userChoice, pollActivity_imageView_userAddContent_n10);
                            Bitmap bitmap = new ImageSaver(PollRankingActivity.this).setFileName(contentKey + "_10")
                                    .setDirectoryName("AQA").load();
                            GlideApp.with(PollRankingActivity.this).load(bitmap).centerCrop().into(pollActivity_imageView_userAddContent_10);
                        }

                    }
                    break;
                case 10000:
                    if (data.getStringArrayListExtra("resultDelete").get(1).equals("삭제하기")) {
                        String replyKey = data.getStringArrayListExtra("resultDelete").get(0);
                        firebaseDatabase.getReference().child("reply").child(contentKey).child(replyKey).removeValue();
                        removeReplyCount(firebaseDatabase.getReference().child("user_contents").child(contentKey));

                    }
                    break;
                case 20000:
                    String alarmCount = data.getStringExtra("resultAlarmCount");
                    AlarmDoneDialog alarmDoneDialog = AlarmDoneDialog.newInstance(alarmCount);
                    alarmDoneDialog.show(getSupportFragmentManager(), "alarmDoneDialog");
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //선택한 이미지에 순위 표시
    private void imageChoiceNumber(String userChoice, ImageView imageView) {
        switch (userChoice) {
            case "1 위":
                Glide.with(PollRankingActivity.this).load(R.drawable.ic_n_one).into(imageView);
                break;
            case "2 위":
                Glide.with(PollRankingActivity.this).load(R.drawable.ic_n_two).into(imageView);
                break;
            case "3 위":
                Glide.with(PollRankingActivity.this).load(R.drawable.ic_n_three).into(imageView);
                break;
            case "4 위":
                Glide.with(PollRankingActivity.this).load(R.drawable.ic_n_four).into(imageView);
                break;
            case "5 위":
                Glide.with(PollRankingActivity.this).load(R.drawable.ic_n_five).into(imageView);
                break;
            case "6 위":
                Glide.with(PollRankingActivity.this).load(R.drawable.ic_n_six).into(imageView);
                break;
            case "7 위":
                Glide.with(PollRankingActivity.this).load(R.drawable.ic_n_seven).into(imageView);
                break;
            case "8 위":
                Glide.with(PollRankingActivity.this).load(R.drawable.ic_n_eight).into(imageView);
                break;
            case "9 위":
                Glide.with(PollRankingActivity.this).load(R.drawable.ic_n_nine).into(imageView);
                break;
            case "10 위":
                Glide.with(PollRankingActivity.this).load(R.drawable.ic_n_ten).into(imageView);
                break;
        }
    }

    private void removeReplyCount(DatabaseReference ref) {
        ref.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                ContentDTO contentDTO = mutableData.getValue(ContentDTO.class);

                if (contentDTO == null) {
                    return Transaction.success(mutableData);
                }

                contentDTO.replyCount = contentDTO.replyCount - 1;
                mutableData.setValue(contentDTO);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                Toast.makeText(getApplicationContext(), "댓글 삭제 완료", Toast.LENGTH_LONG).show();
            }
        });
    }


    //userPoint(userClass) 점수추가
    public void userPointAdd(final int point) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final String uId = firebaseUser.getUid();
        final DatabaseReference tmpRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(uId);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Object> user = (Map<String, Object>) dataSnapshot.getValue();
                int currentPoint = Integer.parseInt(String.valueOf(user.get("userClass")));
                currentPoint = currentPoint + point;
                if (currentPoint > 500) {
                    tmpRef.child("users").child(uId).child("userClass").setValue(500);
                } else {
                    tmpRef.child("users").child(uId).child("userClass").setValue(currentPoint);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void fabCheck(DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                ContentDTO contentDTO = mutableData.getValue(ContentDTO.class);
                if (contentDTO == null) {
                    return Transaction.success(mutableData);
                }
                if (contentDTO.contentPicker.containsKey(auth.getCurrentUser().getUid())) {

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    pollActivity_fab_result.setImageResource(R.drawable.ic_aqa_qicon);//fab 파란색
                                    pollActivity_imageView_state.setImageResource(R.drawable.ic_aqa_qicon);
                                }
                            });
                        }
                    }).start();

                    checkUserHitContent = true;//투표여부
                } else {

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    pollActivity_fab_result.setImageResource(R.drawable.ic_aqa_qw);//fab 흰색
                                    pollActivity_imageView_state.setImageResource(R.drawable.ic_aqa_qw);
                                }
                            });
                        }
                    }).start();

                    checkUserHitContent = false;//투표여부
                }
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.poll_ranking_menu, menu);
        final MenuItem item = menu.findItem(R.id.menu_delete);
        Uri data = getIntent().getData();
        if (data != null) {
            Log.d("lkj Data2 in intent2", data.toString());
            Log.d("lkj Data2 in intent3", data.getLastPathSegment());
            Log.d("lkj Data2 in intent4", data.getQueryParameter("contentKey"));
            contentKey = data.getQueryParameter("contentKey");
        } else {
            contentKey = getIntent().getStringExtra("contentKey");
        }
        firebaseDatabase.getReference().child("user_contents").child(contentKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ContentDTO contentDTO = dataSnapshot.getValue(ContentDTO.class);
                try {
                    if (contentDTO.getUid().equals(auth.getCurrentUser().getUid())) {
                        item.setVisible(true);
                        pollActivity_imageView_alarm.setVisibility(View.VISIBLE);
                    } else {
                        item.setVisible(false);
                        pollActivity_imageView_alarm.setVisibility(View.GONE);
                    }
                } catch (Exception e) {

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int curId = item.getItemId();
        switch (curId) {
            case R.id.menu_refresh:
                refreshActivity();
                break;
            case R.id.menu_goHome:
                Intent intent = new Intent(PollRankingActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.menu_delete:
                DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
                mRef.child("user_contents").child(contentKey).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                        Log.d("lkj replyCount", String.valueOf(map.get("replyCount")));
                        if (Integer.parseInt(String.valueOf(map.get("replyCount"))) == 0) {
                            openContentDeleteDialog();
                        } else {
                            CannotDeleteDialog cannotDeleteDialog = new CannotDeleteDialog();
                            cannotDeleteDialog.show(getSupportFragmentManager(), "cannotDeleteDialog");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                break;

            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //투표 삭제
    private void openContentDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("삭제 알림");
        builder.setMessage("정말 이 투표를 삭제 하시겠습니까?\n 삭제되면 복구할 수 없습니다.");
        builder.setPositiveButton("확 인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
                DatabaseReference mReference = FirebaseDatabase.getInstance().getReference();
                String pollKey;
                Uri data = getIntent().getData();
                if (data != null) {
                    pollKey = data.getQueryParameter("contentKey");
                } else {
                    pollKey = getIntent().getStringExtra("contentKey");
                }


                storageDelete();
                mReference.child("user_contents").child(pollKey).removeValue();
                mReference.child("users").child(mUser.getUid()).child("uploadContent").child(pollKey).removeValue();
                Toast toast = Toast.makeText(getApplicationContext(), "삭제 되었습니다.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
                toast.show();
                finish();


//                Intent intentAqa = new Intent(PollRankingActivity.this, HomeActivity.class);
//                intentAqa.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intentAqa);

            }
        });
        builder.setNeutralButton("취 소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }

    private void storageDelete() {
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
        mRef.child("user_contents").child(contentKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ContentDTO contentDTO = dataSnapshot.getValue(ContentDTO.class);

                try {
                    String url_0 = contentDTO.imageUrl_0;
                    if (!url_0.isEmpty()) {
                        startDelete(getFileName(url_0));
                    }
                    String url_1 = contentDTO.imageUrl_1;
                    if (!url_1.isEmpty()) {
                        startDelete(getFileName(url_1));
                    }
                    String url_2 = contentDTO.imageUrl_2;
                    if (!url_2.isEmpty()) {
                        startDelete(getFileName(url_2));
                    }
                    String url_3 = contentDTO.imageUrl_3;
                    if (!url_3.isEmpty()) {
                        startDelete(getFileName(url_3));
                    }
                    String url_4 = contentDTO.imageUrl_4;
                    if (!url_4.isEmpty()) {
                        startDelete(getFileName(url_4));
                    }
                    String url_5 = contentDTO.imageUrl_5;
                    if (!url_5.isEmpty()) {
                        startDelete(getFileName(url_5));
                    }
                    String url_6 = contentDTO.imageUrl_6;
                    if (!url_6.isEmpty()) {
                        startDelete(getFileName(url_6));
                    }
                    String url_7 = contentDTO.imageUrl_7;
                    if (!url_7.isEmpty()) {
                        startDelete(getFileName(url_7));
                    }
                    String url_8 = contentDTO.imageUrl_8;
                    if (!url_8.isEmpty()) {
                        startDelete(getFileName(url_8));
                    }
                    String url_9 = contentDTO.imageUrl_9;
                    if (!url_9.isEmpty()) {
                        startDelete(getFileName(url_9));
                    }
                } catch (Exception e) {

                }


                FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
                DatabaseReference mReference = FirebaseDatabase.getInstance().getReference();
                mReference.child("user_contents").child(contentKey).removeValue();
                mReference.child("users").child(mUser.getUid()).child("uploadContent").child(contentKey).removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void startDelete(Object fileName) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference desertRef = storageRef.child("images/" + fileName);
        desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        });
    }

    private Object getFileName(String url) {
        url = url.substring(81);
        String[] urlArray = url.split("\\?");
        urlArray[1] = null;
        return urlArray[0];
    }


    public void refreshActivity() {
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }


    //picker의 현재픽,성별,나이 가져와서 통계항목에 + n
    private String addStatistics(String statistics_code, ArrayList<Integer> currentPickScore, String gender, int age) {
        String[] stringArray = null;
        stringArray = statistics_code.split(":");
        int[] tmpStatistics = new int[stringArray.length];
        for (int i = 0; i < stringArray.length; i++) {
            tmpStatistics[i] = Integer.parseInt(stringArray[i]);
        }

        if (gender.equals("여")) {
            try {
                for (int i = 0; i < 10; i++) {
                    tmpStatistics[i] = tmpStatistics[i] + currentPickScore.get(i);
                }
            } catch (Exception e) {
                Log.w("lkj exception", e);
            }


        }
        if (gender.equals("남")) {
            try {
                for (int i = 10; i < 20; i++) {
                    tmpStatistics[i] = tmpStatistics[i] + currentPickScore.get(i - 10);
                }
            } catch (Exception e) {
                Log.w("lkj exception", e);
            }
        }
        if (age == 10 || age == 11 || age == 12) {
            try {
                for (int i = 20; i < 30; i++) {
                    tmpStatistics[i] = tmpStatistics[i] + currentPickScore.get(i - 20);
                }
            } catch (Exception e) {
                Log.w("lkj exception", e);
            }
        }
        if (age == 13 || age == 14 || age == 15 || age == 16) {
            try {
                for (int i = 30; i < 40; i++) {
                    tmpStatistics[i] = tmpStatistics[i] + currentPickScore.get(i - 30);
                }
            } catch (Exception e) {
                Log.w("lkj exception", e);
            }
        }
        if (age == 17 || age == 18 || age == 19) {
            try {
                for (int i = 40; i < 50; i++) {
                    tmpStatistics[i] = tmpStatistics[i] + currentPickScore.get(i - 40);
                }
            } catch (Exception e) {
                Log.w("lkj exception", e);
            }
        }
        if (age == 20 || age == 21 || age == 22) {
            try {
                for (int i = 50; i < 60; i++) {
                    tmpStatistics[i] = tmpStatistics[i] + currentPickScore.get(i - 50);
                }
            } catch (Exception e) {
                Log.w("lkj exception", e);
            }
        }
        if (age == 23 || age == 24 || age == 25 || age == 26) {
            try {
                for (int i = 60; i < 70; i++) {
                    tmpStatistics[i] = tmpStatistics[i] + currentPickScore.get(i - 60);
                }
            } catch (Exception e) {
                Log.w("lkj exception", e);
            }
        }
        if (age == 27 || age == 28 || age == 29) {
            try {
                for (int i = 70; i < 80; i++) {
                    tmpStatistics[i] = tmpStatistics[i] + currentPickScore.get(i - 70);
                }
            } catch (Exception e) {
                Log.w("lkj exception", e);
            }
        }
        if (age == 30 || age == 31 || age == 32) {
            try {
                for (int i = 80; i < 90; i++) {
                    tmpStatistics[i] = tmpStatistics[i] + currentPickScore.get(i - 80);
                }
            } catch (Exception e) {
                Log.w("lkj exception", e);
            }
        }
        if (age == 33 || age == 34 || age == 35 || age == 36) {
            try {
                for (int i = 90; i < 100; i++) {
                    tmpStatistics[i] = tmpStatistics[i] + currentPickScore.get(i - 90);
                }
            } catch (Exception e) {
                Log.w("lkj exception", e);
            }
        }
        if (age == 37 || age == 38 || age == 39) {
            try {
                for (int i = 100; i < 110; i++) {
                    tmpStatistics[i] = tmpStatistics[i] + currentPickScore.get(i - 100);
                }
            } catch (Exception e) {
                Log.w("lkj exception", e);
            }
        }
        if (age == 40 || age == 41 || age == 42) {
            try {
                for (int i = 110; i < 120; i++) {
                    tmpStatistics[i] = tmpStatistics[i] + currentPickScore.get(i - 110);
                }
            } catch (Exception e) {
                Log.w("lkj exception", e);
            }
        }
        if (age == 43 || age == 44 || age == 45 || age == 46) {
            try {
                for (int i = 120; i < 130; i++) {
                    tmpStatistics[i] = tmpStatistics[i] + currentPickScore.get(i - 120);
                }
            } catch (Exception e) {
                Log.w("lkj exception", e);
            }
        }
        if (age == 47 || age == 48 || age == 49) {
            try {
                for (int i = 130; i < 140; i++) {
                    tmpStatistics[i] = tmpStatistics[i] + currentPickScore.get(i - 130);
                }
            } catch (Exception e) {
                Log.w("lkj exception", e);
            }
        }
        if (age == 50 || age == 51 || age == 52) {
            try {
                for (int i = 140; i < 150; i++) {
                    tmpStatistics[i] = tmpStatistics[i] + currentPickScore.get(i - 140);
                }
            } catch (Exception e) {
                Log.w("lkj exception", e);
            }
        }
        if (age == 53 || age == 54 || age == 55 || age == 56) {
            try {
                for (int i = 150; i < 160; i++) {
                    tmpStatistics[i] = tmpStatistics[i] + currentPickScore.get(i - 150);
                }
            } catch (Exception e) {
                Log.w("lkj exception", e);
            }
        }
        if (age == 57 || age == 58 || age == 59) {
            try {
                for (int i = 160; i < 170; i++) {
                    tmpStatistics[i] = tmpStatistics[i] + currentPickScore.get(i - 160);
                }
            } catch (Exception e) {
                Log.w("lkj exception", e);
            }
        }
        if (age == 60 || age == 61 || age == 62) {
            try {
                for (int i = 170; i < 180; i++) {
                    tmpStatistics[i] = tmpStatistics[i] + currentPickScore.get(i - 170);
                }
            } catch (Exception e) {
                Log.w("lkj exception", e);
            }
        }
        if (age == 63 || age == 64 || age == 65 || age == 66) {
            try {
                for (int i = 180; i < 190; i++) {
                    tmpStatistics[i] = tmpStatistics[i] + currentPickScore.get(i - 180);
                }
            } catch (Exception e) {
                Log.w("lkj exception", e);
            }
        }
        if (age == 67 || age == 68 || age == 69) {
            try {
                for (int i = 190; i < 200; i++) {
                    tmpStatistics[i] = tmpStatistics[i] + currentPickScore.get(i - 190);
                }
            } catch (Exception e) {
                Log.w("lkj exception", e);
            }
        }
        if (age == 70 || age == 71 || age == 72) {
            try {
                for (int i = 200; i < 210; i++) {
                    tmpStatistics[i] = tmpStatistics[i] + currentPickScore.get(i - 200);
                }
            } catch (Exception e) {
                Log.w("lkj exception", e);
            }
        }
        if (age == 73 || age == 74 || age == 75 || age == 76) {
            try {
                for (int i = 210; i < 220; i++) {
                    tmpStatistics[i] = tmpStatistics[i] + currentPickScore.get(i - 210);
                }
            } catch (Exception e) {
                Log.w("lkj exception", e);
            }
        }
        if (age == 77 || age == 78 || age == 79) {
            try {
                for (int i = 220; i < 230; i++) {
                    tmpStatistics[i] = tmpStatistics[i] + currentPickScore.get(i - 220);
                }
            } catch (Exception e) {
                Log.w("lkj exception", e);
            }
        }
        if (age == 80 || age == 81 || age == 82) {
            try {
                for (int i = 230; i < 240; i++) {
                    tmpStatistics[i] = tmpStatistics[i] + currentPickScore.get(i - 230);
                }
            } catch (Exception e) {
                Log.w("lkj exception", e);
            }
        }
        if (age == 83 || age == 84 || age == 85 || age == 86) {
            try {
                for (int i = 240; i < 250; i++) {
                    tmpStatistics[i] = tmpStatistics[i] + currentPickScore.get(i - 240);
                }
            } catch (Exception e) {
                Log.w("lkj exception", e);
            }
        }
        if (age == 87 || age == 88 || age == 89) {
            try {
                for (int i = 250; i < 260; i++) {
                    tmpStatistics[i] = tmpStatistics[i] + currentPickScore.get(i - 250);
                }
            } catch (Exception e) {
                Log.w("lkj exception", e);
            }
        }

        String callbackStatistics = java.util.Arrays.toString(tmpStatistics);
        callbackStatistics = callbackStatistics.replace(", ", ":");
        callbackStatistics = callbackStatistics.replace("[", "");
        callbackStatistics = callbackStatistics.replace("]", "");

        return callbackStatistics;
    }



    //댓글 좋아요 클릭
    private void onReplyLikeClicked(final DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                ReplyDTO replyDTO = mutableData.getValue(ReplyDTO.class);

                if (replyDTO == null) {
                    return Transaction.success(mutableData);
                }
                //좋아요 누른 이력 있으면 지우고 카운트-1
                if (replyDTO.likes.containsKey(auth.getCurrentUser().getUid())) {
                    replyDTO.likeCount = replyDTO.likeCount - 1;
                    replyDTO.likes.remove(auth.getCurrentUser().getUid());
                    //좋아요 누른 이력 없으면 더하고 카운트+1
                } else {
                    replyDTO.likeCount = replyDTO.likeCount + 1;
                    replyDTO.likes.put(auth.getCurrentUser().getUid(), true);
                }

                // Set value and report transaction success
                mutableData.setValue(replyDTO);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                // Transaction completed
                Log.d("lkjlkj", "postTransaction:onComplete:" + databaseError);


            }
        });
    }


    //게시물 좋아요 클릭 (따봉 이미지)
    private void onLikeClicked(final DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                ContentDTO contentDTO = mutableData.getValue(ContentDTO.class);

                if (contentDTO == null) {
                    return Transaction.success(mutableData);
                }
                //좋아요 누른 이력 있으면 지우고 카운트-1
                if (contentDTO.likes.containsKey(auth.getCurrentUser().getUid())) {
                    contentDTO.likeCount = contentDTO.likeCount - 1;
                    contentDTO.likes.remove(auth.getCurrentUser().getUid());

                    // users/내uid/likeContent/컨텐트key : false      : 좋아요 누른 컨텐츠 리스트 false
                    firebaseDatabase.getReference()
                            .child("users")
                            .child(auth.getCurrentUser().getUid())
                            .child("likeContent")
                            .child(contentDTO.getContentKey())
                            .setValue("false");

                    //좋아요 누른 이력 없으면 더하고 카운트+1
                } else {
                    contentDTO.likeCount = contentDTO.likeCount + 1;
                    contentDTO.likes.put(auth.getCurrentUser().getUid(), true);

                    // users/내uid/likeContent/컨텐트key : trud      : 좋아요 누른 컨텐츠 리스트 true
                    firebaseDatabase.getReference()
                            .child("users")
                            .child(auth.getCurrentUser().getUid())
                            .child("likeContent")
                            .child(contentDTO.getContentKey())
                            .setValue("true");

                }

                // Set value and report transaction success
                mutableData.setValue(contentDTO);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                // Transaction completed
                Log.d("lkjlkj", "postTransaction:onComplete:" + databaseError);


            }
        });
    }


    //이미지 blur 효과 주기
    public static Bitmap imageBlur(Context context, Bitmap bitmap, float radius) {

        RenderScript rs = RenderScript.create(context);

        final Allocation input = Allocation.createFromBitmap(rs, bitmap);
        final Allocation output = Allocation.createTyped(rs, input.getType());
        final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        script.setRadius(radius);
        script.setInput(input);
        script.forEach(output);
        output.copyTo(bitmap);

        return bitmap;
    }

    @Override
    public void ShareDialogCallback(String string) {
        if (string.equals("공유하기")) {

            Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setType("text/plain");
//                     Set default text message
//                     카톡, 이메일, MMS 다 이걸로 설정 가능
            String subject = "하나만 선택해 어서! AQA!";
            String text = shareUrl;
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            intent.putExtra(Intent.EXTRA_TEXT, text);
//                     Title of intent
            Intent chooser = Intent.createChooser(intent, "공유하기");
            startActivity(chooser);

        }
    }

    //이미지 비트맵으로 만들어서 캐시에 저장
    private class BlurAsyncTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    final ContentDTO contentDTO = dataSnapshot.getValue(ContentDTO.class);
                    try {
                        switch (contentDTO.getItemViewType()) {
                            case 2:
                                Glide.with(getApplicationContext()).asBitmap().load(contentDTO.getImageUrl_0())
                                        .apply(new RequestOptions().override(200, 200))
                                        .into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                Bitmap bitmap = imageBlur(getApplicationContext(), resource, 24f);
                                                ImageSaver imageSaver = new ImageSaver(getApplicationContext());
                                                imageSaver.setFileName(contentDTO.contentKey + "_1").setDirectoryName("AQA").save(bitmap);
                                            }
                                        });
                                Glide.with(getApplicationContext()).asBitmap().load(contentDTO.getImageUrl_1())
                                        .apply(new RequestOptions().override(200, 200))
                                        .into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                Bitmap bitmap = imageBlur(getApplicationContext(), resource, 24f);
                                                ImageSaver imageSaver = new ImageSaver(getApplicationContext());
                                                imageSaver.setFileName(contentDTO.contentKey + "_2").setDirectoryName("AQA").save(bitmap);
                                            }
                                        });
                                break;
                            case 3:
                                Glide.with(getApplicationContext()).asBitmap().load(contentDTO.getImageUrl_0())
                                        .apply(new RequestOptions().override(200, 200))
                                        .into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                Bitmap bitmap = imageBlur(getApplicationContext(), resource, 24f);
                                                ImageSaver imageSaver = new ImageSaver(getApplicationContext());
                                                imageSaver.setFileName(contentDTO.contentKey + "_1").setDirectoryName("AQA").save(bitmap);
                                            }
                                        });
                                Glide.with(getApplicationContext()).asBitmap().load(contentDTO.getImageUrl_1())
                                        .apply(new RequestOptions().override(200, 200))
                                        .into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                Bitmap bitmap = imageBlur(getApplicationContext(), resource, 24f);
                                                ImageSaver imageSaver = new ImageSaver(getApplicationContext());
                                                imageSaver.setFileName(contentDTO.contentKey + "_2").setDirectoryName("AQA").save(bitmap);
                                            }
                                        });
                                Glide.with(getApplicationContext()).asBitmap().load(contentDTO.getImageUrl_2())
                                        .apply(new RequestOptions().override(200, 200))
                                        .into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                Bitmap bitmap = imageBlur(getApplicationContext(), resource, 24f);
                                                ImageSaver imageSaver = new ImageSaver(getApplicationContext());
                                                imageSaver.setFileName(contentDTO.contentKey + "_3").setDirectoryName("AQA").save(bitmap);
                                            }
                                        });
                                break;
                            case 4:
                                Glide.with(getApplicationContext()).asBitmap().load(contentDTO.getImageUrl_0())
                                        .apply(new RequestOptions().override(200, 200))
                                        .into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                Bitmap bitmap = imageBlur(getApplicationContext(), resource, 24f);
                                                ImageSaver imageSaver = new ImageSaver(getApplicationContext());
                                                imageSaver.setFileName(contentDTO.contentKey + "_1").setDirectoryName("AQA").save(bitmap);
                                            }
                                        });
                                Glide.with(getApplicationContext()).asBitmap().load(contentDTO.getImageUrl_1())
                                        .apply(new RequestOptions().override(200, 200))
                                        .into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                Bitmap bitmap = imageBlur(getApplicationContext(), resource, 24f);
                                                ImageSaver imageSaver = new ImageSaver(getApplicationContext());
                                                imageSaver.setFileName(contentDTO.contentKey + "_2").setDirectoryName("AQA").save(bitmap);
                                            }
                                        });
                                Glide.with(getApplicationContext()).asBitmap().load(contentDTO.getImageUrl_2())
                                        .apply(new RequestOptions().override(200, 200))
                                        .into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                Bitmap bitmap = imageBlur(getApplicationContext(), resource, 24f);
                                                ImageSaver imageSaver = new ImageSaver(getApplicationContext());
                                                imageSaver.setFileName(contentDTO.contentKey + "_3").setDirectoryName("AQA").save(bitmap);
                                            }
                                        });
                                Glide.with(getApplicationContext()).asBitmap().load(contentDTO.getImageUrl_3())
                                        .apply(new RequestOptions().override(200, 200))
                                        .into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                Bitmap bitmap = imageBlur(getApplicationContext(), resource, 24f);
                                                ImageSaver imageSaver = new ImageSaver(getApplicationContext());
                                                imageSaver.setFileName(contentDTO.contentKey + "_4").setDirectoryName("AQA").save(bitmap);
                                            }
                                        });
                            case 5:
                                Glide.with(getApplicationContext()).asBitmap().load(contentDTO.getImageUrl_0())
                                        .apply(new RequestOptions().override(200, 200))
                                        .into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                Bitmap bitmap = imageBlur(getApplicationContext(), resource, 24f);
                                                ImageSaver imageSaver = new ImageSaver(getApplicationContext());
                                                imageSaver.setFileName(contentDTO.contentKey + "_1").setDirectoryName("AQA").save(bitmap);
                                            }
                                        });
                                Glide.with(getApplicationContext()).asBitmap().load(contentDTO.getImageUrl_1())
                                        .apply(new RequestOptions().override(200, 200))
                                        .into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                Bitmap bitmap = imageBlur(getApplicationContext(), resource, 24f);
                                                ImageSaver imageSaver = new ImageSaver(getApplicationContext());
                                                imageSaver.setFileName(contentDTO.contentKey + "_2").setDirectoryName("AQA").save(bitmap);
                                            }
                                        });
                                Glide.with(getApplicationContext()).asBitmap().load(contentDTO.getImageUrl_2())
                                        .apply(new RequestOptions().override(200, 200))
                                        .into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                Bitmap bitmap = imageBlur(getApplicationContext(), resource, 24f);
                                                ImageSaver imageSaver = new ImageSaver(getApplicationContext());
                                                imageSaver.setFileName(contentDTO.contentKey + "_3").setDirectoryName("AQA").save(bitmap);
                                            }
                                        });
                                Glide.with(getApplicationContext()).asBitmap().load(contentDTO.getImageUrl_3())
                                        .apply(new RequestOptions().override(200, 200))
                                        .into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                Bitmap bitmap = imageBlur(getApplicationContext(), resource, 24f);
                                                ImageSaver imageSaver = new ImageSaver(getApplicationContext());
                                                imageSaver.setFileName(contentDTO.contentKey + "_4").setDirectoryName("AQA").save(bitmap);
                                            }
                                        });

                                Glide.with(getApplicationContext()).asBitmap().load(contentDTO.getImageUrl_4())
                                        .apply(new RequestOptions().override(200, 200))
                                        .into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                Bitmap bitmap = imageBlur(getApplicationContext(), resource, 24f);
                                                ImageSaver imageSaver = new ImageSaver(getApplicationContext());
                                                imageSaver.setFileName(contentDTO.contentKey + "_5").setDirectoryName("AQA").save(bitmap);
                                            }
                                        });
                                break;
                            case 6:
                                Glide.with(getApplicationContext()).asBitmap().load(contentDTO.getImageUrl_0())
                                        .apply(new RequestOptions().override(200, 200))
                                        .into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                Bitmap bitmap = imageBlur(getApplicationContext(), resource, 24f);
                                                ImageSaver imageSaver = new ImageSaver(getApplicationContext());
                                                imageSaver.setFileName(contentDTO.contentKey + "_1").setDirectoryName("AQA").save(bitmap);
                                            }
                                        });
                                Glide.with(getApplicationContext()).asBitmap().load(contentDTO.getImageUrl_1())
                                        .apply(new RequestOptions().override(200, 200))
                                        .into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                Bitmap bitmap = imageBlur(getApplicationContext(), resource, 24f);
                                                ImageSaver imageSaver = new ImageSaver(getApplicationContext());
                                                imageSaver.setFileName(contentDTO.contentKey + "_2").setDirectoryName("AQA").save(bitmap);
                                            }
                                        });
                                Glide.with(getApplicationContext()).asBitmap().load(contentDTO.getImageUrl_2())
                                        .apply(new RequestOptions().override(200, 200))
                                        .into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                Bitmap bitmap = imageBlur(getApplicationContext(), resource, 24f);
                                                ImageSaver imageSaver = new ImageSaver(getApplicationContext());
                                                imageSaver.setFileName(contentDTO.contentKey + "_3").setDirectoryName("AQA").save(bitmap);
                                            }
                                        });
                                Glide.with(getApplicationContext()).asBitmap().load(contentDTO.getImageUrl_3())
                                        .apply(new RequestOptions().override(200, 200))
                                        .into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                Bitmap bitmap = imageBlur(getApplicationContext(), resource, 24f);
                                                ImageSaver imageSaver = new ImageSaver(getApplicationContext());
                                                imageSaver.setFileName(contentDTO.contentKey + "_4").setDirectoryName("AQA").save(bitmap);
                                            }
                                        });

                                Glide.with(getApplicationContext()).asBitmap().load(contentDTO.getImageUrl_4())
                                        .apply(new RequestOptions().override(200, 200))
                                        .into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                Bitmap bitmap = imageBlur(getApplicationContext(), resource, 24f);
                                                ImageSaver imageSaver = new ImageSaver(getApplicationContext());
                                                imageSaver.setFileName(contentDTO.contentKey + "_5").setDirectoryName("AQA").save(bitmap);
                                            }
                                        });
                                Glide.with(getApplicationContext()).asBitmap().load(contentDTO.getImageUrl_5())
                                        .apply(new RequestOptions().override(200, 200))
                                        .into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                Bitmap bitmap = imageBlur(getApplicationContext(), resource, 24f);
                                                ImageSaver imageSaver = new ImageSaver(getApplicationContext());
                                                imageSaver.setFileName(contentDTO.contentKey + "_6").setDirectoryName("AQA").save(bitmap);
                                            }
                                        });
                                break;
                            case 7:
                                Glide.with(getApplicationContext()).asBitmap().load(contentDTO.getImageUrl_0())
                                        .apply(new RequestOptions().override(200, 200))
                                        .into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                Bitmap bitmap = imageBlur(getApplicationContext(), resource, 24f);
                                                ImageSaver imageSaver = new ImageSaver(getApplicationContext());
                                                imageSaver.setFileName(contentDTO.contentKey + "_1").setDirectoryName("AQA").save(bitmap);
                                            }
                                        });
                                Glide.with(getApplicationContext()).asBitmap().load(contentDTO.getImageUrl_1())
                                        .apply(new RequestOptions().override(200, 200))
                                        .into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                Bitmap bitmap = imageBlur(getApplicationContext(), resource, 24f);
                                                ImageSaver imageSaver = new ImageSaver(getApplicationContext());
                                                imageSaver.setFileName(contentDTO.contentKey + "_2").setDirectoryName("AQA").save(bitmap);
                                            }
                                        });
                                Glide.with(getApplicationContext()).asBitmap().load(contentDTO.getImageUrl_2())
                                        .apply(new RequestOptions().override(200, 200))
                                        .into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                Bitmap bitmap = imageBlur(getApplicationContext(), resource, 24f);
                                                ImageSaver imageSaver = new ImageSaver(getApplicationContext());
                                                imageSaver.setFileName(contentDTO.contentKey + "_3").setDirectoryName("AQA").save(bitmap);
                                            }
                                        });
                                Glide.with(getApplicationContext()).asBitmap().load(contentDTO.getImageUrl_3())
                                        .apply(new RequestOptions().override(200, 200))
                                        .into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                Bitmap bitmap = imageBlur(getApplicationContext(), resource, 24f);
                                                ImageSaver imageSaver = new ImageSaver(getApplicationContext());
                                                imageSaver.setFileName(contentDTO.contentKey + "_4").setDirectoryName("AQA").save(bitmap);
                                            }
                                        });

                                Glide.with(getApplicationContext()).asBitmap().load(contentDTO.getImageUrl_4())
                                        .apply(new RequestOptions().override(200, 200))
                                        .into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                Bitmap bitmap = imageBlur(getApplicationContext(), resource, 24f);
                                                ImageSaver imageSaver = new ImageSaver(getApplicationContext());
                                                imageSaver.setFileName(contentDTO.contentKey + "_5").setDirectoryName("AQA").save(bitmap);
                                            }
                                        });
                                Glide.with(getApplicationContext()).asBitmap().load(contentDTO.getImageUrl_5())
                                        .apply(new RequestOptions().override(200, 200))
                                        .into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                Bitmap bitmap = imageBlur(getApplicationContext(), resource, 24f);
                                                ImageSaver imageSaver = new ImageSaver(getApplicationContext());
                                                imageSaver.setFileName(contentDTO.contentKey + "_6").setDirectoryName("AQA").save(bitmap);
                                            }
                                        });
                                Glide.with(getApplicationContext()).asBitmap().load(contentDTO.getImageUrl_6())
                                        .apply(new RequestOptions().override(200, 200))
                                        .into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                Bitmap bitmap = imageBlur(getApplicationContext(), resource, 24f);
                                                ImageSaver imageSaver = new ImageSaver(getApplicationContext());
                                                imageSaver.setFileName(contentDTO.contentKey + "_7").setDirectoryName("AQA").save(bitmap);
                                            }
                                        });
                                break;
                            case 8:
                                Glide.with(getApplicationContext()).asBitmap().load(contentDTO.getImageUrl_0())
                                        .apply(new RequestOptions().override(200, 200))
                                        .into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                Bitmap bitmap = imageBlur(getApplicationContext(), resource, 24f);
                                                ImageSaver imageSaver = new ImageSaver(getApplicationContext());
                                                imageSaver.setFileName(contentDTO.contentKey + "_1").setDirectoryName("AQA").save(bitmap);
                                            }
                                        });
                                Glide.with(getApplicationContext()).asBitmap().load(contentDTO.getImageUrl_1())
                                        .apply(new RequestOptions().override(200, 200))
                                        .into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                Bitmap bitmap = imageBlur(getApplicationContext(), resource, 24f);
                                                ImageSaver imageSaver = new ImageSaver(getApplicationContext());
                                                imageSaver.setFileName(contentDTO.contentKey + "_2").setDirectoryName("AQA").save(bitmap);
                                            }
                                        });
                                Glide.with(getApplicationContext()).asBitmap().load(contentDTO.getImageUrl_2())
                                        .apply(new RequestOptions().override(200, 200))
                                        .into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                Bitmap bitmap = imageBlur(getApplicationContext(), resource, 24f);
                                                ImageSaver imageSaver = new ImageSaver(getApplicationContext());
                                                imageSaver.setFileName(contentDTO.contentKey + "_3").setDirectoryName("AQA").save(bitmap);
                                            }
                                        });
                                Glide.with(getApplicationContext()).asBitmap().load(contentDTO.getImageUrl_3())
                                        .apply(new RequestOptions().override(200, 200))
                                        .into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                Bitmap bitmap = imageBlur(getApplicationContext(), resource, 24f);
                                                ImageSaver imageSaver = new ImageSaver(getApplicationContext());
                                                imageSaver.setFileName(contentDTO.contentKey + "_4").setDirectoryName("AQA").save(bitmap);
                                            }
                                        });

                                Glide.with(getApplicationContext()).asBitmap().load(contentDTO.getImageUrl_4())
                                        .apply(new RequestOptions().override(200, 200))
                                        .into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                Bitmap bitmap = imageBlur(getApplicationContext(), resource, 24f);
                                                ImageSaver imageSaver = new ImageSaver(getApplicationContext());
                                                imageSaver.setFileName(contentDTO.contentKey + "_5").setDirectoryName("AQA").save(bitmap);
                                            }
                                        });
                                Glide.with(getApplicationContext()).asBitmap().load(contentDTO.getImageUrl_5())
                                        .apply(new RequestOptions().override(200, 200))
                                        .into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                Bitmap bitmap = imageBlur(getApplicationContext(), resource, 24f);
                                                ImageSaver imageSaver = new ImageSaver(getApplicationContext());
                                                imageSaver.setFileName(contentDTO.contentKey + "_6").setDirectoryName("AQA").save(bitmap);
                                            }
                                        });
                                Glide.with(getApplicationContext()).asBitmap().load(contentDTO.getImageUrl_6())
                                        .apply(new RequestOptions().override(200, 200))
                                        .into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                Bitmap bitmap = imageBlur(getApplicationContext(), resource, 24f);
                                                ImageSaver imageSaver = new ImageSaver(getApplicationContext());
                                                imageSaver.setFileName(contentDTO.contentKey + "_7").setDirectoryName("AQA").save(bitmap);
                                            }
                                        });

                                Glide.with(getApplicationContext()).asBitmap().load(contentDTO.getImageUrl_7())
                                        .apply(new RequestOptions().override(200, 200))
                                        .into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                Bitmap bitmap = imageBlur(getApplicationContext(), resource, 24f);
                                                ImageSaver imageSaver = new ImageSaver(getApplicationContext());
                                                imageSaver.setFileName(contentDTO.contentKey + "_8").setDirectoryName("AQA").save(bitmap);
                                            }
                                        });
                                break;
                            case 9:
                                Glide.with(getApplicationContext()).asBitmap().load(contentDTO.getImageUrl_0())
                                        .apply(new RequestOptions().override(200, 200))
                                        .into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                Bitmap bitmap = imageBlur(getApplicationContext(), resource, 24f);
                                                ImageSaver imageSaver = new ImageSaver(getApplicationContext());
                                                imageSaver.setFileName(contentDTO.contentKey + "_1").setDirectoryName("AQA").save(bitmap);
                                            }
                                        });
                                Glide.with(getApplicationContext()).asBitmap().load(contentDTO.getImageUrl_1())
                                        .apply(new RequestOptions().override(200, 200))
                                        .into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                Bitmap bitmap = imageBlur(getApplicationContext(), resource, 24f);
                                                ImageSaver imageSaver = new ImageSaver(getApplicationContext());
                                                imageSaver.setFileName(contentDTO.contentKey + "_2").setDirectoryName("AQA").save(bitmap);
                                            }
                                        });
                                Glide.with(getApplicationContext()).asBitmap().load(contentDTO.getImageUrl_2())
                                        .apply(new RequestOptions().override(200, 200))
                                        .into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                Bitmap bitmap = imageBlur(getApplicationContext(), resource, 24f);
                                                ImageSaver imageSaver = new ImageSaver(getApplicationContext());
                                                imageSaver.setFileName(contentDTO.contentKey + "_3").setDirectoryName("AQA").save(bitmap);
                                            }
                                        });
                                Glide.with(getApplicationContext()).asBitmap().load(contentDTO.getImageUrl_3())
                                        .apply(new RequestOptions().override(200, 200))
                                        .into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                Bitmap bitmap = imageBlur(getApplicationContext(), resource, 24f);
                                                ImageSaver imageSaver = new ImageSaver(getApplicationContext());
                                                imageSaver.setFileName(contentDTO.contentKey + "_4").setDirectoryName("AQA").save(bitmap);
                                            }
                                        });

                                Glide.with(getApplicationContext()).asBitmap().load(contentDTO.getImageUrl_4())
                                        .apply(new RequestOptions().override(200, 200))
                                        .into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                Bitmap bitmap = imageBlur(getApplicationContext(), resource, 24f);
                                                ImageSaver imageSaver = new ImageSaver(getApplicationContext());
                                                imageSaver.setFileName(contentDTO.contentKey + "_5").setDirectoryName("AQA").save(bitmap);
                                            }
                                        });
                                Glide.with(getApplicationContext()).asBitmap().load(contentDTO.getImageUrl_5())
                                        .apply(new RequestOptions().override(200, 200))
                                        .into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                Bitmap bitmap = imageBlur(getApplicationContext(), resource, 24f);
                                                ImageSaver imageSaver = new ImageSaver(getApplicationContext());
                                                imageSaver.setFileName(contentDTO.contentKey + "_6").setDirectoryName("AQA").save(bitmap);
                                            }
                                        });
                                Glide.with(getApplicationContext()).asBitmap().load(contentDTO.getImageUrl_6())
                                        .apply(new RequestOptions().override(200, 200))
                                        .into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                Bitmap bitmap = imageBlur(getApplicationContext(), resource, 24f);
                                                ImageSaver imageSaver = new ImageSaver(getApplicationContext());
                                                imageSaver.setFileName(contentDTO.contentKey + "_7").setDirectoryName("AQA").save(bitmap);
                                            }
                                        });

                                Glide.with(getApplicationContext()).asBitmap().load(contentDTO.getImageUrl_7())
                                        .apply(new RequestOptions().override(200, 200))
                                        .into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                Bitmap bitmap = imageBlur(getApplicationContext(), resource, 24f);
                                                ImageSaver imageSaver = new ImageSaver(getApplicationContext());
                                                imageSaver.setFileName(contentDTO.contentKey + "_8").setDirectoryName("AQA").save(bitmap);
                                            }
                                        });
                                Glide.with(getApplicationContext()).asBitmap().load(contentDTO.getImageUrl_8())
                                        .apply(new RequestOptions().override(200, 200))
                                        .into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                Bitmap bitmap = imageBlur(getApplicationContext(), resource, 24f);
                                                ImageSaver imageSaver = new ImageSaver(getApplicationContext());
                                                imageSaver.setFileName(contentDTO.contentKey + "_9").setDirectoryName("AQA").save(bitmap);
                                            }
                                        });
                                break;
                            case 10:
                                Glide.with(getApplicationContext()).asBitmap().load(contentDTO.getImageUrl_0())
                                        .apply(new RequestOptions().override(200, 200))
                                        .into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                Bitmap bitmap = imageBlur(getApplicationContext(), resource, 24f);
                                                ImageSaver imageSaver = new ImageSaver(getApplicationContext());
                                                imageSaver.setFileName(contentDTO.contentKey + "_1").setDirectoryName("AQA").save(bitmap);
                                            }
                                        });
                                Glide.with(getApplicationContext()).asBitmap().load(contentDTO.getImageUrl_1())
                                        .apply(new RequestOptions().override(200, 200))
                                        .into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                Bitmap bitmap = imageBlur(getApplicationContext(), resource, 24f);
                                                ImageSaver imageSaver = new ImageSaver(getApplicationContext());
                                                imageSaver.setFileName(contentDTO.contentKey + "_2").setDirectoryName("AQA").save(bitmap);
                                            }
                                        });
                                Glide.with(getApplicationContext()).asBitmap().load(contentDTO.getImageUrl_2())
                                        .apply(new RequestOptions().override(200, 200))
                                        .into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                Bitmap bitmap = imageBlur(getApplicationContext(), resource, 24f);
                                                ImageSaver imageSaver = new ImageSaver(getApplicationContext());
                                                imageSaver.setFileName(contentDTO.contentKey + "_3").setDirectoryName("AQA").save(bitmap);
                                            }
                                        });
                                Glide.with(getApplicationContext()).asBitmap().load(contentDTO.getImageUrl_3())
                                        .apply(new RequestOptions().override(200, 200))
                                        .into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                Bitmap bitmap = imageBlur(getApplicationContext(), resource, 24f);
                                                ImageSaver imageSaver = new ImageSaver(getApplicationContext());
                                                imageSaver.setFileName(contentDTO.contentKey + "_4").setDirectoryName("AQA").save(bitmap);
                                            }
                                        });

                                Glide.with(getApplicationContext()).asBitmap().load(contentDTO.getImageUrl_4())
                                        .apply(new RequestOptions().override(200, 200))
                                        .into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                Bitmap bitmap = imageBlur(getApplicationContext(), resource, 24f);
                                                ImageSaver imageSaver = new ImageSaver(getApplicationContext());
                                                imageSaver.setFileName(contentDTO.contentKey + "_5").setDirectoryName("AQA").save(bitmap);
                                            }
                                        });
                                Glide.with(getApplicationContext()).asBitmap().load(contentDTO.getImageUrl_5())
                                        .apply(new RequestOptions().override(200, 200))
                                        .into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                Bitmap bitmap = imageBlur(getApplicationContext(), resource, 24f);
                                                ImageSaver imageSaver = new ImageSaver(getApplicationContext());
                                                imageSaver.setFileName(contentDTO.contentKey + "_6").setDirectoryName("AQA").save(bitmap);
                                            }
                                        });
                                Glide.with(getApplicationContext()).asBitmap().load(contentDTO.getImageUrl_6())
                                        .apply(new RequestOptions().override(200, 200))
                                        .into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                Bitmap bitmap = imageBlur(getApplicationContext(), resource, 24f);
                                                ImageSaver imageSaver = new ImageSaver(getApplicationContext());
                                                imageSaver.setFileName(contentDTO.contentKey + "_7").setDirectoryName("AQA").save(bitmap);
                                            }
                                        });

                                Glide.with(getApplicationContext()).asBitmap().load(contentDTO.getImageUrl_7())
                                        .apply(new RequestOptions().override(200, 200))
                                        .into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                Bitmap bitmap = imageBlur(getApplicationContext(), resource, 24f);
                                                ImageSaver imageSaver = new ImageSaver(getApplicationContext());
                                                imageSaver.setFileName(contentDTO.contentKey + "_8").setDirectoryName("AQA").save(bitmap);
                                            }
                                        });
                                Glide.with(getApplicationContext()).asBitmap().load(contentDTO.getImageUrl_8())
                                        .apply(new RequestOptions().override(200, 200))
                                        .into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                Bitmap bitmap = imageBlur(getApplicationContext(), resource, 24f);
                                                ImageSaver imageSaver = new ImageSaver(getApplicationContext());
                                                imageSaver.setFileName(contentDTO.contentKey + "_9").setDirectoryName("AQA").save(bitmap);
                                            }
                                        });
                                Glide.with(getApplicationContext()).asBitmap().load(contentDTO.getImageUrl_9())
                                        .apply(new RequestOptions().override(200, 200))
                                        .into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                Bitmap bitmap = imageBlur(getApplicationContext(), resource, 24f);
                                                ImageSaver imageSaver = new ImageSaver(getApplicationContext());
                                                imageSaver.setFileName(contentDTO.contentKey + "_10").setDirectoryName("AQA").save(bitmap);
                                            }
                                        });
                                break;
                        }

                    } catch (Exception e) {

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            return null;
        }
    }


    //딥링크 만들기
    private Uri getDeepLink(String contentKey) {
        return Uri.parse("https://aqa_ranking_deeplink/?" + "contentKey" + "=" + contentKey);
    }


    @Override
    public void ContentDeleteDialogCallback(String string) {
        //게시글 삭제
        if (string.equals("확인")) {
            FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
            DatabaseReference mReference = FirebaseDatabase.getInstance().getReference();
            String pollKey;
            Uri data = getIntent().getData();
            if (data != null) {
                Log.d("lkj Data2 in intent2", data.toString());
                Log.d("lkj Data2 in intent3", data.getLastPathSegment());
                Log.d("lkj Data2 in intent4", data.getQueryParameter("contentKey"));
                pollKey = data.getQueryParameter("contentKey");
            } else {
                pollKey = getIntent().getStringExtra("contentKey");
            }
            finish();
            mReference.child("user_contents").child(pollKey).removeValue();
            mReference.child("users").child(mUser.getUid()).child("uploadContent").child(pollKey).removeValue();
        }
    }

    @Override
    public void PollButtonInfoDialogCallback(String string) {
        if (string.equals("확인")) {

        } else if (string.equals("다시보지않기")) {

        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (adView != null) {
            adView.pause();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adView != null) {
            adView.destroy();
            adView = null;
        }

    }
}


