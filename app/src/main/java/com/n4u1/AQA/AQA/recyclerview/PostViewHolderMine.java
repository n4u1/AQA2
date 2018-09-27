package com.n4u1.AQA.AQA.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.n4u1.AQA.AQA.R;


//adapter에 viewHolder가 들어갈것임
public class PostViewHolderMine extends RecyclerView.ViewHolder{

    public ImageView imageView_share;
    public TextView textView_userName, textView_title, textView_contentType, textView_hitCount;




    public PostViewHolderMine(View itemView) {
        super(itemView);
        imageView_share = itemView.findViewById(R.id.imageView_share);
        textView_hitCount = itemView.findViewById(R.id.textView_hitCount);
        textView_userName = itemView.findViewById(R.id.textView_userName);
        textView_title = itemView.findViewById(R.id.textView_title);
        textView_contentType = itemView.findViewById(R.id.textView_contentType);

    }
}
