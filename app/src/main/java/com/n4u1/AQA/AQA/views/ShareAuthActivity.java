package com.n4u1.AQA.AQA.views;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.n4u1.AQA.AQA.R;
import com.n4u1.AQA.AQA.dialog.ShareAuthInfoDialog;
import com.n4u1.AQA.AQA.dialog.ShareDialog;
import com.n4u1.AQA.AQA.models.ShareAuthDTO;
import com.n4u1.AQA.AQA.models.SuggestDTO;
import com.n4u1.AQA.AQA.recyclerview.ShareAuthAdapter;
import com.n4u1.AQA.AQA.recyclerview.SuggestAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class ShareAuthActivity extends AppCompatActivity {

    FirebaseUser mUser;
    DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;
    String userId = "";
    final ArrayList<ShareAuthDTO> shareAuthDTOS = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_auth);


        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(null);
            getSupportActionBar().setSubtitle("공유 인증 하기");
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        mDatabase = FirebaseDatabase.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        TextView shareAuthActivity_textView_shareInfo = findViewById(R.id.shareAuthActivity_textView_shareInfo);
        final SwipeRefreshLayout mSwipeRefreshLayout = findViewById(R.id.swipeRFL);
        final RecyclerView shareAuthActivity_recyclerView = findViewById(R.id.shareAuthActivity_recyclerView);
        FloatingActionButton shareAuthActivity_fab_addContent = findViewById(R.id.shareAuthActivity_fab_addContent);
        final ShareAuthAdapter shareAuthAdapter = new ShareAuthAdapter(this, shareAuthDTOS, shareAuthActivity_recyclerView);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLayoutManager.isSmoothScrollbarEnabled();
        mLayoutManager.setStackFromEnd(true);
        shareAuthActivity_recyclerView.setLayoutManager(mLayoutManager);

        shareAuthActivity_recyclerView.setAdapter(shareAuthAdapter);
        shareAuthAdapter.notifyDataSetChanged();


        //onCreate시 액티비티 최초1회 바인딩
        mDatabase.getReference().child("shareAuth").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                shareAuthDTOS.clear();
                ArrayList<ShareAuthDTO> shareAuthDTOTemp = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ShareAuthDTO shareAuthDTO = snapshot.getValue(ShareAuthDTO.class);
                    shareAuthDTOTemp.add(shareAuthDTO);
                }
                Collections.reverse(shareAuthDTOTemp);
                for (int i = 0; i < shareAuthDTOTemp.size(); i++) {
                    try {
                        shareAuthDTOS.add(shareAuthDTOTemp.get(i));
                    } catch (Exception e) {

                    }
                }
                shareAuthAdapter.notifyDataSetChanged();
                shareAuthActivity_recyclerView.scrollToPosition(0);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //공유 인증 하는방법
        shareAuthActivity_textView_shareInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareAuthInfoDialog shareAuthInfoDialog = new ShareAuthInfoDialog();
                shareAuthInfoDialog.show(getSupportFragmentManager(), "shareAuthInfoDialog");
            }
        });

        //userId 구하기
        mDatabaseReference.child("users").child(mUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Object> user = (Map<String, Object>) dataSnapshot.getValue();
                userId = user.get("userId").toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //Pull to Refresh 당겨서 새로고침
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mDatabase.getReference().child("shareAuth").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        shareAuthDTOS.clear();
                        ArrayList<ShareAuthDTO> shareAuthDTOSTemp = new ArrayList<>();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            ShareAuthDTO shareAuthDTO = snapshot.getValue(ShareAuthDTO.class);
                            shareAuthDTOSTemp.add(shareAuthDTO);
                        }

                        Collections.reverse(shareAuthDTOSTemp);

                        for (int i = 0; i < shareAuthDTOSTemp.size(); i++) {
                            try {
                                shareAuthDTOS.add(shareAuthDTOSTemp.get(i));
                            } catch (Exception e) {

                            }
                        }
                        shareAuthAdapter.notifyDataSetChanged();
                        shareAuthActivity_recyclerView.scrollToPosition(0);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });


        //공유인증하기 글쓰기 클릭 floating action button
        shareAuthActivity_fab_addContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShareAuthActivity.this, UserShareAuthUploadActivity.class);
                intent.putExtra("shareAuthUserId", userId);
                startActivity(intent);
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share_auth_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int curId = item.getItemId();
        switch (curId) {
            case R.id.menu_share:
//                    real 버전
                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                intent.setType("text/plain");
//                     Set default text message
//                     카톡, 이메일, MMS 다 이걸로 설정 가능
//                    String subject = "문자의 제목";
                String text = "AQA\nFunny Trustly Purely\n골라봐!\nhttps://play.google.com/apps/testing/com.n4u1.AQA.AQA";
//                    intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                intent.putExtra(Intent.EXTRA_TEXT, text);
//                     Title of intent
                    Intent chooser = Intent.createChooser(intent, "공유하기");
                    startActivity(chooser);
                break;

            case R.id.menu_home:
                Intent intentAqa = new Intent(ShareAuthActivity.this, HomeActivity.class);
                overridePendingTransition(0, 0);
                intentAqa.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                overridePendingTransition(0, 0);
                startActivity(intentAqa);
                break;

            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
