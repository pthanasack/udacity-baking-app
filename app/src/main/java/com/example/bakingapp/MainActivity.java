package com.example.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements MainAdapter.MainAdapterOnClickHandler {

    private ProgressBar mProgressBar;
    private TextView MainTextView;

    private MainAdapter mMainAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressBar = (ProgressBar) findViewById(R.id.main_progress_bar);
        MainTextView = (TextView) findViewById(R.id.main_text);
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.main_recycler_view);

        LinearLayoutManager linearLayoutManager
                = new LinearLayoutManager (this, LinearLayoutManager.VERTICAL, false);

        //Set the layoutManager on RecyclerView
        mRecyclerView.setLayoutManager(linearLayoutManager);

        //new MoviesAdapter
        mMainAdapter = new MainAdapter(this);
        //set the adapter to the RecyclerView
        mRecyclerView.setAdapter(mMainAdapter);


        mProgressBar.setVisibility(View.VISIBLE);
      // MainTextView.setVisibility(View.INVISIBLE);
        String mApiKey = "";
        String mApiKeyQueryParam = "";
        String mBaseUrl = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
        URL mRecipesUrl = NetworkUtilities.getDataURL(mApiKey, mBaseUrl, mApiKeyQueryParam);
        new loadRecipes().execute(mRecipesUrl);
    }

    @Override
    public void onClick(String mMainData) {
        // Intent parameters: context, destination class
        Context context = this;
        Class destinationClass = RecipeDetailActivity.class;
        //new intent with extra_text
        Intent startDetailedActivityIntent = new Intent(context, destinationClass);
        startDetailedActivityIntent.putExtra(Intent.EXTRA_TEXT, mMainData);
        //start new activity with Intent
        startActivity(startDetailedActivityIntent);

    }


    public class loadRecipes extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            String mRecipesData = null;
            try {
                    mRecipesData = NetworkUtilities.getResponseFromHttpUrl(url);
            } catch (IOException io){
                io.printStackTrace();
            }
            return mRecipesData;
        }

        @Override
        protected void onPostExecute(String mRecipesData) {
            if (mRecipesData!=null && !mRecipesData.equals(""))
            {
                String[] mArrayRecipesData =NetworkUtilities.getArrayFromJSON(mRecipesData);

               // MainTextView.setText(mArrayRecipesData[0]);
                //pass the String[] to the MainAdapter to display into RecyclerView
                mMainAdapter.setMainData(mArrayRecipesData);
            }
            else {
                MainTextView.setText(R.string.error_msg_catching_data);
                MainTextView.setVisibility(View.VISIBLE);
            }
            mProgressBar.setVisibility(View.INVISIBLE);


        }
    }

}