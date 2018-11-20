package com.n4u1.AQA.AQA.admin;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.n4u1.AQA.AQA.R;
import com.n4u1.AQA.AQA.models.User;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class AdminUserActivity extends AppCompatActivity {

    TextView admin_textView_userId, admin_textView_age, admin_textView_email,
            admin_textView_gender, admin_textView_guid, admin_textView_qpoint, admin_textView_uid;
    EditText admin_editText_email, admin_editText_id;
    Button admin_button_email, admin_button_id;
    DatabaseReference mDatabaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        admin_button_id = findViewById(R.id.admin_button_id);
        admin_editText_id = findViewById(R.id.admin_editText_id);
        admin_button_email = findViewById(R.id.admin_button_email);
        admin_editText_email = findViewById(R.id.admin_editText_email);

        admin_textView_userId = findViewById(R.id.admin_textView_userId);
        admin_textView_age = findViewById(R.id.admin_textView_age);
        admin_textView_email = findViewById(R.id.admin_textView_email);
        admin_textView_gender = findViewById(R.id.admin_textView_gender);
        admin_textView_guid = findViewById(R.id.admin_textView_guid);
        admin_textView_qpoint = findViewById(R.id.admin_textView_qpoint);
        admin_textView_uid = findViewById(R.id.admin_textView_uid);


        admin_button_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String searchEmail = admin_editText_email.getText().toString();
                mDatabaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Iterator<DataSnapshot> UserIterator = dataSnapshot.getChildren().iterator();
                        while (UserIterator.hasNext()) {
                            Map<String, Object> userMap = (Map<String, Object>) UserIterator.next().getValue();
                            if (searchEmail.equals(String.valueOf(userMap.get("email")))) {
                                mDatabaseReference.child("users").child(String.valueOf(userMap.get("uid"))).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        Map<String, Object> userMapTemp = (Map<String, Object>) dataSnapshot.getValue();
                                        admin_textView_userId.setText(String.valueOf(userMapTemp.get("userId")));
                                        admin_textView_age.setText(String.valueOf(userMapTemp.get("age")));
                                        admin_textView_email.setText(String.valueOf(userMapTemp.get("email")));
                                        admin_textView_guid.setText(String.valueOf(userMapTemp.get("guid")));
                                        admin_textView_gender.setText(String.valueOf(userMapTemp.get("sex")));
                                        admin_textView_uid.setText(String.valueOf(userMapTemp.get("uid")));
                                        admin_textView_qpoint.setText(String.valueOf(userMapTemp.get("userClass")));
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



        admin_button_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String searchId = admin_editText_id.getText().toString();
                mDatabaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Iterator<DataSnapshot> UserIterator = dataSnapshot.getChildren().iterator();
                        while (UserIterator.hasNext()) {
                            Map<String, Object> userMap = (Map<String, Object>) UserIterator.next().getValue();
                            Log.d("lkj user", String.valueOf(userMap.get("email")));
                            if (searchId.equals(String.valueOf(userMap.get("userId")))) {
                                mDatabaseReference.child("users").child(String.valueOf(userMap.get("uid"))).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        Map<String, Object> userMapTemp = (Map<String, Object>) dataSnapshot.getValue();
                                        admin_textView_userId.setText(String.valueOf(userMapTemp.get("userId")));
                                        admin_textView_age.setText(String.valueOf(userMapTemp.get("age")));
                                        admin_textView_email.setText(String.valueOf(userMapTemp.get("email")));
                                        admin_textView_guid.setText(String.valueOf(userMapTemp.get("guid")));
                                        admin_textView_gender.setText(String.valueOf(userMapTemp.get("sex")));
                                        admin_textView_uid.setText(String.valueOf(userMapTemp.get("uid")));
                                        admin_textView_qpoint.setText(String.valueOf(userMapTemp.get("userClass")));
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        }
//
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });



    }
}
