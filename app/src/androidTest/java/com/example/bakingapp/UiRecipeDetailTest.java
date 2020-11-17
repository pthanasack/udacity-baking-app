package com.example.bakingapp;

import android.content.Context;
import android.content.Intent;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class UiRecipeDetailTest {

        @Rule
        public ActivityTestRule activityTestRule = new ActivityTestRule<StepDetailActivity>(StepDetailActivity.class) {

            //launch StepDetailActivity with an Intent
            @Override
         protected Intent getActivityIntent() {
                //defining the intent for the activity
             String dataString = "[{    \"id\": 0,    \"shortDescription\": \"Recipe Introduction\",    \"description\": \"Recipe Introduction\",    \"videoURL\": \"https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd974_-intro-creampie/-intro-creampie.mp4\",    \"thumbnailURL\": \"\"  },  {    \"id\": 1,    \"shortDescription\": \"Starting prep\",    \"description\": \"1. Preheat the oven to 350\\\\u00b0F. Butter a 9\\\\\" deep dish pie pan.\",    \"videoURL\": \"\",    \"thumbnailURL\": \"\"  },  {    \"id\": 2,    \"shortDescription\": \"Prep the cookie crust.\",    \"description\": \"2. Whisk the graham cracker crumbs, 50 grams (1/4 cup) of sugar, and 1/2 teaspoon of salt together in a medium bowl. Pour the melted butter and 1 teaspoon of vanilla into the dry ingredients and stir together until evenly mixed.\",    \"videoURL\": \"https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd9a6_2-mix-sugar-crackers-creampie/2-mix-sugar-crackers-creampie.mp4\",    \"thumbnailURL\": \"\"  },  {    \"id\": 3,    \"shortDescription\": \"Press the crust into baking form.\",    \"description\": \"3. Press the cookie crumb mixture into the prepared pie pan and bake for 12 minutes. Let crust cool to room temperature.\",    \"videoURL\": \"https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd9cb_4-press-crumbs-in-pie-plate-creampie/4-press-crumbs-in-pie-plate-creampie.mp4\",    \"thumbnailURL\": \"\"  },  {    \"id\": 4,    \"shortDescription\": \"Start filling prep\",    \"description\": \"4. Beat together the nutella, mascarpone, 1 teaspoon of salt, and 1 tablespoon of vanilla on medium speed in a stand mixer or high speed with a hand mixer until fluffy.\",    \"videoURL\": \"https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd97a_1-mix-marscapone-nutella-creampie/1-mix-marscapone-nutella-creampie.mp4\",    \"thumbnailURL\": \"\"  },  {    \"id\": 5,    \"shortDescription\": \"Finish filling prep\",    \"description\": \"5. Beat the cream cheese and 50 grams (1/4 cup) of sugar on medium speed in a stand mixer or high speed with a hand mixer for 3 minutes. Decrease the speed to medium-low and gradually add in the cold cream. Add in 2 teaspoons of vanilla and beat until stiff peaks form.\",    \"videoURL\": \"\",    \"thumbnailURL\": \"https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffda20_7-add-cream-mix-creampie/7-add-cream-mix-creampie.mp4\"  },  {    \"id\": 6,    \"shortDescription\": \"Finishing Steps\",    \"description\": \"6. Pour the filling into the prepared crust and smooth the top. Spread the whipped cream over the filling. Refrigerate the pie for at least 2 hours. Then it\\'s ready to serve!\",    \"videoURL\": \"https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffda45_9-add-mixed-nutella-to-crust-creampie/9-add-mixed-nutella-to-crust-creampie.mp4\",    \"thumbnailURL\": \"\"  }]";
             String[] dataStringArray = new String[] {dataString};
             Context targetContext = InstrumentationRegistry.getInstrumentation()
                     .getTargetContext();
             String INTENT_STEPS_ARRAY = "INTENT_STEPS_ARRAY";
             String INTENT_STEPS_POSITION = "INTENT_STEPS_POSITION";
             Intent result = new Intent(targetContext, StepDetailActivity.class);
             result.putExtra(INTENT_STEPS_ARRAY, dataStringArray);
             result.putExtra(INTENT_STEPS_POSITION, 0);
             return result;
         }
     };

    @Test
    public void testMainRecyclerView() {
        //check that the PlayerView is displayed
              Espresso.onView(ViewMatchers.withId(R.id.styledPlayerView)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }




}