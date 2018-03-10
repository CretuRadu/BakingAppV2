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
public interface StepDao {
    @Insert
    void insert(Step step);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Step> steps);

    @Update
    void update(Step... steps);

    @Delete
    void delete(Step... steps);

    @Query("SELECT * FROM steps_table WHERE id= :stepId")
    LiveData<Step> getStepById(int stepId);

    @Query("SELECT * FROM steps_table")
    LiveData<List<Step>> getAllSteps();

    @Query("SELECT * FROM steps_table WHERE recipeId= :id")
    LiveData<List<Step>> getAllStepsForRecipe(int id);
}
