package com.example.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RecipeDetailActivity extends AppCompatActivity implements StepsAdapter.StepAdapterOnClickHandler {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        // get the intent and display related info
        Intent mDetailIntent = getIntent();
        if (mDetailIntent.hasExtra(Intent.EXTRA_TEXT)) {
            LinearLayout activity_recipe_detail_ingredients_ll = (LinearLayout) findViewById(R.id.activity_recipe_detail_ingredients_ll);
            //convert the data in array format from JSON String
            String recipeDetailsString = mDetailIntent.getStringExtra(Intent.EXTRA_TEXT);
            JSONObject recipeDetailsJSONObject;

            try {
                assert recipeDetailsString != null;
                recipeDetailsJSONObject = new JSONObject(recipeDetailsString);

                //arrange the ingredients in a linear layout list
                String ingredientsString = recipeDetailsJSONObject.getString("ingredients");
                JSONArray ingredientsJSONArray = new JSONArray(ingredientsString);

                for (int i = 0; i < ingredientsJSONArray.length(); i++) {

                    LinearLayout row = new LinearLayout(RecipeDetailActivity.this);
                    row.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    try {
                        //get the ingredient data from the JSON
                        JSONObject IngredientDataJSONObject = ingredientsJSONArray.getJSONObject(i);
                        String ingredientQuantity = IngredientDataJSONObject.getString("quantity");
                        String ingredientMeasure = IngredientDataJSONObject.getString("measure");
                        String ingredientName = IngredientDataJSONObject.getString("ingredient");
                        //create a new label textview for the ingredient
                        TextView ingredientFormatedTv = new TextView(RecipeDetailActivity.this);
                        //set the text for the button and the label
                        String ingredientFormatedLabel = i + ". " + ingredientQuantity + " " + ingredientMeasure + " of " + ingredientName;
                        ingredientFormatedTv.setText(ingredientFormatedLabel);
                        //add the button and label to the row
                        row.addView(ingredientFormatedTv);
                        //add the full row to the Trailer LinearLayout
                        activity_recipe_detail_ingredients_ll.addView(row);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


                //generate the StepsList fragment
                StepsListFragment stepsListFragment = new StepsListFragment(recipeDetailsString, RecipeDetailActivity.this);
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .add(R.id.activity_recipe_detail_framelayout, stepsListFragment)
                        .commit();


            } catch (JSONException e) {
                e.printStackTrace();
            }//end try JSOn


        }


    }

    @Override
    public void onClick(String mStepData) {
        // Intent parameters: context, destination clas
        Context context = this;
        Class destinationClass = StepsDetailActivity.class;
        //new intent with extra_text
        Intent startDetailedActivityIntent = new Intent(context, destinationClass);
        startDetailedActivityIntent.putExtra(Intent.EXTRA_TEXT, mStepData);
        //start new activity with Intent
        startActivity(startDetailedActivityIntent);

    }
}


