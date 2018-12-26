package com.n4u1.AQA.AQA.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.n4u1.AQA.AQA.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.n4u1.AQA.AQA.admin.AdminContentsActivity;
import com.n4u1.AQA.AQA.admin.AdminEtcActivity;
import com.n4u1.AQA.AQA.dialog.FindEmailDialog;
import com.n4u1.AQA.AQA.dialog.SignOutDialog;
import com.n4u1.AQA.AQA.dialog.LogOutDialog;
import com.n4u1.AQA.AQA.models.User;

import java.util.Map;

public class MineActivity extends AppCompatActivity implements LogOutDialog.LogOutDialogListener,
        SignOutDialog.SignOutDialogListener, FindEmailDialog.FindEmailDialogListener {

    FirebaseUser mFireBaseUser;
    DatabaseReference mDatabaseReference;
    DatabaseReference mDatabaseReferenceAdmin;

    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

    String adminAuthId = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        setContentView(R.layout.activity_mine);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(null);
            getSupportActionBar().setSubtitle("설 정");
        }

        mFireBaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("users").child(mFireBaseUser.getUid());
        mDatabaseReferenceAdmin = FirebaseDatabase.getInstance().getReference("adminAuth").child("1");

//        AdView adView = findViewById(R.id.adView);
        TextView mineActivity_textView_logOut = findViewById(R.id.mineActivity_textView_logOut);
        final TextView mineActivity_textView_id = findViewById(R.id.mineActivity_textView_id);
        final TextView mineActivity_textView_userClass = findViewById(R.id.mineActivity_textView_userClass);
        final TextView mineActivity_textView_gender = findViewById(R.id.mineActivity_textView_gender);
        final TextView mineActivity_textView_age = findViewById(R.id.mineActivity_textView_age);
        final ImageView mineActivity_imageView_userClass = findViewById(R.id.mineActivity_imageView_userClass);
        final TextView mineActivity_textView_account = findViewById(R.id.mineActivity_textView_account);
        LinearLayout mineActivity_linearLayout_email = findViewById(R.id.mineActivity_linearLayout_email);
        LinearLayout mineActivity_linearLayout_like = findViewById(R.id.mineActivity_linearLayout_like);
        LinearLayout mineActivity_linearLayout_pickContent = findViewById(R.id.mineActivity_linearLayout_pickContent);
        LinearLayout mineActivity_linearLayout_reply = findViewById(R.id.mineActivity_linearLayout_reply);
        LinearLayout mineActivity_linearLayout_upload = findViewById(R.id.mineActivity_linearLayout_upload);
        LinearLayout mineActivity_linearLayout_logOut = findViewById(R.id.mineActivity_linearLayout_logOut);
        LinearLayout mineActivity_linearLayout_authOut = findViewById(R.id.mineActivity_linearLayout_authOut);
        LinearLayout mineActivity_linearLayout_notice = findViewById(R.id.mineActivity_linearLayout_notice);
        LinearLayout mineActivity_linearLayout_suggest = findViewById(R.id.mineActivity_linearLayout_suggest);
        LinearLayout mineActivity_linearLayout_aqa = findViewById(R.id.mineActivity_linearLayout_aqa);
        LinearLayout mineActivity_linearLayout_servicePolicy = findViewById(R.id.mineActivity_linearLayout_servicePolicy);
        LinearLayout mineActivity_linearLayout_privacyPolicy = findViewById(R.id.mineActivity_linearLayout_privacyPolicy);
        LinearLayout mineActivity_linearLayout_version = findViewById(R.id.mineActivity_linearLayout_version);
        LinearLayout mineActivity_linearLayout_userClass = findViewById(R.id.mineActivity_linearLayout_userClass);
        LinearLayout mineActivity_linearLayout_password = findViewById(R.id.mineActivity_linearLayout_password);


        //admin LinearLayout
        final LinearLayout mineActivity_linearLayout_adminEtc = findViewById(R.id.mineActivity_linearLayout_adminEtc);
        final LinearLayout mineActivity_linearLayout_adminContents = findViewById(R.id.mineActivity_linearLayout_adminContents);
        final LinearLayout mineActivity_linearLayout_adminUser = findViewById(R.id.mineActivity_linearLayout_adminUser);


        //admin 페이지 활성화
        mDatabaseReferenceAdmin.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (int i = 0; i < dataSnapshot.getChildrenCount(); i++) {
                    Map<String, Boolean> adminMap = (Map<String, Boolean>) dataSnapshot.getValue();
                    adminAuthId = adminMap.keySet().toString();
                }
                mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            Map<String, Object> users = (Map<String, Object>) dataSnapshot.getValue();
                            if (adminAuthId.contains(String.valueOf(users.get("userId")))) {
                                mineActivity_linearLayout_adminUser.setVisibility(View.VISIBLE);
                                mineActivity_linearLayout_adminContents.setVisibility(View.VISIBLE);
                                mineActivity_linearLayout_adminEtc.setVisibility(View.VISIBLE);
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

        mineActivity_linearLayout_adminContents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MineActivity.this, AdminContentsActivity.class);
                startActivity(intent);
            }
        });
        mineActivity_linearLayout_adminEtc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MineActivity.this, AdminEtcActivity.class);
                startActivity(intent);
            }
        });


        //이메일 가져오기
        if (mUser.isAnonymous()) {
            mineActivity_textView_account.setText("비회원");
        } else {
            mDatabaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Map<String, Object> user = (Map<String, Object>) dataSnapshot.getValue();
                    mineActivity_textView_account.setText(String.valueOf(user.get("email")));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }



        //이메일 변경하기
        mineActivity_linearLayout_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mUser.isAnonymous()) {
                    Intent intent = new Intent(MineActivity.this, ChangeEmailActivity.class);
                    startActivity(intent);
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "회원가입을 해야합니다.", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();
                }

            }
        });

        //성별 나이 아이디 가져오기
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@Nullable DataSnapshot dataSnapshot) {
                try {
                    if (mUser.isAnonymous()) {
                        mineActivity_textView_gender.setText("비회원");
                        mineActivity_textView_age.setText("비회원");
                        mineActivity_textView_id.setText("비회원");
                    } else {
                        Map<String, Object> users = (Map<String, Object>) dataSnapshot.getValue();
                        mineActivity_textView_gender.setText(String.valueOf(users.get("sex")));
                        mineActivity_textView_age.setText(String.valueOf(users.get("age")));
                        mineActivity_textView_id.setText(String.valueOf(users.get("userId")));
                    }

                } catch (Exception e) {

                }


            }

            @Override
            public void onCancelled(@Nullable DatabaseError databaseError) {

            }
        });

        //Q포인트 점수,
        mDatabaseReference.child("userClass").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@Nullable DataSnapshot dataSnapshot) {
                try {
                    int userClass = Integer.parseInt(dataSnapshot.getValue().toString());
                    mineActivity_textView_userClass.setText(String.valueOf(userClass));
                    if (userClass >= 0 && userClass < 50) {
                        mineActivity_imageView_userClass.setImageResource(R.drawable.q_class_red_2);
                    } else if (userClass >= 50 && userClass < 100) {
                        mineActivity_imageView_userClass.setImageResource(R.drawable.q_class_red_1);
                    } else if (userClass >= 100 && userClass < 150) {
                        mineActivity_imageView_userClass.setImageResource(R.drawable.q_class_orange_1);
                    } else if (userClass >= 150 && userClass < 200) {
                        mineActivity_imageView_userClass.setImageResource(R.drawable.q_class_orange_2);
                    } else if (userClass >= 200 && userClass < 250) {
                        mineActivity_imageView_userClass.setImageResource(R.drawable.q_class_yellow_1);
                    } else if (userClass >= 250 && userClass < 300) {
                        mineActivity_imageView_userClass.setImageResource(R.drawable.q_class_yellow_2);
                    } else if (userClass >= 300 && userClass < 350) {
                        mineActivity_imageView_userClass.setImageResource(R.drawable.q_class_green_1);
                    } else if (userClass >= 350 && userClass < 400) {
                        mineActivity_imageView_userClass.setImageResource(R.drawable.q_class_green_2);
                    } else if (userClass >= 400 && userClass < 450) {
                        mineActivity_imageView_userClass.setImageResource(R.drawable.q_class_blue_2);
                    } else if (userClass >= 450 && userClass < 501) {
                        mineActivity_imageView_userClass.setImageResource(R.drawable.q_class_blue_2);
                    } else if (userClass >= 501) {
                        mineActivity_imageView_userClass.setImageResource(R.drawable.q_class_black);
                    }
                } catch (Exception e) {
                    mineActivity_imageView_userClass.setImageResource(R.drawable.ic_aqa_qw);
                    mineActivity_textView_userClass.setText("비회원");
                }

            }

            @Override
            public void onCancelled(@Nullable DatabaseError databaseError) {

            }
        });


        //Q포인트 점수 알아보기
        mineActivity_linearLayout_userClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MineActivity.this, UserClassInfoActivity.class);
                startActivity(intent);
            }
        });


        //공지사항
        mineActivity_linearLayout_notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast toast = Toast.makeText(getApplicationContext(), "아직 공지사항이 없습니다 :)", Toast.LENGTH_SHORT);
