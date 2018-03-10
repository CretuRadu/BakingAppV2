package com.example.xayru.bakingappv2.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.example.xayru.bakingappv2.BasicApp;
import com.example.xayru.bakingappv2.RecipeRepository;
import com.example.xayru.bakingappv2.data.Ingredient;
import com.example.xayru.bakingappv2.data.Recipe;
import com.example.xayru.bakingappv2.data.Step;

import java.util.List;

/**
 * Created by xayru on 3/2/2018.
 */

public class RecipeDetailViewModel extends AndroidViewModel {

    private final LiveData<Recipe> mObservableRecipe;
    private final LiveData<List<Step>> mObservableSteps;
    private final LiveData<List<Ingredient>> mObservableIngs;
    public ObservableField<Recipe> recipe = new ObservableField<>();
    private final int recipeId;

    public RecipeDetailViewModel(Application application, RecipeRepository repository, final int id) {
        super(application);
        recipeId = id;
        mObservableSteps = repository.loadSteps(recipeId);
        mObservableIngs = repository.loadIngredients(recipeId);
        mObservableRecipe = repository.loadRecipe(recipeId);
    }

    public LiveData<List<Step>> getSteps() {
        return mObservableSteps;
    }

    public LiveData<List<Ingredient>> getIngredients() {
        return mObservableIngs;
    }

    public LiveData<Recipe> getRecipe() {
        return mObservableRecipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe.set(recipe);
    }


    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        private final Application mApplication;
        private final int mRecipeId;
        private final RecipeRepository repository;

        public Factory(@NonNull Application application, int recipeId) {
            mApplication = application;
            mRecipeId = recipeId;
            repository = ((BasicApp) application).getRepository();
        }

        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new RecipeDetailViewModel(mApplication, repository,mRecipeId);

        }
    }
}

