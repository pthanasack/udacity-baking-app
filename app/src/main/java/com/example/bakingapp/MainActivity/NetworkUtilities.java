package com.example.bakingapp.MainActivity;

import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;


public class NetworkUtilities {

    //Build the URL to get the movies for Main activity
    public static URL getDataURL(String apiKey, String baseUrl, String apiKeyQueryParam) {

        Uri.Builder builtUri = Uri.parse(baseUrl).buildUpon()
                .appendQueryParameter(apiKeyQueryParam, apiKey);
        URL dataUrl = null;
        try {
            dataUrl = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return dataUrl;
    }

    //generic method to get HttpResponse to URL request
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    //specific method to get an String[] Array from the movie data json in string format - each entry is json movie data
    public static String[] getArrayFromJSON(String jsonString) {
        String[] arrayData = null;

        try {
/*
no need to use an object as the recipe data is already an array
            JSONObject dataJSONObject = new JSONObject(jsonString);
            JSONArray dataResults = dataJSONObject.getJSONArray();
*/
            JSONArray dataResults = new JSONArray(jsonString);
            arrayData = new String[dataResults.length()];
            for (int i = 0; i < dataResults.length(); i++) {
                JSONObject movie = dataResults.getJSONObject(i);
                arrayData[i] = movie.toString();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayData;
    }

}



