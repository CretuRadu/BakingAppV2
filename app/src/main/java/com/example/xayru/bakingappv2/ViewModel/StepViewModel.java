package com.example.xayru.bakingappv2.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.xayru.bakingappv2.BasicApp;
import com.example.xayru.bakingappv2.RecipeRepository;
import com.example.xayru.bakingappv2.data.Step;

import java.util.List;

/**
 * Created by xayru on 3/4/2018.
 */

public class StepViewModel extends AndroidViewModel {

    private final LiveData<Step> stepLiveData;
    public StepViewModel(Application application, RecipeRepository repository, final int id) {
        super(application);
        stepLiveData = repository.getStepById(id);
    }

    public LiveData<Step> getStep() {
        return stepLiveData;
    }



    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        private final Application mApplication;
        private final int mStepId;
        private final RecipeRepository repository;

        public Factory(@NonNull Application application, int recipeId) {
            mApplication = application;
            mStepId = recipeId;
            repository = ((BasicApp) application).getRepository();
        }

        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new StepViewModel(mApplication, repository, mStepId);

        }
    }
}


