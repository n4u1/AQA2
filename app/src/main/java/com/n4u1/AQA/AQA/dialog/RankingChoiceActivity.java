package com.n4u1.AQA.AQA.dialog;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.widget.TextView;

import com.n4u1.AQA.AQA.R;

import java.util.ArrayList;

public class RankingChoiceActivity extends AppCompatActivity {
    ArrayList<String> rankingArrayLists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking_choice);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);

        TextView textView_1, textView_2, textView_3, textView_4,
                textView_5, textView_6, textView_7, textView_8,
                textView_9, textView_10, textView_11, textView_12;

        textView_1 = findViewById(R.id.textView_1);
        textView_2 = findViewById(R.id.textView_2);
        textView_3 = findViewById(R.id.textView_3);
        textView_4 = findViewById(R.id.textView_4);
        textView_5 = findViewById(R.id.textView_5);
        textView_6 = findViewById(R.id.textView_6);
        textView_7 = findViewById(R.id.textView_7);
        textView_8 = findViewById(R.id.textView_8);
        textView_9 = findViewById(R.id.textView_9);
        textView_10 = findViewById(R.id.textView_10);
        textView_11 = findViewById(R.id.textView_11);
        textView_12 = findViewById(R.id.textView_12);


        //데이터 가져오기
        int contentsCount = getIntent().getIntExtra("contentsCount", 100);
        ArrayList<String> stringArrayList = getIntent().getStringArrayListExtra("rankingTextCheck");

        //초기세팅
        rankingArrayLists.add("1위로 선택");
        rankingArrayLists.add("2위로 선택");
        rankingArrayLists.add("3위로 선택");
        rankingArrayLists.add("4위로 선택");
        rankingArrayLists.add("5위로 선택");
        rankingArrayLists.add("6위로 선택");
        rankingArrayLists.add("7위로 선택");
        rankingArrayLists.add("8위로 선택");
        rankingArrayLists.add("9위로 선택");
        rankingArrayLists.add("10위로 선택");
        rankingArrayLists.add("모두 다시 선택");
        rankingArrayLists.add("원본 보기");
        textView_1.setVisibility(View.VISIBLE);
        textView_2.setVisibility(View.VISIBLE);
        textView_3.setVisibility(View.VISIBLE);
        textView_4.setVisibility(View.VISIBLE);
        textView_5.setVisibility(View.VISIBLE);
        textView_6.setVisibility(View.VISIBLE);
        textView_7.setVisibility(View.VISIBLE);
        textView_8.setVisibility(View.VISIBLE);
        textView_9.setVisibility(View.VISIBLE);
        textView_10.setVisibility(View.VISIBLE);
        textView_11.setVisibility(View.VISIBLE);
        textView_12.setVisibility(View.VISIBLE);
        textView_1.setText(rankingArrayLists.get(0));
        textView_2.setText(rankingArrayLists.get(1));
        textView_3.setText(rankingArrayLists.get(2));
        textView_4.setText(rankingArrayLists.get(3));
        textView_5.setText(rankingArrayLists.get(4));
        textView_6.setText(rankingArrayLists.get(5));
        textView_7.setText(rankingArrayLists.get(6));
        textView_8.setText(rankingArrayLists.get(7));
        textView_9.setText(rankingArrayLists.get(8));
        textView_10.setText(rankingArrayLists.get(9));
        textView_11.setText(rankingArrayLists.get(10));
        textView_12.setText(rankingArrayLists.get(11));


        //현재 선택되어있는 순위 빼고 남기기
        if (stringArrayList.contains("10 위")) {
            rankingArrayLists.remove(9);
            textView_10.setVisibility(View.GONE);
        }

        if (stringArrayList.contains("9 위")) {
            rankingArrayLists.remove(8);
            textView_9.setVisibility(View.GONE);

        }
        if (stringArrayList.contains("8 위")) {
            rankingArrayLists.remove(7);
            textView_8.setVisibility(View.GONE);

        }
        if (stringArrayList.contains("7 위")) {
            rankingArrayLists.remove(6);
            textView_7.setVisibility(View.GONE);

        }
        if (stringArrayList.contains("6 위")) {
            rankingArrayLists.remove(5);
            textView_6.setVisibility(View.GONE);

        }
        if (stringArrayList.contains("5 위")) {
            rankingArrayLists.remove(4);
            textView_5.setVisibility(View.GONE);

        }
        if (stringArrayList.contains("4 위")) {
            rankingArrayLists.remove(3);
            textView_4.setVisibility(View.GONE);

        }
        if (stringArrayList.contains("3 위")) {
            rankingArrayLists.remove(2);
            textView_3.setVisibility(View.GONE);

        }
        if (stringArrayList.contains("2 위")) {
            rankingArrayLists.remove(1);
            textView_2.setVisibility(View.GONE);

        }
        if (stringArrayList.contains("1 위")) {
            rankingArrayLists.remove(0);
            textView_1.setVisibility(View.GONE);
        }


        //후보갯수만큼 순위 리스트 보여주기
        switch (contentsCount) {
            case 2 :
                textView_3.setVisibility(View.GONE);
                textView_4.setVisibility(View.GONE);
                textView_5.setVisibility(View.GONE);
                textView_6.setVisibility(View.GONE);
                textView_7.setVisibility(View.GONE);
                textView_8.setVisibility(View.GONE);
                textView_9.setVisibility(View.GONE);
                textView_10.setVisibility(View.GONE);
                break;
            case 3 :
                textView_4.setVisibility(View.GONE);
                textView_5.setVisibility(View.GONE);
                textView_6.setVisibility(View.GONE);
                textView_7.setVisibility(View.GONE);
                textView_8.setVisibility(View.GONE);
                textView_9.setVisibility(View.GONE);
                textView_10.setVisibility(View.GONE);
                break;
            case 4 :
                textView_5.setVisibility(View.GONE);
                textView_6.setVisibility(View.GONE);
                textView_7.setVisibility(View.GONE);
                textView_8.setVisibility(View.GONE);
                textView_9.setVisibility(View.GONE);
                textView_10.setVisibility(View.GONE);
                break;
            case 5 :
                textView_6.setVisibility(View.GONE);
                textView_7.setVisibility(View.GONE);
                textView_8.setVisibility(View.GONE);
                textView_9.setVisibility(View.GONE);
                textView_10.setVisibility(View.GONE);
                break;
            case 6 :
                textView_7.setVisibility(View.GONE);
                textView_8.setVisibility(View.GONE);
                textView_9.setVisibility(View.GONE);
                textView_10.setVisibility(View.GONE);
                break;
            case 7 :
                textView_8.setVisibility(View.GONE);
                textView_9.setVisibility(View.GONE);
                textView_10.setVisibility(View.GONE);
                break;
            case 8 :
                textView_9.setVisibility(View.GONE);
                textView_10.setVisibility(View.GONE);
                break;
            case 9 :
                textView_10.setVisibility(View.GONE);
                break;
        }


        textView_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("result", "1 위");
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        textView_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("result", "2 위");
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        textView_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("result", "3 위");
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        textView_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("result", "4 위");
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        textView_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("result", "5 위");
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        textView_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("result", "6 위");
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        textView_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("result", "7 위");
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        textView_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("result", "8 위");
                setResult(RESULT_OK, resultIntent);
                finish();

            }
        });

        textView_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("result", "9 위");
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        textView_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("result", "10 위");
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        textView_11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("result", "refresh");
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        textView_12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("result", "원본 보기");
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });


//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        this.setContentView(R.layout.activity_ranking_choice);


    }


}
