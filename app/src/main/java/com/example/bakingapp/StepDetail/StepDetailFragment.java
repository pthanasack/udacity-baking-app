package com.example.bakingapp.StepDetail;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bakingapp.R;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.StyledPlayerView;


import org.json.JSONException;
import org.json.JSONObject;

public class StepDetailFragment extends Fragment {
    private String mStepDetailsString;
    private SimpleExoPlayer  mSimpleExoPlayer;
    private MediaItem mMediaItem;
    //empty constructor
    public StepDetailFragment(){
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        String OUTSTATE_STEP_DETAILS_STRING = this.getString(R.string.OUTSTATE_STEP_DETAILS_STRING);
        outState.putString(OUTSTATE_STEP_DETAILS_STRING, mStepDetailsString);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Context context = getActivity();
         mSimpleExoPlayer = new SimpleExoPlayer.Builder(context).build();

        if(savedInstanceState != null) {
            String OUTSTATE_STEP_DETAILS_STRING = this.getString(R.string.OUTSTATE_STEP_DETAILS_STRING);
            mStepDetailsString = savedInstanceState.getString(OUTSTATE_STEP_DETAILS_STRING);
        } else {//get StepDetailsString from argument bundle
            Bundle bundl = getArguments();
            String BUNDLE_STEP_DETAILS_STRING = this.getString(R.string.BUNDLE_STEP_DETAILS_STRING);
            mStepDetailsString = bundl.getString(BUNDLE_STEP_DETAILS_STRING);
        }

        TextView mVideoLink_tv;
        TextView mStepDescription_tv;

        //get the R.id
        final View fragmentView = inflater.inflate(R.layout.fragment_step_detail, container, false);

        mVideoLink_tv = fragmentView.findViewById(R.id.video_link_tv);
        mStepDescription_tv = fragmentView.findViewById(R.id.step_description_tv);

        StyledPlayerView styledPlayerView = fragmentView.findViewById(R.id.styledPlayerView);

       //get the description and videoURL from the StepString in JSON format
        try {
            if (mStepDetailsString!= null) {
                JSONObject stepDetailsJSONObject = new JSONObject(mStepDetailsString);
                String stepDescriptionString = stepDetailsJSONObject.getString("description");
                String stepVideoUrlString = stepDetailsJSONObject.getString("videoURL");
                //if available, display the step desciprtion in its textview
                if (stepDescriptionString != null && stepDescriptionString != ""){
                    mStepDescription_tv.setText(stepDescriptionString);
                }  else {
                    mStepDescription_tv.setText(context.getString(R.string.no_description_available));
                }
                //if available, use videolink in SimpleExoplayer
                if (stepVideoUrlString != null && stepVideoUrlString != ""){
                    mVideoLink_tv.setText(stepVideoUrlString);
                    //attach the Player to the PlayerView
                    styledPlayerView.setPlayer(mSimpleExoPlayer);
                    //Build the VideoUri
                    Uri videoUri =  Uri.parse(stepVideoUrlString);
                    // Build the media item.
                    mMediaItem = MediaItem.fromUri(videoUri);
                    // Set the media item to be played.
                    mSimpleExoPlayer.setMediaItem(mMediaItem);
                    // Prepare the player.
                    mSimpleExoPlayer.prepare();
                    // Start the playback.
                    //mSimpleExoPlayer.play();
                } else
                {
                    mVideoLink_tv.setText(context.getString(R.string.no_videolink_available));
                }


            }
        } catch (JSONException e)
        {
            e.printStackTrace();
        }

        //return final view
        return fragmentView;
    }

    public void releasePlayer() {
        if (mSimpleExoPlayer !=null) {
            mSimpleExoPlayer.stop();
            mSimpleExoPlayer.release();
            mSimpleExoPlayer = null;
            mMediaItem = null;
        }
    }

    public void pausePlayer() {
        if (mSimpleExoPlayer !=null){mSimpleExoPlayer.pause();}
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }


}
