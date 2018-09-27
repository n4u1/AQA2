package com.n4u1.AQA.AQA.views;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.n4u1.AQA.AQA.R;
import com.n4u1.AQA.AQA.models.ContentDTO;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PollActivity extends AppCompatActivity {

    DatabaseReference mDatabaseReference;

    TextView pollActivity_textView_title, pollActivity_textView_description,
            pollActivity_textView_pollMode, pollActivity_textView_contentType, pollActivity_textView_date;
    ImageView pollActivity_imageView_userAddContent_1, pollActivity_imageView_userAddContent_2,
            pollActivity_imageView_userAddContent_3, pollActivity_imageView_userAddContent_4,
            pollActivity_imageView_userAddContent_5, pollActivity_imageView_userAddContent_6,
            pollActivity_imageView_userAddContent_7, pollActivity_imageView_userAddContent_8,
            pollActivity_imageView_userAddContent_9, pollActivity_imageView_userAddContent_10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poll);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
//        if (getSupportActionBar() != null){
//            getSupportActionBar().setTitle("  AQA");
//        }
        getSupportActionBar().setIcon(R.mipmap.ic_q_custom);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        String contentKey = getIntent().getStringExtra("contentKey");
//        pollActivity_textView_title.setText(contentKey);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("user_contents").child(contentKey);

        pollActivity_textView_title = findViewById(R.id.pollActivity_textView_title);
        pollActivity_textView_description = findViewById(R.id.pollActivity_textView_description);
        pollActivity_textView_contentType = findViewById(R.id.pollActivity_textView_contentType);
        pollActivity_textView_pollMode = findViewById(R.id.pollActivity_textView_pollMode);
        pollActivity_textView_date = findViewById(R.id.pollActivity_textView_date);

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


        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ContentDTO contentDTO = dataSnapshot.getValue(ContentDTO.class);
                contentDTO.getDescription();
                int itemViewCount = contentDTO.getItemViewType();
                        pollActivity_textView_date.setText(contentDTO.getUploadDate());
                pollActivity_textView_title.setText(contentDTO.getTitle());
                pollActivity_textView_contentType.setText(contentDTO.getContentType());
                pollActivity_textView_description.setText(contentDTO.getDescription());
                pollActivity_textView_pollMode.setText(contentDTO.getPollMode());
