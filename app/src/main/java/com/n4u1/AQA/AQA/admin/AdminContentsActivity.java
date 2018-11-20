package com.n4u1.AQA.AQA.admin;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.n4u1.AQA.AQA.R;

import java.util.Iterator;
import java.util.Map;

public class AdminContentsActivity extends AppCompatActivity {

    EditText admin_editText_title;
    Button admin_button_title;
    DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_contents);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        admin_editText_title = findViewById(R.id.admin_editText_title);
        admin_button_title = findViewById(R.id.admin_button_title);

        admin_button_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String searchTitle = admin_editText_title.getText().toString();
                mDatabaseReference.child("user_contents").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Iterator<DataSnapshot> contentsIterator = dataSnapshot.getChildren().iterator();
                        while (contentsIterator.hasNext()) {
                            Map<String, Object> contentMap = (Map<String, Object>) contentsIterator.next().getValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }
}
