package com.n4u1.AQA.AQA.models;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class SuggestDTO {

    public String imageUrl_1, imageUrl_2;

    public String title;
    public String description;
    public String uid;
    public String userID;
    public String contentId;
    public String uploadDate;
    public String suggestKey;
    public String replyDate;
    public String userEmail;

    public boolean isUserLike;
    public int likeCount = 0;

    public int replyCount = 0;
    public Map<String, String> reply = new HashMap<>();
    public Map<String, Boolean> likes = new HashMap<>();




    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
//        result.put("contentPicker", contentPicker);


        return result;
    }

    public SuggestDTO(){}


    public String getImageUrl_1() {
        return imageUrl_1;
    }

    public void setImageUrl_1(String imageUrl_1) {
        this.imageUrl_1 = imageUrl_1;
    }

    public String getImageUrl_2() {
        return imageUrl_2;
    }

    public void setImageUrl_2(String imageUrl_2) {
        this.imageUrl_2 = imageUrl_2;
    }

    public String getSuggestKey() {
        return suggestKey;
    }

    public void setSuggestKey(String suggestKey) {
        this.suggestKey = suggestKey;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }



    public Map<String, String> getReply() {
        return reply;
    }

    public void setReply(Map<String, String> reply) {
        this.reply = reply;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }



    public String getReplyDate() {
        return replyDate;
    }

    public void setReplyDate(String replyDate) {
        this.replyDate = replyDate;
    }

    public boolean isUserLike() {
        return isUserLike;
    }

    public void setUserLike(boolean userLike) {
        isUserLike = userLike;
    }




    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    public Map<String, Boolean> getLikes() {
        return likes;
    }

    public void setLikes(Map<String, Boolean> likes) {
        this.likes = likes;
    }
}
