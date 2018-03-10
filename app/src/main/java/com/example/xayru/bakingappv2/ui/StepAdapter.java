package com.example.xayru.bakingappv2.ui;

import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.xayru.bakingappv2.R;
import com.example.xayru.bakingappv2.data.Step;
import com.example.xayru.bakingappv2.databinding.StepListContentBinding;

import java.util.List;
import java.util.Objects;

/**
 * Created by xayru on 3/2/2018.
 */

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewholder> {
    private List<Step> mStepList;
    @Nullable
    private final StepClickCallback mStepClickCallback;

    public StepAdapter(StepClickCallback clickCallback) {
        mStepClickCallback = clickCallback;
    }

    public void setStepList(List<Step> stepList) {
        if (mStepList == null) {
            mStepList = stepList;
            notifyItemRangeInserted(0, stepList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mStepList.size();
                }

                @Override
                public int getNewListSize() {
                    return stepList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return mStepList.get(oldItemPosition).getServer_id() ==
                            stepList.get(newItemPosition).getServer_id();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    Step newProduct = stepList.get(newItemPosition);
                    Step oldProduct = mStepList.get(oldItemPosition);
                    return newProduct.getServer_id() == oldProduct.getServer_id()
                            && Objects.equals(newProduct.getShrtDesc(), oldProduct.getShrtDesc())
                            && Objects.equals(newProduct.getDesc(), oldProduct.getDesc())
                            && Objects.equals(newProduct.getThumbUrl(), oldProduct.getThumbUrl())
                            && newProduct.getVideoUrl().equals(oldProduct.getVideoUrl());
                }
            });
            mStepList = stepList;
            result.dispatchUpdatesTo(this);
        }
    }

    @Override
    public StepViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        StepListContentBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.step_list_content, parent, false);
        binding.setCallback(mStepClickCallback);
        return new StepViewholder(binding);
    }

    @Override
    public void onBindViewHolder(StepViewholder holder, int position) {
        holder.mBinding.setStep(mStepList.get(position));
        holder.mBinding.executePendingBindings();

    }

    @Override
    public int getItemCount() {
        return mStepList == null ? 0 : mStepList.size();
    }

    static class StepViewholder extends RecyclerView.ViewHolder {

        final StepListContentBinding mBinding;

        StepViewholder(StepListContentBinding binding) {
            super(binding.getRoot());
            this.mBinding = binding;
        }

    }
}
