package com.example.xayru.bakingappv2.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;

import com.example.xayru.bakingappv2.BasicApp;
import com.example.xayru.bakingappv2.data.Recipe;

import java.util.List;

/**
 * Created by xayru on 2/28/2018.
 */

public class RecipeListViewModel extends AndroidViewModel {

    private final MediatorLiveData<List<Recipe>> mObservableRecipes;

    public RecipeListViewModel(Application application) {
        super(application);
        mObservableRecipes = new MediatorLiveData<>();
        mObservableRecipes.setValue(null);
        LiveData<List<Recipe>> recipes = ((BasicApp) application).getRepository().getRecipes();
        mObservableRecipes.addSource(recipes, mObservableRecipes::setValue);
    }

    public LiveData<List<Recipe>> getRecipes() {
        return mObservableRecipes;
    }
}

