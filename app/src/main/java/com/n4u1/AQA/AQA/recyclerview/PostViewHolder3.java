package com.n4u1.AQA.AQA.recyclerview;

import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.n4u1.AQA.AQA.R;

import jp.wasabeef.recyclerview.animators.holder.AnimateViewHolder;


//adapter에 viewHolder가 들어갈것임
public class PostViewHolder3 extends RecyclerView.ViewHolder implements AnimateViewHolder {

    public ImageView imageView_postImg_0, imageView_postImg_1, imageView_postImg_2,
            imageView_postImg_3, imageView_like, imageView_share, imageView_hitCount, imageView_state, imageView_userClass;
    public TextView textView_likeCount, textView_userName, textView_title, textView_contentType, textView_hitCount, textView_replyCount, textView_pollMode;


    public PostViewHolder3(View itemView) {
        super(itemView);
        imageView_userClass = itemView.findViewById(R.id.imageView_userClass);
        imageView_postImg_0 = itemView.findViewById(R.id.imageView_postImg_0);
        imageView_postImg_1 = itemView.findViewById(R.id.imageView_postImg_1);
        imageView_postImg_2 = itemView.findViewById(R.id.imageView_postImg_2);
        imageView_postImg_3 = itemView.findViewById(R.id.imageView_postImg_3);
        imageView_state = itemView.findViewById(R.id.imageView_state);
        imageView_hitCount = itemView.findViewById(R.id.imageView_hitCount);
        imageView_like = itemView.findViewById(R.id.imageView_like);
        imageView_share = itemView.findViewById(R.id.imageView_share);
        textView_hitCount = itemView.findViewById(R.id.textView_hitCount);
        textView_likeCount = itemView.findViewById(R.id.textView_likeCount);
        textView_userName = itemView.findViewById(R.id.textView_userName);
        textView_title = itemView.findViewById(R.id.textView_title);
        textView_contentType = itemView.findViewById(R.id.textView_contentType);
        textView_replyCount = itemView.findViewById(R.id.textView_replyCount);
        textView_pollMode = itemView.findViewById(R.id.textView_pollMode);

    }

    @Override
    public void preAnimateRemoveImpl(RecyclerView.ViewHolder holder) {

    }

    @Override
    public void animateRemoveImpl(RecyclerView.ViewHolder holder, ViewPropertyAnimatorListener listener) {
        ViewCompat.animate(itemView)
                .translationY(-itemView.getHeight() * 0.3f)
                .alpha(0)
                .setDuration(300)
                .setListener(listener)
                .start();
    }

    @Override
    public void preAnimateAddImpl(RecyclerView.ViewHolder holder) {
        ViewCompat.setTranslationY(itemView, -itemView.getHeight() * 0.3f);
        ViewCompat.setAlpha(itemView, 0);
    }

    @Override
    public void animateAddImpl(RecyclerView.ViewHolder holder, ViewPropertyAnimatorListener listener) {
        ViewCompat.animate(itemView)
                .translationY(0)
                .alpha(1)
                .setDuration(300)
                .setListener(listener)
                .start();
    }
}
