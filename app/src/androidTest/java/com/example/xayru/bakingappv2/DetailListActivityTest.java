package com.example.xayru.bakingappv2;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.xayru.bakingappv2.ui.RecipeDetailActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;

/**
 * Created by xayru on 3/14/2018.
 */
@RunWith(AndroidJUnit4.class)
public class DetailListActivityTest {
    @Rule
    public ActivityTestRule<RecipeDetailActivity> detailActiv = new ActivityTestRule<RecipeDetailActivity>(RecipeDetailActivity.class){
        @Override
        protected Intent getActivityIntent() {
            Intent intent = new Intent(InstrumentationRegistry.getInstrumentation().getTargetContext(), RecipeDetailActivity.class);
            intent.putExtra("recipe_id", 1);
            return intent;
        }
    };

    @Test
    public void detailRecyclerTest() {
        onView(ViewMatchers.withId(R.id.steps_recycler))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        onView(ViewMatchers.withId(R.id.step_description)).check(matches(isDisplayed()));
    }
}
