package com.n4u1.AQA.AQA.views;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
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
import com.google.firebase.auth.FirebaseUser;
import com.n4u1.AQA.AQA.R;
import com.n4u1.AQA.AQA.dialog.AlarmDoneDialog;
import com.n4u1.AQA.AQA.dialog.ContentDeleteDialog;
import com.n4u1.AQA.AQA.dialog.DeleteModificationActivity;
import com.n4u1.AQA.AQA.dialog.PollResultDialog;
import com.n4u1.AQA.AQA.dialog.ShowMoreActivity;
import com.n4u1.AQA.AQA.models.ContentDTO;
import com.n4u1.AQA.AQA.models.ReplyDTO;
import com.n4u1.AQA.AQA.models.User;
import com.n4u1.AQA.AQA.recyclerview.ReplyAdapter;
import com.n4u1.AQA.AQA.recyclerview.ReplyViewHolder;
import com.n4u1.AQA.AQA.util.GlideApp;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
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

public class PollSingleActivity extends AppCompatActivity implements View.OnClickListener, ContentDeleteDialog.ContentDeleteDialogListener {

    private boolean ACTIVITY_REPLY_FLAG;
    private boolean ACTIVITY_BESTREPLY_FLAG;
    private int pickCandidate = 0;
    private FirebaseAuth auth;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference mDatabaseReferencePicker;
    private FirebaseDatabase firebaseDatabase;

    private FirebaseDatabase likeFirebaseDatabase;
    private String replyKey;

    final ArrayList<ReplyDTO> replyDTOS = new ArrayList<>();
    private HashMap<String, String> issueMap = new HashMap<>();

    int contentHit;
    boolean checkUserHitContent = false;


    FloatingActionButton pollActivity_fab_result;
    TextView pollActivity_textView_title, pollActivity_textView_description,
            pollActivity_textView_pollMode, pollActivity_textView_contentType, pollActivity_textView_date;
    ImageView pollActivity_imageView_userAddContent_1, pollActivity_imageView_userAddContent_2,
            pollActivity_imageView_userAddContent_3, pollActivity_imageView_userAddContent_4,
            pollActivity_imageView_userAddContent_5, pollActivity_imageView_userAddContent_6,
            pollActivity_imageView_userAddContent_7, pollActivity_imageView_userAddContent_8,
            pollActivity_imageView_userAddContent_9, pollActivity_imageView_userAddContent_10;

    ImageView pollActivity_imageView_choice_1, pollActivity_imageView_choice_2,
            pollActivity_imageView_choice_3, pollActivity_imageView_choice_4,
            pollActivity_imageView_choice_5, pollActivity_imageView_choice_6,
            pollActivity_imageView_choice_7, pollActivity_imageView_choice_8,
            pollActivity_imageView_choice_9, pollActivity_imageView_choice_10,
            pollActivity_imageView_around_1, pollActivity_imageView_around_2,
            pollActivity_imageView_around_3, pollActivity_imageView_around_4,
            pollActivity_imageView_around_5, pollActivity_imageView_around_6,
            pollActivity_imageView_around_7, pollActivity_imageView_around_8,
            pollActivity_imageView_around_9, pollActivity_imageView_around_10;

    ImageView pollActivity_imageView_userClass, imageView_userClass0, imageView_userClass1, imageView_userClass2;
    ImageView pollActivity_button_replySend;
    EditText pollActivity_editText_reply;
    RecyclerView pollActivity_recyclerView_reply;
    RelativeLayout pollActivity_relativeLayout_reply;

    LinearLayout linearLayout_bestReply0, linearLayout_bestReply1, linearLayout_bestReply2;
    TextView bestReply_id0, bestReply_id1, bestReply_id2, bestReply_reply0, bestReply_reply1, bestReply_reply2,
            bestReply_date0, bestReply_date1, bestReply_date2, bestReply_likeCount0, bestReply_likeCount1, bestReply_likeCount2;
    ImageView bestReply_thumbImg0, bestReply_thumbImg1, bestReply_thumbImg2;

    TextView pollActivity_textView_check_1, pollActivity_textView_check_2,
            pollActivity_textView_check_3, pollActivity_textView_check_4,
            pollActivity_textView_check_5, pollActivity_textView_check_6,
            pollActivity_textView_check_7, pollActivity_textView_check_8,
            pollActivity_textView_check_9, pollActivity_textView_check_10, pollActivity_textView_userId;

    TextView pollActivity_textView_hitCount, pollActivity_textView_likeCount, pollActivity_textView_contentId, pollActivity_textView_replyCount;
    ImageView pollActivity_imageView_state, pollActivity_imageView_like;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poll_single);


        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(null);
        }


        final String contentKey = getIntent().getStringExtra("contentKey");
        contentHit = getIntent().getIntExtra("contentHit", 999999);

        Log.d("lkj noti", contentKey);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("user_contents").child(contentKey);
        mDatabaseReferencePicker = FirebaseDatabase.getInstance().getReference("users");
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        likeFirebaseDatabase = FirebaseDatabase.getInstance();


        pollActivity_imageView_around_1 = findViewById(R.id.pollActivity_imageView_around_1);


//        scrollView = findViewById(R.id.scrollView);
        pollActivity_fab_result = findViewById(R.id.pollActivity_fab_result);
        pollActivity_textView_title = findViewById(R.id.pollActivity_textView_title);
        pollActivity_textView_description = findViewById(R.id.pollActivity_textView_description);
        pollActivity_textView_contentType = findViewById(R.id.pollActivity_textView_contentType);
        pollActivity_textView_pollMode = findViewById(R.id.pollActivity_textView_pollMode);
        pollActivity_textView_date = findViewById(R.id.pollActivity_textView_date);
        pollActivity_textView_contentId = findViewById(R.id.pollActivity_textView_contentId);

        pollActivity_imageView_userClass = findViewById(R.id.pollActivity_imageView_userClass);
        imageView_userClass0 = findViewById(R.id.imageView_userClass0);
        imageView_userClass1 = findViewById(R.id.imageView_userClass1);
        imageView_userClass2 = findViewById(R.id.imageView_userClass2);


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

        pollActivity_textView_userId = findViewById(R.id.pollActivity_textView_userId);
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

        pollActivity_relativeLayout_reply = findViewById(R.id.pollActivity_relativeLayout_reply);
        pollActivity_recyclerView_reply = findViewById(R.id.pollActivity_recyclerView_reply);
        pollActivity_editText_reply = findViewById(R.id.pollActivity_editText_reply);
        pollActivity_button_replySend = findViewById(R.id.pollActivity_button_replySend);

        pollActivity_imageView_choice_1 = findViewById(R.id.pollActivity_imageView_choice_1);
        pollActivity_imageView_choice_2 = findViewById(R.id.pollActivity_imageView_choice_2);
        pollActivity_imageView_choice_3 = findViewById(R.id.pollActivity_imageView_choice_3);
        pollActivity_imageView_choice_4 = findViewById(R.id.pollActivity_imageView_choice_4);
        pollActivity_imageView_choice_5 = findViewById(R.id.pollActivity_imageView_choice_5);
        pollActivity_imageView_choice_6 = findViewById(R.id.pollActivity_imageView_choice_6);
        pollActivity_imageView_choice_7 = findViewById(R.id.pollActivity_imageView_choice_7);
        pollActivity_imageView_choice_8 = findViewById(R.id.pollActivity_imageView_choice_8);
        pollActivity_imageView_choice_9 = findViewById(R.id.pollActivity_imageView_choice_9);
        pollActivity_imageView_choice_10 = findViewById(R.id.pollActivity_imageView_choice_10);

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

        pollActivity_imageView_around_1 = findViewById(R.id.pollActivity_imageView_around_1);
        pollActivity_imageView_around_2 = findViewById(R.id.pollActivity_imageView_around_2);
        pollActivity_imageView_around_3 = findViewById(R.id.pollActivity_imageView_around_3);
        pollActivity_imageView_around_4 = findViewById(R.id.pollActivity_imageView_around_4);
        pollActivity_imageView_around_5 = findViewById(R.id.pollActivity_imageView_around_5);
        pollActivity_imageView_around_6 = findViewById(R.id.pollActivity_imageView_around_6);
        pollActivity_imageView_around_7 = findViewById(R.id.pollActivity_imageView_around_7);
        pollActivity_imageView_around_8 = findViewById(R.id.pollActivity_imageView_around_8);
        pollActivity_imageView_around_9 = findViewById(R.id.pollActivity_imageView_around_9);
        pollActivity_imageView_around_10 = findViewById(R.id.pollActivity_imageView_around_10);


        pollActivity_textView_hitCount = findViewById(R.id.pollActivity_textView_hitCount);
        pollActivity_textView_likeCount = findViewById(R.id.pollActivity_textView_likeCount);
        pollActivity_imageView_state = findViewById(R.id.pollActivity_imageView_state);
        pollActivity_imageView_like = findViewById(R.id.pollActivity_imageView_like);


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


        //제목옆에 더보기 클릭
