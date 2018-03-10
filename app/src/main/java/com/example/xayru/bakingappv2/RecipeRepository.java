package com.example.xayru.bakingappv2;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.util.Log;

import com.example.xayru.bakingappv2.data.Ingredient;
import com.example.xayru.bakingappv2.data.Recipe;
import com.example.xayru.bakingappv2.data.RecipeDatabase;
import com.example.xayru.bakingappv2.data.Step;

import java.util.List;

import static com.google.android.exoplayer2.mediacodec.MediaCodecInfo.TAG;

/**
 * Created by xayru on 2/27/2018.
 */

public class RecipeRepository {
    private static RecipeRepository sInstance;
    private final RecipeDatabase mDatabase;
    private MediatorLiveData<List<Recipe>> mObservableRecipes;

    private RecipeRepository(final RecipeDatabase database) {
        mDatabase = database;
        mObservableRecipes = new MediatorLiveData<>();
        mObservableRecipes.addSource(mDatabase.recipeDao().getAllRecipes(), recipes -> {
            if (mDatabase.getDatabaseCreated().getValue() != null) {
                mObservableRecipes.postValue(recipes);
            }
        });
    }

    public static RecipeRepository getInstance(final RecipeDatabase database) {
        if (sInstance == null) {
            synchronized (RecipeRepository.class) {
                if (sInstance == null) {
                    sInstance = new RecipeRepository(database);
                }
            }
        }
        return sInstance;
    }

    public LiveData<List<Recipe>> getRecipes() {
        return mObservableRecipes;
    }

    public LiveData<List<Ingredient>> loadIngredients(final int recipeId) {
        Log.d(TAG, "loadIngredients: for recipe " + recipeId);
        return mDatabase.ingredientsDao().getAllIngredientsForRecipe(recipeId);
    }

    public LiveData<List<Step>> loadSteps(final int recipeId) {
        return mDatabase.stepDao().getAllStepsForRecipe(recipeId);
    }

    public LiveData<Recipe> loadRecipe(final int recipeId) {
        return mDatabase.recipeDao().getRecipeForId(recipeId);
    }

    public LiveData<Step> getStepById(final int stepId){
        return mDatabase.stepDao().getStepById(stepId);
    }

}
