package com.n4u1.AQA.AQA.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.n4u1.AQA.AQA.R;
import com.n4u1.AQA.AQA.models.ContentDTO;
import com.n4u1.AQA.AQA.util.OnLoadMoreListener;
import com.n4u1.AQA.AQA.views.PollRankingActivity;
import com.n4u1.AQA.AQA.views.PollSingleActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PostAdapterMine extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private ArrayList<ContentDTO> contentDTOS;
    private static final int ITEM_VIEW_TYPE_1 = 1;
    private static final int ITEM_VIEW_TYPE_2 = 2;
    private static final int ITEM_VIEW_TYPE_3 = 3;
    private static final int ITEM_VIEW_TYPE_0 = 0;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private int visibleThreshold = 1;
    private OnLoadMoreListener onLoadMoreListener;
    private String uId;



    public PostAdapterMine(Context context, ArrayList<ContentDTO> listItem, RecyclerView recyclerView) {
        this.mContext = context;
        this.contentDTOS = listItem;


        //load more 아래로 당겨서 로딩
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        // End has been reached
                        // Do something
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                        loading = true;
                    }
                }
            });
        }
    }


    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item_mine, parent, false);
        return new PostViewHolderMine(view);
    }


    @Override
    public int getItemViewType(int position) {
        if (contentDTOS.get(position).getItemViewType() == 1) {
            return ITEM_VIEW_TYPE_0;
        } else if (contentDTOS.get(position).getItemViewType() == 2) {
            return ITEM_VIEW_TYPE_1;
        } else if (contentDTOS.get(position).getItemViewType() == 3) {
            return ITEM_VIEW_TYPE_2;
        } else if (contentDTOS.get(position).getItemViewType() == 4) {
            return ITEM_VIEW_TYPE_3;
        } else if (contentDTOS.get(position).getItemViewType() == 5) {
            return ITEM_VIEW_TYPE_3;
        } else if (contentDTOS.get(position).getItemViewType() == 5) {
            return ITEM_VIEW_TYPE_3;
        } else if (contentDTOS.get(position).getItemViewType() == 6) {
            return ITEM_VIEW_TYPE_3;
        } else if (contentDTOS.get(position).getItemViewType() == 7) {
            return ITEM_VIEW_TYPE_3;
        } else if (contentDTOS.get(position).getItemViewType() == 8) {
            return ITEM_VIEW_TYPE_3;
        } else if (contentDTOS.get(position).getItemViewType() == 9) {
            return ITEM_VIEW_TYPE_3;
        } else if (contentDTOS.get(position).getItemViewType() == 10) {
            return ITEM_VIEW_TYPE_3;
        } else {
            return ITEM_VIEW_TYPE_1;
        }
    }

    //contentDTO 내용을 아이템셋에 배치
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        

        //아이템 바인딩
        ((PostViewHolderMine) holder).textView_title.setText(contentDTOS.get(position).title);
        ((PostViewHolderMine) holder).textView_userName.setText(contentDTOS.get(position).userID);
        ((PostViewHolderMine) holder).textView_contentType.setText(contentDTOS.get(position).contentType);
        ((PostViewHolderMine) holder).textView_hitCount.setText(String.valueOf(contentDTOS.get(position).contentHit));
        ((PostViewHolderMine) holder).textView_likeCount.setText(String.valueOf(contentDTOS.get(position).likeCount));
        ((PostViewHolderMine)holder).textView_replyCount.setText(String.valueOf(contentDTOS.get(position).replyCount));
        ((PostViewHolderMine)holder).textView_pollMode.setText(contentDTOS.get(position).pollMode + " / " + contentDTOS.get(position).itemViewType);
        if (contentDTOS.get(position).likes.containsKey(mUser.getUid())) {
            ((PostViewHolderMine) holder).imageView_like.setImageResource(R.drawable.ic_thumb_up_blue);
            ((PostViewHolderMine) holder).textView_likeCount.setText(String.valueOf(contentDTOS.get(position).likeCount));
        } else {
            ((PostViewHolderMine) holder).imageView_like.setImageResource(R.drawable.ic_outline_thumb_up_24px);
            ((PostViewHolderMine) holder).textView_likeCount.setText(String.valueOf(contentDTOS.get(position).likeCount));
        }
        if (contentDTOS.get(position).contentPicker.containsKey(mUser.getUid())) { 
            ((PostViewHolderMine) holder).imageView_state.setImageResource(R.drawable.ic_aqa_qicon);
        } else {
            ((PostViewHolderMine) holder).imageView_state.setImageResource(R.drawable.ic_aqa_qw);
        }
        //Q userClass 별로 색 세팅
        Log.d("lkj dtoUid", contentDTOS.get(position).uid);
        mDatabase.getReference().child("users").child(contentDTOS.get(position).uid).child("userClass").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    int userClass = Integer.parseInt(dataSnapshot.getValue().toString());
                    if (userClass >= 0 && userClass < 50) {
                        ((PostViewHolderMine)holder).imageView_userClass.setImageResource(R.drawable.q_class_red_2);
                    } else if (userClass >= 50 && userClass < 100) {
                        ((PostViewHolderMine)holder).imageView_userClass.setImageResource(R.drawable.q_class_red_1);
                    } else if (userClass >= 100 && userClass < 150) {
                        ((PostViewHolderMine)holder).imageView_userClass.setImageResource(R.drawable.q_class_orange_1);
                    } else if (userClass >= 150 && userClass < 200) {
                        ((PostViewHolderMine)holder).imageView_userClass.setImageResource(R.drawable.q_class_orange_2);
                    } else if (userClass >= 200 && userClass < 250) {
                        ((PostViewHolderMine)holder).imageView_userClass.setImageResource(R.drawable.q_class_yellow_1);
                    } else if (userClass >= 250 && userClass < 300) {
                        ((PostViewHolderMine)holder).imageView_userClass.setImageResource(R.drawable.q_class_yellow_2);
                    } else if (userClass >= 300 && userClass < 350) {
                        ((PostViewHolderMine)holder).imageView_userClass.setImageResource(R.drawable.q_class_green_1);
                    } else if (userClass >= 350 && userClass < 400) {
                        ((PostViewHolderMine)holder).imageView_userClass.setImageResource(R.drawable.q_class_green_2);
                    } else if (userClass >= 400 && userClass < 450) {
                        ((PostViewHolderMine)holder).imageView_userClass.setImageResource(R.drawable.q_class_blue_2);
                    } else if (userClass >= 450 && userClass < 501) {
                        ((PostViewHolderMine)holder).imageView_userClass.setImageResource(R.drawable.q_class_blue_2);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        //클릭시 PollRankingActivity 또는 PollSingleActivity 넘어감
        ((PostViewHolderMine) holder).linearLayout_mine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movePoll(position);
            }
        });

    }

    public void setLoaded() {
        loading = false;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    private void movePoll(int position) {
        String string = contentDTOS.get(position).contentKey;

        if (contentDTOS.get(position).getPollMode().equals("순위 투표")) {

            Intent intent = new Intent(mContext, PollRankingActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("contentKey", string);
            bundle.putInt("itemViewType", contentDTOS.get(position).itemViewType);
            bundle.putInt("contentHit", contentDTOS.get(position).contentHit);
            intent.putExtras(bundle);
            mContext.startActivity(intent);

        }
        if (contentDTOS.get(position).getPollMode().equals("단일 투표")) {

            Intent intent = new Intent(mContext, PollSingleActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("contentKey", string);
            bundle.putInt("itemViewType", contentDTOS.get(position).itemViewType);
            bundle.putInt("contentHit", contentDTOS.get(position).contentHit);
            intent.putExtras(bundle);
            mContext.startActivity(intent);

        }
    }

    @Override
    public int getItemCount() {
        return contentDTOS.size();
    }


}
