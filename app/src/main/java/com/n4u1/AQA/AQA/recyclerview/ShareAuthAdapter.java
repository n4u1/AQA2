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
import com.n4u1.AQA.AQA.models.ShareAuthDTO;
import com.n4u1.AQA.AQA.models.SuggestDTO;
import com.n4u1.AQA.AQA.views.ShareAuthActivity;
import com.n4u1.AQA.AQA.views.ShareAuthDetailActivity;
import com.n4u1.AQA.AQA.views.SuggestDetailActivity;

import java.util.ArrayList;

public class ShareAuthAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private ArrayList<ShareAuthDTO> shareAuthDTOS;


    public ShareAuthAdapter(Context context, ArrayList<ShareAuthDTO> listItem, RecyclerView recyclerView) {
        this.mContext = context;
        this.shareAuthDTOS = listItem;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.share_auth_item, viewGroup, false);
        return new ShareAuthViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int i) {
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        //아이템 바인딩
        ((ShareAuthViewHolder) viewHolder).textView_title.setText(shareAuthDTOS.get(i).getTitle());
        ((ShareAuthViewHolder) viewHolder).textView_userName.setText(shareAuthDTOS.get(i).getUserID());
        ((ShareAuthViewHolder) viewHolder).suggest_linearLayout_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                movePoll(i);
            }
        });


        //Q userClass 별로 색 세팅
        mDatabase.getReference().child("users").child(shareAuthDTOS.get(i).uid).child("userClass").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int userClass = Integer.parseInt(dataSnapshot.getValue().toString());
                if (userClass >= 0 && userClass < 50) {
                    ((ShareAuthViewHolder) viewHolder).imageView_userClass.setImageResource(R.drawable.q_class_red_1);
                } else if (userClass >= 50 && userClass < 100) {
                    ((ShareAuthViewHolder) viewHolder).imageView_userClass.setImageResource(R.drawable.q_class_red_2);
                } else if (userClass >= 100 && userClass < 150) {
                    ((ShareAuthViewHolder) viewHolder).imageView_userClass.setImageResource(R.drawable.q_class_orange_1);
                } else if (userClass >= 150 && userClass < 200) {
                    ((ShareAuthViewHolder) viewHolder).imageView_userClass.setImageResource(R.drawable.q_class_orange_2);
                } else if (userClass >= 200 && userClass < 250) {
                    ((ShareAuthViewHolder) viewHolder).imageView_userClass.setImageResource(R.drawable.q_class_yellow_1);
                } else if (userClass >= 250 && userClass < 300) {
                    ((ShareAuthViewHolder) viewHolder).imageView_userClass.setImageResource(R.drawable.q_class_yellow_2);
                } else if (userClass >= 300 && userClass < 350) {
                    ((ShareAuthViewHolder) viewHolder).imageView_userClass.setImageResource(R.drawable.q_class_green_1);
                } else if (userClass >= 350 && userClass < 400) {
                    ((ShareAuthViewHolder) viewHolder).imageView_userClass.setImageResource(R.drawable.q_class_green_2);
                } else if (userClass >= 400 && userClass < 450) {
                    ((ShareAuthViewHolder) viewHolder).imageView_userClass.setImageResource(R.drawable.q_class_blue_1);
                } else if (userClass >= 450 && userClass < 501) {
                    ((ShareAuthViewHolder) viewHolder).imageView_userClass.setImageResource(R.drawable.q_class_blue_2);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return shareAuthDTOS.size();
    }


    private void movePoll(int position) {
        String string = shareAuthDTOS.get(position).shareAuthKey;
        Intent intent = new Intent(mContext, ShareAuthDetailActivity.class);
        intent.putExtra("shareAuthKey", string);
        mContext.startActivity(intent);
    }
}
