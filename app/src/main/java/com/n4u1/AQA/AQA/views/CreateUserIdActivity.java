package com.n4u1.AQA.AQA.views;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.n4u1.AQA.AQA.R;
import com.n4u1.AQA.AQA.dialog.AgainPasswordDialog;
import com.n4u1.AQA.AQA.dialog.BirthInputFailDialog;
import com.n4u1.AQA.AQA.dialog.ConfirmPasswordFailDialog;
import com.n4u1.AQA.AQA.dialog.LiveEmailDialog;
import com.n4u1.AQA.AQA.dialog.LiveIdDialog;
import com.n4u1.AQA.AQA.dialog.NotEmailDialog;
import com.n4u1.AQA.AQA.dialog.NotGenderDialog;
import com.n4u1.AQA.AQA.dialog.NotIdlDialog;
import com.n4u1.AQA.AQA.dialog.NullEmailDialog;
import com.n4u1.AQA.AQA.dialog.ShortIdDialog;

import java.util.Iterator;
import java.util.Map;


public class CreateUserIdActivity extends AppCompatActivity {

    TextView createUserId_textView_next;
    ImageView createUserId_imageView_back;
    EditText createUserId_editText_id;
    String email, pw;
    boolean CURRENT_ID_CHECK_FLAG = true;

    private DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user_id);

        email = getIntent().getStringExtra("email");
        pw = getIntent().getStringExtra("pw");

        mRef = FirebaseDatabase.getInstance().getReference();
        createUserId_textView_next = findViewById(R.id.createUserId_textView_next);
        createUserId_imageView_back = findViewById(R.id.createUserId_imageView_back);
        createUserId_editText_id = findViewById(R.id.createUserId_editText_id);





        createUserId_textView_next.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    createUserId_textView_next.setTextColor(0xFF88B6E7);
                }
                if (MotionEvent.ACTION_UP == event.getAction()) {
                    createUserId_textView_next.setTextColor(0xff4485c9);
                    if (checkingId()) {
                        mRef.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Iterator<DataSnapshot> usersIterator = dataSnapshot.getChildren().iterator();
                                while (usersIterator.hasNext()) {
                                    Map<String, Object> user = (Map<String, Object>) usersIterator.next().getValue();
                                    String userId = String.valueOf(user.get("userId"));
                                    if (userId.equals(createUserId_editText_id.getText().toString())) {
                                        CURRENT_ID_CHECK_FLAG = false;
                                        LiveIdDialog liveIdDialog = new LiveIdDialog();
                                        liveIdDialog.show(getSupportFragmentManager(), "liveIdDialog");
                                    }
                                }
                                //모두 이상없으면 다음으로 이동
                                if (CURRENT_ID_CHECK_FLAG) {
                                    Intent intent = new Intent(CreateUserIdActivity.this, CreateUserAGActivity.class);
                                    intent.putExtra("email", email);
                                    intent.putExtra("pw", pw);
                                    intent.putExtra("id", createUserId_editText_id.getText().toString());
                                    startActivity(intent);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                }
                return true;

            }
        });

        createUserId_imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



    }
    /*
    onCreate();
     */


    private boolean checkingId() {

        if(createUserId_editText_id.getText().toString().isEmpty()) {
            NotIdlDialog notIdlDialog = new NotIdlDialog();
            notIdlDialog.show(getSupportFragmentManager(), "notIdlDialog");
            return false;
        }

        if (createUserId_editText_id.getText().toString().length() < 4) {
            ShortIdDialog shortIdDialog = new ShortIdDialog();
            shortIdDialog.show(getSupportFragmentManager(), "shortIdDialog");
            return false;
        }

        if (createUserId_editText_id.getText().toString().length() > 10) {
            ShortIdDialog shortIdDialog = new ShortIdDialog();
            shortIdDialog.show(getSupportFragmentManager(), "shortIdDialog");
            return false;
        }
        return true;
    }

}
