package com.n4u1.AQA.AQA.recyclerview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.n4u1.AQA.AQA.R;
import com.n4u1.AQA.AQA.models.ContentDTO;
import com.n4u1.AQA.AQA.models.ReplyDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReplyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<ReplyDTO> replyDTO;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth auth;
    private FirebaseDatabase mDatabase;
    private OnItemClickListener mOnItemClickListener;


    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }


    public ReplyAdapter(Context context, ArrayList<ReplyDTO> listItem, OnItemClickListener onItemClickListener) {
        super();
        this.mContext = context;
        this.replyDTO = listItem;
        mOnItemClickListener = onItemClickListener; //댓글 좋아요 클릭 리스너 PollSingleActivity, PollRankingActivity
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reply_item_poll, parent, false);
        return new ReplyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {

        firebaseDatabase = FirebaseDatabase.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        int userClass = replyDTO.get(position).getqPoint();

        ((ReplyViewHolder)holder).relativeLayout_like.setTag("replyAdapter_relativeLayout_like");
        ((ReplyViewHolder)holder).relativeLayout_main.setTag("replyAdapter_relativeLayout_main");

        ((ReplyViewHolder)holder).relativeLayout_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(v, position);
            }
        });
        ((ReplyViewHolder)holder).relativeLayout_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(v, position);
            }
        });

        if (userClass >= 0 && userClass < 50) {
            ((ReplyViewHolder)holder).imageView_userClass.setImageResource(R.drawable.q_class_red_1);
        } else if (userClass >= 50 && userClass < 100) {
            ((ReplyViewHolder)holder).imageView_userClass.setImageResource(R.drawable.q_class_red_2);
        } else if (userClass >= 100 && userClass < 150) {
            ((ReplyViewHolder)holder).imageView_userClass.setImageResource(R.drawable.q_class_orange_1);
        } else if (userClass >= 150 && userClass < 200) {
            ((ReplyViewHolder)holder).imageView_userClass.setImageResource(R.drawable.q_class_orange_2);
        } else if (userClass >= 200 && userClass < 250) {
            ((ReplyViewHolder)holder).imageView_userClass.setImageResource(R.drawable.q_class_yellow_1);
        } else if (userClass >= 250 && userClass < 300) {
            ((ReplyViewHolder)holder).imageView_userClass.setImageResource(R.drawable.q_class_yellow_2);
        } else if (userClass >= 300 && userClass < 350) {
            ((ReplyViewHolder)holder).imageView_userClass.setImageResource(R.drawable.q_class_green_1);
        } else if (userClass >= 350 && userClass < 400) {
            ((ReplyViewHolder)holder).imageView_userClass.setImageResource(R.drawable.q_class_green_2);
        } else if (userClass >= 400 && userClass < 450) {
            ((ReplyViewHolder)holder).imageView_userClass.setImageResource(R.drawable.q_class_blue_1);
        } else if (userClass >= 450 && userClass < 501) {
            ((ReplyViewHolder)holder).imageView_userClass.setImageResource(R.drawable.q_class_blue_2);
        } else if (userClass == 1000) {
            ((ReplyViewHolder)holder).imageView_userClass.setImageResource(R.drawable.q_class_black);
        } else {
            ((ReplyViewHolder)holder).imageView_userClass.setImageResource(R.drawable.q_class_red_1);
        }



        //좋아요 색 변경
        if (replyDTO.get(position).likes.containsKey(auth.getCurrentUser().getUid())) {
            ((ReplyViewHolder)holder).imageView_like.setImageResource(R.drawable.ic_thumb_up_blue);
            ((ReplyViewHolder)holder).textView_like.setText(String.valueOf(replyDTO.get(position).likeCount));
        } else {
            ((ReplyViewHolder)holder).imageView_like.setImageResource(R.drawable.ic_outline_thumb_up_24px);
            ((ReplyViewHolder)holder).textView_like.setText(String.valueOf(replyDTO.get(position).likeCount));
        }





        ((ReplyViewHolder)holder).textView_id.setText(replyDTO.get(position).getId());
        ((ReplyViewHolder)holder).textView_date.setText(replyDTO.get(position).getDate());
        ((ReplyViewHolder)holder).textView_reply.setText(replyDTO.get(position).getReply());





    }

    @Override
    public int getItemCount() {
        return replyDTO.size();
    }


}
