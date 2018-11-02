package com.n4u1.AQA.AQA.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.n4u1.AQA.AQA.R;


//adapter에 viewHolder가 들어갈것임
public class ReplySuggestViewHolder extends RecyclerView.ViewHolder{

    public TextView textView_id, textView_reply, textView_date, textView_like;
    public ImageView imageView_like, imageView_userClass;
    public RelativeLayout relativeLayout_like, relativeLayout_main;

    public ReplySuggestViewHolder(View itemView) {
        super(itemView);
        imageView_userClass = itemView.findViewById(R.id.imageView_userClass);
        relativeLayout_main = itemView.findViewById(R.id.relativeLayout_main);
        relativeLayout_like = itemView.findViewById(R.id.relativeLayout_like);
        imageView_like = itemView.findViewById(R.id.imageView_like);
        textView_like = itemView.findViewById(R.id.textView_like);
        textView_date = itemView.findViewById(R.id.textView_date);
        textView_reply = itemView.findViewById(R.id.textView_reply);
        textView_id = itemView.findViewById(R.id.textView_id);
    }
}
