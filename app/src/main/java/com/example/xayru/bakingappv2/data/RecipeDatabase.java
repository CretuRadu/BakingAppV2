package com.example.xayru.bakingappv2.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.VisibleForTesting;

import java.util.List;

/**
 * Created by xayru on 2/27/2018.
 */
@Database(entities = {Recipe.class, Step.class, Ingredient.class}, version = 1, exportSchema = false)
public abstract class RecipeDatabase extends RoomDatabase {
    @VisibleForTesting
    private static final String DATABASE_NAME = "recipe_database";
    private final MutableLiveData<Boolean> mIsDatabaseCreated = new MutableLiveData<>();
    public abstract RecipeDao recipeDao();
    public abstract StepDao stepDao();
    public abstract IngredientDao ingredientsDao();
    private static  RecipeDatabase INSTANCE;

    public static RecipeDatabase getInstance(final Context context ){
        if (INSTANCE == null) {
            synchronized (RecipeDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RecipeDatabase.class, DATABASE_NAME)
                            .build();
                    INSTANCE.updateDatabaseCreated(context);
                }
            }
        }
        return INSTANCE;
    }

    static void insertData(final RecipeDatabase database, final List<Recipe> recipes,
                           final List<Ingredient> ingredients, final List<Step> steps) {
        database.runInTransaction(() -> {
            database.recipeDao().insertAll(recipes);
            database.ingredientsDao().insertAll(ingredients);
            database.stepDao().insertAll(steps);
        });
    }

    private void updateDatabaseCreated(final Context context) {
        if (context.getDatabasePath(DATABASE_NAME).exists()) {
            setDatabaseCreated();
        }
    }

    private void setDatabaseCreated() {
        mIsDatabaseCreated.postValue(true);
    }

    public LiveData<Boolean> getDatabaseCreated() {
        return mIsDatabaseCreated;
    }
}
