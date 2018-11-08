package com.n4u1.AQA.AQA.views;

import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.n4u1.AQA.AQA.R;
import com.n4u1.AQA.AQA.dialog.NotEmailDialog;
import com.n4u1.AQA.AQA.dialog.NullEmailDialog;
import com.n4u1.AQA.AQA.models.ContentDTO;
import com.n4u1.AQA.AQA.models.User;

import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateUserEmailActivity extends AppCompatActivity {


    private DatabaseReference mRef;
    private FirebaseUser mUser;
    EditText createUserEmail_editText_email;
    TextView createUserEmail_textView_next;
    ImageView createUserEmail_imageView_back;
    String guid;
    String TAG = "lkjlkj ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user_email);

        guid = getIntent().getStringExtra("guid");
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference();
        createUserEmail_textView_next = findViewById(R.id.createUserEmail_textView_next);
        createUserEmail_editText_email = findViewById(R.id.createUserEmail_editText_email);
        createUserEmail_imageView_back = findViewById(R.id.createUserEmail_imageView_back);

        mRef.child("users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());
                Iterator<DataSnapshot> usersIterator = dataSnapshot.getChildren().iterator();
                while (usersIterator.hasNext()) {
//                    User user = usersIterator.next().getValue(User.class);
//
//                    Map<String, Object> user = (Map<String, Object>) usersIterator.getValue();
////                    Map<String, Object> user = (Map<String,Object>) UsersIterator.next().getValue(User.class);
////                    Map<String, Object> user = (Map<String,Object>)usersIterator.next().getChildren();
//
//                    if (user.getGuid().contains(guid)) {
//                        Log.d(TAG, "guid equal");
//                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException());
            }
        });



        //다음 클릭
        createUserEmail_textView_next.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    createUserEmail_textView_next.setTextColor(0xFF88B6E7);
                }
                if (MotionEvent.ACTION_UP == event.getAction()) {
                    createUserEmail_textView_next.setTextColor(0xff4485c9);
                    if (createUserEmail_editText_email.getText().toString().isEmpty()) {
                        NullEmailDialog nullEmailDialog = new NullEmailDialog();
                        nullEmailDialog.show(getSupportFragmentManager(), "nullEmailDialog");
                    } else if (!checkEmail(createUserEmail_editText_email.getText().toString())) {
                        NotEmailDialog notEmailDialog = new NotEmailDialog();
                        notEmailDialog.show(getSupportFragmentManager(), "notEmailDialog");
                    } else {
                        String email = createUserEmail_editText_email.getText().toString();
                        Intent intent = new Intent(CreateUserEmailActivity.this, CreateUserIdActivity.class);
                        intent.putExtra("email", email);
                        startActivity(intent);
                    }


                }
                return true;

            }
        });


        //뒤로 클릭
        createUserEmail_imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

    private boolean checkEmail(String inputUserEmail) {
        String regex = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(inputUserEmail);
        boolean isNormal = m.matches();
        return isNormal;
    }
}
