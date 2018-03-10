package com.example.xayru.bakingappv2.ui;

import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.xayru.bakingappv2.R;
import com.example.xayru.bakingappv2.data.Recipe;
import com.example.xayru.bakingappv2.databinding.RecipeListContentBinding;

import java.util.List;
import java.util.Objects;

/**
 * Created by xayru on 2/28/2018.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    List<Recipe> mRecipeList;

    @Nullable
    private final RecipeClickCallback recipeClickCallback;

    public RecipeAdapter(@Nullable RecipeClickCallback recipeClickCallback1) {
        recipeClickCallback = recipeClickCallback1;
    }

    public void setRecipeList(List<Recipe> recipeList) {
        if (mRecipeList == null) {
            mRecipeList = recipeList;
            notifyItemRangeInserted(0, recipeList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mRecipeList.size();
                }

                @Override
                public int getNewListSize() {
                    return recipeList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return mRecipeList.get(oldItemPosition).getId() ==
                            recipeList.get(newItemPosition).getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    Recipe newProduct = recipeList.get(newItemPosition);
                    Recipe oldProduct = mRecipeList.get(oldItemPosition);
                    return newProduct.getId() == oldProduct.getId()
                            && Objects.equals(newProduct.getImage(), oldProduct.getImage())
                            && Objects.equals(newProduct.getName(), oldProduct.getName())
                            && newProduct.getServings() == oldProduct.getServings();
                }
            });
            mRecipeList = recipeList;
            result.dispatchUpdatesTo(this);
        }
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecipeListContentBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.recipe_list_content,
                parent, false);
        binding.setCallback(recipeClickCallback);
        return new RecipeViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        if (mRecipeList != null) {
            holder.binding.setRecipe(mRecipeList.get(position));
            holder.binding.executePendingBindings();
        }
    }

    @Override
    public int getItemCount() {
        return mRecipeList == null ? 0 : mRecipeList.size();
    }

    static class RecipeViewHolder extends RecyclerView.ViewHolder {

        final RecipeListContentBinding binding;

        public RecipeViewHolder(RecipeListContentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
