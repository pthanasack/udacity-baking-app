package com.example.bakingapp.RecipeDetailActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.bakingapp.MainActivity.NetworkUtilities;
import com.example.bakingapp.R;
import com.example.bakingapp.StepDetail.StepDetailActivity;
import com.example.bakingapp.StepDetail.StepDetailFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RecipeDetailActivity extends AppCompatActivity implements StepsAdapter.StepAdapterOnClickHandler {
    private String OUTSTATE_RECIPE_DETAILS;
    private String mRecipeDetailsString;
    private JSONObject mRecipeDetailsJSONObject;

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        //save the recipe details
        outState.putString(OUTSTATE_RECIPE_DETAILS, mRecipeDetailsString);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        OUTSTATE_RECIPE_DETAILS = this.getString(R.string.OUTSTATE_RECIPE_DETAILS);
        //
        Intent mDetailIntent = getIntent();
        if (mDetailIntent.hasExtra(Intent.EXTRA_TEXT)) {
            mRecipeDetailsString = mDetailIntent.getStringExtra(Intent.EXTRA_TEXT);
        }else
            if (savedInstanceState != null){
                mRecipeDetailsString = savedInstanceState.getString(OUTSTATE_RECIPE_DETAILS);
            }

        try {
            assert mRecipeDetailsString != null;
            //convert the data in array format from JSON String
            mRecipeDetailsJSONObject = new JSONObject(mRecipeDetailsString);
            //get the recipe name and display it
            String recipeNameString = mRecipeDetailsJSONObject.getString("name");
            TextView mRecipeNameString = findViewById(R.id.activity_recipe_detail_title_tv);
            recipeNameString = recipeNameString + " " +  this.getString(R.string.INGREDIENTS_LIST_TITLE);
            mRecipeNameString.setText(recipeNameString);
            //get ingredients list as a string Array
            String ingredientsString = mRecipeDetailsJSONObject.getString("ingredients");
            JSONArray ingredientsJSONArray = new JSONArray(ingredientsString);
            //arrange the ingredients in a linear layout list
            LinearLayout activity_recipe_detail_ingredients_ll = findViewById(R.id.activity_recipe_detail_ingredients_ll);
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

            //only if savedInstanceState is null ie this is first OnCreate - on rotation, fragment will handle its recreation itself with the Fragment manager
            //use a bundle to pass clickHandler and mRecipeDetailsString to the fragment
            if (savedInstanceState == null) {
                String BUNDLE_RECIPE_DETAILS_STRING = this.getString(R.string.BUNDLE_RECIPE_DETAILS_STRING);
                Bundle bundl = new Bundle();
                bundl.putString(BUNDLE_RECIPE_DETAILS_STRING, mRecipeDetailsString);
                //generate the StepsList fragment
                RecipeStepsListFragment recipeStepsListFragment = new RecipeStepsListFragment();
                recipeStepsListFragment.setArguments(bundl);
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .add(R.id.activity_recipe_detail_framelayout, recipeStepsListFragment)
                        .commit();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }//end try JSOn

        //if layout is tablet with sw > 600dp then display right panel with detail step fragment
        if (this.getResources().getBoolean(R.bool.is_sw_600dp) ==  true){
            String stepsString = null;
            try {
                stepsString = mRecipeDetailsJSONObject.getString("steps");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String[] mArrayStepsData = NetworkUtilities.getArrayFromJSON(stepsString);
            //by default display step 1
            int currentPosition = 0;
            String BUNDLE_STEP_DETAILS_STRING = this.getString(R.string.BUNDLE_STEP_DETAILS_STRING);
            Bundle bundl = new Bundle();
            bundl.putString(BUNDLE_STEP_DETAILS_STRING, mArrayStepsData[currentPosition]);

            StepDetailFragment stepDetailFragment = new StepDetailFragment();
            stepDetailFragment.setArguments(bundl);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.activity_recipe_detail_step_detail_framelayout, stepDetailFragment)
                    .commit();
        }

    }

    //empty onClick override in Activity as new activity is called from ViewHolder in StepsAdapter
    @Override
    public void onClick(String[] mStepData, int position) {
    }

    // method called from ViewHolder in StepsAdapter to open New Activity
    public void onClickRecipeDetailActivity(String[] mStepData, int position) {
        //if we are not on a tablet, then open a new activity
        if (this.getResources().getBoolean(R.bool.is_sw_600dp) ==  false) {
            // Intent parameters: context, destination clas
            Context context = this;
            Class destinationClass = StepDetailActivity.class;
            String INTENT_STEPS_ARRAY = this.getString(R.string.INTENT_STEPS_ARRAY);
            String INTENT_STEPS_POSITION = this.getString(R.string.INTENT_STEPS_POSITION);

            //new intent with extra_text
            Intent startDetailedActivityIntent = new Intent(context, destinationClass);
            startDetailedActivityIntent.putExtra(INTENT_STEPS_ARRAY, mStepData);
            startDetailedActivityIntent.putExtra(INTENT_STEPS_POSITION, position);
            //start new activity with Intent
            startActivity(startDetailedActivityIntent);
        }
        //else we are on a tablet, then change reload the right panel fragment with the selected instruction
        else
        {
            String BUNDLE_STEP_DETAILS_STRING = this.getString(R.string.BUNDLE_STEP_DETAILS_STRING);
            Bundle bundl = new Bundle();
            bundl.putString(BUNDLE_STEP_DETAILS_STRING, mStepData[position]);
            StepDetailFragment stepDetailFragment = new StepDetailFragment();
            stepDetailFragment.setArguments(bundl);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.activity_recipe_detail_step_detail_framelayout, stepDetailFragment)
                    .commit();
        }

    }
}


