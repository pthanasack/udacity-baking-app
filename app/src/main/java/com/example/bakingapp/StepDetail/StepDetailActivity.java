package com.example.bakingapp.StepDetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.bakingapp.R;

public class StepDetailActivity extends AppCompatActivity {

    private String[] mStepsData;
    private int mCurrentPosition;
    private StepDetailFragment mStepDetailFragment;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);

            String INTENT_STEPS_ARRAY = this.getString(R.string.INTENT_STEPS_ARRAY);
            String INTENT_STEPS_POSITION = this.getString(R.string.INTENT_STEPS_POSITION);
            //get and set the step data to the views
            Intent mStepDetailIntent = getIntent();
            if (mStepDetailIntent.hasExtra(INTENT_STEPS_ARRAY) && mStepDetailIntent.hasExtra(INTENT_STEPS_POSITION)) {
                String[] stepDetailsStringArray = mStepDetailIntent.getStringArrayExtra(INTENT_STEPS_ARRAY);
                int currentPosition = mStepDetailIntent.getIntExtra(INTENT_STEPS_POSITION, 0);
                //store the members value
                mStepsData = stepDetailsStringArray;
                mCurrentPosition = currentPosition;
            }
        /*}*/
        //generate the StepsList fragment
        //only if savedInstanceState is null ie this is first OnCreate - on rotation, fragment will handle its recreation itself with the Fragment manager
        if (savedInstanceState == null) {

            String BUNDLE_STEP_DETAILS_STRING = this.getString(R.string.BUNDLE_STEP_DETAILS_STRING);
            Bundle bundl = new Bundle();
            bundl.putString(BUNDLE_STEP_DETAILS_STRING, mStepsData[mCurrentPosition]);

            mStepDetailFragment = new StepDetailFragment();
            mStepDetailFragment.setArguments(bundl);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.activity_step_detail_framelayout, mStepDetailFragment)
                    .commit();
        }
        // The "Next" button launches a next StepDetailActivity
        Button nextButton = (Button) findViewById(R.id.next_step_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NextStep();

            }
        });
        // The "Previous" button launches a previous StepDetailActivity
        Button prevButton = (Button) findViewById(R.id.previous_step_button);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PreviousStep();
            }
        });


    }


    public void PreviousStep() {
        Context context = this;
        Class destinationClass = StepDetailActivity.class;
        String INTENT_STEPS_ARRAY = this.getString(R.string.INTENT_STEPS_ARRAY);
        String INTENT_STEPS_POSITION = this.getString(R.string.INTENT_STEPS_POSITION);
        Intent startDetailedActivityIntent = new Intent(context, destinationClass);
        startDetailedActivityIntent.putExtra(INTENT_STEPS_ARRAY, mStepsData);
        startDetailedActivityIntent.putExtra(INTENT_STEPS_POSITION, mCurrentPosition - 1);
        //release the ExoPlayer when changing step
        mStepDetailFragment.releasePlayer();
        //start new activity with Intent if not at last step, and finish the current one
        if ((mCurrentPosition) > 0 ) {
            startActivity(startDetailedActivityIntent);
            finish();
        }
    }


    public void NextStep() {
        Context context = this;
        Class destinationClass = StepDetailActivity.class;
        String INTENT_STEPS_ARRAY = this.getString(R.string.INTENT_STEPS_ARRAY);
        String INTENT_STEPS_POSITION = this.getString(R.string.INTENT_STEPS_POSITION);
        Intent startDetailedActivityIntent = new Intent(context, destinationClass);
        startDetailedActivityIntent.putExtra(INTENT_STEPS_ARRAY, mStepsData);
        startDetailedActivityIntent.putExtra(INTENT_STEPS_POSITION, mCurrentPosition + 1);
        //release the ExoPlayer when changing step
        mStepDetailFragment.releasePlayer();
        //start new activity with Intent if not at last step, and finish the current one
        if ((mCurrentPosition + 1) < mStepsData.length) {
            startActivity(startDetailedActivityIntent);
            finish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        //release the ExoPlayer when on Pause
        if (mStepDetailFragment != null){mStepDetailFragment.pausePlayer();}
    }
}
