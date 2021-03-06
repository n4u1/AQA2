package com.n4u1.AQA.AQA.models;

import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

public class ReplyDTO {
    private String id;
    private String reply;
    private String date;
    private String contentKey, replyKey, uId;
    private int qPoint;

    public int likeCount = 0;


    public Map<String, Boolean> likes = new HashMap<>();

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public int getqPoint() {
        return qPoint;
    }

    public void setqPoint(int qPoint) {
        this.qPoint = qPoint;
    }

    public String getReplyKey() {
        return replyKey;
    }

    public void setReplyKey(String replyKey) {
        this.replyKey = replyKey;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public String getContentKey() {
        return contentKey;
    }

    public void setContentKey(String contentKey) {
        this.contentKey = contentKey;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
