package com.example.android.newsapp;

import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;

import  android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<News>> {

    public static final String request_url = "https://content.guardianapis.com/search?q=debates&api-key=test";

    public static final String LOG_TAG = MainActivity.class.getName();

    private TextView mEmptyStateTextView;

    private NewsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_list);
        //ArrayList<News> news = new ArrayList<News>();

//        news.add(new News("Donald Trump lost","10/10/2001","Article","Politics"));
//        news.add(new News("Donald Trump lost","10/10/2001","Article","Politics"));
//        news.add(new News("Donald Trump lost","10/10/2001","Article","Politics"));
//        news.add(new News("Donald Trump lost","10/10/2001","Article","Politics"));

        //NewsAdapter adapter =
                //new NewsAdapter(this,news);
        ListView listView = (ListView)findViewById(R.id.news_list);
        //listView.setAdapter(adapter);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        listView.setEmptyView(mEmptyStateTextView);

        mAdapter = new NewsAdapter(this,new ArrayList<News>());

        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                News currentNews = mAdapter.getItem(position);

                Uri newsUri = Uri.parse(currentNews.getUrl());

                Intent websiteIntent =new Intent(Intent.ACTION_VIEW,newsUri);

                startActivity(websiteIntent);
            }
        });

        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected()){

            //NewsAsyncTask task = new NewsAsyncTask();
            //task.execute(request_url);
            LoaderManager loaderManager = getLoaderManager();

            loaderManager.initLoader(0,null,this);
        }

        else{
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            mEmptyStateTextView.setText(R.string.no_internet);
        }



    }
//    private class NewsAsyncTask extends AsyncTask<String,Void,List<News>>{
//
//        @Override
//        protected List<News> doInBackground(String... urls) {
//            if(urls.length < 1 || urls[0] == null) {
//                return null;
//            }
//
//            List<News> result = Queryutils.fetchNewsData(urls[0]);
//            return result;
//        }
//
//        @Override
//        protected void onPostExecute(List<News> data) {
//            mAdapter.clear();
//
//            if(data != null && !data.isEmpty()){
//                mAdapter.addAll(data);
//            }
//        }
//    }


    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        return new NewsLoader(this,request_url);
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {

        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        mEmptyStateTextView.setText(R.string.no_news);
        mAdapter.clear();

        if(news != null && !news.isEmpty()){
            mAdapter.addAll(news);
        }
    }

    @Override
    public void onLoaderReset( Loader<List<News>> loader) {
        mAdapter.clear();
    }
}