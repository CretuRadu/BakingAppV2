package com.example.xayru.bakingappv2.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.xayru.bakingappv2.R;
import com.example.xayru.bakingappv2.ViewModel.RecipeDetailViewModel;
import com.example.xayru.bakingappv2.data.Ingredient;
import com.example.xayru.bakingappv2.databinding.RecipeDetailBinding;

import java.util.List;

/**
 * A fragment representing a single Recipe detail screen.
 * This fragment is either contained in a {@link RecipeListActivity}
 * in two-pane mode (on tablets) or a {@link RecipeDetailActivity}
 * on handsets.
 */
public class RecipeDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_RECIPE_ID = "recipe_id";
    private RecipeDetailBinding mBinding;
    private StepAdapter adapter;
    private int stepsSize;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecipeDetailFragment() {
    }

    public static RecipeDetailFragment forRecipe(int recipeId) {
        RecipeDetailFragment fragment = new RecipeDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_RECIPE_ID, recipeId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RecipeDetailViewModel.Factory factory = new RecipeDetailViewModel.Factory(getActivity().getApplication(),
                getArguments().getInt(ARG_RECIPE_ID));
        final RecipeDetailViewModel viewModel = ViewModelProviders.of(this, factory).get(RecipeDetailViewModel.class);
        subscribeUI(viewModel);
    }

    private void subscribeUI(final RecipeDetailViewModel viewModel) {
        viewModel.getSteps().observe(this, steps -> {
            if (steps != null) {
                stepsSize = steps.size();
                mBinding.setIsLoading(false);
                adapter.setStepList(steps);

            } else {
                mBinding.setIsLoading(true);
            }
        });
        viewModel.getIngredients().observe(this, ingredients -> mBinding.ingredientsTv.setText(formatIngs(ingredients)));
        viewModel.getRecipe().observe(this, recipe -> {
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) this.getActivity().findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) appBarLayout.setTitle(recipe.getName());
        });
        mBinding.executePendingBindings();

    }

    private String formatIngs(List<Ingredient> ingredients) {
        String ingStr = "";
        for (int i = 0; i < ingredients.size(); i++) {
            ingStr = ingStr + ingredients.get(i).getIngredient()
                    + " - " +
                    ingredients.get(i).getQuantity()
                    + " "
                    +ingredients.get(i).getMeasure()
                    +"\n";
        }
        return ingStr;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (getArguments().containsKey(ARG_RECIPE_ID)) {
//            // Load the dummy content specified by the fragment
//            // arguments. In a real-world scenario, use a Loader
//            // to load content from a content provider.
//            mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_RECIPE_ID));
//
//            Activity activity = this.getActivity();
//            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
//            if (appBarLayout != null) {
//                appBarLayout.setTitle(mItem.content);
//            }
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.recipe_detail, container, false);
        adapter = new StepAdapter(callback);
        mBinding.stepsRecycler.setAdapter(adapter);

        return mBinding.getRoot();
    }

    private final StepClickCallback callback = step -> {
        Bundle args = new Bundle();
        args.putInt(StepFragment.KEY_STEP_ID, step.getId());
        args.putInt(StepFragment.KEY_STEPS_SIZE, stepsSize);
        StepFragment fragment = StepFragment.forStep(step.getId(),stepsSize);
        fragment.setArguments(args);
        getFragmentManager().beginTransaction().replace(R.id.recipe_detail_container,fragment).addToBackStack(null).commit();
    };
}
