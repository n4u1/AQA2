package com.n4u1.AQA.AQA.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.n4u1.AQA.AQA.R;
import com.n4u1.AQA.AQA.models.NoticeDTO;
import com.n4u1.AQA.AQA.views.NoticeDetailActivity;

import java.util.ArrayList;

public class NoticeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private ArrayList<NoticeDTO> noticeDTO;

    public NoticeAdapter(Context mContext, ArrayList<NoticeDTO> noticeDTO) {
        this.mContext = mContext;
        this.noticeDTO = noticeDTO;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notice_item, viewGroup, false);
        return new NoticeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int i) {
        ((NoticeViewHolder) viewHolder).notice_textView_date.setText(noticeDTO.get(i).getDate());
        ((NoticeViewHolder) viewHolder).notice_textView_title.setText(noticeDTO.get(i).getTitle());
        ((NoticeViewHolder) viewHolder).notice_textView_number.setText(String.valueOf(noticeDTO.get(i).getNumber()));
        ((NoticeViewHolder) viewHolder).notice_linearLayout_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, NoticeDetailActivity.class);
                intent.putExtra("noticeKey", noticeDTO.get(i).getNoticeKey());
                mContext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return noticeDTO.size();
    }
}