//        pollActivity_imageView_showMore.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d("lkjshormore", "showmore");
//                Intent intentShowMore = new Intent(PollSingleActivity.this, ShowMoreActivity.class);
//                intentShowMore.putExtra("pollKey", contentKey);
//                intentShowMore.putExtra("hitCount", contentHit);
//                startActivityForResult(intentShowMore, 20000);
//            }
//        });
//


        //reply item click listener 댓글 클릭 리스너
        final ReplyAdapter replyAdapter = new ReplyAdapter(getApplicationContext(), replyDTOS, new ReplyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final View view, final int position) {

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
//                                int temp = replyDTOS.size() - position - 1;
                                        String replyKey = replyDTOTemp.get(position).getReplyKey();

                                        //수정하기, 선택하기 액티비티(다이얼로그)띄우기
                                        Intent intent = new Intent(PollSingleActivity.this, DeleteModificationActivity.class);
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
            }
        });

        //이미투표했는지 여부 확인해서 floating action button 색 넣기
        fabCheck(firebaseDatabase.getReference().child("user_contents").child(contentKey));


        //투표하고 결과보기
        pollActivity_imageView_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabaseReferencePicker.child(auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Map<String, Object> user = (Map<String, Object>) dataSnapshot.getValue();
                        Object object = user.get("age");
                        int currentAge = Integer.parseInt(object.toString());
                        String currentGender = user.get("sex").toString();
                        onResultClicked(firebaseDatabase.getReference().child("user_contents").child(contentKey), currentAge, currentGender);
//                        issueContents 테스트 디비 입력용
//                        long issueDate = getCurrentDate();
//                        issueMap.put(String.valueOf(issueDate), contentKey);
//                        firebaseDatabase.getReference().child("issueContents").child(String.valueOf(issueDate)).setValue(issueMap);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        //투표하고 결과보기
        pollActivity_fab_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDatabaseReferencePicker.child(auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Map<String, Object> user = (Map<String, Object>) dataSnapshot.getValue();
                        Object object = user.get("age");
                        int currentAge = Integer.parseInt(object.toString());
                        String currentGender = user.get("sex").toString();

                        onResultClicked(firebaseDatabase.getReference().child("user_contents").child(contentKey), currentAge, currentGender);

//                        issueContents 테스트 디비 입력용
                        long issueDate = getCurrentDate();
                        issueMap.put(String.valueOf(issueDate), contentKey);
                        firebaseDatabase.getReference().child("issueContents").child(String.valueOf(issueDate)).setValue(issueMap);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });


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
                        int replyCount = contentDTO.getReplyCount();

                        openReply(replyCount);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                //댓글의 좋아요 갯수 정렬
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
                likeClick();
            }
        });
        //따봉버튼 클릭리스너,  숫자 클릭
        pollActivity_textView_likeCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeClick();
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
//        final PostAdapter postAdapter = new PostAdapter(getApplication(), contentDTOS); //20180730 전날꺼 보기 getApplication()전에 this,contentDTOS 였음
        pollActivity_recyclerView_reply.setAdapter(replyAdapter);


        //reply button click, 댓글달기버튼
        pollActivity_button_replySend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                            replyDTO.setReply(pollActivity_editText_reply.getText().toString());
                            replyDTO.setuId(auth.getCurrentUser().getUid());
                            replyDTO.setContentKey(contentKey);
                            replyDTO.setqPoint(Integer.parseInt(users.get("userClass").toString()));
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
        });


        //이미지 크게보기
