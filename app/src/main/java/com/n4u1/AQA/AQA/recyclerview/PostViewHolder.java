package com.n4u1.AQA.AQA.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.n4u1.AQA.AQA.R;


//adapter에 viewHolder가 들어갈것임
public class PostViewHolder extends RecyclerView.ViewHolder{

    public ImageView imageView_postImg_0, imageView_like, imageView_share, imageView_reply;
    public TextView textView_likeCount, textView_userName, textView_title, textView_contentType,
            textView_hitCount, textView_reply;




    public PostViewHolder(View itemView) {
        super(itemView);
        imageView_postImg_0 = itemView.findViewById(R.id.imageView_postImg_0);

        imageView_like = itemView.findViewById(R.id.imageView_like);
        imageView_share = itemView.findViewById(R.id.imageView_share);
        imageView_reply = itemView.findViewById(R.id.imageView_reply);
        textView_reply = itemView.findViewById(R.id.textView_reply);
        textView_hitCount = itemView.findViewById(R.id.textView_hitCount);
        textView_likeCount = itemView.findViewById(R.id.textView_likeCount);
        textView_userName = itemView.findViewById(R.id.textView_userName);
        textView_title = itemView.findViewById(R.id.textView_title);
        textView_contentType = itemView.findViewById(R.id.textView_contentType);

    }
}
