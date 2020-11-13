package com.example.bakingapp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

public class RecipeStepsListFragment extends Fragment {
     String mStepsString;
     StepsAdapter.StepAdapterOnClickHandler mClickHandler;
    public RecipeStepsListFragment(){
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_recipe_steps_list, container, false);

        Context context = getActivity();
        //arrange the steps in the recycler view
        RecyclerView mRecyclerView = (RecyclerView) fragmentView.findViewById(R.id.steps_recycler_view);

        LinearLayoutManager linearLayoutManager
                = new LinearLayoutManager (context, LinearLayoutManager.VERTICAL, false);

        //Set the layoutManager on RecyclerView
        mRecyclerView.setLayoutManager(linearLayoutManager);

        //new MoviesAdapter
        //with a clickhandler that will come from RecipeDetailActivity class
        mClickHandler = (StepsAdapter.StepAdapterOnClickHandler) context;
        StepsAdapter mStepAdapter = new StepsAdapter(mClickHandler);
        //set the adapter to the RecyclerView
        mRecyclerView.setAdapter(mStepAdapter);

        //get the mStepsString from bundle
        String BUNDLE_RECIPE_DETAILS_STRING = this.getString(R.string.BUNDLE_RECIPE_DETAILS_STRING);
        Bundle bundl = getArguments();
        if (bundl != null) {
            mStepsString = bundl.getString(BUNDLE_RECIPE_DETAILS_STRING);
        }
        if (mStepsString != null) {
            try {
                JSONObject stepsJSONObject = new JSONObject(mStepsString);
                String stepsString = stepsJSONObject.getString("steps");
                String[] mArrayStepsData = NetworkUtilities.getArrayFromJSON(stepsString);
                mStepAdapter.setMainData(mArrayStepsData);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return fragmentView;

    }
}
