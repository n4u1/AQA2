package com.n4u1.AQA.AQA.recyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.n4u1.AQA.AQA.R;

public class ShareAuthViewHolder extends RecyclerView.ViewHolder {
    public ImageView imageView_like, imageView_userClass;
    public TextView textView_likeCount, textView_userName, textView_title, textView_replyCount;
    public LinearLayout suggest_linearLayout_item;

    public ShareAuthViewHolder(@NonNull View itemView) {
        super(itemView);
        suggest_linearLayout_item = itemView.findViewById(R.id.suggest_linearLayout_item);
        imageView_userClass = itemView.findViewById(R.id.imageView_userClass);
        imageView_like = itemView.findViewById(R.id.imageView_like);
        textView_likeCount = itemView.findViewById(R.id.textView_likeCount);
        textView_userName = itemView.findViewById(R.id.textView_userName);
        textView_title = itemView.findViewById(R.id.textView_title);
        textView_replyCount = itemView.findViewById(R.id.textView_replyCount);
    }
}
