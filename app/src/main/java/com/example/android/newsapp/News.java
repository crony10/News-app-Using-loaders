package com.example.android.newsapp;

import android.support.annotation.NonNull;

public class News {

    private String mTitle;

    private String mDate;

    private String mType;

    private String mSection;

    private String mUrl;

    public News(String title, String date, String type, String section,String url){

        mTitle = title;
        mDate = date;
        mType = type;
        mSection = section;
        mUrl = url;
    }

    public String getDate() {
        return mDate;
    }

    public String getSection() {
        return mSection;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getType() {
        return mType;
    }

    public String getUrl(){
        return mUrl;
    }

    @NonNull
    @Override
    public String toString() {
        return "News{" +
                "mTitle" + mTitle +
                ", mDate= ' " + mDate +'\''+
                ", mType= ' " + mType +'\''+
                ", mSection=" + mSection +
                '}';
    }
}
