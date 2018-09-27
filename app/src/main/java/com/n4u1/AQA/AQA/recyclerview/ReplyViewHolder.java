package com.n4u1.AQA.AQA.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.n4u1.AQA.AQA.R;


//adapter에 viewHolder가 들어갈것임
public class ReplyViewHolder extends RecyclerView.ViewHolder{

    public TextView textView_id, textView_reply, textView_date;

    public ReplyViewHolder(View itemView) {
        super(itemView);
        textView_date = itemView.findViewById(R.id.textView_date);
        textView_reply = itemView.findViewById(R.id.textView_reply);
        textView_id = itemView.findViewById(R.id.textView_id);
    }
}
