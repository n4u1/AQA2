package com.n4u1.AQA.AQA.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.n4u1.AQA.AQA.R;
import com.n4u1.AQA.AQA.models.SuggestDTO;
import com.n4u1.AQA.AQA.views.SuggestDetailActivity;

import java.util.ArrayList;

public class SuggestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private ArrayList<SuggestDTO> suggestDTOS;


    public SuggestAdapter(Context context, ArrayList<SuggestDTO> listItem, RecyclerView recyclerView) {
        this.mContext = context;
        this.suggestDTOS = listItem;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.suggest_item, viewGroup, false);
        return new SuggestViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int i) {
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        String replyCount = " [" + String.valueOf(suggestDTOS.get(i).getReplyCount()) + "]";

        //아이템 바인딩
        if (suggestDTOS.get(i).getTitle().length() >= 13) {
            ((SuggestViewHolder) viewHolder).textView_title.setText(suggestDTOS.get(i).getTitle() + "..." + replyCount);
        } else {
            ((SuggestViewHolder) viewHolder).textView_title.setText(suggestDTOS.get(i).getTitle() + replyCount);
        }

        ((SuggestViewHolder) viewHolder).textView_userName.setText(suggestDTOS.get(i).getUserID());
        ((SuggestViewHolder) viewHolder).textView_likeCount.setText(String.valueOf(suggestDTOS.get(i).getLikeCount()));

        if (suggestDTOS.get(i).likes.containsKey(mUser.getUid())) {
            ((SuggestViewHolder) viewHolder).imageView_like.setImageResource(R.drawable.ic_suggest_up_fill);
        } else {
            ((SuggestViewHolder) viewHolder).imageView_like.setImageResource(R.drawable.ic_suggest_up);

        }

        ((SuggestViewHolder) viewHolder).suggest_linearLayout_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                movePoll(i);
            }
        });


        //Q userClass 별로 색 세팅
        mDatabase.getReference().child("users").child(suggestDTOS.get(i).uid).child("userClass").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    int userClass = Integer.parseInt(dataSnapshot.getValue().toString());
                    if (userClass >= 0 && userClass < 50) {
                        ((SuggestViewHolder) viewHolder).imageView_userClass.setImageResource(R.drawable.q_class_red_2);
                    } else if (userClass >= 50 && userClass < 100) {
                        ((SuggestViewHolder) viewHolder).imageView_userClass.setImageResource(R.drawable.q_class_red_1);
                    } else if (userClass >= 100 && userClass < 150) {
                        ((SuggestViewHolder) viewHolder).imageView_userClass.setImageResource(R.drawable.q_class_orange_1);
                    } else if (userClass >= 150 && userClass < 200) {
                        ((SuggestViewHolder) viewHolder).imageView_userClass.setImageResource(R.drawable.q_class_orange_2);
                    } else if (userClass >= 200 && userClass < 250) {
                        ((SuggestViewHolder) viewHolder).imageView_userClass.setImageResource(R.drawable.q_class_yellow_1);
                    } else if (userClass >= 250 && userClass < 300) {
                        ((SuggestViewHolder) viewHolder).imageView_userClass.setImageResource(R.drawable.q_class_yellow_2);
                    } else if (userClass >= 300 && userClass < 350) {
                        ((SuggestViewHolder) viewHolder).imageView_userClass.setImageResource(R.drawable.q_class_green_1);
                    } else if (userClass >= 350 && userClass < 400) {
                        ((SuggestViewHolder) viewHolder).imageView_userClass.setImageResource(R.drawable.q_class_green_2);
                    } else if (userClass >= 400 && userClass < 450) {
                        ((SuggestViewHolder) viewHolder).imageView_userClass.setImageResource(R.drawable.q_class_blue_2);
                    } else if (userClass >= 450 && userClass < 501) {
                        ((SuggestViewHolder) viewHolder).imageView_userClass.setImageResource(R.drawable.q_class_blue_2);
                    }
                } catch (Exception e) {
                    ((SuggestViewHolder) viewHolder).imageView_userClass.setImageResource(R.drawable.q_class_null);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return suggestDTOS.size();
    }


    private void movePoll(int position) {
        String string = suggestDTOS.get(position).suggestKey;
        Intent intent = new Intent(mContext, SuggestDetailActivity.class);
        intent.putExtra("suggestKey", string);
        mContext.startActivity(intent);
    }


}
