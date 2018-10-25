package com.n4u1.AQA.AQA.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.n4u1.AQA.AQA.R;


//adapter에 viewHolder가 들어갈것임
public class PostViewHolderMine extends RecyclerView.ViewHolder{

    public ImageView imageView_share, imageView_userClass, imageView_like, imageView_state;
    public TextView textView_userName, textView_title, textView_contentType,
            textView_hitCount, textView_likeCount, textView_replyCount, textView_pollMode;
    public LinearLayout linearLayout_mine;




    public PostViewHolderMine(View itemView) {
        super(itemView);
        imageView_state = itemView.findViewById(R.id.imageView_state);
        textView_pollMode = itemView.findViewById(R.id.textView_pollMode);
        imageView_like = itemView.findViewById(R.id.imageView_like);
        textView_likeCount = itemView.findViewById(R.id.textView_likeCount);
        textView_replyCount = itemView.findViewById(R.id.textView_replyCount);
        linearLayout_mine = itemView.findViewById(R.id.linearLayout_mine);
        imageView_userClass = itemView.findViewById(R.id.imageView_userClass);
        imageView_share = itemView.findViewById(R.id.imageView_share);
        textView_hitCount = itemView.findViewById(R.id.textView_hitCount);
        textView_userName = itemView.findViewById(R.id.textView_userName);
        textView_title = itemView.findViewById(R.id.textView_title);
        textView_contentType = itemView.findViewById(R.id.textView_contentType);

    }
}
