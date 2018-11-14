package com.n4u1.AQA.AQA.dialog;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.n4u1.AQA.AQA.R;

import java.util.ArrayList;

public class PollSingleChoiceActivity extends AppCompatActivity {

    ArrayList<String> choiceArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poll_single_choice);

        TextView textView_1, textView_2;

        textView_1 = findViewById(R.id.textView_1);
        textView_2 = findViewById(R.id.textView_2);

        //데이터 가져오기
        int contentsCount = getIntent().getIntExtra("contentsCount", 100);


        //초기세팅
        choiceArrayList.add("선택 하기");
        choiceArrayList.add("원본 보기");
        textView_1.setVisibility(View.VISIBLE);
        textView_2.setVisibility(View.VISIBLE);
        textView_1.setText(choiceArrayList.get(0));
        textView_2.setText(choiceArrayList.get(1));

        if (contentsCount == 1) {
            textView_1.setVisibility(View.GONE);
        }


        textView_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("result", "선택 하기");
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });


        textView_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("result", "원본 보기");
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

    }
}
