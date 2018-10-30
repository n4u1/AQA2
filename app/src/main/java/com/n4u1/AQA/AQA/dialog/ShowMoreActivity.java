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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
    Boolean ALARM_LAYOUT_FLAG = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_more);

        final String pollKey = getIntent().getStringExtra("pollKey");
        final int contentHit = getIntent().getIntExtra("hitCount", 0);


        final TextView showMore_textView_countResult = findViewById(R.id.showMore_textView_countResult);
        TextView showMore_textView_alarmOff = findViewById(R.id.showMore_textView_alarmOff);

        TextView showMore_textView_done = findViewById(R.id.showMore_textView_done);
        TextView showMore_textView_cancel = findViewById(R.id.showMore_textView_cancel);

        final EditText showMore_editText_count = findViewById(R.id.showMore_editText_count);

        //이 게시글 삭제하기 클릭
//        showMore_textView_delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ContentDeleteDialog contentDeleteDialog = new ContentDeleteDialog();
//                contentDeleteDialog.show(getSupportFragmentManager(), "contentDeleteDialog");
//
//            }
//        });


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
                    int oneCount = Integer.parseInt(temp.substring(index_ + 1, temp.length()));//

                    if (oneCount == 0) {
                        showMore_textView_countResult.setText("알람을 등록 해보세요.");
                    } else {
                        showMore_textView_countResult.setText(oneCount + "명 이 투표하면 알려 드립니다.");
                    }



                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //알람해제
        showMore_textView_alarmOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(getApplicationContext(), "알람이 해제 되었습니다.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
                toast.show();
                finish();

                mReference.child("user_contents").child(pollKey).child("alarm").setValue("C0O0");
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
                                mReference.child("user_contents").child(pollKey).child("alarm").setValue(alarmCount); //DB/user_contests/alarm:C0O0
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
