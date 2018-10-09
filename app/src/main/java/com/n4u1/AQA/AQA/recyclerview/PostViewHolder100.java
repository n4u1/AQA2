package com.n4u1.AQA.AQA.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.n4u1.AQA.AQA.R;


//adapter에 viewHolder가 들어갈것임
public class PostViewHolder100 extends RecyclerView.ViewHolder{

    public ImageView imageView_like, imageView_share, imageView_hitCount, imageView_state;
    public TextView textView_likeCount, textView_userName, textView_title, textView_contentType, textView_hitCount, textView_replyCount, textView_pollMode;
    public LinearLayout linearLayout_exoPlayer;
    public SimpleExoPlayer exo_play_post_0, exo_play_post_1;
    PlayerView exo_play_postVideo_0, exo_play_postVideo_1;

    public PostViewHolder100(View itemView) {
        super(itemView);

        exo_play_postVideo_0 = itemView.findViewById(R.id.exo_play_postVideo_0);
        exo_play_postVideo_1 = itemView.findViewById(R.id.exo_play_postVideo_1);
        linearLayout_exoPlayer = itemView.findViewById(R.id.linearLayout_exoPlayer);
        imageView_hitCount = itemView.findViewById(R.id.imageView_hitCount);
        imageView_state = itemView.findViewById(R.id.imageView_state);
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
}
