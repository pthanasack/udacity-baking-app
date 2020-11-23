package com.example.bakingapp.IngredientsWidget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;

public class IngredientsWidgetService extends IntentService {

    public static final String ACTION_UPDATE_INGREDIENTS_LIST = "@string/ACTION_UPDATE_WIDGET_INGREDIENTS_LIST";

    public static final String EXTRA_RECIPE_POSITION_INTENT = "@string/EXTRA_RECIPE_POSITION_INTENT";

    public IngredientsWidgetService() {
        super("@strings/IngredientsWidgetService_STRING");
    }

    public static void startActionUpdateWidgetIngredients(Context context, Integer recipePosition) {
        Intent intent = new Intent(context, IngredientsWidgetService.class);
        intent.putExtra(EXTRA_RECIPE_POSITION_INTENT, recipePosition);
        intent.setAction(ACTION_UPDATE_INGREDIENTS_LIST);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE_INGREDIENTS_LIST.equals(action)) {
                handleActionUpdateIngredientsList(intent.getIntExtra(EXTRA_RECIPE_POSITION_INTENT, 0));
            }
        }
    }


    private void handleActionUpdateIngredientsList(Integer recipePosition){
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, IngredientsWidgetProvider.class));
        //Now update all widgets
        IngredientsWidgetProvider.updateAllAppWidget(this, appWidgetManager, appWidgetIds, recipePosition);
    }



}
