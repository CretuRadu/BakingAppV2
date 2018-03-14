package com.example.xayru.bakingappv2.data;

import android.app.IntentService;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xayru on 2/27/2018.
 */

public class UpdateService extends IntentService {

    private static final String TAG = "UpdateService";

    public static final String BROADCAST_ACTION_STATE_CHANGE
            = "com.example.xayru.bakingappv2.intent.action.STATE_CHANGE";
    public static final String EXTRA_REFRESHING
            = "com.example.xayru.bakingappv2.intent.extra.REFRESHING";

    private final static String JSON_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    public UpdateService() {
        super(TAG);
    }
    
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "onHandleIntent: service started");
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null || !ni.isConnected()) {
            Log.v(TAG, "Not online, not refreshing.");
            return;
        }
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(JSON_URL, response -> {
            List<Recipe> mRecipes = new ArrayList<>();
            List<Ingredient> mIngs = new ArrayList<>();
            List<Step> mSteps = new ArrayList<>();

            try {
                for (int r = 0; r < response.length(); r++) {
                    JSONObject recipeObject = response.getJSONObject(r);
                    String name = recipeObject.getString("name");
                    String recipeId = recipeObject.getString("id");
                    String servings = recipeObject.getString("servings");
                    String imageUrl = recipeObject.getString("image");
                    mRecipes.add(new Recipe(Integer.parseInt(recipeId),
                            name,
                            Integer.parseInt(servings),
                            imageUrl));
                    JSONArray ingredientsArray = recipeObject.getJSONArray("ingredients");
                    for (int i = 0; i < ingredientsArray.length(); i++) {
                        JSONObject ingObject = ingredientsArray.getJSONObject(i);
                        String quantity = ingObject.getString("quantity");
                        String measure = ingObject.getString("measure");
                        String ing = ingObject.getString("ingredient");
                        mIngs.add(new Ingredient(Integer.parseInt(recipeId),
                                quantity,
                                measure,
                                ing));
                    }
                    JSONArray stepJsonArray = recipeObject.getJSONArray("steps");
                    for (int s = 0; s < stepJsonArray.length(); s++) {
                        JSONObject stepObject = stepJsonArray.getJSONObject(s);
                        String stepId = stepObject.getString("id");
                        String srtDesc = stepObject.getString("shortDescription");
                        String desc = stepObject.getString("description");
                        String videoURl = stepObject.getString("videoURL");
                        String thumbnailURl = stepObject.getString("thumbnailURL");
                        mSteps.add(new Step(Integer.parseInt(stepId),
                                Integer.parseInt(recipeId),
                                srtDesc,
                                desc,
                                videoURl,
                                thumbnailURl));
                    }
                }
                RecipeDatabase database = RecipeDatabase.getInstance(getApplicationContext());
                AsyncTask.execute(() -> RecipeDatabase.insertData(database, mRecipes, mIngs, mSteps));
                Log.d(TAG, "onHandleIntent: data loaded");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Log.d(TAG, "onHandleIntent: Error loading data:" + error));

        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        requestQueue.add(jsonArrayRequest);
        
    }
}
