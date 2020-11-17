package com.example.bakingapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsDataViewHolder> {
    //Click Handler -- Begin//
    //defines the clickhandler interface and attribute
    private final StepAdapterOnClickHandler mClickHandler;
    String[] mStepsData;
    //List of all Textview for color change
    public List<TextView> mTextViewList = new ArrayList<>();

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
    public void onBindViewHolder(@NonNull final StepsDataViewHolder holder, int position) {
        String jsonStringRecipeDisplayed = mStepsData[position];
        try {
            JSONObject jsonObjectRecipeDisplayed = new JSONObject(jsonStringRecipeDisplayed);
            String recipeStepNbr = jsonObjectRecipeDisplayed.getString("id");
            String recipeShortStep = jsonObjectRecipeDisplayed.getString("shortDescription");
            String recipeShortStepLabel = "Recipe Step " + recipeStepNbr + ": " + recipeShortStep;
            holder.mStepsTextView.setText(recipeShortStepLabel);
            //implement the color change onClick
            mTextViewList.add(holder.mStepsTextView);
            holder.mStepsTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //All card color is set to colorDefault
                    Context context = view.getContext();
                    for(TextView mTextViewList : mTextViewList){
                        mTextViewList.setBackground(context.getResources().getDrawable(R.drawable.recipe_steps_list_background));
                    }
                    //The selected card is set to colorSelected
                    holder.mStepsTextView.setBackground(context.getResources().getDrawable(R.drawable.selected_recipe_steps_list_background));
                }
            });

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
        void onClick(String[] mStepsData, int position);
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
            mClickHandler.onClick(mStepsData, adapterPosition);

        }
    }


}
