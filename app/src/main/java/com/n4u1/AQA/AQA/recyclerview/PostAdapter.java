package com.n4u1.AQA.AQA.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.firebase.auth.FirebaseUser;
import com.n4u1.AQA.AQA.R;
import com.n4u1.AQA.AQA.models.ContentDTO;
import com.n4u1.AQA.AQA.models.User;
import com.n4u1.AQA.AQA.util.GlideApp;
import com.n4u1.AQA.AQA.util.OnLoadMoreListener;
import com.n4u1.AQA.AQA.views.PollRankingActivity;
import com.n4u1.AQA.AQA.views.PollSingleActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.n4u1.AQA.AQA.views.TestActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private ArrayList<ContentDTO> contentDTOS;
    public static final int ACTIVITY_BACK_VERRIFICATION = 98765432;
    private static final int ITEM_VIEW_TYPE_1 = 1;
    private static final int ITEM_VIEW_TYPE_2 = 2;
    private static final int ITEM_VIEW_TYPE_3 = 3;
    private static final int ITEM_VIEW_TYPE_0 = 0;
    private static final int ITEM_VIEW_TYPE_100 = 100;
    private static final int VIEW_PROG = 10;
    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseDatabase mDatabase;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private int visibleThreshold = 1;
    private OnLoadMoreListener onLoadMoreListener;

    public PostAdapter(Context context, ArrayList<ContentDTO> listItem, RecyclerView recyclerView) {
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
        auth = FirebaseAuth.getInstance();
         if (viewType == ITEM_VIEW_TYPE_1) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item_home_1_img, parent, false);
            return new PostViewHolder1(view);
        } else if (viewType == ITEM_VIEW_TYPE_2) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item_home_2_img, parent, false);
            return new PostViewHolder2(view);
        } else if (viewType == ITEM_VIEW_TYPE_3) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item_home_3_img, parent, false);
            return new PostViewHolder3(view);
        } else if (viewType == ITEM_VIEW_TYPE_100) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item_home_vdo, parent, false);
            return new PostViewHolder100(view);
        } else if (viewType == VIEW_PROG) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.progressbar_recyclerview, parent, false);
            return new ProgressViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item_home_1_img, parent, false);
            return new PostViewHolder(view);
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (contentDTOS.get(position) == null) {
            return VIEW_PROG;
        } else {
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
            } else if (contentDTOS.get(position).getItemViewType() == 102) {
                return ITEM_VIEW_TYPE_100;
            } else return ITEM_VIEW_TYPE_1;
        }
    }

    //contentDTO 내용을 아이템셋에 배치
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {


        final List<String> uidLists = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        mDatabase = FirebaseDatabase.getInstance();



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

        switch (holder.getItemViewType()) {

            case ITEM_VIEW_TYPE_0:
                //이미지 클릭시 컨텐트 디테일(PollActivity) 로 넘어감
                ((PostViewHolder) holder).imageView_postImg_0.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        movePoll(position);
                    }
                });
                //아이템 바인딩
                ((PostViewHolder) holder).textView_title.setText(contentDTOS.get(position).title);
                ((PostViewHolder) holder).textView_userName.setText(contentDTOS.get(position).userID);
                ((PostViewHolder) holder).textView_contentType.setText(contentDTOS.get(position).contentType);
                ((PostViewHolder) holder).textView_hitCount.setText(String.valueOf(contentDTOS.get(position).contentHit));

                GlideApp.with(holder.itemView.getContext()).load(contentDTOS.get(position).imageUrl_0).centerCrop().thumbnail(Glide.with(holder.itemView.getContext()).load(R.drawable.loadingicon)).into(((PostViewHolder) holder).imageView_postImg_0);

                if (contentDTOS.get(position).likes.containsKey(auth.getCurrentUser().getUid())) {
                    ((PostViewHolder) holder).imageView_like.setImageResource(R.drawable.ic_thumb_up_blue);
                    ((PostViewHolder) holder).textView_likeCount.setText(String.valueOf(contentDTOS.get(position).likeCount));
                } else {
                    ((PostViewHolder) holder).imageView_like.setImageResource(R.drawable.ic_outline_thumb_up_24px);
                    ((PostViewHolder) holder).textView_likeCount.setText(String.valueOf(contentDTOS.get(position).likeCount));
                }


                //좋아요 버튼 클릭 이벤트
                ((PostViewHolder) holder).imageView_like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onLikeClicked(firebaseDatabase.getReference().child("user_contents").child(uidLists.get(position)));
                        mDatabase.getReference().child("user_contents").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                contentDTOS.clear();
                                ArrayList<ContentDTO> contentDTOSTemp = new ArrayList<>();

                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    ContentDTO contentDTO = snapshot.getValue(ContentDTO.class);
                                    contentDTOSTemp.add(contentDTO);
                                }

                                Collections.reverse(contentDTOSTemp);
                                contentDTOS.addAll(contentDTOSTemp);

                                if (contentDTOS.get(position).likes.containsKey(auth.getCurrentUser().getUid())) {
                                    ((PostViewHolder) holder).imageView_like.setImageResource(R.drawable.ic_thumb_up_blue);
                                    ((PostViewHolder) holder).textView_likeCount.setText(String.valueOf(contentDTOS.get(position).likeCount));
                                } else {
                                    ((PostViewHolder) holder).imageView_like.setImageResource(R.drawable.ic_outline_thumb_up_24px);
                                    ((PostViewHolder) holder).textView_likeCount.setText(String.valueOf(contentDTOS.get(position).likeCount));
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                    }
                });

                break;

            case ITEM_VIEW_TYPE_1:
                ((PostViewHolder1) holder).imageView_postImg_0.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        movePoll(position);
                    }
                });
                ((PostViewHolder1) holder).imageView_postImg_1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        movePoll(position);
                    }
                });

                ((PostViewHolder1) holder).imageView_hitCount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast toast = Toast.makeText(mContext, "투표에 참가한 인원 입니다!", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
                        toast.show();
                    }
                });

                ((PostViewHolder1) holder).textView_hitCount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast toast = Toast.makeText(mContext, "투표에 참가한 인원 입니다!", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
                        toast.show();
                    }
                });

                ((PostViewHolder1) holder).imageView_state.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast toast = Toast.makeText(mContext, "우와! 투표했더니 파란색이 칠해졌다!", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
                        toast.show();
                    }
                });

                //좋아요 버튼 클릭 이벤트
                ((PostViewHolder1) holder).imageView_like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (contentDTOS.get(position).uid.equals(auth.getCurrentUser().getUid())) {
                            Toast toast = Toast.makeText(mContext, contentDTOS.get(position).userID + "님의 투표입니다!", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
                            toast.show();
                        } else {
                            onLikeClicked(firebaseDatabase.getReference().child("user_contents").child(uidLists.get(position)));
                        }

                        mDatabase.getReference().child("user_contents").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                contentDTOS.clear();
                                ArrayList<ContentDTO> contentDTOSTemp = new ArrayList<>();

                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    ContentDTO contentDTO = snapshot.getValue(ContentDTO.class);
                                    contentDTOSTemp.add(contentDTO);
                                }

                                Collections.reverse(contentDTOSTemp);
                                contentDTOS.addAll(contentDTOSTemp);

                                if (contentDTOS.get(position).likes.containsKey(auth.getCurrentUser().getUid())) {
                                    ((PostViewHolder1) holder).imageView_like.setImageResource(R.drawable.ic_thumb_up_blue);
                                    ((PostViewHolder1) holder).textView_likeCount.setText(String.valueOf(contentDTOS.get(position).likeCount));
                                } else {
                                    ((PostViewHolder1) holder).imageView_like.setImageResource(R.drawable.ic_outline_thumb_up_24px);
                                    ((PostViewHolder1) holder).textView_likeCount.setText(String.valueOf(contentDTOS.get(position).likeCount));
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                });



                if (contentDTOS.get(position).contentPicker.containsKey(auth.getCurrentUser().getUid())) {
                    ((PostViewHolder1) holder).imageView_state.setImageResource(R.drawable.q);
                } else {
                    ((PostViewHolder1) holder).imageView_state.setImageResource(R.drawable.q_bg_w);
                }

                if (contentDTOS.get(position).likes.containsKey(auth.getCurrentUser().getUid())) {
                    ((PostViewHolder1) holder).imageView_like.setImageResource(R.drawable.ic_thumb_up_blue);
                    ((PostViewHolder1) holder).textView_likeCount.setText(String.valueOf(contentDTOS.get(position).likeCount));
                } else {
                    ((PostViewHolder1) holder).imageView_like.setImageResource(R.drawable.ic_outline_thumb_up_24px);
                    ((PostViewHolder1) holder).textView_likeCount.setText(String.valueOf(contentDTOS.get(position).likeCount));
                }


                if (contentDTOS.get(position).title.length() >= 15) {
                    ((PostViewHolder1) holder).textView_title.setText(contentDTOS.get(position).title.substring(0,15) + "...");
                } else {
                    ((PostViewHolder1) holder).textView_title.setText(contentDTOS.get(position).title);
                }
                ((PostViewHolder1) holder).textView_userName.setText(contentDTOS.get(position).userID);

                ((PostViewHolder1)holder).textView_pollMode.setText(contentDTOS.get(position).pollMode + " / " + contentDTOS.get(position).itemViewType);
                ((PostViewHolder1)holder).textView_contentType.setText(contentDTOS.get(position).contentType);
                ((PostViewHolder1)holder).textView_hitCount.setText(String.valueOf(contentDTOS.get(position).contentHit));
                ((PostViewHolder1)holder).textView_replyCount.setText(" [" + String.valueOf(contentDTOS.get(position).replyCount) + "]");

                //Q userClass 별로 색 세팅
                try {
                    firebaseDatabase.getReference().child("users").child(contentDTOS.get(position).uid).child("userClass").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            try {
                                int userClass = Integer.parseInt(dataSnapshot.getValue().toString());
                                if (userClass >= 0 && userClass < 50) {
                                    ((PostViewHolder1)holder).imageView_userClass.setImageResource(R.drawable.q_class_red_1);
                                } else if (userClass >= 50 && userClass < 100) {
                                    ((PostViewHolder1)holder).imageView_userClass.setImageResource(R.drawable.q_class_red_2);
                                } else if (userClass >= 100 && userClass < 150) {
                                    ((PostViewHolder1)holder).imageView_userClass.setImageResource(R.drawable.q_class_orange_1);
                                } else if (userClass >= 150 && userClass < 200) {
                                    ((PostViewHolder1)holder).imageView_userClass.setImageResource(R.drawable.q_class_orange_2);
                                } else if (userClass >= 200 && userClass < 250) {
                                    ((PostViewHolder1)holder).imageView_userClass.setImageResource(R.drawable.q_class_yellow_1);
                                } else if (userClass >= 250 && userClass < 300) {
                                    ((PostViewHolder1)holder).imageView_userClass.setImageResource(R.drawable.q_class_yellow_2);
                                } else if (userClass >= 300 && userClass < 350) {
                                    ((PostViewHolder1)holder).imageView_userClass.setImageResource(R.drawable.q_class_green_1);
                                } else if (userClass >= 350 && userClass < 400) {
                                    ((PostViewHolder1)holder).imageView_userClass.setImageResource(R.drawable.q_class_green_2);
                                } else if (userClass >= 400 && userClass < 450) {
                                    ((PostViewHolder1)holder).imageView_userClass.setImageResource(R.drawable.q_class_blue_1);
                                } else if (userClass >= 450 && userClass < 501) {
                                    ((PostViewHolder1)holder).imageView_userClass.setImageResource(R.drawable.q_class_blue_2);
                                } else if (userClass == 1000) {
                                    ((PostViewHolder1)holder).imageView_userClass.setImageResource(R.drawable.q_class_black);
                                } else {
                                    ((PostViewHolder1)holder).imageView_userClass.setImageResource(R.drawable.q_class_red_1);
                                }
                            } catch (Exception e) {

                            }

                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                } catch (Exception e) {

                }


                GlideApp.with(holder.itemView.getContext()).load(contentDTOS.get(position).imageUrl_0).centerCrop().thumbnail(Glide.with(holder.itemView.getContext()).load(R.drawable.loadingicon)).into(((PostViewHolder1) holder).imageView_postImg_0);
                GlideApp.with(holder.itemView.getContext()).load(contentDTOS.get(position).imageUrl_1).centerCrop().thumbnail(Glide.with(holder.itemView.getContext()).load(R.drawable.loadingicon)).into(((PostViewHolder1) holder).imageView_postImg_1);
                break;

            case ITEM_VIEW_TYPE_2:
                ((PostViewHolder2) holder).imageView_postImg_0.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        movePoll(position);
                    }
                });
                ((PostViewHolder2) holder).imageView_postImg_1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        movePoll(position);
                    }
                });
                ((PostViewHolder2) holder).imageView_postImg_2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        movePoll(position);
                    }
                });

                //좋아요 버튼 클릭 이벤트
                ((PostViewHolder2) holder).imageView_like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (contentDTOS.get(position).uid.equals(auth.getCurrentUser().getUid())) {
                            Toast toast = Toast.makeText(mContext, contentDTOS.get(position).userID + "님의 투표입니다!", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
                            toast.show();
                        } else {
                            onLikeClicked(firebaseDatabase.getReference().child("user_contents").child(uidLists.get(position)));
                        }
                        mDatabase.getReference().child("user_contents").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                contentDTOS.clear();
                                ArrayList<ContentDTO> contentDTOSTemp = new ArrayList<>();

                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    ContentDTO contentDTO = snapshot.getValue(ContentDTO.class);
                                    contentDTOSTemp.add(contentDTO);
                                }

                                Collections.reverse(contentDTOSTemp);
                                contentDTOS.addAll(contentDTOSTemp);

                                if (contentDTOS.get(position).likes.containsKey(auth.getCurrentUser().getUid())) {
                                    ((PostViewHolder2) holder).imageView_like.setImageResource(R.drawable.ic_thumb_up_blue);
                                    ((PostViewHolder2) holder).textView_likeCount.setText(String.valueOf(contentDTOS.get(position).likeCount));
                                } else {
                                    ((PostViewHolder2) holder).imageView_like.setImageResource(R.drawable.ic_outline_thumb_up_24px);
                                    ((PostViewHolder2) holder).textView_likeCount.setText(String.valueOf(contentDTOS.get(position).likeCount));
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });

                ((PostViewHolder2) holder).imageView_hitCount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast toast = Toast.makeText(mContext, "투표에 참가한 인원 입니다!", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
                        toast.show();
                    }
                });

                ((PostViewHolder2) holder).textView_hitCount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast toast = Toast.makeText(mContext, "투표에 참가한 인원 입니다!", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
                        toast.show();
                    }
                });

                ((PostViewHolder2) holder).imageView_state.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast toast = Toast.makeText(mContext, "우와! 투표했더니 파란색이 칠해졌다!", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
                        toast.show();
                    }
                });

                if (contentDTOS.get(position).contentPicker.containsKey(auth.getCurrentUser().getUid())) {
                    ((PostViewHolder2) holder).imageView_state.setImageResource(R.drawable.q);
                } else {
                    ((PostViewHolder2) holder).imageView_state.setImageResource(R.drawable.q_bg_w);
                }
                if (contentDTOS.get(position).likes.containsKey(auth.getCurrentUser().getUid())) {
                    ((PostViewHolder2) holder).imageView_like.setImageResource(R.drawable.ic_thumb_up_blue);
                    ((PostViewHolder2) holder).textView_likeCount.setText(String.valueOf(contentDTOS.get(position).likeCount));
                } else {
                    ((PostViewHolder2) holder).imageView_like.setImageResource(R.drawable.ic_outline_thumb_up_24px);
                    ((PostViewHolder2) holder).textView_likeCount.setText(String.valueOf(contentDTOS.get(position).likeCount));
                }


                if (contentDTOS.get(position).title.length() >= 15) {
                    ((PostViewHolder2) holder).textView_title.setText(contentDTOS.get(position).title.substring(0,15) + "...");
                } else {
                    ((PostViewHolder2) holder).textView_title.setText(contentDTOS.get(position).title);
                }
                ((PostViewHolder2) holder).textView_userName.setText(contentDTOS.get(position).userID);

                ((PostViewHolder2)holder).textView_pollMode.setText(contentDTOS.get(position).pollMode + " / " + contentDTOS.get(position).itemViewType);
                ((PostViewHolder2) holder).textView_contentType.setText(contentDTOS.get(position).contentType);
                ((PostViewHolder2) holder).textView_hitCount.setText(String.valueOf(contentDTOS.get(position).contentHit));
                ((PostViewHolder2)holder).textView_replyCount.setText(" [" + String.valueOf(contentDTOS.get(position).replyCount) + "]");

                //Q userClass 별로 색 세팅
                try {
                    firebaseDatabase.getReference().child("users").child(contentDTOS.get(position).uid).child("userClass").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int userClass = Integer.parseInt(dataSnapshot.getValue().toString());
                            if (userClass >= 0 && userClass < 50) {
                                ((PostViewHolder2)holder).imageView_userClass.setImageResource(R.drawable.q_class_red_1);
                            } else if (userClass >= 50 && userClass < 100) {
                                ((PostViewHolder2)holder).imageView_userClass.setImageResource(R.drawable.q_class_red_2);
                            } else if (userClass >= 100 && userClass < 150) {
                                ((PostViewHolder2)holder).imageView_userClass.setImageResource(R.drawable.q_class_orange_1);
                            } else if (userClass >= 150 && userClass < 200) {
                                ((PostViewHolder2)holder).imageView_userClass.setImageResource(R.drawable.q_class_orange_2);
                            } else if (userClass >= 200 && userClass < 250) {
                                ((PostViewHolder2)holder).imageView_userClass.setImageResource(R.drawable.q_class_yellow_1);
                            } else if (userClass >= 250 && userClass < 300) {
                                ((PostViewHolder2)holder).imageView_userClass.setImageResource(R.drawable.q_class_yellow_2);
                            } else if (userClass >= 300 && userClass < 350) {
                                ((PostViewHolder2)holder).imageView_userClass.setImageResource(R.drawable.q_class_green_1);
                            } else if (userClass >= 350 && userClass < 400) {
                                ((PostViewHolder2)holder).imageView_userClass.setImageResource(R.drawable.q_class_green_2);
                            } else if (userClass >= 400 && userClass < 450) {
                                ((PostViewHolder2)holder).imageView_userClass.setImageResource(R.drawable.q_class_blue_1);
                            } else if (userClass >= 450 && userClass < 501) {
                                ((PostViewHolder2)holder).imageView_userClass.setImageResource(R.drawable.q_class_blue_2);
                            } else if (userClass == 1000) {
                                ((PostViewHolder2)holder).imageView_userClass.setImageResource(R.drawable.q_class_black);
                            } else {
                                ((PostViewHolder2)holder).imageView_userClass.setImageResource(R.drawable.q_class_red_1);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }catch (Exception e) {

                }
                GlideApp.with(holder.itemView.getContext()).load(contentDTOS.get(position).imageUrl_0).centerCrop().thumbnail(Glide.with(holder.itemView.getContext()).load(R.drawable.loadingicon)).into(((PostViewHolder2) holder).imageView_postImg_0);
                GlideApp.with(holder.itemView.getContext()).load(contentDTOS.get(position).imageUrl_1).centerCrop().thumbnail(Glide.with(holder.itemView.getContext()).load(R.drawable.loadingicon)).into(((PostViewHolder2) holder).imageView_postImg_1);
                GlideApp.with(holder.itemView.getContext()).load(contentDTOS.get(position).imageUrl_2).centerCrop().thumbnail(Glide.with(holder.itemView.getContext()).load(R.drawable.loadingicon)).into(((PostViewHolder2) holder).imageView_postImg_2);

                break;

            case ITEM_VIEW_TYPE_3:
                ((PostViewHolder3) holder).imageView_postImg_0.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        movePoll(position);
                    }
                });
                ((PostViewHolder3) holder).imageView_postImg_1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        movePoll(position);
                    }
                });
                ((PostViewHolder3) holder).imageView_postImg_2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        movePoll(position);
                    }
                });
                ((PostViewHolder3) holder).imageView_postImg_3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        movePoll(position);
                    }
                });

                //좋아요 버튼 클릭 이벤트
                ((PostViewHolder3) holder).imageView_like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (contentDTOS.get(position).uid.equals(auth.getCurrentUser().getUid())) {
                            Toast toast = Toast.makeText(mContext, contentDTOS.get(position).userID + "님의 투표입니다!", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
                            toast.show();
                        } else {
                            onLikeClicked(firebaseDatabase.getReference().child("user_contents").child(uidLists.get(position)));
                        }
                        mDatabase.getReference().child("user_contents").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                contentDTOS.clear();
                                ArrayList<ContentDTO> contentDTOSTemp = new ArrayList<>();

                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    ContentDTO contentDTO = snapshot.getValue(ContentDTO.class);
                                    contentDTOSTemp.add(contentDTO);
                                }

                                Collections.reverse(contentDTOSTemp);
                                contentDTOS.addAll(contentDTOSTemp);


                                if (contentDTOS.get(position).likes.containsKey(auth.getCurrentUser().getUid())) {
                                    ((PostViewHolder3) holder).imageView_like.setImageResource(R.drawable.ic_thumb_up_blue);
                                    ((PostViewHolder3) holder).textView_likeCount.setText(String.valueOf(contentDTOS.get(position).likeCount));
                                } else {
                                    ((PostViewHolder3) holder).imageView_like.setImageResource(R.drawable.ic_outline_thumb_up_24px);
                                    ((PostViewHolder3) holder).textView_likeCount.setText(String.valueOf(contentDTOS.get(position).likeCount));
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                    }
                });

                ((PostViewHolder3) holder).imageView_hitCount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast toast = Toast.makeText(mContext, "투표에 참가한 인원 입니다!", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
                        toast.show();
                    }
                });

                ((PostViewHolder3) holder).textView_hitCount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast toast = Toast.makeText(mContext, "투표에 참가한 인원 입니다!", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
                        toast.show();
                    }
                });

                ((PostViewHolder3) holder).imageView_state.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast toast = Toast.makeText(mContext, "우와! 투표했더니 파란색이 칠해졌다!", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
                        toast.show();
                    }
                });

                if (contentDTOS.get(position).contentPicker.containsKey(auth.getCurrentUser().getUid())) {
                    ((PostViewHolder3) holder).imageView_state.setImageResource(R.drawable.q);
                } else {
                    ((PostViewHolder3) holder).imageView_state.setImageResource(R.drawable.q_bg_w);
                }

                if (contentDTOS.get(position).likes.containsKey(auth.getCurrentUser().getUid())) {
                    ((PostViewHolder3) holder).imageView_like.setImageResource(R.drawable.ic_thumb_up_blue);
                    ((PostViewHolder3) holder).textView_likeCount.setText(String.valueOf(contentDTOS.get(position).likeCount));
                } else {
                    ((PostViewHolder3) holder).imageView_like.setImageResource(R.drawable.ic_outline_thumb_up_24px);
                    ((PostViewHolder3) holder).textView_likeCount.setText(String.valueOf(contentDTOS.get(position).likeCount));
                }


                if (contentDTOS.get(position).title.length() >= 15) {
                    ((PostViewHolder3) holder).textView_title.setText(contentDTOS.get(position).title.substring(0,15) + "...");
                } else {
                    ((PostViewHolder3) holder).textView_title.setText(contentDTOS.get(position).title);
                }
                ((PostViewHolder3) holder).textView_userName.setText(contentDTOS.get(position).userID);
                ((PostViewHolder3)holder).textView_pollMode.setText(contentDTOS.get(position).pollMode + " / " + contentDTOS.get(position).itemViewType);
                ((PostViewHolder3) holder).textView_contentType.setText(contentDTOS.get(position).contentType);
                ((PostViewHolder3) holder).textView_hitCount.setText(String.valueOf(contentDTOS.get(position).contentHit));
                ((PostViewHolder3)holder).textView_replyCount.setText(" [" + String.valueOf(contentDTOS.get(position).replyCount) + "]");

                //Q userClass 별로 색 세팅
                try {
                    firebaseDatabase.getReference().child("users").child(contentDTOS.get(position).uid).child("userClass").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int userClass = Integer.parseInt(dataSnapshot.getValue().toString());
                            if (userClass >= 0 && userClass < 50) {
                                ((PostViewHolder3)holder).imageView_userClass.setImageResource(R.drawable.q_class_red_1);
                            } else if (userClass >= 50 && userClass < 100) {
                                ((PostViewHolder3)holder).imageView_userClass.setImageResource(R.drawable.q_class_red_2);
                            } else if (userClass >= 100 && userClass < 150) {
                                ((PostViewHolder3)holder).imageView_userClass.setImageResource(R.drawable.q_class_orange_1);
                            } else if (userClass >= 150 && userClass < 200) {
                                ((PostViewHolder3)holder).imageView_userClass.setImageResource(R.drawable.q_class_orange_2);
                            } else if (userClass >= 200 && userClass < 250) {
                                ((PostViewHolder3)holder).imageView_userClass.setImageResource(R.drawable.q_class_yellow_1);
                            } else if (userClass >= 250 && userClass < 300) {
                                ((PostViewHolder3)holder).imageView_userClass.setImageResource(R.drawable.q_class_yellow_2);
                            } else if (userClass >= 300 && userClass < 350) {
                                ((PostViewHolder3)holder).imageView_userClass.setImageResource(R.drawable.q_class_green_1);
                            } else if (userClass >= 350 && userClass < 400) {
                                ((PostViewHolder3)holder).imageView_userClass.setImageResource(R.drawable.q_class_green_2);
                            } else if (userClass >= 400 && userClass < 450) {
                                ((PostViewHolder3)holder).imageView_userClass.setImageResource(R.drawable.q_class_blue_1);
                            } else if (userClass >= 450 && userClass < 501) {
                                ((PostViewHolder3)holder).imageView_userClass.setImageResource(R.drawable.q_class_blue_2);
                            } else if (userClass == 1000) {
                                ((PostViewHolder3)holder).imageView_userClass.setImageResource(R.drawable.q_class_black);
                            } else {
                                ((PostViewHolder3)holder).imageView_userClass.setImageResource(R.drawable.q_class_red_1);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                } catch (Exception e) {

                }
                GlideApp.with(holder.itemView.getContext()).load(contentDTOS.get(position).imageUrl_0).centerCrop().thumbnail(Glide.with(holder.itemView.getContext()).load(R.drawable.loadingicon)).into(((PostViewHolder3) holder).imageView_postImg_0);
                GlideApp.with(holder.itemView.getContext()).load(contentDTOS.get(position).imageUrl_1).centerCrop().thumbnail(Glide.with(holder.itemView.getContext()).load(R.drawable.loadingicon)).into(((PostViewHolder3) holder).imageView_postImg_1);
                GlideApp.with(holder.itemView.getContext()).load(contentDTOS.get(position).imageUrl_2).centerCrop().thumbnail(Glide.with(holder.itemView.getContext()).load(R.drawable.loadingicon)).into(((PostViewHolder3) holder).imageView_postImg_2);
                GlideApp.with(holder.itemView.getContext()).load(contentDTOS.get(position).imageUrl_3).centerCrop().thumbnail(Glide.with(holder.itemView.getContext()).load(R.drawable.loadingicon)).into(((PostViewHolder3) holder).imageView_postImg_3);
                break;


            case ITEM_VIEW_TYPE_100:
                ((PostViewHolder100) holder).linearLayout_exoPlayer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        movePoll(position);
                    }
                });

                ((PostViewHolder100) holder).imageView_hitCount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast toast = Toast.makeText(mContext, "투표에 참가한 인원 입니다!", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
                        toast.show();
                    }
                });

                ((PostViewHolder100) holder).textView_hitCount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast toast = Toast.makeText(mContext, "투표에 참가한 인원 입니다!", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
                        toast.show();
                    }
                });

                ((PostViewHolder100) holder).imageView_state.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast toast = Toast.makeText(mContext, "우와! 투표했더니 파란색이 칠해졌다!", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
                        toast.show();
                    }
                });

                //좋아요 버튼 클릭 이벤트
                ((PostViewHolder100) holder).imageView_like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (contentDTOS.get(position).uid.equals(auth.getCurrentUser().getUid())) {
                            Toast toast = Toast.makeText(mContext, contentDTOS.get(position).userID + "님의 투표입니다!", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
                            toast.show();
                        } else {
                            onLikeClicked(firebaseDatabase.getReference().child("user_contents").child(uidLists.get(position)));
                        }

                        mDatabase.getReference().child("user_contents").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                contentDTOS.clear();
                                ArrayList<ContentDTO> contentDTOSTemp = new ArrayList<>();

                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    ContentDTO contentDTO = snapshot.getValue(ContentDTO.class);
                                    contentDTOSTemp.add(contentDTO);
                                }

                                Collections.reverse(contentDTOSTemp);
                                contentDTOS.addAll(contentDTOSTemp);

                                if (contentDTOS.get(position).likes.containsKey(auth.getCurrentUser().getUid())) {
                                    ((PostViewHolder100) holder).imageView_like.setImageResource(R.drawable.ic_thumb_up_blue);
                                    ((PostViewHolder100) holder).textView_likeCount.setText(String.valueOf(contentDTOS.get(position).likeCount));
                                } else {
                                    ((PostViewHolder100) holder).imageView_like.setImageResource(R.drawable.ic_outline_thumb_up_24px);
                                    ((PostViewHolder100) holder).textView_likeCount.setText(String.valueOf(contentDTOS.get(position).likeCount));
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                });

                //투표여부
                if (contentDTOS.get(position).contentPicker.containsKey(auth.getCurrentUser().getUid())) {
                    ((PostViewHolder100) holder).imageView_state.setImageResource(R.drawable.q);
                } else {
                    ((PostViewHolder100) holder).imageView_state.setImageResource(R.drawable.q_bg_w);
                }

                //좋아요, 따봉아이콘
                if (contentDTOS.get(position).likes.containsKey(auth.getCurrentUser().getUid())) {
                    ((PostViewHolder100) holder).imageView_like.setImageResource(R.drawable.ic_thumb_up_blue);
                    ((PostViewHolder100) holder).textView_likeCount.setText(String.valueOf(contentDTOS.get(position).likeCount));
                } else {
                    ((PostViewHolder100) holder).imageView_like.setImageResource(R.drawable.ic_outline_thumb_up_24px);
                    ((PostViewHolder100) holder).textView_likeCount.setText(String.valueOf(contentDTOS.get(position).likeCount));
                }

                //타이틀은 15자 이상이면 '...'표시
                if (contentDTOS.get(position).title.length() >= 15) {
                    ((PostViewHolder100) holder).textView_title.setText(contentDTOS.get(position).title.substring(0,15) + "...");
                } else {
                    ((PostViewHolder100) holder).textView_title.setText(contentDTOS.get(position).title);
                }
                ((PostViewHolder100) holder).textView_userName.setText(contentDTOS.get(position).userID);

                //각 아이템들 삽입
                ((PostViewHolder100)holder).textView_pollMode.setText(contentDTOS.get(position).pollMode + " / " + contentDTOS.get(position).itemViewType);
                ((PostViewHolder100)holder).textView_contentType.setText(contentDTOS.get(position).contentType);
                ((PostViewHolder100)holder).textView_hitCount.setText(String.valueOf(contentDTOS.get(position).contentHit));
                ((PostViewHolder100)holder).textView_replyCount.setText(" [" + String.valueOf(contentDTOS.get(position).replyCount) + "]");

                try{
                    DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
                    TrackSelection.Factory videoTrackSelectionFactory =
                            new AdaptiveTrackSelection.Factory(null);
                    DefaultTrackSelector trackSelector =
                            new DefaultTrackSelector(videoTrackSelectionFactory);
                    ((PostViewHolder100)holder).exo_play_post_0 = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector);
                    ((PostViewHolder100)holder).exo_play_post_1 = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector);
                    DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(mContext,
                            Util.getUserAgent(mContext, "AQA"), bandwidthMeter);

                    MediaSource videoSource0 = new ExtractorMediaSource.Factory(dataSourceFactory)
                            .createMediaSource(Uri.parse(contentDTOS.get(position).getVideoUrl_0()));

                    MediaSource videoSource1 = new ExtractorMediaSource.Factory(dataSourceFactory)
                            .createMediaSource(Uri.parse(contentDTOS.get(position).getVideoUrl_1()));
                    // Prepare the player with the source.
                    ((PostViewHolder100)holder).exo_play_post_0.prepare(videoSource0);
                    ((PostViewHolder100)holder).exo_play_post_1.prepare(videoSource1);
                    ((PostViewHolder100)holder).exo_play_postVideo_0.setPlayer(((PostViewHolder100)holder).exo_play_post_0);
                    ((PostViewHolder100)holder).exo_play_postVideo_1.setPlayer(((PostViewHolder100)holder).exo_play_post_1);
                } catch (Exception e) {
                    Log.w("lkj exception", e);
                }

                break;

            default:
                ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
                break;
        }
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
            if (contentDTOS.get(position).itemViewType == 102) { //video 투표
                Intent intent = new Intent(mContext, TestActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("contentKey", string);
                bundle.putInt("itemViewType", contentDTOS.get(position).itemViewType);
                bundle.putInt("contentHit", contentDTOS.get(position).contentHit);
                intent.putExtras(bundle);
//                mContext.startActivity(intent);


            } else { //image 투표
                Intent intent = new Intent(mContext, PollRankingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("contentKey", string);
                bundle.putInt("itemViewType", contentDTOS.get(position).itemViewType);
                bundle.putInt("contentHit", contentDTOS.get(position).contentHit);
                intent.putExtras(bundle);
                mContext.startActivity(intent);


            }
        }

        if (contentDTOS.get(position).getPollMode().equals("단일 투표")) {
            if (contentDTOS.get(position).itemViewType == 102) { //video 투표
                Intent intent = new Intent(mContext, TestActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("contentKey", string);
                bundle.putInt("itemViewType", contentDTOS.get(position).itemViewType);
                bundle.putInt("contentHit", contentDTOS.get(position).contentHit);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            } else { //image 투표
                Intent intent = new Intent(mContext, PollSingleActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("contentKey", string);
                bundle.putInt("itemViewType", contentDTOS.get(position).itemViewType);
                bundle.putInt("contentHit", contentDTOS.get(position).contentHit);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }


        }
    }


    @Override
    public int getItemCount() {
        return contentDTOS.size();
    }

//    private void resetHomeActivity() {
//        if (mContext instanceof HomeActivity) {
//            ((HomeActivity)mContext).resetActivity();
//        }
//    }
//
//    private int getUserClass() {
//        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
//        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
//        String uId = mUser.getUid();
//        mDatabase.child("users").child(uId).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                User user = dataSnapshot.getValue(User.class);
//                return user.getUserClass();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//        return 0;
//    }


    private void onLikeClicked(final DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                ContentDTO contentDTO = mutableData.getValue(ContentDTO.class);
                if (contentDTO == null) {
                    return Transaction.success(mutableData);
                }

                if (contentDTO.likes.containsKey(auth.getCurrentUser().getUid())) {
                    // Unstar the post and remove self from stars
                    // 좋아요카운트 -1 하고 리스트에서 삭제
                    contentDTO.likeCount = contentDTO.likeCount - 1;
                    contentDTO.likes.remove(auth.getCurrentUser().getUid());
                    // users/내uid/컨텐트key/false      : 좋아요 누른 컨텐츠 리스트 false
                    firebaseDatabase.getReference()
                            .child("users")
                            .child(auth.getCurrentUser().getUid())
                            .child("likeContent")
                            .child(contentDTO.getContentKey())
                            .setValue("false");

                } else {
                    // Star the post and add self to stars
                    contentDTO.likeCount = contentDTO.likeCount + 1;
                    contentDTO.likes.put(auth.getCurrentUser().getUid(), true);
                    // users/내uid/컨텐트key/true       : 좋아요 누른 컨텐츠 리스트 true
                    firebaseDatabase.getReference()
                            .child("users")
                            .child(auth.getCurrentUser().getUid())
                            .child("likeContent")
                            .child(contentDTO.getContentKey())
                            .setValue("true");
                }

                // Set value and report transaction success
                mutableData.setValue(contentDTO);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                // Transaction completed
                Log.d("lkjlkj", "postTransaction:onComplete:" + databaseError);


            }
        });
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = v.findViewById(R.id.progressBar1);
        }
    }
}
