package com.n4u1.AQA.AQA.models;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class User {

    private String tmpImgUrl_0,tmpImgUrl_1,tmpImgUrl_2,tmpImgUrl_3,tmpImgUrl_4,tmpImgUrl_5,tmpImgUrl_6,tmpImgUrl_7,tmpImgUrl_8,tmpImgUrl_9;
    private String deviceName, sex, job, uid, email, userId, guid;
    private String pickContent;
    private int age, userClass;
    private String search_flag_category, search_flag_title;

    public Map<String, Boolean> likeContent = new HashMap<>();

    public User () {    }

    public User(String tmpImgUrl_0, String tmpImgUrl_1, String tmpImgUrl_2, String tmpImgUrl_3, String tmpImgUrl_4, String tmpImgUrl_5, String tmpImgUrl_6, String tmpImgUrl_7, String tmpImgUrl_8, String tmpImgUrl_9, String deviceName, String sex, String job, String uid, String email, String userId, String pickContent, int age, String search_flag_category, String search_flag_title, Map<String, Boolean> likeContent) {
        this.tmpImgUrl_0 = tmpImgUrl_0;
        this.tmpImgUrl_1 = tmpImgUrl_1;
        this.tmpImgUrl_2 = tmpImgUrl_2;
        this.tmpImgUrl_3 = tmpImgUrl_3;
        this.tmpImgUrl_4 = tmpImgUrl_4;
        this.tmpImgUrl_5 = tmpImgUrl_5;
        this.tmpImgUrl_6 = tmpImgUrl_6;
        this.tmpImgUrl_7 = tmpImgUrl_7;
        this.tmpImgUrl_8 = tmpImgUrl_8;
        this.tmpImgUrl_9 = tmpImgUrl_9;
        this.deviceName = deviceName;
        this.sex = sex;
        this.job = job;
        this.uid = uid;
        this.email = email;
        this.userId = userId;
        this.pickContent = pickContent;
        this.age = age;
        this.search_flag_category = search_flag_category;
        this.search_flag_title = search_flag_title;
        this.likeContent = likeContent;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("guid", guid);
        result.put("userclass", userClass);
        result.put("userId", userId);
        result.put("likeContent", likeContent);
        result.put("sex", sex);
        result.put("age", age);
        result.put("job", job);
        result.put("search_flag_category", search_flag_category);
        result.put("search_flag_title", search_flag_title);

        return result;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public int getUserClass() {
        return userClass;
    }

    public void setUserClass(int userClass) {
        this.userClass = userClass;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSearch_flag_category() {
        return search_flag_category;
    }

    public void setSearch_flag_category(String search_flag_category) {
        this.search_flag_category = search_flag_category;
    }

    public String getSearch_flag_title() {
        return search_flag_title;
    }

    public void setSearch_flag_title(String search_flag_title) {
        this.search_flag_title = search_flag_title;
    }

    public Map<String, Boolean> getLikeContent() {
        return likeContent;
    }

    public void setLikeContent(Map<String, Boolean> likeContent) {
        this.likeContent = likeContent;
    }

    public String getTmpImgUrl_0() {
        return tmpImgUrl_0;
    }

    public void setTmpImgUrl_0(String tmpImgUrl_0) {
        this.tmpImgUrl_0 = tmpImgUrl_0;
    }

    public String getTmpImgUrl_1() {
        return tmpImgUrl_1;
    }

    public void setTmpImgUrl_1(String tmpImgUrl_1) {
        this.tmpImgUrl_1 = tmpImgUrl_1;
    }

    public String getTmpImgUrl_2() {
        return tmpImgUrl_2;
    }

    public void setTmpImgUrl_2(String tmpImgUrl_2) {
        this.tmpImgUrl_2 = tmpImgUrl_2;
    }

    public String getTmpImgUrl_3() {
        return tmpImgUrl_3;
    }

    public void setTmpImgUrl_3(String tmpImgUrl_3) {
        this.tmpImgUrl_3 = tmpImgUrl_3;
    }

    public String getTmpImgUrl_4() {
        return tmpImgUrl_4;
    }

    public void setTmpImgUrl_4(String tmpImgUrl_4) {
        this.tmpImgUrl_4 = tmpImgUrl_4;
    }

    public String getTmpImgUrl_5() {
        return tmpImgUrl_5;
    }

    public void setTmpImgUrl_5(String tmpImgUrl_5) {
        this.tmpImgUrl_5 = tmpImgUrl_5;
    }

    public String getTmpImgUrl_6() {
        return tmpImgUrl_6;
    }

    public void setTmpImgUrl_6(String tmpImgUrl_6) {
        this.tmpImgUrl_6 = tmpImgUrl_6;
    }

    public String getTmpImgUrl_7() {
        return tmpImgUrl_7;
    }

    public void setTmpImgUrl_7(String tmpImgUrl_7) {
        this.tmpImgUrl_7 = tmpImgUrl_7;
    }

    public String getTmpImgUrl_8() {
        return tmpImgUrl_8;
    }

    public void setTmpImgUrl_8(String tmpImgUrl_8) {
        this.tmpImgUrl_8 = tmpImgUrl_8;
    }

    public String getTmpImgUrl_9() {
        return tmpImgUrl_9;
    }

    public void setTmpImgUrl_9(String tmpImgUrl_9) {
        this.tmpImgUrl_9 = tmpImgUrl_9;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPickContent() {
        return pickContent;
    }

    public void setPickContent(String pickContent) {
        this.pickContent = pickContent;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

}
