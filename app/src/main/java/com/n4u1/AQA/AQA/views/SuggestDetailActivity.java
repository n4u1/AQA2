package com.n4u1.AQA.AQA.views;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.n4u1.AQA.AQA.R;
import com.n4u1.AQA.AQA.dialog.DeleteModificationActivity;
import com.n4u1.AQA.AQA.models.ReplyDTO;
import com.n4u1.AQA.AQA.models.SuggestDTO;
import com.n4u1.AQA.AQA.recyclerview.ReplySuggestAdapter;
import com.n4u1.AQA.AQA.util.GlideApp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class SuggestDetailActivity extends AppCompatActivity {

    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseDatabase likeFirebaseDatabase;
    private String replyKey;
    final ArrayList<ReplyDTO> replyDTOS = new ArrayList<>();

    TextView suggestDetailActivity_textView_title, suggestDetailActivity_textView_userId,
            suggestDetailActivity_textView_contentId, suggestDetailActivity_textView_date,
            suggestDetailActivity_textView_description, suggestDetailActivity_textView_likeCount,
            suggestDetailActivity_textView_replyCount;
    ImageView suggestDetailActivity_imageView_userClass, suggestDetailActivity_imageView_like,
            suggestDetailActivity_imageView_reply, suggestDetailActivity_button_replySend,
            suggestDetailActivity_imageView_userAddContent_2, suggestDetailActivity_imageView_userAddContent_1;
    RecyclerView suggestDetailActivity_recyclerView_reply;
    EditText suggestDetailActivity_editText_reply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        setContentView(R.layout.activity_suggest_detail);


        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(null);
            getSupportActionBar().setSubtitle("건의 사항");
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        final String suggestKey = getIntent().getStringExtra("suggestKey");
        firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        likeFirebaseDatabase = FirebaseDatabase.getInstance();

        suggestDetailActivity_textView_title = findViewById(R.id.suggestDetailActivity_textView_title);
        suggestDetailActivity_textView_userId = findViewById(R.id.suggestDetailActivity_textView_userId);
        suggestDetailActivity_textView_contentId = findViewById(R.id.suggestDetailActivity_textView_contentId);
        suggestDetailActivity_textView_date = findViewById(R.id.suggestDetailActivity_textView_date);
        suggestDetailActivity_textView_description = findViewById(R.id.suggestDetailActivity_textView_description);
        suggestDetailActivity_textView_likeCount = findViewById(R.id.suggestDetailActivity_textView_likeCount);
        suggestDetailActivity_textView_replyCount = findViewById(R.id.suggestDetailActivity_textView_replyCount);

        suggestDetailActivity_imageView_userAddContent_1 = findViewById(R.id.suggestDetailActivity_imageView_userAddContent_1);
        suggestDetailActivity_imageView_userAddContent_2 = findViewById(R.id.suggestDetailActivity_imageView_userAddContent_2);
        suggestDetailActivity_imageView_userClass = findViewById(R.id.suggestDetailActivity_imageView_userClass);
        suggestDetailActivity_imageView_like = findViewById(R.id.suggestDetailActivity_imageView_like);
        suggestDetailActivity_imageView_reply = findViewById(R.id.suggestDetailActivity_imageView_reply);
        suggestDetailActivity_button_replySend = findViewById(R.id.suggestDetailActivity_button_replySend);
        suggestDetailActivity_recyclerView_reply = findViewById(R.id.suggestDetailActivity_recyclerView_reply);
        suggestDetailActivity_editText_reply = findViewById(R.id.suggestDetailActivity_editText_reply);

//reply item click listener 댓글 클릭 리스너
        final ReplySuggestAdapter replySuggestAdapter = new ReplySuggestAdapter(getApplicationContext(), replyDTOS, new ReplySuggestAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final View view, final int position) {

                FirebaseDatabase replyDatabase;
                replyDatabase = FirebaseDatabase.getInstance();
                replyDatabase.getReference().child("users").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Map<String, Object> user = (Map<String, Object>) dataSnapshot.getValue();
                        if (replyDTOS.get(position).getId().equals(user.get("userId").toString())) {
                            if (view.getTag().equals("replySuggestAdapter_relativeLayout_like")) {
                                Toast.makeText(getApplicationContext(), user.get("userId").toString() + "님 댓글 입니다.", Toast.LENGTH_SHORT).show();
                            } else if (view.getTag().equals("replySuggestAdapter_relativeLayout_main")) { //댓글 클릭, 삭제 또는 수정
                                firebaseDatabase.getReference().child("suggestReply").child(suggestKey).addListenerForSingleValueEvent(new ValueEventListener() {
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
                                        Intent intent = new Intent(SuggestDetailActivity.this, DeleteModificationActivity.class);
                                        intent.putExtra("replyKey", replyKey);
                                        startActivityForResult(intent, 11000);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        } else {
                            if (view.getTag().equals("replySuggestAdapter_relativeLayout_like")) {
                                firebaseDatabase.getReference().child("suggestReply").child(suggestKey).addListenerForSingleValueEvent(new ValueEventListener() {
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
                                        onReplyLikeClicked(firebaseDatabase.getReference().child("suggestReply").child(suggestKey).child(replyDTOTemp.get(temp).getReplyKey()));
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


        //suggestDTO 화면 초기세팅
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mDatabaseReference.child("suggest").child(suggestKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                SuggestDTO suggestDTO = dataSnapshot.getValue(SuggestDTO.class);
                settingUserIcon(suggestDTO.getUid());
                suggestDetailActivity_textView_title.setText(suggestDTO.getTitle());
                suggestDetailActivity_textView_userId.setText(suggestDTO.getUserID());
                suggestDetailActivity_textView_contentId.setText(suggestDTO.getContentId());
                suggestDetailActivity_textView_date.setText(suggestDTO.getUploadDate());
                suggestDetailActivity_textView_description.setText(suggestDTO.getDescription());
                suggestDetailActivity_textView_replyCount.setText(String.valueOf(suggestDTO.getReplyCount()));
                suggestDetailActivity_textView_likeCount.setText(String.valueOf(suggestDTO.getLikeCount()));

                if (suggestDTO.getImageUrl_1() != null) {
                    suggestDetailActivity_imageView_userAddContent_1.setVisibility(View.VISIBLE);
                    GlideApp.with(getApplicationContext()).load(suggestDTO.getImageUrl_1()).centerCrop().thumbnail(Glide.with(getApplicationContext()).load(R.drawable.loadingicon)).into(suggestDetailActivity_imageView_userAddContent_1).getView();
                }
                if (suggestDTO.getImageUrl_2() != null) {
                    suggestDetailActivity_imageView_userAddContent_2.setVisibility(View.VISIBLE);
                    GlideApp.with(getApplicationContext()).load(suggestDTO.getImageUrl_2()).centerCrop().thumbnail(Glide.with(getApplicationContext()).load(R.drawable.loadingicon)).into(suggestDetailActivity_imageView_userAddContent_2).getView();
                }

                if (suggestDTO.likes.containsKey(mAuth.getCurrentUser().getUid())) {
                    suggestDetailActivity_imageView_like.setImageResource(R.drawable.ic_suggest_up_fill);
                } else {
                    suggestDetailActivity_imageView_like.setImageResource(R.drawable.ic_suggest_up);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //따봉버튼 클릭리스너,  좋아요(따봉) 이미지 클릭
        suggestDetailActivity_imageView_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeClick();
            }
        });
        //따봉버튼 클릭리스너,  숫자 클릭
        suggestDetailActivity_textView_likeCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeClick();
            }
        });


        //댓글 등록 버튼 색 변경
        suggestDetailActivity_editText_reply.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() > 0) {
                    suggestDetailActivity_button_replySend.setImageResource(R.drawable.ic_play_triangle_2);
                } else {
                    suggestDetailActivity_button_replySend.setImageResource(R.drawable.ic_play_triangle_1);
                }
            }
        });


        //reply 초기세팅
        firebaseDatabase.getReference().child("suggestReply").child(suggestKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                replyDTOS.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ReplyDTO replyDTO = snapshot.getValue(ReplyDTO.class);
                    replyDTOS.add(replyDTO);
                }
                suggestDetailActivity_textView_replyCount.setText(String.valueOf(dataSnapshot.getChildrenCount()));


                replySuggestAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //댓글 리사이클러뷰 스크롤
        suggestDetailActivity_recyclerView_reply.setNestedScrollingEnabled(false);
        suggestDetailActivity_recyclerView_reply.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });


        //댓글 리사이클러뷰
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLayoutManager.isSmoothScrollbarEnabled();
        mLayoutManager.setStackFromEnd(true);
        mLayoutManager.setReverseLayout(false);

        suggestDetailActivity_recyclerView_reply.setLayoutManager(mLayoutManager);
        suggestDetailActivity_recyclerView_reply.setAdapter(replySuggestAdapter);


        //댓글 달기
        suggestDetailActivity_button_replySend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (suggestDetailActivity_editText_reply.getText().length() > 0) {
                    FirebaseDatabase replyDatabase;
                    replyDatabase = FirebaseDatabase.getInstance();
                    replyDatabase.getReference().child("users").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Map<String, Object> users = (Map<String, Object>) dataSnapshot.getValue();
                            String date = getDate();
                            onReplyClicked(firebaseDatabase.getReference().child("suggest").child(suggestKey));
                            replyKey = firebaseDatabase.getReference().child("suggestReply").push().getKey();
                            ReplyDTO replyDTO = new ReplyDTO();
                            replyDTO.setReplyKey(replyKey);
                            replyDTO.setDate(date);
                            replyDTO.setId(users.get("userId").toString());
                            replyDTO.setuId(mAuth.getCurrentUser().getUid());
                            replyDTO.setReply(suggestDetailActivity_editText_reply.getText().toString());
                            replyDTO.setqPoint(Integer.parseInt(users.get("userClass").toString()));
                            replyDTO.setContentKey(suggestKey);
                            firebaseDatabase.getReference().child("suggestReply").child(suggestKey).child(replyKey).setValue(replyDTO);
                            firebaseDatabase.getReference().child("users").child(mAuth.getCurrentUser().getUid()).child("suggestReply").child(suggestKey).push().setValue(replyDTO);
                            suggestDetailActivity_editText_reply.setText(null);//editText 초기화
                            suggestDetailActivity_editText_reply.setHint("댓글...");
                            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); //키보드 숨기기
                            inputMethodManager.hideSoftInputFromWindow(suggestDetailActivity_editText_reply.getWindowToken(), 0); //키보드 숨기기

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

            }
        });


    }
    /*
    onCreate();
     */

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
                if (replyDTO.likes.containsKey(mAuth.getCurrentUser().getUid())) {
                    replyDTO.likeCount = replyDTO.likeCount - 1;
                    replyDTO.likes.remove(mAuth.getCurrentUser().getUid());
                    //좋아요 누른 이력 없으면 더하고 카운트+1
                } else {
                    replyDTO.likeCount = replyDTO.likeCount + 1;
                    replyDTO.likes.put(mAuth.getCurrentUser().getUid(), true);
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 11000:
                    if (data.getStringArrayListExtra("resultDelete").get(1).equals("삭제하기")) {
                        String contentKey = getIntent().getStringExtra("suggestKey");
                        String replyKey = data.getStringArrayListExtra("resultDelete").get(0);

                        firebaseDatabase.getReference().child("suggestReply").child(contentKey).child(replyKey).removeValue();
                        removeReplyCount(firebaseDatabase.getReference().child("suggest").child(contentKey));

                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void removeReplyCount(DatabaseReference ref) {
        ref.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                SuggestDTO suggestDTO = mutableData.getValue(SuggestDTO.class);

                if (suggestDTO == null) {
                    return Transaction.success(mutableData);
                }
                suggestDTO.replyCount = suggestDTO.replyCount - 1;
                mutableData.setValue(suggestDTO);
                return  Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                Toast.makeText(getApplicationContext(), "댓글 삭제 완료", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mine_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int curId = item.getItemId();
        switch (curId) {
            case R.id.menu_home:
                Intent intentHome = new Intent(this, HomeActivity.class);
                intentHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentHome);
                finish();
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void settingUserIcon(String uId) {
        mDatabaseReference.child("users").child(uId).child("userClass").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    int userClass = Integer.parseInt(dataSnapshot.getValue().toString());
                    if (userClass >= 0 && userClass < 50) {
                        suggestDetailActivity_imageView_userClass.setImageResource(R.drawable.q_class_red_2);
                    } else if (userClass >= 50 && userClass < 100) {
                        suggestDetailActivity_imageView_userClass.setImageResource(R.drawable.q_class_red_1);
                    } else if (userClass >= 100 && userClass < 150) {
                        suggestDetailActivity_imageView_userClass.setImageResource(R.drawable.q_class_orange_1);
                    } else if (userClass >= 150 && userClass < 200) {
                        suggestDetailActivity_imageView_userClass.setImageResource(R.drawable.q_class_orange_2);
                    } else if (userClass >= 200 && userClass < 250) {
                        suggestDetailActivity_imageView_userClass.setImageResource(R.drawable.q_class_yellow_1);
                    } else if (userClass >= 250 && userClass < 300) {
                        suggestDetailActivity_imageView_userClass.setImageResource(R.drawable.q_class_yellow_2);
                    } else if (userClass >= 300 && userClass < 350) {
                        suggestDetailActivity_imageView_userClass.setImageResource(R.drawable.q_class_green_1);
                    } else if (userClass >= 350 && userClass < 400) {
                        suggestDetailActivity_imageView_userClass.setImageResource(R.drawable.q_class_green_2);
                    } else if (userClass >= 400 && userClass < 450) {
                        suggestDetailActivity_imageView_userClass.setImageResource(R.drawable.q_class_blue_2);
                    } else if (userClass >= 450 && userClass < 501) {
                        suggestDetailActivity_imageView_userClass.setImageResource(R.drawable.q_class_blue_2);
                    }
                } catch (Exception e) {
                    suggestDetailActivity_imageView_userClass.setImageResource(R.drawable.q_class_null);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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


    //댓글수 추가 (replyCount)
    private void onReplyClicked(final DatabaseReference postRef) {
        final String date = getDate();
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                SuggestDTO suggestDTO = mutableData.getValue(SuggestDTO.class);
                if (suggestDTO == null) {
                    return Transaction.success(mutableData);
                }
//                contentDTO.reply.put(date, pollActivity_editText_reply.getText().toString());
                mutableData.setValue(suggestDTO);
                firebaseDatabase.getReference().child("suggestReply").child(postRef.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        firebaseDatabase.getReference().child("suggest").child(postRef.getKey()).child("replyCount").setValue(dataSnapshot.getChildrenCount());
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


            }
        });
    }

    private void likeClick() {
        final String suggestKey = getIntent().getStringExtra("suggestKey");
        firebaseDatabase.getReference().child("suggest").child(suggestKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                SuggestDTO suggestDTO = dataSnapshot.getValue(SuggestDTO.class);
                if (suggestDTO.uid.equals(mAuth.getCurrentUser().getUid())) {
                    Toast toast = Toast.makeText(getApplicationContext(), suggestDTO.userID + "님의 글 입니다.", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();
                } else {
                    onLikeClicked(firebaseDatabase.getReference().child("suggest").child(suggestKey));
                }

                likeFirebaseDatabase.getReference().child("suggest").child(suggestKey).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        SuggestDTO suggestDTO_ = dataSnapshot.getValue(SuggestDTO.class);
                        if (suggestDTO_.likes.containsKey(mAuth.getCurrentUser().getUid())) {
                            suggestDetailActivity_imageView_like.setImageResource(R.drawable.ic_suggest_up_fill);
                            suggestDetailActivity_textView_likeCount.setText(String.valueOf(suggestDTO_.likeCount));
                        } else {
                            suggestDetailActivity_imageView_like.setImageResource(R.drawable.ic_suggest_up);
                            suggestDetailActivity_textView_likeCount.setText(String.valueOf(suggestDTO_.likeCount));
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


    //    게시물 좋아요 클릭 (따봉 이미지)
    private void onLikeClicked(final DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                SuggestDTO suggestDTO = mutableData.getValue(SuggestDTO.class);

                if (suggestDTO == null) {
                    return Transaction.success(mutableData);
                }
                //좋아요 누른 이력 있으면 지우고 카운트-1
                if (suggestDTO.likes.containsKey(mAuth.getCurrentUser().getUid())) {
                    suggestDTO.likeCount = suggestDTO.likeCount - 1;
                    suggestDTO.likes.remove(mAuth.getCurrentUser().getUid());

                    // users/내uid/likeContent/컨텐트key : false      : 좋아요 누른 컨텐츠 리스트 false
                    firebaseDatabase.getReference()
                            .child("users")
                            .child(mAuth.getCurrentUser().getUid())
                            .child("suggestLike")
                            .child(suggestDTO.getSuggestKey())
                            .setValue("false");

                    //좋아요 누른 이력 없으면 더하고 카운트+1
                } else {
                    suggestDTO.likeCount = suggestDTO.likeCount + 1;
                    suggestDTO.likes.put(mAuth.getCurrentUser().getUid(), true);

                    // users/내uid/likeContent/컨텐트key : trud      : 좋아요 누른 컨텐츠 리스트 true
                    firebaseDatabase.getReference()
                            .child("users")
                            .child(mAuth.getCurrentUser().getUid())
                            .child("suggestLike")
                            .child(suggestDTO.getSuggestKey())
                            .setValue("true");

                }

                // Set value and report transaction success
                mutableData.setValue(suggestDTO);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                // Transaction completed
                Log.d("lkjlkj", "postTransaction:onComplete:" + databaseError);


            }
        });
    }


}
