package com.example.android.newsapp;

import android.content.Context;
import android.content.AsyncTaskLoader;

import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<News>> {

    private static final String TAG = NewsLoader.class.getName();

    private String mUrl;

    public NewsLoader(Context context, String url) {

        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<News> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        List<News> news = Queryutils.fetchNewsData(mUrl);
        return news;
    }
}
