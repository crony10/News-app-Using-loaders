package com.example.android.newsapp;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class NewsAdapter extends ArrayAdapter<News> {
    public NewsAdapter(Activity context, ArrayList<News> news) {
        super(context, 0, news);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        News currentNews = getItem(position);

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_item, parent, false);
        }

        TextView titleText = listItemView.findViewById(R.id.Title);
        titleText.setText(currentNews.getTitle());

        TextView dateText = listItemView.findViewById(R.id.date);
        dateText.setText(currentNews.getDate());

        TextView typeText = listItemView.findViewById(R.id.type);
        typeText.setText(currentNews.getType());

        TextView sectionText = listItemView.findViewById(R.id.section);
        sectionText.setText(currentNews.getSection());

        TextView authorText = listItemView.findViewById(R.id.author);
        authorText.setText(currentNews.getAuthor());

        return listItemView;
    }
}