//        isImageFitToScreen = getIntent().getStringExtra("fullScreenIndicator");
//        if ("y".equals(isImageFitToScreen)) {
//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
//            getSupportActionBar().hide();
//
//            Uri uri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/test130-1068f.appspot.com/o/images%2F3qq998d5x3ire7qynj72.jpg?alt=media&token=feddba03-1e1d-4131-a713-b0f4042a7653");
//
//            pollActivity_imageView_userAddContent_1.setImageURI(uri);
//            pollActivity_imageView_userAddContent_1.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
//            pollActivity_imageView_userAddContent_1.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
//            pollActivity_imageView_userAddContent_1.setAdjustViewBounds(false);
//            pollActivity_imageView_userAddContent_1.setScaleType(ImageView.ScaleType.FIT_XY);
//        }

        //show more ; show or noShow


        Log.d("lkj noti", contentKey);
        //contentDTO 화면 초기세팅
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ContentDTO contentDTO = dataSnapshot.getValue(ContentDTO.class);
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
                if (contentDTO.likes.containsKey(auth.getCurrentUser().getUid())) {
                    pollActivity_imageView_like.setImageResource(R.drawable.ic_thumb_up_blue);
                } else {
                    pollActivity_imageView_like.setImageResource(R.drawable.ic_outline_thumb_up_24px);
                }


                switch (contentDTO.getItemViewType()) {
                    case 2:
                        pollActivity_textView_check_1.setVisibility(View.VISIBLE);
                        pollActivity_textView_check_2.setVisibility(View.VISIBLE);
                        pollActivity_imageView_around_1.setVisibility(View.VISIBLE);
                        pollActivity_imageView_around_2.setVisibility(View.VISIBLE);
                        pollActivity_imageView_choice_1.setVisibility(View.VISIBLE);
                        pollActivity_imageView_choice_2.setVisibility(View.VISIBLE);
                        pollActivity_imageView_userAddContent_1.setVisibility(View.VISIBLE);
                        pollActivity_imageView_userAddContent_2.setVisibility(View.VISIBLE);
                        GlideApp.with(getApplicationContext()).load(contentDTO.getImageUrl_0()).centerCrop().thumbnail(Glide.with(getApplicationContext()).load(R.drawable.loadingicon)).into(pollActivity_imageView_userAddContent_1).getView();
                        GlideApp.with(getApplicationContext()).load(contentDTO.getImageUrl_1()).centerCrop().thumbnail(Glide.with(getApplicationContext()).load(R.drawable.loadingicon)).into(pollActivity_imageView_userAddContent_2).getView();
                        break;
                    case 3:
                        pollActivity_textView_check_1.setVisibility(View.VISIBLE);
                        pollActivity_textView_check_2.setVisibility(View.VISIBLE);
                        pollActivity_textView_check_3.setVisibility(View.VISIBLE);
                        pollActivity_imageView_around_1.setVisibility(View.VISIBLE);
                        pollActivity_imageView_around_2.setVisibility(View.VISIBLE);
                        pollActivity_imageView_around_3.setVisibility(View.VISIBLE);
                        pollActivity_imageView_choice_1.setVisibility(View.VISIBLE);
                        pollActivity_imageView_choice_2.setVisibility(View.VISIBLE);
                        pollActivity_imageView_choice_3.setVisibility(View.VISIBLE);
                        pollActivity_imageView_userAddContent_1.setVisibility(View.VISIBLE);
                        pollActivity_imageView_userAddContent_2.setVisibility(View.VISIBLE);
                        pollActivity_imageView_userAddContent_3.setVisibility(View.VISIBLE);
                        GlideApp.with(getApplicationContext()).load(contentDTO.getImageUrl_0()).centerCrop().thumbnail(Glide.with(getApplicationContext()).load(R.drawable.loadingicon)).into(pollActivity_imageView_userAddContent_1).getView();
                        GlideApp.with(getApplicationContext()).load(contentDTO.getImageUrl_1()).centerCrop().thumbnail(Glide.with(getApplicationContext()).load(R.drawable.loadingicon)).into(pollActivity_imageView_userAddContent_2).getView();
                        GlideApp.with(getApplicationContext()).load(contentDTO.getImageUrl_2()).centerCrop().thumbnail(Glide.with(getApplicationContext()).load(R.drawable.loadingicon)).into(pollActivity_imageView_userAddContent_3).getView();
                        break;
                    case 4:
                        pollActivity_textView_check_1.setVisibility(View.VISIBLE);
                        pollActivity_textView_check_2.setVisibility(View.VISIBLE);
                        pollActivity_textView_check_3.setVisibility(View.VISIBLE);
                        pollActivity_textView_check_4.setVisibility(View.VISIBLE);
                        pollActivity_imageView_around_1.setVisibility(View.VISIBLE);
                        pollActivity_imageView_around_2.setVisibility(View.VISIBLE);
                        pollActivity_imageView_around_3.setVisibility(View.VISIBLE);
                        pollActivity_imageView_around_4.setVisibility(View.VISIBLE);
                        pollActivity_imageView_choice_1.setVisibility(View.VISIBLE);
                        pollActivity_imageView_choice_2.setVisibility(View.VISIBLE);
                        pollActivity_imageView_choice_3.setVisibility(View.VISIBLE);
                        pollActivity_imageView_choice_4.setVisibility(View.VISIBLE);
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
                        pollActivity_textView_check_1.setVisibility(View.VISIBLE);
                        pollActivity_textView_check_2.setVisibility(View.VISIBLE);
                        pollActivity_textView_check_3.setVisibility(View.VISIBLE);
                        pollActivity_textView_check_4.setVisibility(View.VISIBLE);
                        pollActivity_textView_check_5.setVisibility(View.VISIBLE);
                        pollActivity_textView_check_6.setVisibility(View.VISIBLE);
                        pollActivity_imageView_around_1.setVisibility(View.VISIBLE);
                        pollActivity_imageView_around_2.setVisibility(View.VISIBLE);
                        pollActivity_imageView_around_3.setVisibility(View.VISIBLE);
                        pollActivity_imageView_around_4.setVisibility(View.VISIBLE);
                        pollActivity_imageView_around_5.setVisibility(View.VISIBLE);
                        pollActivity_imageView_choice_1.setVisibility(View.VISIBLE);
                        pollActivity_imageView_choice_2.setVisibility(View.VISIBLE);
                        pollActivity_imageView_choice_3.setVisibility(View.VISIBLE);
                        pollActivity_imageView_choice_4.setVisibility(View.VISIBLE);
                        pollActivity_imageView_choice_5.setVisibility(View.VISIBLE);
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
                        pollActivity_textView_check_1.setVisibility(View.VISIBLE);
                        pollActivity_textView_check_2.setVisibility(View.VISIBLE);
                        pollActivity_textView_check_3.setVisibility(View.VISIBLE);
                        pollActivity_textView_check_4.setVisibility(View.VISIBLE);
                        pollActivity_textView_check_5.setVisibility(View.VISIBLE);
                        pollActivity_textView_check_6.setVisibility(View.VISIBLE);
                        pollActivity_imageView_around_1.setVisibility(View.VISIBLE);
                        pollActivity_imageView_around_2.setVisibility(View.VISIBLE);
                        pollActivity_imageView_around_3.setVisibility(View.VISIBLE);
                        pollActivity_imageView_around_4.setVisibility(View.VISIBLE);
                        pollActivity_imageView_around_5.setVisibility(View.VISIBLE);
                        pollActivity_imageView_around_6.setVisibility(View.VISIBLE);
                        pollActivity_imageView_choice_1.setVisibility(View.VISIBLE);
                        pollActivity_imageView_choice_2.setVisibility(View.VISIBLE);
                        pollActivity_imageView_choice_3.setVisibility(View.VISIBLE);
                        pollActivity_imageView_choice_4.setVisibility(View.VISIBLE);
                        pollActivity_imageView_choice_5.setVisibility(View.VISIBLE);
                        pollActivity_imageView_choice_6.setVisibility(View.VISIBLE);
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
                        pollActivity_textView_check_1.setVisibility(View.VISIBLE);
                        pollActivity_textView_check_2.setVisibility(View.VISIBLE);
                        pollActivity_textView_check_3.setVisibility(View.VISIBLE);
                        pollActivity_textView_check_4.setVisibility(View.VISIBLE);
                        pollActivity_textView_check_5.setVisibility(View.VISIBLE);
                        pollActivity_textView_check_6.setVisibility(View.VISIBLE);
                        pollActivity_textView_check_7.setVisibility(View.VISIBLE);
                        pollActivity_imageView_around_1.setVisibility(View.VISIBLE);
                        pollActivity_imageView_around_2.setVisibility(View.VISIBLE);
                        pollActivity_imageView_around_3.setVisibility(View.VISIBLE);
                        pollActivity_imageView_around_4.setVisibility(View.VISIBLE);
                        pollActivity_imageView_around_5.setVisibility(View.VISIBLE);
                        pollActivity_imageView_around_6.setVisibility(View.VISIBLE);
                        pollActivity_imageView_around_7.setVisibility(View.VISIBLE);
                        pollActivity_imageView_choice_1.setVisibility(View.VISIBLE);
                        pollActivity_imageView_choice_2.setVisibility(View.VISIBLE);
                        pollActivity_imageView_choice_3.setVisibility(View.VISIBLE);
                        pollActivity_imageView_choice_4.setVisibility(View.VISIBLE);
                        pollActivity_imageView_choice_5.setVisibility(View.VISIBLE);
                        pollActivity_imageView_choice_6.setVisibility(View.VISIBLE);
                        pollActivity_imageView_choice_7.setVisibility(View.VISIBLE);
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
                        pollActivity_textView_check_1.setVisibility(View.VISIBLE);
                        pollActivity_textView_check_2.setVisibility(View.VISIBLE);
                        pollActivity_textView_check_3.setVisibility(View.VISIBLE);
                        pollActivity_textView_check_4.setVisibility(View.VISIBLE);
                        pollActivity_textView_check_5.setVisibility(View.VISIBLE);
                        pollActivity_textView_check_6.setVisibility(View.VISIBLE);
                        pollActivity_textView_check_7.setVisibility(View.VISIBLE);
                        pollActivity_textView_check_8.setVisibility(View.VISIBLE);
                        pollActivity_imageView_around_1.setVisibility(View.VISIBLE);
                        pollActivity_imageView_around_2.setVisibility(View.VISIBLE);
                        pollActivity_imageView_around_3.setVisibility(View.VISIBLE);
                        pollActivity_imageView_around_4.setVisibility(View.VISIBLE);
                        pollActivity_imageView_around_5.setVisibility(View.VISIBLE);
                        pollActivity_imageView_around_6.setVisibility(View.VISIBLE);
                        pollActivity_imageView_around_7.setVisibility(View.VISIBLE);
                        pollActivity_imageView_around_8.setVisibility(View.VISIBLE);
                        pollActivity_imageView_choice_1.setVisibility(View.VISIBLE);
                        pollActivity_imageView_choice_2.setVisibility(View.VISIBLE);
                        pollActivity_imageView_choice_3.setVisibility(View.VISIBLE);
                        pollActivity_imageView_choice_4.setVisibility(View.VISIBLE);
                        pollActivity_imageView_choice_5.setVisibility(View.VISIBLE);
                        pollActivity_imageView_choice_6.setVisibility(View.VISIBLE);
                        pollActivity_imageView_choice_7.setVisibility(View.VISIBLE);
                        pollActivity_imageView_choice_8.setVisibility(View.VISIBLE);
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
                        pollActivity_textView_check_1.setVisibility(View.VISIBLE);
                        pollActivity_textView_check_2.setVisibility(View.VISIBLE);
                        pollActivity_textView_check_3.setVisibility(View.VISIBLE);
                        pollActivity_textView_check_4.setVisibility(View.VISIBLE);
                        pollActivity_textView_check_5.setVisibility(View.VISIBLE);
                        pollActivity_textView_check_6.setVisibility(View.VISIBLE);
                        pollActivity_textView_check_7.setVisibility(View.VISIBLE);
                        pollActivity_textView_check_8.setVisibility(View.VISIBLE);
                        pollActivity_textView_check_9.setVisibility(View.VISIBLE);
                        pollActivity_imageView_around_1.setVisibility(View.VISIBLE);
                        pollActivity_imageView_around_2.setVisibility(View.VISIBLE);
                        pollActivity_imageView_around_3.setVisibility(View.VISIBLE);
                        pollActivity_imageView_around_4.setVisibility(View.VISIBLE);
                        pollActivity_imageView_around_5.setVisibility(View.VISIBLE);
                        pollActivity_imageView_around_6.setVisibility(View.VISIBLE);
                        pollActivity_imageView_around_7.setVisibility(View.VISIBLE);
                        pollActivity_imageView_around_8.setVisibility(View.VISIBLE);
                        pollActivity_imageView_around_9.setVisibility(View.VISIBLE);
                        pollActivity_imageView_choice_1.setVisibility(View.VISIBLE);
                        pollActivity_imageView_choice_2.setVisibility(View.VISIBLE);
                        pollActivity_imageView_choice_3.setVisibility(View.VISIBLE);
                        pollActivity_imageView_choice_4.setVisibility(View.VISIBLE);
                        pollActivity_imageView_choice_5.setVisibility(View.VISIBLE);
                        pollActivity_imageView_choice_6.setVisibility(View.VISIBLE);
                        pollActivity_imageView_choice_7.setVisibility(View.VISIBLE);
                        pollActivity_imageView_choice_8.setVisibility(View.VISIBLE);
                        pollActivity_imageView_choice_9.setVisibility(View.VISIBLE);
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
                        pollActivity_textView_check_1.setVisibility(View.VISIBLE);
                        pollActivity_textView_check_2.setVisibility(View.VISIBLE);
                        pollActivity_textView_check_3.setVisibility(View.VISIBLE);
                        pollActivity_textView_check_4.setVisibility(View.VISIBLE);
                        pollActivity_textView_check_5.setVisibility(View.VISIBLE);
                        pollActivity_textView_check_6.setVisibility(View.VISIBLE);
                        pollActivity_textView_check_7.setVisibility(View.VISIBLE);
                        pollActivity_textView_check_8.setVisibility(View.VISIBLE);
                        pollActivity_textView_check_9.setVisibility(View.VISIBLE);
                        pollActivity_textView_check_10.setVisibility(View.VISIBLE);
                        pollActivity_imageView_around_1.setVisibility(View.VISIBLE);
                        pollActivity_imageView_around_2.setVisibility(View.VISIBLE);
                        pollActivity_imageView_around_3.setVisibility(View.VISIBLE);
                        pollActivity_imageView_around_4.setVisibility(View.VISIBLE);
                        pollActivity_imageView_around_5.setVisibility(View.VISIBLE);
                        pollActivity_imageView_around_6.setVisibility(View.VISIBLE);
                        pollActivity_imageView_around_7.setVisibility(View.VISIBLE);
                        pollActivity_imageView_around_8.setVisibility(View.VISIBLE);
                        pollActivity_imageView_around_9.setVisibility(View.VISIBLE);
                        pollActivity_imageView_around_10.setVisibility(View.VISIBLE);
                        pollActivity_imageView_choice_1.setVisibility(View.VISIBLE);
                        pollActivity_imageView_choice_2.setVisibility(View.VISIBLE);
                        pollActivity_imageView_choice_3.setVisibility(View.VISIBLE);
                        pollActivity_imageView_choice_4.setVisibility(View.VISIBLE);
                        pollActivity_imageView_choice_5.setVisibility(View.VISIBLE);
                        pollActivity_imageView_choice_6.setVisibility(View.VISIBLE);
                        pollActivity_imageView_choice_7.setVisibility(View.VISIBLE);
                        pollActivity_imageView_choice_8.setVisibility(View.VISIBLE);
                        pollActivity_imageView_choice_9.setVisibility(View.VISIBLE);
                        pollActivity_imageView_choice_10.setVisibility(View.VISIBLE);
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void settingUserIcon(String userId) {
        firebaseDatabase.getReference().child("users").child(userId).child("userClass").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int userClass = Integer.parseInt(dataSnapshot.getValue().toString());
                if (userClass >= 0 && userClass < 50) {
                    pollActivity_imageView_userClass.setImageResource(R.drawable.q_class_red_1);
                } else if (userClass >= 50 && userClass < 100) {
                    pollActivity_imageView_userClass.setImageResource(R.drawable.q_class_red_2);
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
                    pollActivity_imageView_userClass.setImageResource(R.drawable.q_class_blue_1);
                } else if (userClass >= 450 && userClass < 501) {
                    pollActivity_imageView_userClass.setImageResource(R.drawable.q_class_blue_2);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    /**
     * onCreate()
     */

    private void likeClick() {
        final String contentKey = getIntent().getStringExtra("contentKey");
        firebaseDatabase.getReference().child("user_contents").child(contentKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ContentDTO contentDTO = dataSnapshot.getValue(ContentDTO.class);
                Log.d("lkj contentdto", contentDTO.description);
                if (contentDTO.uid.equals(auth.getCurrentUser().getUid())) {
                    Toast toast = Toast.makeText(getApplicationContext(), contentDTO.userID + "님의 투표입니다!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();
                } else {
                    onLikeClicked(firebaseDatabase.getReference().child("user_contents").child(contentKey));
                }

                likeFirebaseDatabase.getReference().child("user_contents").child(contentKey).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ContentDTO contentDTO_ = dataSnapshot.getValue(ContentDTO.class);
                        if (contentDTO_.likes.containsKey(auth.getCurrentUser().getUid())) {
                            pollActivity_imageView_like.setImageResource(R.drawable.ic_thumb_up_blue);
                            pollActivity_textView_likeCount.setText(String.valueOf(contentDTO_.likeCount));
                        } else {
                            pollActivity_imageView_like.setImageResource(R.drawable.ic_outline_thumb_up_24px);
                            pollActivity_textView_likeCount.setText(String.valueOf(contentDTO_.likeCount));
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

    //베스트댓글 보여주기
    private void openBestReply(ArrayList<ReplyDTO> replyDTOS) {
        if (!ACTIVITY_BESTREPLY_FLAG) {
            //베스트3를 보여주는데 좋아요가 눌린 댓글수가 3개 미만일수도있으니

            try {
                if (replyDTOS.get(0) != null && replyDTOS.get(0).likeCount > 0) {
                    int userClass = replyDTOS.get(0).getqPoint();
                    if (userClass >= 0 && userClass < 50) {
                        imageView_userClass0.setImageResource(R.drawable.q_class_red_1);
                    } else if (userClass >= 50 && userClass < 100) {
                        imageView_userClass0.setImageResource(R.drawable.q_class_red_2);
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
                        imageView_userClass0.setImageResource(R.drawable.q_class_blue_1);
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
                        imageView_userClass0.setImageResource(R.drawable.q_class_red_1);
                    } else if (userClass >= 50 && userClass < 100) {
                        imageView_userClass0.setImageResource(R.drawable.q_class_red_2);
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
                        imageView_userClass0.setImageResource(R.drawable.q_class_blue_1);
                    } else if (userClass >= 450 && userClass < 501) {
                        imageView_userClass0.setImageResource(R.drawable.q_class_blue_2);
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
                        imageView_userClass0.setImageResource(R.drawable.q_class_red_1);
                    } else if (userClass >= 50 && userClass < 100) {
                        imageView_userClass0.setImageResource(R.drawable.q_class_red_2);
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
                        imageView_userClass0.setImageResource(R.drawable.q_class_blue_1);
                    } else if (userClass >= 450 && userClass < 501) {
                        imageView_userClass0.setImageResource(R.drawable.q_class_blue_2);
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
            pollActivity_relativeLayout_reply.setFocusableInTouchMode(true);
            pollActivity_relativeLayout_reply.requestFocus();
            pollActivity_recyclerView_reply.setNestedScrollingEnabled(false);
            pollActivity_recyclerView_reply.setVisibility(View.VISIBLE);
            pollActivity_editText_reply.setVisibility(View.VISIBLE);
            pollActivity_button_replySend.setVisibility(View.VISIBLE);
            pollActivity_fab_result.hide();
            ACTIVITY_REPLY_FLAG = true;
        } else {
            pollActivity_editText_reply.setText(null);//editText 초기화
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); //키보드 숨기기
            inputMethodManager.hideSoftInputFromWindow(pollActivity_editText_reply.getWindowToken(), 0); //키보드 숨기기
            pollActivity_recyclerView_reply.setVisibility(View.GONE);
            pollActivity_editText_reply.setVisibility(View.GONE);
            pollActivity_button_replySend.setVisibility(View.GONE);
            pollActivity_fab_result.show();
            ACTIVITY_REPLY_FLAG = false;
        }

    }


    //현재 Activity 에서의 투표 체크 유무
    private boolean pollChecking() {
        if (((ColorDrawable) pollActivity_imageView_choice_1.getBackground()).getColor() == 0xff4485c9
                || ((ColorDrawable) pollActivity_imageView_choice_2.getBackground()).getColor() == 0xff4485c9
                || ((ColorDrawable) pollActivity_imageView_choice_3.getBackground()).getColor() == 0xff4485c9
                || ((ColorDrawable) pollActivity_imageView_choice_4.getBackground()).getColor() == 0xff4485c9
                || ((ColorDrawable) pollActivity_imageView_choice_5.getBackground()).getColor() == 0xff4485c9
                || ((ColorDrawable) pollActivity_imageView_choice_6.getBackground()).getColor() == 0xff4485c9
                || ((ColorDrawable) pollActivity_imageView_choice_7.getBackground()).getColor() == 0xff4485c9
                || ((ColorDrawable) pollActivity_imageView_choice_8.getBackground()).getColor() == 0xff4485c9
                || ((ColorDrawable) pollActivity_imageView_choice_9.getBackground()).getColor() == 0xff4485c9
                || ((ColorDrawable) pollActivity_imageView_choice_10.getBackground()).getColor() == 0xff4485c9) {
            return true;
        } else {
            return false;
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
        final int contentAmount = getIntent().getIntExtra("itemViewType", 0);
        Log.d("lkj contentAmount", String.valueOf(contentAmount));

        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                final ContentDTO contentDTO = mutableData.getValue(ContentDTO.class);
                if (contentDTO == null) {
                    return Transaction.success(mutableData);
                }
                if (contentDTO.contentPicker.containsKey(auth.getCurrentUser().getUid())) {
                    //투표가 되어있으면 PollResultDialog
                    PollResultDialog pollResultDialog = new PollResultDialog();
                    Bundle bundle = new Bundle();

                    bundle.putInt("imagePick", currentPick());
                    bundle.putInt("imageN", getIntent().getIntExtra("itemViewType", 100));
                    bundle.putInt("contentHits", contentHit);
                    bundle.putString("currentContent", getIntent().getStringExtra("contentKey"));
                    bundle.putString("statisticsCode", contentDTO.statistics_code);

                    pollResultDialog.setArguments(bundle);
                    pollResultDialog.show(getSupportFragmentManager(), "pollResultDialog");

                    //투표가 안되어있으면 투표하고 PollResultDialog
                } else {
                    //투표 선택 되있으면 true, 안되있으면 false
                    if (pollChecking()) {
                        //true면 투표수 추가하고 PollResultDialog
                        contentDTO.contentHit = contentDTO.contentHit + 1;
                        if (currentPick() == 0)
                            contentDTO.candidateScore_0 = contentDTO.candidateScore_0 + 1;
                        if (currentPick() == 1)
                            contentDTO.candidateScore_1 = contentDTO.candidateScore_1 + 1;
                        if (currentPick() == 2)
                            contentDTO.candidateScore_2 = contentDTO.candidateScore_2 + 1;
                        if (currentPick() == 3)
                            contentDTO.candidateScore_3 = contentDTO.candidateScore_3 + 1;
                        if (currentPick() == 4)
                            contentDTO.candidateScore_4 = contentDTO.candidateScore_4 + 1;
                        if (currentPick() == 5)
                            contentDTO.candidateScore_5 = contentDTO.candidateScore_5 + 1;
                        if (currentPick() == 6)
                            contentDTO.candidateScore_6 = contentDTO.candidateScore_6 + 1;
                        if (currentPick() == 7)
                            contentDTO.candidateScore_7 = contentDTO.candidateScore_7 + 1;
                        if (currentPick() == 8)
                            contentDTO.candidateScore_8 = contentDTO.candidateScore_8 + 1;
                        if (currentPick() == 9)
                            contentDTO.candidateScore_9 = contentDTO.candidateScore_9 + 1;

                        contentDTO.statistics_code = addStatistics(contentDTO.statistics_code, currentPick(), currentGender, currentAge);

//                        tmp.add(Integer.parseInt(object0.toString()));
//                        mDatabaseReferencePicker.child(auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                Map<String, Object> user = (Map<String, Object>) dataSnapshot.getValue();
//                                Object object = user.get("age");
//                                int currentAge = Integer.parseInt(object.toString());
//                                String currentGender = user.get("sex").toString();
//                                String statisticsCodeTmp = addStatistics(contentDTO.statistics_code, currentPick(), currentGender, currentAge);
////                                contentDTO.statistics_code = addStatistics(contentDTO.statistics_code, currentPick(), currentGender, currentAge);
//                                mDatabaseReference.child("statistics_code").setValue(statisticsCodeTmp);
//
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                            }
//                        });

                        //몇변에 투표했는지 users/pickContent:N
                        contentDTO.contentPicker.put(auth.getCurrentUser().getUid(), currentPick());
                        String key = getIntent().getStringExtra("contentKey");
                        firebaseDatabase.getReference()
                                .child("users")
                                .child(auth.getCurrentUser().getUid())
                                .child("pickContent")
                                .child(key)
                                .setValue(currentPick());


                        //투표했을때 DB/issueContents 에 시간 : contentKey 입력
                        long issueDate = getCurrentDate();
                        issueMap.put(String.valueOf(issueDate), key);
                        firebaseDatabase.getReference().child("issueContents").child(String.valueOf(issueDate)).setValue(issueMap);


                        //투표 완료후 결과 차트 열기
                        PollResultDialog pollResultDialog = new PollResultDialog();
                        Bundle bundle = new Bundle();
                        bundle.putInt("imagePick", currentPick());
                        bundle.putInt("imageN", getIntent().getIntExtra("itemViewType", 100));
                        bundle.putString("currentContent", getIntent().getStringExtra("contentKey"));
                        bundle.putString("statisticsCode", contentDTO.statistics_code);
                        pollResultDialog.setArguments(bundle);
                        pollResultDialog.show(getSupportFragmentManager(), "pollResultDialog");

                    } else { //투표 선택 안되있으면
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "투표하면 알랴쥼 ^ㅠ^", Toast.LENGTH_SHORT).show();
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


    private void fabCheck(DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                ContentDTO contentDTO = mutableData.getValue(ContentDTO.class);
                if (contentDTO == null) {
                    return Transaction.success(mutableData);
                }
                if (contentDTO.contentPicker.containsKey(auth.getCurrentUser().getUid())) {
                    pollActivity_fab_result.setImageResource(R.drawable.q); //fab 파란색
                    pollActivity_imageView_state.setImageResource(R.drawable.q);
                    checkUserHitContent = true;//투표여부
                } else {
                    pollActivity_fab_result.setImageResource(R.drawable.q_bg_w); //fab 흰색
                    pollActivity_imageView_state.setImageResource(R.drawable.q_bg_w);
                    checkUserHitContent = false;//투표여부
                }
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
            }
        });
    }


    private int currentPick() {
        if (((ColorDrawable) pollActivity_imageView_choice_1.getBackground()).getColor() == 0xff4485c9) {
            return 0;
        } else if (((ColorDrawable) pollActivity_imageView_choice_2.getBackground()).getColor() == 0xff4485c9) {
            return 1;
        } else if (((ColorDrawable) pollActivity_imageView_choice_3.getBackground()).getColor() == 0xff4485c9) {
            return 2;
        } else if (((ColorDrawable) pollActivity_imageView_choice_4.getBackground()).getColor() == 0xff4485c9) {
            return 3;
        } else if (((ColorDrawable) pollActivity_imageView_choice_5.getBackground()).getColor() == 0xff4485c9) {
            return 4;
        } else if (((ColorDrawable) pollActivity_imageView_choice_6.getBackground()).getColor() == 0xff4485c9) {
            return 5;
        } else if (((ColorDrawable) pollActivity_imageView_choice_7.getBackground()).getColor() == 0xff4485c9) {
            return 6;
        } else if (((ColorDrawable) pollActivity_imageView_choice_8.getBackground()).getColor() == 0xff4485c9) {
            return 7;
        } else if (((ColorDrawable) pollActivity_imageView_choice_9.getBackground()).getColor() == 0xff4485c9) {
            return 8;
        } else if (((ColorDrawable) pollActivity_imageView_choice_10.getBackground()).getColor() == 0xff4485c9) {
            return 9;
        } else return 100;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 10000:
                    if (data.getStringArrayListExtra("resultDelete").get(1).equals("삭제하기")) {
                        String contentKey = getIntent().getStringExtra("contentKey");
                        String replyKey = data.getStringArrayListExtra("resultDelete").get(0);

                        firebaseDatabase.getReference().child("reply").child(contentKey).child(replyKey).removeValue();

                        Toast.makeText(getApplicationContext(), "삭제하기", Toast.LENGTH_SHORT).show();
                    } else if (data.getStringArrayListExtra("resultDelete").get(1).equals("수정하기")) {
                        Toast.makeText(getApplicationContext(), "수정하기", Toast.LENGTH_SHORT).show();
                    } else break;
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


    public void checking_img_1() {
        pickCandidate = 1;
        pollActivity_imageView_around_1.setBackgroundColor(0xff4485c9);
        pollActivity_imageView_choice_1.setBackgroundColor(0xff4485c9);
    }


    public void checking_img_1_rt() {
        pickCandidate = 0;
        pollActivity_imageView_around_1.setBackgroundColor(0xfff2f2f2);
        pollActivity_imageView_choice_1.setBackgroundColor(0xfff2f2f2);
    }


    public void checking_img_2() {
        pickCandidate = 2;
        pollActivity_imageView_around_2.setBackgroundColor(0xff4485c9);
        pollActivity_imageView_choice_2.setBackgroundColor(0xff4485c9);
    }


    public void checking_img_2_rt() {
        pickCandidate = 0;
        pollActivity_imageView_around_2.setBackgroundColor(0xfff2f2f2);
        pollActivity_imageView_choice_2.setBackgroundColor(0xfff2f2f2);
    }


    public void checking_img_3() {
        pickCandidate = 3;
        pollActivity_imageView_around_3.setBackgroundColor(0xff4485c9);
        pollActivity_imageView_choice_3.setBackgroundColor(0xff4485c9);
    }


    public void checking_img_3_rt() {
        pickCandidate = 0;
        pollActivity_imageView_around_3.setBackgroundColor(0xfff2f2f2);
        pollActivity_imageView_choice_3.setBackgroundColor(0xfff2f2f2);
    }


    public void checking_img_4() {
        pickCandidate = 4;
        pollActivity_imageView_around_4.setBackgroundColor(0xff4485c9);
        pollActivity_imageView_choice_4.setBackgroundColor(0xff4485c9);
    }


    public void checking_img_4_rt() {
        pickCandidate = 0;
        pollActivity_imageView_around_4.setBackgroundColor(0xfff2f2f2);
        pollActivity_imageView_choice_4.setBackgroundColor(0xfff2f2f2);
    }


    public void checking_img_5() {
        pickCandidate = 5;
        pollActivity_imageView_around_5.setBackgroundColor(0xff4485c9);
        pollActivity_imageView_choice_5.setBackgroundColor(0xff4485c9);
    }


    public void checking_img_5_rt() {
        pickCandidate = 0;
        pollActivity_imageView_around_5.setBackgroundColor(0xfff2f2f2);
        pollActivity_imageView_choice_5.setBackgroundColor(0xfff2f2f2);
    }


    public void checking_img_6() {
        pickCandidate = 6;
        pollActivity_imageView_around_6.setBackgroundColor(0xff4485c9);
        pollActivity_imageView_choice_6.setBackgroundColor(0xff4485c9);
    }


    public void checking_img_6_rt() {
        pickCandidate = 0;
        pollActivity_imageView_around_6.setBackgroundColor(0xfff2f2f2);
        pollActivity_imageView_choice_6.setBackgroundColor(0xfff2f2f2);
    }


    public void checking_img_7() {
        pickCandidate = 7;
        pollActivity_imageView_around_7.setBackgroundColor(0xff4485c9);
        pollActivity_imageView_choice_7.setBackgroundColor(0xff4485c9);
    }


    public void checking_img_7_rt() {
        pickCandidate = 0;
        pollActivity_imageView_around_7.setBackgroundColor(0xfff2f2f2);
        pollActivity_imageView_choice_7.setBackgroundColor(0xfff2f2f2);
    }


    public void checking_img_8() {
        pickCandidate = 8;
        pollActivity_imageView_around_8.setBackgroundColor(0xff4485c9);
        pollActivity_imageView_choice_8.setBackgroundColor(0xff4485c9);
    }


    public void checking_img_8_rt() {
        pickCandidate = 0;
        pollActivity_imageView_around_8.setBackgroundColor(0xfff2f2f2);
        pollActivity_imageView_choice_8.setBackgroundColor(0xfff2f2f2);
    }


    public void checking_img_9() {
        pickCandidate = 9;
        pollActivity_imageView_around_9.setBackgroundColor(0xff4485c9);
        pollActivity_imageView_choice_9.setBackgroundColor(0xff4485c9);
    }


    public void checking_img_9_rt() {
        pickCandidate = 0;
        pollActivity_imageView_around_9.setBackgroundColor(0xfff2f2f2);
        pollActivity_imageView_choice_9.setBackgroundColor(0xfff2f2f2);
    }


    public void checking_img_10() {
        pickCandidate = 10;
        pollActivity_imageView_around_10.setBackgroundColor(0xff4485c9);
        pollActivity_imageView_choice_10.setBackgroundColor(0xff4485c9);
    }


    public void checking_img_10_rt() {
        pickCandidate = 0;
        pollActivity_imageView_around_10.setBackgroundColor(0xfff2f2f2);
        pollActivity_imageView_choice_10.setBackgroundColor(0xfff2f2f2);
    }


    //이미지 선택시 전체화면으로 보여주기
    //N선택 선택시 파란색으로 바뀌면서 체크
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pollActivity_imageView_userAddContent_1:
                mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Map<String, Object> contentInfo = (Map<String, Object>) dataSnapshot.getValue();

                        String url = contentInfo.get("imageUrl_0").toString();

                        Intent intent = new Intent(PollSingleActivity.this, FullImageActivity.class);
                        intent.putExtra("imgUrl", url);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
                break;
            case R.id.pollActivity_imageView_userAddContent_2:
                mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Map<String, Object> contentInfo = (Map<String, Object>) dataSnapshot.getValue();

                        String url = contentInfo.get("imageUrl_1").toString();

                        Intent intent = new Intent(PollSingleActivity.this, FullImageActivity.class);
                        intent.putExtra("imgUrl", url);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
                break;
            case R.id.pollActivity_imageView_userAddContent_3:
                mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Map<String, Object> contentInfo = (Map<String, Object>) dataSnapshot.getValue();

                        String url = contentInfo.get("imageUrl_2").toString();

                        Intent intent = new Intent(PollSingleActivity.this, FullImageActivity.class);
                        intent.putExtra("imgUrl", url);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
                break;
            case R.id.pollActivity_imageView_userAddContent_4:
                mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Map<String, Object> contentInfo = (Map<String, Object>) dataSnapshot.getValue();

                        String url = contentInfo.get("imageUrl_3").toString();

                        Intent intent = new Intent(PollSingleActivity.this, FullImageActivity.class);
                        intent.putExtra("imgUrl", url);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
                break;
            case R.id.pollActivity_imageView_userAddContent_5:
                mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Map<String, Object> contentInfo = (Map<String, Object>) dataSnapshot.getValue();

                        String url = contentInfo.get("imageUrl_4").toString();

                        Intent intent = new Intent(PollSingleActivity.this, FullImageActivity.class);
                        intent.putExtra("imgUrl", url);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
                break;
            case R.id.pollActivity_imageView_userAddContent_6:
                mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Map<String, Object> contentInfo = (Map<String, Object>) dataSnapshot.getValue();

                        String url = contentInfo.get("imageUrl_5").toString();

                        Intent intent = new Intent(PollSingleActivity.this, FullImageActivity.class);
                        intent.putExtra("imgUrl", url);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
                break;
            case R.id.pollActivity_imageView_userAddContent_7:
                mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Map<String, Object> contentInfo = (Map<String, Object>) dataSnapshot.getValue();

                        String url = contentInfo.get("imageUrl_6").toString();

                        Intent intent = new Intent(PollSingleActivity.this, FullImageActivity.class);
                        intent.putExtra("imgUrl", url);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
                break;
            case R.id.pollActivity_imageView_userAddContent_8:
                mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Map<String, Object> contentInfo = (Map<String, Object>) dataSnapshot.getValue();

                        String url = contentInfo.get("imageUrl_7").toString();

                        Intent intent = new Intent(PollSingleActivity.this, FullImageActivity.class);
                        intent.putExtra("imgUrl", url);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
                break;
            case R.id.pollActivity_imageView_userAddContent_9:
                mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Map<String, Object> contentInfo = (Map<String, Object>) dataSnapshot.getValue();

                        String url = contentInfo.get("imageUrl_8").toString();

                        Intent intent = new Intent(PollSingleActivity.this, FullImageActivity.class);
                        intent.putExtra("imgUrl", url);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
                break;
            case R.id.pollActivity_imageView_userAddContent_10:
                mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Map<String, Object> contentInfo = (Map<String, Object>) dataSnapshot.getValue();

                        String url = contentInfo.get("imageUrl_9").toString();

                        Intent intent = new Intent(PollSingleActivity.this, FullImageActivity.class);
                        intent.putExtra("imgUrl", url);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
                break;

            case R.id.pollActivity_textView_check_1:
                if (checkUserHitContent) {
                    Toast.makeText(getApplicationContext(), "투표하셨거나 자신의 투표입니다.", Toast.LENGTH_SHORT).show();
                } else {
                    if (((ColorDrawable) pollActivity_imageView_choice_1.getBackground()).getColor() == 0xff4485c9) {
                        checking_img_1_rt();
                    } else if (((ColorDrawable) pollActivity_imageView_choice_1.getBackground()).getColor() == 0xfff2f2f2) {
                        checking_img_1();
                        checking_img_2_rt();
                        checking_img_3_rt();
                        checking_img_4_rt();
                        checking_img_5_rt();
                        checking_img_6_rt();
                        checking_img_7_rt();
                        checking_img_8_rt();
                        checking_img_9_rt();
                        checking_img_10_rt();
                    }
                }
                break;

            case R.id.pollActivity_textView_check_2:
                if (checkUserHitContent) {
                    Toast.makeText(getApplicationContext(), "투표하셨거나 자신의 투표입니다.", Toast.LENGTH_SHORT).show();
                } else {
                    if (((ColorDrawable) pollActivity_imageView_choice_2.getBackground()).getColor() == 0xff4485c9) {
                        checking_img_2_rt();
                    } else if (((ColorDrawable) pollActivity_imageView_choice_2.getBackground()).getColor() == 0xfff2f2f2) {
                        checking_img_2();
                        checking_img_1_rt();
                        checking_img_3_rt();
                        checking_img_4_rt();
                        checking_img_5_rt();
                        checking_img_6_rt();
                        checking_img_7_rt();
                        checking_img_8_rt();
                        checking_img_9_rt();
                        checking_img_10_rt();
                    }
                }
                break;

            case R.id.pollActivity_textView_check_3:
                if (checkUserHitContent) {
                    Toast.makeText(getApplicationContext(), "투표하셨거나 자신의 투표입니다.", Toast.LENGTH_SHORT).show();
                } else {
                    if (((ColorDrawable) pollActivity_imageView_choice_3.getBackground()).getColor() == 0xff4485c9) {
                        checking_img_3_rt();
                    } else if (((ColorDrawable) pollActivity_imageView_choice_3.getBackground()).getColor() == 0xfff2f2f2) {
                        checking_img_3();
                        checking_img_1_rt();
                        checking_img_2_rt();
                        checking_img_4_rt();
                        checking_img_5_rt();
                        checking_img_6_rt();
                        checking_img_7_rt();
                        checking_img_8_rt();
                        checking_img_9_rt();
                        checking_img_10_rt();
                    }
                }
                break;

            case R.id.pollActivity_textView_check_4:
                if (checkUserHitContent) {
                    Toast.makeText(getApplicationContext(), "투표하셨거나 자신의 투표입니다.", Toast.LENGTH_SHORT).show();
                } else {
                    if (((ColorDrawable) pollActivity_imageView_choice_4.getBackground()).getColor() == 0xff4485c9) {
                        checking_img_4_rt();
                    } else if (((ColorDrawable) pollActivity_imageView_choice_4.getBackground()).getColor() == 0xfff2f2f2) {
                        checking_img_4();
                        checking_img_1_rt();
                        checking_img_2_rt();
                        checking_img_3_rt();
                        checking_img_5_rt();
                        checking_img_6_rt();
                        checking_img_7_rt();
                        checking_img_8_rt();
                        checking_img_9_rt();
                        checking_img_10_rt();
                    }
                }
                break;

            case R.id.pollActivity_textView_check_5:
                if (checkUserHitContent) {
                    Toast.makeText(getApplicationContext(), "투표하셨거나 자신의 투표입니다.", Toast.LENGTH_SHORT).show();
                } else {
                    if (((ColorDrawable) pollActivity_imageView_choice_5.getBackground()).getColor() == 0xff4485c9) {
                        checking_img_5_rt();
                    } else if (((ColorDrawable) pollActivity_imageView_choice_5.getBackground()).getColor() == 0xfff2f2f2) {
                        checking_img_5();
                        checking_img_1_rt();
                        checking_img_2_rt();
                        checking_img_3_rt();
                        checking_img_4_rt();
                        checking_img_6_rt();
                        checking_img_7_rt();
                        checking_img_8_rt();
                        checking_img_9_rt();
                        checking_img_10_rt();
                    }
                }
                break;

            case R.id.pollActivity_textView_check_6:
                if (checkUserHitContent) {
                    Toast.makeText(getApplicationContext(), "투표하셨거나 자신의 투표입니다.", Toast.LENGTH_SHORT).show();
                } else {
                    if (((ColorDrawable) pollActivity_imageView_choice_6.getBackground()).getColor() == 0xff4485c9) {
                        checking_img_6_rt();
                    } else if (((ColorDrawable) pollActivity_imageView_choice_6.getBackground()).getColor() == 0xfff2f2f2) {
                        checking_img_6();
                        checking_img_1_rt();
                        checking_img_2_rt();
                        checking_img_3_rt();
                        checking_img_4_rt();
                        checking_img_5_rt();
                        checking_img_7_rt();
                        checking_img_8_rt();
                        checking_img_9_rt();
                        checking_img_10_rt();
                    }
                }
                break;

            case R.id.pollActivity_textView_check_7:
                if (checkUserHitContent) {
                    Toast.makeText(getApplicationContext(), "투표하셨거나 자신의 투표입니다.", Toast.LENGTH_SHORT).show();
                } else {
                    if (((ColorDrawable) pollActivity_imageView_choice_7.getBackground()).getColor() == 0xff4485c9) {
                        checking_img_7_rt();
                    } else if (((ColorDrawable) pollActivity_imageView_choice_7.getBackground()).getColor() == 0xfff2f2f2) {
                        checking_img_7();
                        checking_img_1_rt();
                        checking_img_2_rt();
                        checking_img_3_rt();
                        checking_img_4_rt();
                        checking_img_5_rt();
                        checking_img_6_rt();
                        checking_img_8_rt();
                        checking_img_9_rt();
                        checking_img_10_rt();
                    }
                }
                break;

            case R.id.pollActivity_textView_check_8:
                if (checkUserHitContent) {
                    Toast.makeText(getApplicationContext(), "투표하셨거나 자신의 투표입니다.", Toast.LENGTH_SHORT).show();
                } else {
                    if (((ColorDrawable) pollActivity_imageView_choice_8.getBackground()).getColor() == 0xff4485c9) {
                        checking_img_8_rt();
                    } else if (((ColorDrawable) pollActivity_imageView_choice_8.getBackground()).getColor() == 0xfff2f2f2) {
                        checking_img_8();
                        checking_img_1_rt();
                        checking_img_2_rt();
                        checking_img_3_rt();
                        checking_img_4_rt();
                        checking_img_5_rt();
                        checking_img_6_rt();
                        checking_img_7_rt();
                        checking_img_9_rt();
                        checking_img_10_rt();
                    }
                }
                break;

            case R.id.pollActivity_textView_check_9:
                if (checkUserHitContent) {
                    Toast.makeText(getApplicationContext(), "투표하셨거나 자신의 투표입니다.", Toast.LENGTH_SHORT).show();
                } else {
                    if (((ColorDrawable) pollActivity_imageView_choice_9.getBackground()).getColor() == 0xff4485c9) {
                        checking_img_9_rt();
                    } else if (((ColorDrawable) pollActivity_imageView_choice_9.getBackground()).getColor() == 0xfff2f2f2) {
                        checking_img_9();
                        checking_img_1_rt();
                        checking_img_2_rt();
                        checking_img_3_rt();
                        checking_img_4_rt();
                        checking_img_5_rt();
                        checking_img_6_rt();
                        checking_img_7_rt();
                        checking_img_8_rt();
                        checking_img_10_rt();
                    }
                }

                break;

            case R.id.pollActivity_textView_check_10:
                if (checkUserHitContent) {
                    Toast.makeText(getApplicationContext(), "투표하셨거나 자신의 투표입니다.", Toast.LENGTH_SHORT).show();
                } else {
                    if (((ColorDrawable) pollActivity_imageView_choice_10.getBackground()).getColor() == 0xff4485c9) {
                        checking_img_10_rt();
                    } else if (((ColorDrawable) pollActivity_imageView_choice_10.getBackground()).getColor() == 0xfff2f2f2) {
                        checking_img_10();
                        checking_img_1_rt();
                        checking_img_2_rt();
                        checking_img_3_rt();
                        checking_img_4_rt();
                        checking_img_5_rt();
                        checking_img_6_rt();
                        checking_img_7_rt();
                        checking_img_8_rt();
                        checking_img_9_rt();
                    }
                }

                break;
        }
    }


    public class ResultValueFormatter implements IValueFormatter {
        private DecimalFormat mFormat;

        public ResultValueFormatter() {
            mFormat = new DecimalFormat("###,###,##0");
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return mFormat.format(value) + "표";
        }
    }

    public class CategoryBarChartXaxisFormatter implements IAxisValueFormatter {
        ArrayList<String> mValues;

        public CategoryBarChartXaxisFormatter(ArrayList<String> values) {
            this.mValues = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {

            int val = (int) value;
            String label = "";
            if (val >= 0 && val < mValues.size()) {
                label = mValues.get(val);
            } else {
                label = "";
            }

            return label;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.poll_single_menu, menu);
        final MenuItem item = menu.findItem(R.id.menu_showMore);
        final String contentKey = getIntent().getStringExtra("contentKey");
        firebaseDatabase.getReference().child("user_contents").child(contentKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ContentDTO contentDTO = dataSnapshot.getValue(ContentDTO.class);
                if (contentDTO.getUid().equals(auth.getCurrentUser().getUid())) {
                    item.setVisible(true);
                } else {
                    item.setVisible(false);
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
                Intent intent = new Intent(PollSingleActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.delete:
                openContentDeleteDialog();
                Log.d("lkj delete", "dedete");


                break;
            case R.id.alarm:
                Log.d("lkj alara", "dedete");
                String contentKey = getIntent().getStringExtra("contentKey");
                Intent intentShowMore = new Intent(PollSingleActivity.this, ShowMoreActivity.class);
                intentShowMore.putExtra("pollKey", contentKey);
                intentShowMore.putExtra("hitCount", contentHit);
                startActivityForResult(intentShowMore, 20000);
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openContentDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("삭제 알림");
        builder.setMessage("정말 이 투표를 삭제 하시겠습니까?\n 삭제되면 복구는 불가능 합니다.");
        builder.setPositiveButton("확 인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
                DatabaseReference mReference = FirebaseDatabase.getInstance().getReference();
                String pollKey = getIntent().getStringExtra("contentKey");
                mReference.child("user_contents").child(pollKey).removeValue();
                mReference.child("users").child(mUser.getUid()).child("uploadContent").child(pollKey).removeValue();
                Toast toast = Toast.makeText(getApplicationContext(), "삭제 되었습니다.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
                toast.show();
                onBackPressed();
            }
        });
        builder.setNeutralButton("취 소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }


    //picker의 현재픽,성별,나이 가져와서 통계항목에 +1
    private String addStatistics(String statistics_code, int currentPick, String gender, int age) {
        String[] stringArray = null;
        stringArray = statistics_code.split(":");
        int[] tmpStatistics = new int[stringArray.length];
        for (int i = 0; i < stringArray.length; i++) {
            tmpStatistics[i] = Integer.parseInt(stringArray[i]);
        }

        if (gender.equals("여")) {
            switch (currentPick) {
                case 0:
                    tmpStatistics[0]++;
                    break;
                case 1:
                    tmpStatistics[1]++;
                    break;
                case 2:
                    tmpStatistics[2]++;
                    break;
                case 3:
                    tmpStatistics[3]++;
                    break;
                case 4:
                    tmpStatistics[4]++;
                    break;
                case 5:
                    tmpStatistics[5]++;
                    break;
                case 6:
                    tmpStatistics[6]++;
                    break;
                case 7:
                    tmpStatistics[7]++;
                    break;
                case 8:
                    tmpStatistics[8]++;
                    break;
                case 9:
                    tmpStatistics[9]++;
                    break;
            }
        }
        if (gender.equals("남")) {
            switch (currentPick) {
                case 0:
                    tmpStatistics[10]++;
                    break;
                case 1:
                    tmpStatistics[11]++;
                    break;
                case 2:
                    tmpStatistics[12]++;
                    break;
                case 3:
                    tmpStatistics[13]++;
                    break;
                case 4:
                    tmpStatistics[14]++;
                    break;
                case 5:
                    tmpStatistics[15]++;
                    break;
                case 6:
                    tmpStatistics[16]++;
                    break;
                case 7:
                    tmpStatistics[17]++;
                    break;
                case 8:
                    tmpStatistics[18]++;
                    break;
                case 9:
                    tmpStatistics[19]++;
                    break;
            }
        }
        if (age == 10 || age == 11 || age == 12) {
            switch (currentPick) {
                case 0:
                    tmpStatistics[20]++;
                    break;
                case 1:
                    tmpStatistics[21]++;
                    break;
                case 2:
                    tmpStatistics[22]++;
                    break;
                case 3:
                    tmpStatistics[23]++;
                    break;
                case 4:
                    tmpStatistics[24]++;
                    break;
                case 5:
                    tmpStatistics[25]++;
                    break;
                case 6:
                    tmpStatistics[26]++;
                    break;
                case 7:
                    tmpStatistics[27]++;
                    break;
                case 8:
                    tmpStatistics[28]++;
                    break;
                case 9:
                    tmpStatistics[29]++;
                    break;
            }
        }
        if (age == 13 || age == 14 || age == 15 || age == 16) {
            switch (currentPick) {
                case 0:
                    tmpStatistics[30]++;
                    break;
                case 1:
                    tmpStatistics[31]++;
                    break;
                case 2:
                    tmpStatistics[32]++;
                    break;
                case 3:
                    tmpStatistics[33]++;
                    break;
                case 4:
                    tmpStatistics[34]++;
                    break;
                case 5:
                    tmpStatistics[35]++;
                    break;
                case 6:
                    tmpStatistics[36]++;
                    break;
                case 7:
                    tmpStatistics[37]++;
                    break;
                case 8:
                    tmpStatistics[38]++;
                    break;
                case 9:
                    tmpStatistics[39]++;
                    break;
            }
        }
        if (age == 17 || age == 18 || age == 19) {
            switch (currentPick) {
                case 0:
                    tmpStatistics[40]++;
                    break;
                case 1:
                    tmpStatistics[41]++;
                    break;
                case 2:
                    tmpStatistics[42]++;
                    break;
                case 3:
                    tmpStatistics[43]++;
                    break;
                case 4:
                    tmpStatistics[44]++;
                    break;
                case 5:
                    tmpStatistics[45]++;
                    break;
                case 6:
                    tmpStatistics[46]++;
                    break;
                case 7:
                    tmpStatistics[47]++;
                    break;
                case 8:
                    tmpStatistics[48]++;
                    break;
                case 9:
                    tmpStatistics[49]++;
                    break;
            }
        }
        if (age == 20 || age == 21 || age == 22) {
            switch (currentPick) {
                case 0:
                    tmpStatistics[50]++;
                    break;
                case 1:
                    tmpStatistics[51]++;
                    break;
                case 2:
                    tmpStatistics[52]++;
                    break;
                case 3:
                    tmpStatistics[53]++;
                    break;
                case 4:
                    tmpStatistics[54]++;
                    break;
                case 5:
                    tmpStatistics[55]++;
                    break;
                case 6:
                    tmpStatistics[56]++;
                    break;
                case 7:
                    tmpStatistics[57]++;
                    break;
                case 8:
                    tmpStatistics[58]++;
                    break;
                case 9:
                    tmpStatistics[59]++;
                    break;
            }
        }
        if (age == 23 || age == 24 || age == 25 || age == 26) {
            switch (currentPick) {
                case 0:
                    tmpStatistics[60]++;
                    break;
                case 1:
                    tmpStatistics[61]++;
                    break;
                case 2:
                    tmpStatistics[62]++;
                    break;
                case 3:
                    tmpStatistics[63]++;
                    break;
                case 4:
                    tmpStatistics[64]++;
                    break;
                case 5:
                    tmpStatistics[65]++;
                    break;
                case 6:
                    tmpStatistics[66]++;
                    break;
                case 7:
                    tmpStatistics[67]++;
                    break;
                case 8:
                    tmpStatistics[68]++;
                    break;
                case 9:
                    tmpStatistics[69]++;
                    break;
            }
        }
        if (age == 27 || age == 28 || age == 29) {
            switch (currentPick) {
                case 0:
                    tmpStatistics[70]++;
                    break;
                case 1:
                    tmpStatistics[71]++;
                    break;
                case 2:
                    tmpStatistics[72]++;
                    break;
                case 3:
                    tmpStatistics[73]++;
                    break;
                case 4:
                    tmpStatistics[74]++;
                    break;
                case 5:
                    tmpStatistics[75]++;
                    break;
                case 6:
                    tmpStatistics[76]++;
                    break;
                case 7:
                    tmpStatistics[77]++;
                    break;
                case 8:
                    tmpStatistics[78]++;
                    break;
                case 9:
                    tmpStatistics[79]++;
                    break;
            }
        }
        if (age == 30 || age == 31 || age == 32) {
            switch (currentPick) {
                case 0:
                    tmpStatistics[80]++;
                    break;
                case 1:
                    tmpStatistics[81]++;
                    break;
                case 2:
                    tmpStatistics[82]++;
                    break;
                case 3:
                    tmpStatistics[83]++;
                    break;
                case 4:
                    tmpStatistics[84]++;
                    break;
                case 5:
                    tmpStatistics[85]++;
                    break;
                case 6:
                    tmpStatistics[86]++;
                    break;
                case 7:
                    tmpStatistics[87]++;
                    break;
                case 8:
                    tmpStatistics[88]++;
                    break;
                case 9:
                    tmpStatistics[89]++;
                    break;
            }
        }
        if (age == 33 || age == 34 || age == 35 || age == 36) {
            switch (currentPick) {
                case 0:
                    tmpStatistics[90]++;
                    break;
                case 1:
                    tmpStatistics[91]++;
                    break;
                case 2:
                    tmpStatistics[92]++;
                    break;
                case 3:
                    tmpStatistics[93]++;
                    break;
                case 4:
                    tmpStatistics[94]++;
                    break;
                case 5:
                    tmpStatistics[95]++;
                    break;
                case 6:
                    tmpStatistics[96]++;
                    break;
                case 7:
                    tmpStatistics[97]++;
                    break;
                case 8:
                    tmpStatistics[98]++;
                    break;
                case 9:
                    tmpStatistics[99]++;
                    break;
            }
        }
        if (age == 37 || age == 38 || age == 39) {
            switch (currentPick) {
                case 0:
                    tmpStatistics[100]++;
                    break;
                case 1:
                    tmpStatistics[101]++;
                    break;
                case 2:
                    tmpStatistics[102]++;
                    break;
                case 3:
                    tmpStatistics[103]++;
                    break;
                case 4:
                    tmpStatistics[104]++;
                    break;
                case 5:
                    tmpStatistics[105]++;
                    break;
                case 6:
                    tmpStatistics[106]++;
                    break;
                case 7:
                    tmpStatistics[107]++;
                    break;
                case 8:
                    tmpStatistics[108]++;
                    break;
                case 9:
                    tmpStatistics[109]++;
                    break;
            }
        }
        if (age == 40 || age == 41 || age == 42) {
            switch (currentPick) {
                case 0:
                    tmpStatistics[110]++;
                    break;
                case 1:
                    tmpStatistics[111]++;
                    break;
                case 2:
                    tmpStatistics[112]++;
                    break;
                case 3:
                    tmpStatistics[113]++;
                    break;
                case 4:
                    tmpStatistics[114]++;
                    break;
                case 5:
                    tmpStatistics[115]++;
                    break;
                case 6:
                    tmpStatistics[116]++;
                    break;
                case 7:
                    tmpStatistics[117]++;
                    break;
                case 8:
                    tmpStatistics[118]++;
                    break;
                case 9:
                    tmpStatistics[119]++;
                    break;
            }
        }
        if (age == 43 || age == 44 || age == 45 || age == 46) {
            switch (currentPick) {
                case 0:
                    tmpStatistics[120]++;
                    break;
                case 1:
                    tmpStatistics[121]++;
                    break;
                case 2:
                    tmpStatistics[122]++;
                    break;
                case 3:
                    tmpStatistics[123]++;
                    break;
                case 4:
                    tmpStatistics[124]++;
                    break;
                case 5:
                    tmpStatistics[125]++;
                    break;
                case 6:
                    tmpStatistics[126]++;
                    break;
                case 7:
                    tmpStatistics[127]++;
                    break;
                case 8:
                    tmpStatistics[128]++;
                    break;
                case 9:
                    tmpStatistics[129]++;
                    break;
            }
        }
        if (age == 47 || age == 48 || age == 49) {
            switch (currentPick) {
                case 0:
                    tmpStatistics[130]++;
                    break;
                case 1:
                    tmpStatistics[131]++;
                    break;
                case 2:
                    tmpStatistics[132]++;
                    break;
                case 3:
                    tmpStatistics[133]++;
                    break;
                case 4:
                    tmpStatistics[134]++;
                    break;
                case 5:
                    tmpStatistics[135]++;
                    break;
                case 6:
                    tmpStatistics[136]++;
                    break;
                case 7:
                    tmpStatistics[137]++;
                    break;
                case 8:
                    tmpStatistics[138]++;
                    break;
                case 9:
                    tmpStatistics[139]++;
                    break;
            }
        }
        if (age == 50 || age == 51 || age == 52) {
            switch (currentPick) {
                case 0:
                    tmpStatistics[140]++;
                    break;
                case 1:
                    tmpStatistics[141]++;
                    break;
                case 2:
                    tmpStatistics[142]++;
                    break;
                case 3:
                    tmpStatistics[143]++;
                    break;
                case 4:
                    tmpStatistics[144]++;
                    break;
                case 5:
                    tmpStatistics[145]++;
                    break;
                case 6:
                    tmpStatistics[146]++;
                    break;
                case 7:
                    tmpStatistics[147]++;
                    break;
                case 8:
                    tmpStatistics[148]++;
                    break;
                case 9:
                    tmpStatistics[149]++;
                    break;
            }
        }
        if (age == 53 || age == 54 || age == 55 || age == 56) {
            switch (currentPick) {
                case 0:
                    tmpStatistics[150]++;
                    break;
                case 1:
                    tmpStatistics[151]++;
                    break;
                case 2:
                    tmpStatistics[152]++;
                    break;
                case 3:
                    tmpStatistics[153]++;
                    break;
                case 4:
                    tmpStatistics[154]++;
                    break;
                case 5:
                    tmpStatistics[155]++;
                    break;
                case 6:
                    tmpStatistics[156]++;
                    break;
                case 7:
                    tmpStatistics[157]++;
                    break;
                case 8:
                    tmpStatistics[158]++;
                    break;
                case 9:
                    tmpStatistics[159]++;
                    break;
            }
        }
        if (age == 57 || age == 58 || age == 59) {
            switch (currentPick) {
                case 0:
                    tmpStatistics[160]++;
                    break;
                case 1:
                    tmpStatistics[161]++;
                    break;
                case 2:
                    tmpStatistics[162]++;
                    break;
                case 3:
                    tmpStatistics[163]++;
                    break;
                case 4:
                    tmpStatistics[164]++;
                    break;
                case 5:
                    tmpStatistics[165]++;
                    break;
                case 6:
                    tmpStatistics[166]++;
                    break;
                case 7:
                    tmpStatistics[167]++;
                    break;
                case 8:
                    tmpStatistics[168]++;
                    break;
                case 9:
                    tmpStatistics[169]++;
                    break;
            }
        }
        if (age == 60 || age == 61 || age == 62) {
            switch (currentPick) {
                case 0:
                    tmpStatistics[170]++;
                    break;
                case 1:
                    tmpStatistics[171]++;
                    break;
                case 2:
                    tmpStatistics[172]++;
                    break;
                case 3:
                    tmpStatistics[173]++;
                    break;
                case 4:
                    tmpStatistics[174]++;
                    break;
                case 5:
                    tmpStatistics[175]++;
                    break;
                case 6:
                    tmpStatistics[176]++;
                    break;
                case 7:
                    tmpStatistics[177]++;
                    break;
                case 8:
                    tmpStatistics[178]++;
                    break;
                case 9:
                    tmpStatistics[179]++;
                    break;
            }
        }
        if (age == 63 || age == 64 || age == 65 || age == 66) {
            switch (currentPick) {
                case 0:
                    tmpStatistics[180]++;
                    break;
                case 1:
                    tmpStatistics[181]++;
                    break;
                case 2:
                    tmpStatistics[182]++;
                    break;
                case 3:
                    tmpStatistics[183]++;
                    break;
                case 4:
                    tmpStatistics[184]++;
                    break;
                case 5:
                    tmpStatistics[185]++;
                    break;
                case 6:
                    tmpStatistics[186]++;
                    break;
                case 7:
                    tmpStatistics[187]++;
                    break;
                case 8:
                    tmpStatistics[188]++;
                    break;
                case 9:
                    tmpStatistics[189]++;
                    break;
            }
        }
        if (age == 67 || age == 68 || age == 69) {
            switch (currentPick) {
                case 0:
                    tmpStatistics[190]++;
                    break;
                case 1:
                    tmpStatistics[191]++;
                    break;
                case 2:
                    tmpStatistics[192]++;
                    break;
                case 3:
                    tmpStatistics[193]++;
                    break;
                case 4:
                    tmpStatistics[194]++;
                    break;
                case 5:
                    tmpStatistics[195]++;
                    break;
                case 6:
                    tmpStatistics[196]++;
                    break;
                case 7:
                    tmpStatistics[197]++;
                    break;
                case 8:
                    tmpStatistics[198]++;
                    break;
                case 9:
                    tmpStatistics[199]++;
                    break;
            }
        }
        if (age == 70 || age == 71 || age == 72) {
            switch (currentPick) {
                case 0:
                    tmpStatistics[200]++;
                    break;
                case 1:
                    tmpStatistics[201]++;
                    break;
                case 2:
                    tmpStatistics[202]++;
                    break;
                case 3:
                    tmpStatistics[203]++;
                    break;
                case 4:
                    tmpStatistics[204]++;
                    break;
                case 5:
                    tmpStatistics[205]++;
                    break;
                case 6:
                    tmpStatistics[206]++;
                    break;
                case 7:
                    tmpStatistics[207]++;
                    break;
                case 8:
                    tmpStatistics[208]++;
                    break;
                case 9:
                    tmpStatistics[209]++;
                    break;
            }
        }
        if (age == 73 || age == 74 || age == 75 || age == 76) {
            switch (currentPick) {
                case 0:
                    tmpStatistics[210]++;
                    break;
                case 1:
                    tmpStatistics[211]++;
                    break;
                case 2:
                    tmpStatistics[212]++;
                    break;
                case 3:
                    tmpStatistics[213]++;
                    break;
                case 4:
                    tmpStatistics[214]++;
                    break;
                case 5:
                    tmpStatistics[215]++;
                    break;
                case 6:
                    tmpStatistics[216]++;
                    break;
                case 7:
                    tmpStatistics[217]++;
                    break;
                case 8:
                    tmpStatistics[218]++;
                    break;
                case 9:
                    tmpStatistics[219]++;
                    break;
            }
        }
        if (age == 77 || age == 78 || age == 79) {
            switch (currentPick) {
                case 0:
                    tmpStatistics[220]++;
                    break;
                case 1:
                    tmpStatistics[221]++;
                    break;
                case 2:
                    tmpStatistics[222]++;
                    break;
                case 3:
                    tmpStatistics[223]++;
                    break;
                case 4:
                    tmpStatistics[224]++;
                    break;
                case 5:
                    tmpStatistics[225]++;
                    break;
                case 6:
                    tmpStatistics[226]++;
                    break;
                case 7:
                    tmpStatistics[227]++;
                    break;
                case 8:
                    tmpStatistics[228]++;
                    break;
                case 9:
                    tmpStatistics[229]++;
                    break;
            }
        }
        if (age == 80 || age == 81 || age == 82) {
            switch (currentPick) {
                case 0:
                    tmpStatistics[230]++;
                    break;
                case 1:
                    tmpStatistics[231]++;
                    break;
                case 2:
                    tmpStatistics[232]++;
                    break;
                case 3:
                    tmpStatistics[233]++;
                    break;
                case 4:
                    tmpStatistics[234]++;
                    break;
                case 5:
                    tmpStatistics[235]++;
                    break;
                case 6:
                    tmpStatistics[236]++;
                    break;
                case 7:
                    tmpStatistics[237]++;
                    break;
                case 8:
                    tmpStatistics[238]++;
                    break;
                case 9:
                    tmpStatistics[239]++;
                    break;
            }
        }
        if (age == 83 || age == 84 || age == 85 || age == 86) {
            switch (currentPick) {
                case 0:
                    tmpStatistics[240]++;
                    break;
                case 1:
                    tmpStatistics[241]++;
                    break;
                case 2:
                    tmpStatistics[242]++;
                    break;
                case 3:
                    tmpStatistics[243]++;
                    break;
                case 4:
                    tmpStatistics[244]++;
                    break;
                case 5:
                    tmpStatistics[245]++;
                    break;
                case 6:
                    tmpStatistics[246]++;
                    break;
                case 7:
                    tmpStatistics[247]++;
                    break;
                case 8:
                    tmpStatistics[248]++;
                    break;
                case 9:
                    tmpStatistics[249]++;
                    break;
            }
        }
        if (age == 87 || age == 88 || age == 89) {
            switch (currentPick) {
                case 0:
                    tmpStatistics[250]++;
                    break;
                case 1:
                    tmpStatistics[251]++;
                    break;
                case 2:
                    tmpStatistics[252]++;
                    break;
                case 3:
                    tmpStatistics[253]++;
                    break;
                case 4:
                    tmpStatistics[254]++;
                    break;
                case 5:
                    tmpStatistics[255]++;
                    break;
                case 6:
                    tmpStatistics[256]++;
                    break;
                case 7:
                    tmpStatistics[257]++;
                    break;
                case 8:
                    tmpStatistics[258]++;
                    break;
                case 9:
                    tmpStatistics[259]++;
                    break;
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


    public void refreshActivity() {
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
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

    @Override
    public void ContentDeleteDialogCallback(String string) {
        if (string.equals("확인")) {
            FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
            DatabaseReference mReference = FirebaseDatabase.getInstance().getReference();
            String pollKey = getIntent().getStringExtra("pollKey");
            finish();
            mReference.child("user_contents").child(pollKey).removeValue();
            mReference.child("users").child(mUser.getUid()).child("uploadContent").child(pollKey).removeValue();
        }

    }
}




