package com.n4u1.AQA.AQA.models;

public class NoticeDTO {
    private String title;
    private String date;
    private String description;
    private String noticeKey;
    private int number;


    public String getNoticeKey() {
        return noticeKey;
    }

    public void setNoticeKey(String noticeKey) {
        this.noticeKey = noticeKey;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
