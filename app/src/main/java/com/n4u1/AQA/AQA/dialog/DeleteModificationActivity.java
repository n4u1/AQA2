package com.n4u1.AQA.AQA.dialog;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.n4u1.AQA.AQA.R;

import java.util.ArrayList;

public class DeleteModificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_del_modi);


        TextView textView_delete, textView_modification;
        textView_delete = findViewById(R.id.textView_delete);
        textView_modification = findViewById(R.id.textView_modification);


        String replyKey = getIntent().getStringExtra("replyKey");
        final ArrayList<String> keyDelete = new ArrayList<>();
        final ArrayList<String> keyModify = new ArrayList<>();
        keyDelete.add(replyKey);
        keyDelete.add("삭제하기");
        keyModify.add(replyKey);
        keyModify.add("수정하기");



        textView_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putStringArrayListExtra("resultDelete", keyDelete);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        textView_modification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putStringArrayListExtra("resultDelete", keyModify);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

    }
}