//                pollActivity_textView_date.setText(contentDTO.getItemViewType());
                switch (itemViewCount) {
                    case 1 :
                        pollActivity_imageView_userAddContent_1.setVisibility(View.VISIBLE);
                        Glide.with(getApplicationContext()).load(contentDTO.getImageUrl_0()).into(pollActivity_imageView_userAddContent_1).getView();
                        break;

                    case 2 :
                        pollActivity_imageView_userAddContent_1.setVisibility(View.VISIBLE);
                        pollActivity_imageView_userAddContent_2.setVisibility(View.VISIBLE);
                        Glide.with(getApplicationContext()).load(contentDTO.getImageUrl_0()).into(pollActivity_imageView_userAddContent_1).getView();
                        Glide.with(getApplicationContext()).load(contentDTO.getImageUrl_1()).into(pollActivity_imageView_userAddContent_2).getView();
                        break;
                    case 3 :
                        pollActivity_imageView_userAddContent_1.setVisibility(View.VISIBLE);
                        pollActivity_imageView_userAddContent_2.setVisibility(View.VISIBLE);
                        pollActivity_imageView_userAddContent_3.setVisibility(View.VISIBLE);
                        Glide.with(getApplicationContext()).load(contentDTO.getImageUrl_0()).into(pollActivity_imageView_userAddContent_1).getView();
                        Glide.with(getApplicationContext()).load(contentDTO.getImageUrl_1()).into(pollActivity_imageView_userAddContent_2).getView();
                        Glide.with(getApplicationContext()).load(contentDTO.getImageUrl_2()).into(pollActivity_imageView_userAddContent_3).getView();
                        break;
                    case 4 :
                        pollActivity_imageView_userAddContent_1.setVisibility(View.VISIBLE);
                        pollActivity_imageView_userAddContent_2.setVisibility(View.VISIBLE);
                        pollActivity_imageView_userAddContent_3.setVisibility(View.VISIBLE);
                        pollActivity_imageView_userAddContent_4.setVisibility(View.VISIBLE);
                        Glide.with(getApplicationContext()).load(contentDTO.getImageUrl_0()).into(pollActivity_imageView_userAddContent_1).getView();
                        Glide.with(getApplicationContext()).load(contentDTO.getImageUrl_1()).into(pollActivity_imageView_userAddContent_2).getView();
                        Glide.with(getApplicationContext()).load(contentDTO.getImageUrl_2()).into(pollActivity_imageView_userAddContent_3).getView();
                        Glide.with(getApplicationContext()).load(contentDTO.getImageUrl_3()).into(pollActivity_imageView_userAddContent_4).getView();
                        break;
                    case 5:
                        pollActivity_imageView_userAddContent_1.setVisibility(View.VISIBLE);
                        pollActivity_imageView_userAddContent_2.setVisibility(View.VISIBLE);
                        pollActivity_imageView_userAddContent_3.setVisibility(View.VISIBLE);
                        pollActivity_imageView_userAddContent_4.setVisibility(View.VISIBLE);
                        pollActivity_imageView_userAddContent_5.setVisibility(View.VISIBLE);
                        Glide.with(getApplicationContext()).load(contentDTO.getImageUrl_0()).into(pollActivity_imageView_userAddContent_1).getView();
                        Glide.with(getApplicationContext()).load(contentDTO.getImageUrl_1()).into(pollActivity_imageView_userAddContent_2).getView();
                        Glide.with(getApplicationContext()).load(contentDTO.getImageUrl_2()).into(pollActivity_imageView_userAddContent_3).getView();
                        Glide.with(getApplicationContext()).load(contentDTO.getImageUrl_3()).into(pollActivity_imageView_userAddContent_4).getView();
                        Glide.with(getApplicationContext()).load(contentDTO.getImageUrl_4()).into(pollActivity_imageView_userAddContent_5).getView();
                        break;
                    case 6 :
                        pollActivity_imageView_userAddContent_1.setVisibility(View.VISIBLE);
                        pollActivity_imageView_userAddContent_2.setVisibility(View.VISIBLE);
                        pollActivity_imageView_userAddContent_3.setVisibility(View.VISIBLE);
                        pollActivity_imageView_userAddContent_4.setVisibility(View.VISIBLE);
                        pollActivity_imageView_userAddContent_5.setVisibility(View.VISIBLE);
                        pollActivity_imageView_userAddContent_6.setVisibility(View.VISIBLE);
                        Glide.with(getApplicationContext()).load(contentDTO.getImageUrl_0()).into(pollActivity_imageView_userAddContent_1).getView();
                        Glide.with(getApplicationContext()).load(contentDTO.getImageUrl_1()).into(pollActivity_imageView_userAddContent_2).getView();
                        Glide.with(getApplicationContext()).load(contentDTO.getImageUrl_2()).into(pollActivity_imageView_userAddContent_3).getView();
                        Glide.with(getApplicationContext()).load(contentDTO.getImageUrl_3()).into(pollActivity_imageView_userAddContent_4).getView();
                        Glide.with(getApplicationContext()).load(contentDTO.getImageUrl_4()).into(pollActivity_imageView_userAddContent_5).getView();
                        Glide.with(getApplicationContext()).load(contentDTO.getImageUrl_5()).into(pollActivity_imageView_userAddContent_6).getView();
                        break;
                    case 7 :
                        pollActivity_imageView_userAddContent_1.setVisibility(View.VISIBLE);
                        pollActivity_imageView_userAddContent_2.setVisibility(View.VISIBLE);
                        pollActivity_imageView_userAddContent_3.setVisibility(View.VISIBLE);
                        pollActivity_imageView_userAddContent_4.setVisibility(View.VISIBLE);
                        pollActivity_imageView_userAddContent_5.setVisibility(View.VISIBLE);
                        pollActivity_imageView_userAddContent_6.setVisibility(View.VISIBLE);
                        pollActivity_imageView_userAddContent_7.setVisibility(View.VISIBLE);
                        Glide.with(getApplicationContext()).load(contentDTO.getImageUrl_0()).into(pollActivity_imageView_userAddContent_1).getView();
                        Glide.with(getApplicationContext()).load(contentDTO.getImageUrl_1()).into(pollActivity_imageView_userAddContent_2).getView();
                        Glide.with(getApplicationContext()).load(contentDTO.getImageUrl_2()).into(pollActivity_imageView_userAddContent_3).getView();
                        Glide.with(getApplicationContext()).load(contentDTO.getImageUrl_3()).into(pollActivity_imageView_userAddContent_4).getView();
                        Glide.with(getApplicationContext()).load(contentDTO.getImageUrl_4()).into(pollActivity_imageView_userAddContent_5).getView();
                        Glide.with(getApplicationContext()).load(contentDTO.getImageUrl_5()).into(pollActivity_imageView_userAddContent_6).getView();
                        Glide.with(getApplicationContext()).load(contentDTO.getImageUrl_6()).into(pollActivity_imageView_userAddContent_7).getView();
                        break;
                    case 8 :
                        pollActivity_imageView_userAddContent_1.setVisibility(View.VISIBLE);
                        pollActivity_imageView_userAddContent_2.setVisibility(View.VISIBLE);
                        pollActivity_imageView_userAddContent_3.setVisibility(View.VISIBLE);
                        pollActivity_imageView_userAddContent_4.setVisibility(View.VISIBLE);
                        pollActivity_imageView_userAddContent_5.setVisibility(View.VISIBLE);
                        pollActivity_imageView_userAddContent_6.setVisibility(View.VISIBLE);
                        pollActivity_imageView_userAddContent_7.setVisibility(View.VISIBLE);
                        pollActivity_imageView_userAddContent_8.setVisibility(View.VISIBLE);
                        Glide.with(getApplicationContext()).load(contentDTO.getImageUrl_0()).into(pollActivity_imageView_userAddContent_1).getView();
                        Glide.with(getApplicationContext()).load(contentDTO.getImageUrl_1()).into(pollActivity_imageView_userAddContent_2).getView();
                        Glide.with(getApplicationContext()).load(contentDTO.getImageUrl_2()).into(pollActivity_imageView_userAddContent_3).getView();
                        Glide.with(getApplicationContext()).load(contentDTO.getImageUrl_3()).into(pollActivity_imageView_userAddContent_4).getView();
                        Glide.with(getApplicationContext()).load(contentDTO.getImageUrl_4()).into(pollActivity_imageView_userAddContent_5).getView();
                        Glide.with(getApplicationContext()).load(contentDTO.getImageUrl_5()).into(pollActivity_imageView_userAddContent_6).getView();
                        Glide.with(getApplicationContext()).load(contentDTO.getImageUrl_6()).into(pollActivity_imageView_userAddContent_7).getView();
                        Glide.with(getApplicationContext()).load(contentDTO.getImageUrl_7()).into(pollActivity_imageView_userAddContent_8).getView();
                        break;
                    case 9 :
                        pollActivity_imageView_userAddContent_1.setVisibility(View.VISIBLE);
                        pollActivity_imageView_userAddContent_2.setVisibility(View.VISIBLE);
                        pollActivity_imageView_userAddContent_3.setVisibility(View.VISIBLE);
                        pollActivity_imageView_userAddContent_4.setVisibility(View.VISIBLE);
                        pollActivity_imageView_userAddContent_5.setVisibility(View.VISIBLE);
                        pollActivity_imageView_userAddContent_6.setVisibility(View.VISIBLE);
                        pollActivity_imageView_userAddContent_7.setVisibility(View.VISIBLE);
                        pollActivity_imageView_userAddContent_8.setVisibility(View.VISIBLE);
                        pollActivity_imageView_userAddContent_9.setVisibility(View.VISIBLE);
                        Glide.with(getApplicationContext()).load(contentDTO.getImageUrl_0()).into(pollActivity_imageView_userAddContent_1).getView();
                        Glide.with(getApplicationContext()).load(contentDTO.getImageUrl_1()).into(pollActivity_imageView_userAddContent_2).getView();
                        Glide.with(getApplicationContext()).load(contentDTO.getImageUrl_2()).into(pollActivity_imageView_userAddContent_3).getView();
                        Glide.with(getApplicationContext()).load(contentDTO.getImageUrl_3()).into(pollActivity_imageView_userAddContent_4).getView();
                        Glide.with(getApplicationContext()).load(contentDTO.getImageUrl_4()).into(pollActivity_imageView_userAddContent_5).getView();
                        Glide.with(getApplicationContext()).load(contentDTO.getImageUrl_5()).into(pollActivity_imageView_userAddContent_6).getView();
                        Glide.with(getApplicationContext()).load(contentDTO.getImageUrl_6()).into(pollActivity_imageView_userAddContent_7).getView();
                        Glide.with(getApplicationContext()).load(contentDTO.getImageUrl_7()).into(pollActivity_imageView_userAddContent_8).getView();
                        Glide.with(getApplicationContext()).load(contentDTO.getImageUrl_8()).into(pollActivity_imageView_userAddContent_9).getView();
                        break;
                    case 10 :
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
                        Glide.with(getApplicationContext()).load(contentDTO.getImageUrl_0()).into(pollActivity_imageView_userAddContent_1).getView();
                        Glide.with(getApplicationContext()).load(contentDTO.getImageUrl_1()).into(pollActivity_imageView_userAddContent_2).getView();
                        Glide.with(getApplicationContext()).load(contentDTO.getImageUrl_2()).into(pollActivity_imageView_userAddContent_3).getView();
                        Glide.with(getApplicationContext()).load(contentDTO.getImageUrl_3()).into(pollActivity_imageView_userAddContent_4).getView();
                        Glide.with(getApplicationContext()).load(contentDTO.getImageUrl_4()).into(pollActivity_imageView_userAddContent_5).getView();
                        Glide.with(getApplicationContext()).load(contentDTO.getImageUrl_5()).into(pollActivity_imageView_userAddContent_6).getView();
                        Glide.with(getApplicationContext()).load(contentDTO.getImageUrl_6()).into(pollActivity_imageView_userAddContent_7).getView();
                        Glide.with(getApplicationContext()).load(contentDTO.getImageUrl_7()).into(pollActivity_imageView_userAddContent_8).getView();
                        Glide.with(getApplicationContext()).load(contentDTO.getImageUrl_8()).into(pollActivity_imageView_userAddContent_9).getView();
                        Glide.with(getApplicationContext()).load(contentDTO.getImageUrl_9()).into(pollActivity_imageView_userAddContent_10).getView();
                        break;
                }
                Glide.with(getApplicationContext())
                        .load(contentDTO.getImageUrl_0())
                        .into(pollActivity_imageView_userAddContent_1)
                        .getView();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
}
