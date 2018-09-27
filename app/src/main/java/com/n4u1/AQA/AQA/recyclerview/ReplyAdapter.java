package com.n4u1.AQA.AQA.recyclerview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    public ReplyAdapter(Context context, ArrayList<ReplyDTO> listItem) {
        this.mContext = context;
        this.replyDTO = listItem;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reply_item_poll, parent, false);
        return new ReplyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        final List<String> uidLists = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        //좋아요 클릭을위해서 참조해서 키값 받아옴
        firebaseDatabase.getReference().child("user_contents").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                uidLists.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String uidKey = snapshot.getKey();
                    uidLists.add(uidKey);
                }
                Collections.reverse(uidLists);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        firebaseDatabase.getReference().child(uidLists.get(position)).child(position)

        ((ReplyViewHolder)holder).relativeLayout_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLikeClicked(firebaseDatabase.getReference().child("reply").child(uidLists.get(position)));
                mDatabase.getReference().child("reply").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        replyDTO.clear();
                        ArrayList<ReplyDTO> replyDTOTemp = new ArrayList<>();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            ReplyDTO replyDTO = snapshot.getValue(ReplyDTO.class);
                            replyDTOTemp.add(replyDTO);
                        }

                        Collections.reverse(replyDTOTemp);
                        replyDTO.addAll(replyDTOTemp);

                        if (replyDTO.get(position).likes.containsKey(auth.getCurrentUser().getUid())) {
                            ((ReplyViewHolder)holder).imageView_like.setImageResource(R.drawable.ic_thumb_up_blue);
                            ((ReplyViewHolder)holder).textView_like.setText(String.valueOf(replyDTO.get(position).likeCount));
                        } else {
                            ((ReplyViewHolder)holder).imageView_like.setImageResource(R.drawable.ic_outline_thumb_up_24px);
                            ((ReplyViewHolder)holder).textView_like.setText(String.valueOf(replyDTO.get(position).likeCount));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });


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



    private void onLikeClicked(final DatabaseReference postRef) {

        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                ReplyDTO replyDTO = mutableData.getValue(ReplyDTO.class);
                if (replyDTO == null) {
                    return Transaction.success(mutableData);
                }

//                if (replyDTO.getLikes().containsKey(auth.getCurrentUser().getUid())) {
                if (replyDTO.likes.containsKey(auth.getCurrentUser().getUid())) {
                    // Unstar the post and remove self from stars
                    // 좋아요카운트 -1 하고 리스트에서 삭제
                    replyDTO.likeCount = replyDTO.likeCount - 1;
                    replyDTO.likes.remove(auth.getCurrentUser().getUid());
                    // users/내uid/컨텐트key/false      : 좋아요 누른 컨텐츠 리스트 false
//                    firebaseDatabase.getReference()
//                            .child("users")
//                            .child(auth.getCurrentUser().getUid())
//                            .child("likeReply")
//                            .child(replyDTO.getContentKey())
//                            .setValue("false");

                } else {
                    // Star the post and add self to stars
                    replyDTO.likeCount = replyDTO.likeCount + 1;
                    replyDTO.likes.put(auth.getCurrentUser().getUid(), true);
                    // users/내uid/컨텐트key/true       : 좋아요 누른 컨텐츠 리스트 true
//                    firebaseDatabase.getReference()
//                            .child("users")
//                            .child(auth.getCurrentUser().getUid())
//                            .child("likeContent")
//                            .child(replyDTO.getContentKey())
//                            .setValue("true");

//                    User user = new User();
//                    Map<String, Object> userValues = user.toMap();
//                    Map<String, Object> childUpdates = new HashMap<>();
//                    childUpdates.put("/users/", userValues);
//                    databaseReference.updateChildren(childUpdates);

                }

                // Set value and report transaction success
                mutableData.setValue(replyDTO);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                // Transaction completed
                Log.d("lkjlkj", "postTransaction:onComplete:" + databaseError);



            }
        });
    }
}
