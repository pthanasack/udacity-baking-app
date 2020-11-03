package com.example.bakingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainDataViewHolder> {
    //Click Handler -- Begin//
    //defines the clickhandler interface and attribute
    private final MainAdapterOnClickHandler mClickHandler;
    private String[] mMainData;

    //default constructor that takes a clickHandler
    public MainAdapter(MainAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public MainDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.recycler_main_layout;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new MainDataViewHolder(view);
    }
    //Click Handler -- End//

    @Override
    public void onBindViewHolder(@NonNull MainDataViewHolder holder, int position) {
        String jsonStringRecipeDisplayed = mMainData[position];
        try {
            JSONObject jsonObjectRecipeDisplayed = new JSONObject(jsonStringRecipeDisplayed);
            String recipeName = jsonObjectRecipeDisplayed.getString("name");
            //String positionString = (String) Integer.toString(position);
            holder.mMainTextView.setText(recipeName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (mMainData == null) return 0;
        return mMainData.length;
    }

    //set the data within the adapter
    public void setMainData(String[] mainData) {
        mMainData = mainData;
        notifyDataSetChanged();
    }

    public interface MainAdapterOnClickHandler {
        void onClick(String mMainData);

    }

    public class MainDataViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView mMainTextView;

        public MainDataViewHolder(@NonNull View itemView) {
            super(itemView);
            //get the textview id
            mMainTextView = (TextView) itemView.findViewById(R.id.recycler_main_layout_text_view);
            //set the onclick listener
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            String clickMainData = mMainData[adapterPosition];
            mClickHandler.onClick(clickMainData);
        }
    }


}
