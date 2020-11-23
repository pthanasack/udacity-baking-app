package com.example.bakingapp;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.espresso.assertion.ViewAssertions;

import com.example.bakingapp.MainActivity.MainActivity;

@RunWith(AndroidJUnit4.class)
public class UiMainToRecipeDetailTest {
    //using ActivityScenarioRule instead of activityTestRule
    @Rule
    public ActivityScenarioRule activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testMainRecyclerView() {
        //launch the ActivityScenarioRule
        activityScenarioRule.getScenario();
        //test that the main recycler view is displayed
        Espresso.onView(ViewMatchers.withId(R.id.main_recycler_view)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

}
