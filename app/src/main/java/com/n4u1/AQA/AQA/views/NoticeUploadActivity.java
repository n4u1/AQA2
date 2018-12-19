package com.n4u1.AQA.AQA.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.n4u1.AQA.AQA.R;
import com.n4u1.AQA.AQA.models.NoticeDTO;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class NoticeUploadActivity extends AppCompatActivity {

    private EditText notice_editText_title, notice_editText_number, notice_editText_description;
    private Button notice_button_gone;
    private String title, desc, date, key;
    private int number;
    private DatabaseReference mRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_upload);

        final NoticeDTO noticeDTO = new NoticeDTO();
        mRef = FirebaseDatabase.getInstance().getReference();
        notice_button_gone = findViewById(R.id.notice_button_gone);
        notice_editText_title = findViewById(R.id.notice_editText_title);
        notice_editText_description = findViewById(R.id.notice_editText_description);
        notice_editText_number = findViewById(R.id.notice_editText_number);


        notice_button_gone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = notice_editText_title.getText().toString();
                desc = notice_editText_description.getText().toString();
                number = Integer.parseInt(notice_editText_number.getText().toString());
                date = getDate();
                key = mRef.child("notice").push().getKey();

                noticeDTO.setNoticeKey(key);
                noticeDTO.setTitle(title);
                noticeDTO.setDescription(desc);
                noticeDTO.setNumber(number);
                noticeDTO.setDate(date);



                mRef.child("notice").child(key).setValue(noticeDTO);

                finish();




            }
        });



    }

    public String getDate() {
        TimeZone timeZone;
        timeZone = TimeZone.getTimeZone("Asia/Seoul");
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN);
        df.setTimeZone(timeZone);
        String currentDate = df.format(date);
        return currentDate;
    }
}
