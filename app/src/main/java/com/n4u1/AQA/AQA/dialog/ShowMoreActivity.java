package com.n4u1.AQA.AQA.dialog;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.n4u1.AQA.AQA.R;

import org.w3c.dom.Text;

import java.util.Map;

public class ShowMoreActivity extends AppCompatActivity {


    DatabaseReference mReference = FirebaseDatabase.getInstance().getReference();
    Boolean ALARM_LAYOUT_FLAG = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_more);

        final String pollKey = getIntent().getStringExtra("pollKey");
        final int contentHit = getIntent().getIntExtra("hitCount", 0);

        final TextView showMore_textView_countResult = findViewById(R.id.showMore_textView_countResult);

        TextView showMore_textView_done = findViewById(R.id.showMore_textView_done);
        TextView showMore_textView_cancel = findViewById(R.id.showMore_textView_cancel);
        TextView showMore_textView_delete = findViewById(R.id.showMore_textView_delete);
        final EditText showMore_editText_count = findViewById(R.id.showMore_editText_count);
        final ImageView showMore_imageView_upDown = findViewById(R.id.showMore_imageView_upDown);
        RelativeLayout showMore_relativeLayout_settingAlarm = findViewById(R.id.showMore_relativeLayout_settingAlarm);
        final String pollCount = showMore_editText_count.getText().toString();


        //알람 숫자 입력에 따라서 텍스트변경
        showMore_editText_count.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().trim().length() > 0) {
                    showMore_textView_countResult.setText(editable + "명 이 투표하면 알려드려요!");
                } else {
                    showMore_textView_countResult.setText("? 명 이 투표하면 알려드려요!");
                }
            }
        });


        //알람설정 여닫기 세팅
        mReference.child("user_contents").child(pollKey).child("alarm").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    String tmp = dataSnapshot.toString();
                    int index = tmp.indexOf("C");//ContinueCount
                    String temp = tmp.substring(index, tmp.length() - 2);
                    int index_ = temp.indexOf("O");//OneCount
//                    int continueCount = Integer.parseInt(temp.substring(1, index_));
                    int oneCount = Integer.parseInt(temp.substring(index_ + 1, temp.length()));
//
                    if (oneCount != 0) {
                        showMore_editText_count.setVisibility(View.VISIBLE);
                        showMore_textView_countResult.setText("현재" + oneCount + "명 이 투표하면 알람이 발생하도록 설정되어 있습니다.");
                        showMore_textView_countResult.setVisibility(View.VISIBLE);
                        showMore_imageView_upDown.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                        ALARM_LAYOUT_FLAG = true;

                    } else {
                        showMore_editText_count.setVisibility(View.GONE);
                        showMore_textView_countResult.setVisibility(View.GONE);
                        showMore_imageView_upDown.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                        ALARM_LAYOUT_FLAG = false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //알람설정 여닫기
        showMore_relativeLayout_settingAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMore_editText_count.setVisibility(View.GONE);
                showMore_textView_countResult.setVisibility(View.GONE);
                showMore_imageView_upDown.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                if (ALARM_LAYOUT_FLAG) {
                    ALARM_LAYOUT_FLAG = false;

                } else {
                    showMore_editText_count.setVisibility(View.VISIBLE);
                    showMore_textView_countResult.setVisibility(View.VISIBLE);
                    showMore_imageView_upDown.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                    ALARM_LAYOUT_FLAG = true;
                }
            }
        });


        //취소 클릭
        showMore_textView_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //확인 클릭
        showMore_textView_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mReference.child("user_contents").child(pollKey).child("alarm").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            String count = showMore_editText_count.getText().toString();
                            String alarmCount = "C0O" + count;
                            if (Integer.parseInt(count) <= contentHit) {
                                Toast toast = Toast.makeText(getApplicationContext(), "현재 투표수는" + contentHit + "입니다.", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
                                toast.show();
                            } else if (showMore_editText_count == null) {
                                Toast toast = Toast.makeText(getApplicationContext(), "? 에 숫자를 입력해주세요!", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
                                toast.show();
                            } else {
                                mReference.child("user_contents").child(pollKey).child("alarm").setValue(alarmCount);
                                Intent resultIntent = new Intent();
                                resultIntent.putExtra("resultAlarmCount", count);
                                setResult(RESULT_OK, resultIntent);
                                finish();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });













    }
    /*
     onCreate()
     */
}
