package com.example.xayru.bakingappv2;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.xayru.bakingappv2.ui.RecipeDetailActivity;
import com.example.xayru.bakingappv2.ui.RecipeListActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;


/**
 * Created by xayru on 3/14/2018.
 */
@RunWith(AndroidJUnit4.class)
public class RecipeListActivityTest {
    @Rule
    public ActivityTestRule<RecipeListActivity> mActivity = new ActivityTestRule<>(RecipeListActivity.class);

    @Test
    public void recyclerClickTest() {
        Intents.init();
        onView(ViewMatchers.withId(R.id.recipe_list)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        intended(hasComponent(RecipeDetailActivity.class.getName()));
        Intents.release();
    }
}