//                toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
//                toast.show();
                Intent intent = new Intent(MineActivity.this, NoticeActivity.class);
                startActivity(intent);
            }
        });

        //aqa...
        mineActivity_linearLayout_aqa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MineActivity.this, AqaIsActivity.class);
                startActivity(intent);
            }
        });


        //건의사항
        mineActivity_linearLayout_suggest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MineActivity.this, SuggestActivity.class);
                startActivity(intent);
            }
        });


        //버전 확인
        mineActivity_linearLayout_version.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MineActivity.this, VersionInfoActivity.class);
                startActivity(intent);
            }
        });


        //비밀번호 변경하기
        mineActivity_linearLayout_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUser.isAnonymous()) {
                    Toast toast = Toast.makeText(getApplicationContext(), "회원가입을 해야합니다.", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();
                } else {
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    String emailAddress = auth.getCurrentUser().getEmail();
                    FindEmailDialog findEmailDialog = FindEmailDialog.newInstance(emailAddress);
                    findEmailDialog.show(getSupportFragmentManager(), "findEmailDialog");
                }


            }
        });


        //노티 스위치 상태
//        if (mineActivity_switch_noti.getTextOn().toString().equals("on")) {
//            mineActivity_textView_noti.setText("알람(사용중)");
//        } else {
//            mineActivity_textView_noti.setText("알람(미사용중)");
//        }


        //좋아요 누른 게시물 모아보기
        mineActivity_linearLayout_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MineActivity.this, MyLikeContentsActivity.class);
                startActivity(intent);
            }
        });

        //참여한 게시물 모아보기
        mineActivity_linearLayout_pickContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MineActivity.this, MyPollActivity.class);
                startActivity(intent);
            }
        });

        //댓글 남긴 게시물 모아보기
        mineActivity_linearLayout_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MineActivity.this, MyReplyContentsActivity.class);
                startActivity(intent);
            }
        });

        //내가 올린 게시물 모아보기
        mineActivity_linearLayout_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MineActivity.this, MyUploadActivity.class);
                startActivity(intent);
            }
        });


        //이용약관
        mineActivity_linearLayout_servicePolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://lkj840211.wixsite.com/aqacompany/privacypolicy"));
                startActivity(intent);
            }
        });


        //개인정보처리방침
        mineActivity_linearLayout_privacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://lkj840211.wixsite.com/aqacompany/servicepolicy"));
                startActivity(intent);
            }
        });


        //로그 아웃 버튼
        if (mUser.isAnonymous()) {
            mineActivity_textView_logOut.setText("회원가입");
        }
        mineActivity_linearLayout_logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUser.isAnonymous()) {
                    Intent intent = new Intent(MineActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    LogOutDialog logOutDialog = new LogOutDialog();
                    logOutDialog.show(getSupportFragmentManager(), "logOutDialog");
                }


            }
        });

        //회원 탈퇴 버튼
        if (mUser.isAnonymous()) {
            mineActivity_linearLayout_authOut.setVisibility(View.GONE);
        }
        mineActivity_linearLayout_authOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignOutDialog authOutDialog = new SignOutDialog();
                authOutDialog.show(getSupportFragmentManager(), "authOutDialog");

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mine_menu, menu);
        return true;
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int curId = item.getItemId();
        switch (curId) {
            case R.id.menu_home:
                Intent intentHome = new Intent(MineActivity.this, HomeActivity.class);
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

    @Override
    public void LogOutDialogCallback(String string) {
        if (string.equals("확인")) {
            FirebaseAuth.getInstance().signOut();
            SharedPreferences pref = getSharedPreferences("com.n4u1.AQA", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("com.n4u1.AQA.fireBaseUserEmail", "a");
            editor.putString("com.n4u1.AQA.fireBaseUserPassword", "b");
            editor.commit();
            Intent intent = new Intent(MineActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            startActivity(intent);

        }
    }

    @Override
    public void SignOutDialogCallback(String string) {
        if (string.equals("탈퇴하기")) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            DatabaseReference authDatabaseRef = FirebaseDatabase.getInstance().getReference();

            authDatabaseRef.child("users").child(user.getUid()).removeValue();

            SharedPreferences pref = getSharedPreferences("com.n4u1.AQA", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("com.n4u1.AQA.fireBaseUserEmail", null);
            editor.putString("com.n4u1.AQA.fireBaseUserPassword", null);
            editor.commit();


            user.delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("lkj authout", "User account deleted.");
                            }
                        }
                    });


            Intent intent = new Intent(MineActivity.this, LoginActivity.class);
            startActivity(intent);
        }

    }

    @Override
    public void FindEmailDialogCallback(String string) {
        if (string.equals("확인")) {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            String emailAddress = auth.getCurrentUser().getEmail();
            auth.sendPasswordResetEmail(emailAddress)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("lkjpassword", "lkjpassword Success");
                            } else {
                                Log.d("lkjpassword", "lkjpassword Fail");
                            }
                        }
                    });

        }
    }
}
