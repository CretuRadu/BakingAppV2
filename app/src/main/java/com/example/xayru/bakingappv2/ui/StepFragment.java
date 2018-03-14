package com.example.xayru.bakingappv2.ui;


import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.xayru.bakingappv2.R;
import com.example.xayru.bakingappv2.ViewModel.StepViewModel;
import com.example.xayru.bakingappv2.data.Step;
import com.example.xayru.bakingappv2.databinding.StepDetailBinding;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import static com.google.android.exoplayer2.mediacodec.MediaCodecInfo.TAG;

/**
 * Created by xayru on 3/4/2018.
 */

public class StepFragment extends Fragment {

    public static final String KEY_STEP_ID = "step_id";
    public static final String STEP_FRAG = "step_frag_id";
    public static final String KEY_STEPS_SIZE = "steps_size";
    private StepDetailBinding stepBinding;
    private SimpleExoPlayer exoPlayer;
    private boolean twoPane;
    private int stepId;

    public StepFragment() {

    }

    public static StepFragment forStep(int id, int size) {
        StepFragment fragment = new StepFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_STEP_ID, id);
        args.putInt(KEY_STEPS_SIZE, size);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        twoPane = this.getResources().getConfiguration().screenWidthDp >= 900;
        StepViewModel.Factory factory = new StepViewModel.Factory(getActivity().getApplication(), getArguments().getInt(KEY_STEP_ID));
        final StepViewModel viewModel = ViewModelProviders.of(this, factory).get(StepViewModel.class);
        Log.d(TAG, "onActivityCreated: id for step " + getArguments().getInt(KEY_STEP_ID));
        subscribeStep(viewModel);
    }

    private void subscribeStep(StepViewModel viewModel) {
        viewModel.getStep().observe(this, step -> {
            if (step != null) {
                stepId = step.getId();
                initializeExoPlayer(step);
                stepBinding.setStep(step);
            }
        });
    }

    private void initializeExoPlayer(Step step) {
        if (exoPlayer == null) {
            exoPlayer = ExoPlayerFactory.newSimpleInstance(
                    new DefaultRenderersFactory(getContext()),
                    new DefaultTrackSelector(), new DefaultLoadControl());
            stepBinding.exoPlayer.setPlayer(exoPlayer);
            exoPlayer.setPlayWhenReady(true);
            String url;
            if (!step.getVideoUrl().isEmpty()) {
                url = step.getVideoUrl();
            } else if (!step.getThumbUrl().isEmpty()) {
                url = step.getThumbUrl();
            } else {
                stepBinding.exoPlayer.setVisibility(View.GONE);
                releasePlayer();
                return;
            }
            Uri uri = Uri.parse(url);
            MediaSource source = buildMediaSource(uri);
            exoPlayer.prepare(source);
            stepBinding.playerContainer.setAspectRatio(1.7f);
            stepBinding.playerContainer.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);
            stepBinding.exoPlayer.setVisibility(View.VISIBLE);


        }
    }

    private void releasePlayer() {
        if (exoPlayer != null) {
            exoPlayer.release();
            exoPlayer = null;
            stepBinding.exoPlayer.setVisibility(View.GONE);
        }
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(new DefaultHttpDataSourceFactory("BackingApp"))
                .createMediaSource(uri);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        stepBinding = DataBindingUtil.inflate(inflater, R.layout.step_detail, container, false);
        stepBinding.executePendingBindings();
        return stepBinding.getRoot();
    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    public void goBack(){
        //TODO if time available implement this and next

    }

    public void goNext(){

    }
}
