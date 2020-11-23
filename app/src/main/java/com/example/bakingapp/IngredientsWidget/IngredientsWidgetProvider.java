package com.example.bakingapp.IngredientsWidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.RemoteViews;

import com.example.bakingapp.MainActivity.MainActivity;
import com.example.bakingapp.MainActivity.NetworkUtilities;
import com.example.bakingapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientsWidgetProvider extends AppWidgetProvider {



    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, int recipePosition) {
        String mApiKey = "";
        String mApiKeyQueryParam = "";
        String mBaseUrl = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
        URL mRecipesUrl = NetworkUtilities.getDataURL(mApiKey, mBaseUrl, mApiKeyQueryParam);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget);
        //set default onclick that open MainActivity
        // Add the plant ID as extra to water only that plant when clicked
        Intent defaultIntent = new Intent(context, MainActivity.class);
        PendingIntent defaultPendingIntent = PendingIntent.getActivity(context, 0, defaultIntent, 0);
        views.setOnClickPendingIntent(R.id.ingredients_widget_main_linearlayout, defaultPendingIntent);
        appWidgetManager.updateAppWidget(appWidgetId, views);
        //get the data and update the widgets
        new IngredientsWidgetProvider.loadRecipesforWidget(views, appWidgetId , appWidgetManager, recipePosition).execute(mRecipesUrl);

    }

    static void updateAllAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int[] appWidgetIds, int recipePosition) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, recipePosition);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        // There may be multiple widgets active, so update all of them
        // by default with recipe number 0
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, 0);
        }

    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    //handle the view update within the asynctask to make sure the http request is over before you use the Json String
    public static class loadRecipesforWidget extends AsyncTask<URL, Void, String> {

        private RemoteViews mViews;
        private int mWidgetID;
        private AppWidgetManager mWidgetManager;
        private int mRecipePosition;

        public loadRecipesforWidget(RemoteViews views, int appWidgetID, AppWidgetManager appWidgetManager, int recipePosition){
            this.mViews = views;
            this.mWidgetID = appWidgetID;
            this.mWidgetManager = appWidgetManager;
            this.mRecipePosition = recipePosition;


        }

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
                //clear all previous child views
                mViews.removeAllViews(R.id.appwidget_linearlayout);
                //
                //convert the data in array format from JSON String
                String[]  mArrayRecipesData = NetworkUtilities.getArrayFromJSON(mRecipesData);
                JSONObject mRecipeDetailsJSONObject = null;
                try {
                    mRecipeDetailsJSONObject = new JSONObject(mArrayRecipesData[mRecipePosition]);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //get the recipe nam
                String recipeNameString = null;
                try {
                    recipeNameString = mRecipeDetailsJSONObject.getString("name");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //set the text for the textview
                RemoteViews titleLayout = new RemoteViews(mViews.getPackage(), R.layout.ingredients_widget_item);
                titleLayout.setTextViewText( R.id.ingredients_widget_item_textview, recipeNameString);
                mViews.addView(R.id.appwidget_linearlayout, titleLayout);
                //get ingredients list as a string Array
                String ingredientsString = null;
                try {
                    ingredientsString = mRecipeDetailsJSONObject.getString("ingredients");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONArray ingredientsJSONArray = null;
                try {
                    ingredientsJSONArray = new JSONArray(ingredientsString);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < ingredientsJSONArray.length(); i++) {
                    //into a ingredients item,
                    RemoteViews listEntryLayout = new RemoteViews(mViews.getPackage(), R.layout.ingredients_widget_item);

                    try {
                        //get the ingredient data from the JSON
                        JSONObject IngredientDataJSONObject = ingredientsJSONArray.getJSONObject(i);
                        String ingredientQuantity = IngredientDataJSONObject.getString("quantity");
                        String ingredientMeasure = IngredientDataJSONObject.getString("measure");
                        String ingredientName = IngredientDataJSONObject.getString("ingredient");
                        //create a new label textview for the ingredient
                        String ingredientFormatedLabel = "- " + ingredientQuantity + " " + ingredientMeasure + " of " + ingredientName;
                        //set the text for the textview
                        listEntryLayout.setTextViewText( R.id.ingredients_widget_item_textview, ingredientFormatedLabel);
                        //add the textView to the remote view
                        mViews.addView(R.id.appwidget_linearlayout, listEntryLayout);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                // Instruct the widget manager to update the widget
                mWidgetManager.updateAppWidget(mWidgetID, mViews);
            }
        }
    }


}