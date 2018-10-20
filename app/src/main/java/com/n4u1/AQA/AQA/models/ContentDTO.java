package com.n4u1.AQA.AQA.models;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class ContentDTO {
//    public String[] imageUrl = null;


    public String statistics_code = "0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0";

    public String imageUrl_0, imageUrl_1, imageUrl_2, imageUrl_3, imageUrl_4, imageUrl_5,
            imageUrl_6, imageUrl_7, imageUrl_8, imageUrl_9;
    public String videoUrl_0, videoUrl_1, videoUrl_2, videoUrl_3, videoUrl_4, videoUrl_5,
            videoUrl_6, videoUrl_7, videoUrl_8, videoUrl_9;

    public String title;
    public String description;
    public String uid;
    public String userID;
    public String pollMode;
    public String contentType;
    public String contentId;
    public String uploadDate;
    public String contentKey;
    public String replyDate;

    public boolean isUserLike;
    public int pickCandidate = 0;
    public int candidateScore_0 = 0;
    public int candidateScore_1 = 0;
    public int candidateScore_2 = 0;
    public int candidateScore_3 = 0;
    public int candidateScore_4 = 0;
    public int candidateScore_5 = 0;
    public int candidateScore_6 = 0;
    public int candidateScore_7 = 0;
    public int candidateScore_8 = 0;
    public int candidateScore_9 = 0;
    public int contentHit = 0;
    public int likeCount = 0;
    public int itemViewType;
    public int replyCount = 0;
    public Map<String, String> reply = new HashMap<>();
    public Map<String, Boolean> likes = new HashMap<>();
    public Map<String, Integer> contentPicker = new HashMap<>();



    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("candidateScore_0", candidateScore_0);
        result.put("candidateScore_1", candidateScore_1);
        result.put("candidateScore_2", candidateScore_2);
        result.put("candidateScore_3", candidateScore_3);
        result.put("candidateScore_4", candidateScore_4);
        result.put("candidateScore_5", candidateScore_5);
        result.put("candidateScore_6", candidateScore_6);
        result.put("candidateScore_7", candidateScore_7);
        result.put("candidateScore_8", candidateScore_8);
        result.put("candidateScore_9", candidateScore_9);
        result.put("videoUrl_0", videoUrl_0);
        result.put("videoUrl_1", videoUrl_1);
        result.put("videoUrl_2", videoUrl_2);
        result.put("videoUrl_3", videoUrl_3);
        result.put("videoUrl_4", videoUrl_4);
        result.put("videoUrl_5", videoUrl_5);
        result.put("videoUrl_6", videoUrl_6);
        result.put("videoUrl_7", videoUrl_7);
        result.put("videoUrl_8", videoUrl_8);
        result.put("videoUrl_9", videoUrl_9);
        result.put("imageUrl_0", imageUrl_0);
        result.put("imageUrl_1", imageUrl_1);
        result.put("imageUrl_2", imageUrl_2);
        result.put("imageUrl_3", imageUrl_3);
        result.put("imageUrl_4", imageUrl_4);
        result.put("imageUrl_5", imageUrl_5);
        result.put("imageUrl_6", imageUrl_6);
        result.put("imageUrl_7", imageUrl_7);
        result.put("imageUrl_8", imageUrl_8);
        result.put("imageUrl_9", imageUrl_9);

//        result.put("contentPicker", contentPicker);


        return result;
    }

    public ContentDTO(){}

    public String getStatistics_code() {
        return statistics_code;
    }

    public void setStatistics_code(String statistics_code) {
        this.statistics_code = statistics_code;
    }

    public String getVideoUrl_0() {
        return videoUrl_0;
    }

    public void setVideoUrl_0(String videoUrl_0) {
        this.videoUrl_0 = videoUrl_0;
    }

    public String getVideoUrl_1() {
        return videoUrl_1;
    }

    public void setVideoUrl_1(String videoUrl_1) {
        this.videoUrl_1 = videoUrl_1;
    }

    public String getVideoUrl_2() {
        return videoUrl_2;
    }

    public void setVideoUrl_2(String videoUrl_2) {
        this.videoUrl_2 = videoUrl_2;
    }

    public String getVideoUrl_3() {
        return videoUrl_3;
    }

    public void setVideoUrl_3(String videoUrl_3) {
        this.videoUrl_3 = videoUrl_3;
    }

    public String getVideoUrl_4() {
        return videoUrl_4;
    }

    public void setVideoUrl_4(String videoUrl_4) {
        this.videoUrl_4 = videoUrl_4;
    }

    public String getVideoUrl_5() {
        return videoUrl_5;
    }

    public void setVideoUrl_5(String videoUrl_5) {
        this.videoUrl_5 = videoUrl_5;
    }

    public String getVideoUrl_6() {
        return videoUrl_6;
    }

    public void setVideoUrl_6(String videoUrl_6) {
        this.videoUrl_6 = videoUrl_6;
    }

    public String getVideoUrl_7() {
        return videoUrl_7;
    }

    public void setVideoUrl_7(String videoUrl_7) {
        this.videoUrl_7 = videoUrl_7;
    }

    public String getVideoUrl_8() {
        return videoUrl_8;
    }

    public void setVideoUrl_8(String videoUrl_8) {
        this.videoUrl_8 = videoUrl_8;
    }

    public String getVideoUrl_9() {
        return videoUrl_9;
    }

    public void setVideoUrl_9(String videoUrl_9) {
        this.videoUrl_9 = videoUrl_9;
    }

    public Map<String, String> getReply() {
        return reply;
    }

    public void setReply(Map<String, String> reply) {
        this.reply = reply;
    }

    public String getImageUrl_0() {
        return imageUrl_0;
    }

    public void setImageUrl_0(String imageUrl_0) {
        this.imageUrl_0 = imageUrl_0;
    }

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

    public String getImageUrl_3() {
        return imageUrl_3;
    }

    public void setImageUrl_3(String imageUrl_3) {
        this.imageUrl_3 = imageUrl_3;
    }

    public String getImageUrl_4() {
        return imageUrl_4;
    }

    public void setImageUrl_4(String imageUrl_4) {
        this.imageUrl_4 = imageUrl_4;
    }

    public String getImageUrl_5() {
        return imageUrl_5;
    }

    public void setImageUrl_5(String imageUrl_5) {
        this.imageUrl_5 = imageUrl_5;
    }

    public String getImageUrl_6() {
        return imageUrl_6;
    }

    public void setImageUrl_6(String imageUrl_6) {
        this.imageUrl_6 = imageUrl_6;
    }

    public String getImageUrl_7() {
        return imageUrl_7;
    }

    public void setImageUrl_7(String imageUrl_7) {
        this.imageUrl_7 = imageUrl_7;
    }

    public String getImageUrl_8() {
        return imageUrl_8;
    }

    public void setImageUrl_8(String imageUrl_8) {
        this.imageUrl_8 = imageUrl_8;
    }

    public String getImageUrl_9() {
        return imageUrl_9;
    }

    public void setImageUrl_9(String imageUrl_9) {
        this.imageUrl_9 = imageUrl_9;
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

    public String getPollMode() {
        return pollMode;
    }

    public void setPollMode(String pollMode) {
        this.pollMode = pollMode;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
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

    public String getContentKey() {
        return contentKey;
    }

    public void setContentKey(String contentKey) {
        this.contentKey = contentKey;
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

    public int getPickCandidate() {
        return pickCandidate;
    }

    public void setPickCandidate(int pickCandidate) {
        this.pickCandidate = pickCandidate;
    }

    public int getCandidateScore_0() {
        return candidateScore_0;
    }

    public void setCandidateScore_0(int candidateScore_0) {
        this.candidateScore_0 = candidateScore_0;
    }

    public int getCandidateScore_1() {
        return candidateScore_1;
    }

    public void setCandidateScore_1(int candidateScore_1) {
        this.candidateScore_1 = candidateScore_1;
    }

    public int getCandidateScore_2() {
        return candidateScore_2;
    }

    public void setCandidateScore_2(int candidateScore_2) {
        this.candidateScore_2 = candidateScore_2;
    }

    public int getCandidateScore_3() {
        return candidateScore_3;
    }

    public void setCandidateScore_3(int candidateScore_3) {
        this.candidateScore_3 = candidateScore_3;
    }

    public int getCandidateScore_4() {
        return candidateScore_4;
    }

    public void setCandidateScore_4(int candidateScore_4) {
        this.candidateScore_4 = candidateScore_4;
    }

    public int getCandidateScore_5() {
        return candidateScore_5;
    }

    public void setCandidateScore_5(int candidateScore_5) {
        this.candidateScore_5 = candidateScore_5;
    }

    public int getCandidateScore_6() {
        return candidateScore_6;
    }

    public void setCandidateScore_6(int candidateScore_6) {
        this.candidateScore_6 = candidateScore_6;
    }

    public int getCandidateScore_7() {
        return candidateScore_7;
    }

    public void setCandidateScore_7(int candidateScore_7) {
        this.candidateScore_7 = candidateScore_7;
    }

    public int getCandidateScore_8() {
        return candidateScore_8;
    }

    public void setCandidateScore_8(int candidateScore_8) {
        this.candidateScore_8 = candidateScore_8;
    }

    public int getCandidateScore_9() {
        return candidateScore_9;
    }

    public void setCandidateScore_9(int candidateScore_9) {
        this.candidateScore_9 = candidateScore_9;
    }

    public int getContentHit() {
        return contentHit;
    }

    public void setContentHit(int contentHit) {
        this.contentHit = contentHit;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getItemViewType() {
        return itemViewType;
    }

    public void setItemViewType(int itemViewType) {
        this.itemViewType = itemViewType;
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

    public Map<String, Integer> getContentPicker() {
        return contentPicker;
    }

    public void setContentPicker(Map<String, Integer> contentPicker) {
        this.contentPicker = contentPicker;
    }
}
