package com.example.bakingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsDataViewHolder> {
    //Click Handler -- Begin//
    //defines the clickhandler interface and attribute
    private final StepAdapterOnClickHandler mClickHandler;
    String[] mStepsData;

    //default constructor that takes a clickHandler
    public StepsAdapter(StepAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public StepsDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.recycler_steps_layout;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new StepsDataViewHolder(view);
    }
    //Click Handler -- End//

    @Override
    public void onBindViewHolder(@NonNull StepsDataViewHolder holder, int position) {
        String jsonStringRecipeDisplayed = mStepsData[position];
        try {
            JSONObject jsonObjectRecipeDisplayed = new JSONObject(jsonStringRecipeDisplayed);
            String recipeStepNbr = jsonObjectRecipeDisplayed.getString("id");
            String recipeShortStep = jsonObjectRecipeDisplayed.getString("shortDescription");
            String recipeShortStepLabel = "Recipe Step " + recipeStepNbr + ": " + recipeShortStep;
            holder.mStepsTextView.setText(recipeShortStepLabel);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (mStepsData == null) return 0;
        return mStepsData.length;
    }

    //set the data within the adapter
    public void setMainData(String[] mainData) {
        mStepsData = mainData;
        notifyDataSetChanged();
    }

    public interface StepAdapterOnClickHandler {
        void onClick(String mStepsData);

    }

    public class StepsDataViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView mStepsTextView;

        public StepsDataViewHolder(@NonNull View itemView) {
            super(itemView);
            //get the textview id
            mStepsTextView = (TextView) itemView.findViewById(R.id.recycler_steps_layout_textview);
            //set the onclick listener
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            String clickStepData = mStepsData[adapterPosition];
            Toast.makeText(v.getContext(), clickStepData, Toast.LENGTH_LONG).show();
            mClickHandler.onClick(clickStepData);

        }
    }


}
