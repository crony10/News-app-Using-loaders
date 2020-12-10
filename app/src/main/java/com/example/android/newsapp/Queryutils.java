package com.example.android.newsapp;
import android.text.TextUtils;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public final class Queryutils {

    private static final String LOG_TAG = Queryutils.class.getSimpleName();

    private Queryutils() {

    }

    public static List<News> fetchNewsData(String req_url) {

        URL url = createUrl(req_url);

        String jsonResponse = null;

        try {
            jsonResponse = makeHttpReq(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem in making HTTP req.", e);
        }
        List<News> news = extractResponseFromJson(jsonResponse);

        return news;
    }

    private static String makeHttpReq(URL url) throws IOException {

        String jsonResponse = "null";

        if (url == null) {
            return jsonResponse;
        }


        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);/*in milliseconds*/
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error retrieving the json response ", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {

        StringBuilder output = new StringBuilder();

        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static URL createUrl(String req_url) {

        URL url = null;

        try {
            url = new URL(req_url);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the url: ", e);
        }
        return url;
    }

    private static String correctDate(String date) {

        String correctedDate = date.substring(0, date.lastIndexOf("T"));
        Log.d(LOG_TAG, "correctDate is: " + correctedDate);
        return correctedDate;
    }

    private static List<News> extractResponseFromJson(String newsJson) {

        if (TextUtils.isEmpty(newsJson)) {
            return null;
        }
        List<News> news = new ArrayList<>();

        try {
            JSONObject baseJsonResponse = new JSONObject(newsJson);

            JSONObject newsObject = baseJsonResponse.getJSONObject("response");

            JSONArray newsArray = newsObject.getJSONArray("results");

            for (int i = 0; i < newsArray.length(); i++) {

                JSONObject currentNews = newsArray.getJSONObject(i);

                String type = "type: ";

                String title = "title: ";

                String section = "section: ";

                String date = "date: ";

                String url = currentNews.getString("webUrl");

                type = type.concat(currentNews.getString("type"));

                title = title.concat(currentNews.getString("webTitle"));

                section = section.concat(currentNews.getString("sectionName"));

                date = date.concat(currentNews.getString("webPublicationDate"));

                date = correctDate(date);

                JSONArray authorArray = currentNews.getJSONArray("tags");

                String authorName = "";

                for (int j = 0; j < authorArray.length(); j++) {

                    JSONObject authorObject = authorArray.getJSONObject(j);

                    Log.i(LOG_TAG, "extractResponseFromJson: " + authorArray);

                    if (authorObject != null) {
                        String authorFirstName = authorObject.getString("firstName");
                        authorFirstName = authorFirstName.concat(" ");
                        String authorLastName = authorObject.getString("lastName");
                        authorName = authorFirstName.concat(authorLastName);
                    } else break;
                }
                if (authorName.equals("")) {
                    authorName = "No author";
                }
                News newsList = new News(title, date, type, section, url, authorName);

                news.add(newsList);
            }
        } catch (JSONException e) {

            Log.e("QueryUtils", "problem thai gai in parsing news JSON results", e);
        }
        return news;
    }
}