package com.example.xayru.bakingappv2.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by xayru on 2/27/2018.
 */
@Dao
public interface IngredientDao {
    @Insert
    void insert(Ingredient ingredient);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Ingredient> ingredients);

    @Update
    void update(Ingredient... ingredients);

    @Delete
    void delete(Ingredient... ingredients);

    @Query("SELECT * FROM ingredients_table")
    List<Ingredient> getAllIngredients();

    @Query("SELECT * FROM ingredients_table WHERE recipeId= :recipeId")
    LiveData<List<Ingredient>> getAllIngredientsForRecipe(int recipeId);

    @Query("SELECT * FROM ingredients_table WHERE recipeId= :recipeId")
    List<Ingredient> getAllIngredientsForRecipeSync(int recipeId);


}
