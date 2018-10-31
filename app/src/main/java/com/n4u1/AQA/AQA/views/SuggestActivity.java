package com.n4u1.AQA.AQA.views;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.n4u1.AQA.AQA.R;

public class SuggestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest);

        TextView testText = findViewById(R.id.testText);
        CardView cardView = findViewById(R.id.cardView);
        RecyclerView suggestActivity_recyclerView = findViewById(R.id.suggestActivity_recyclerView);
        FloatingActionButton suggestActivity_fab_addContent = findViewById(R.id.suggestActivity_fab_addContent);
    }
}
