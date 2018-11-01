package com.n4u1.AQA.AQA.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
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
import com.n4u1.AQA.AQA.models.ContentDTO;
import com.n4u1.AQA.AQA.models.ReplyDTO;
import com.n4u1.AQA.AQA.models.SuggestDTO;
import com.n4u1.AQA.AQA.util.GlideApp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class SuggestDetailActivity extends AppCompatActivity {

    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private String replyKey;


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
        setContentView(R.layout.activity_suggest_detail);


        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(null);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        final String suggestKey = getIntent().getStringExtra("suggestKey");
        firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();


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



        //suggestDTO 화면 초기세팅
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mDatabaseReference.child("suggest").child(suggestKey).addListenerForSingleValueEvent(new ValueEventListener() {
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


                if (suggestDTO.getImageUrl_1() != null) {
                    suggestDetailActivity_imageView_userAddContent_2.setVisibility(View.VISIBLE);
                    GlideApp.with(getApplicationContext()).load(suggestDTO.getImageUrl_1()).centerCrop().thumbnail(Glide.with(getApplicationContext()).load(R.drawable.loadingicon)).into(suggestDetailActivity_imageView_userAddContent_1).getView();
                }
                if (suggestDTO.getImageUrl_2() != null) {
                    suggestDetailActivity_imageView_userAddContent_1.setVisibility(View.VISIBLE);
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
//                likeClick();
            }
        });
        //따봉버튼 클릭리스너,  숫자 클릭
        suggestDetailActivity_textView_likeCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                likeClick();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int curId = item.getItemId();
        switch (curId) {
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
                int userClass = Integer.parseInt(dataSnapshot.getValue().toString());
                if (userClass >= 0 && userClass < 50) {
                    suggestDetailActivity_imageView_userClass.setImageResource(R.drawable.q_class_red_1);
                } else if (userClass >= 50 && userClass < 100) {
                    suggestDetailActivity_imageView_userClass.setImageResource(R.drawable.q_class_red_2);
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
                    suggestDetailActivity_imageView_userClass.setImageResource(R.drawable.q_class_blue_1);
                } else if (userClass >= 450 && userClass < 501) {
                    suggestDetailActivity_imageView_userClass.setImageResource(R.drawable.q_class_blue_2);
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


                // Transaction completed

            }
        });
    }

//    private void likeClick () {
//        final String contentKey = getIntent().getStringExtra("contentKey");
//        firebaseDatabase.getReference().child("user_contents").child(contentKey).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                ContentDTO contentDTO = dataSnapshot.getValue(ContentDTO.class);
//                Log.d("lkj contentdto", contentDTO.description);
//                if (contentDTO.uid.equals(auth.getCurrentUser().getUid())) {
//                    Toast toast = Toast.makeText(getApplicationContext(), contentDTO.userID + "님의 투표입니다!", Toast.LENGTH_SHORT);
//                    toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
//                    toast.show();
//                } else {
//                    onLikeClicked(firebaseDatabase.getReference().child("user_contents").child(contentKey));
//                }
//
//                likeFirebaseDatabase.getReference().child("user_contents").child(contentKey).addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        ContentDTO contentDTO_ = dataSnapshot.getValue(ContentDTO.class);
//                        if (contentDTO_.likes.containsKey(auth.getCurrentUser().getUid())) {
//                            pollActivity_imageView_like.setImageResource(R.drawable.ic_thumb_up_blue);
//                            pollActivity_textView_likeCount.setText(String.valueOf(contentDTO_.likeCount));
//                        } else {
//                            pollActivity_imageView_like.setImageResource(R.drawable.ic_outline_thumb_up_24px);
//                            pollActivity_textView_likeCount.setText(String.valueOf(contentDTO_.likeCount));
//                        }
//                    }
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }



    //게시물 좋아요 클릭 (따봉 이미지)
//    private void onLikeClicked(final DatabaseReference postRef) {
//        postRef.runTransaction(new Transaction.Handler() {
//            @Override
//            public Transaction.Result doTransaction(MutableData mutableData) {
//                ContentDTO contentDTO = mutableData.getValue(ContentDTO.class);
//
//                if (contentDTO == null) {
//                    return Transaction.success(mutableData);
//                }
//                //좋아요 누른 이력 있으면 지우고 카운트-1
//                if (contentDTO.likes.containsKey(auth.getCurrentUser().getUid())) {
//                    contentDTO.likeCount = contentDTO.likeCount - 1;
//                    contentDTO.likes.remove(auth.getCurrentUser().getUid());
//
//                    // users/내uid/likeContent/컨텐트key : false      : 좋아요 누른 컨텐츠 리스트 false
//                    firebaseDatabase.getReference()
//                            .child("users")
//                            .child(auth.getCurrentUser().getUid())
//                            .child("likeContent")
//                            .child(contentDTO.getContentKey())
//                            .setValue("false");
//
//                    //좋아요 누른 이력 없으면 더하고 카운트+1
//                } else {
//                    contentDTO.likeCount = contentDTO.likeCount + 1;
//                    contentDTO.likes.put(auth.getCurrentUser().getUid(), true);
//
//                    // users/내uid/likeContent/컨텐트key : trud      : 좋아요 누른 컨텐츠 리스트 true
//                    firebaseDatabase.getReference()
//                            .child("users")
//                            .child(auth.getCurrentUser().getUid())
//                            .child("likeContent")
//                            .child(contentDTO.getContentKey())
//                            .setValue("true");
//
//                }
//
//                // Set value and report transaction success
//                mutableData.setValue(contentDTO);
//                return Transaction.success(mutableData);
//            }
//
//            @Override
//            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
//                // Transaction completed
//                Log.d("lkjlkj", "postTransaction:onComplete:" + databaseError);
//
//
//            }
//        });
//    }




}
