package com.n4u1.AQA.AQA.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.n4u1.AQA.AQA.R;


//adapter에 viewHolder가 들어갈것임
public class NoticeViewHolder extends RecyclerView.ViewHolder{

    public TextView notice_textView_number, notice_textView_title, notice_textView_date;
    public LinearLayout notice_linearLayout_item;

    public NoticeViewHolder(View itemView) {
        super(itemView);
        notice_linearLayout_item = itemView.findViewById(R.id.notice_linearLayout_item);
        notice_textView_date = itemView.findViewById(R.id.notice_textView_date);
        notice_textView_number = itemView.findViewById(R.id.notice_textView_number);
        notice_textView_title = itemView.findViewById(R.id.notice_textView_title);
    }
}
