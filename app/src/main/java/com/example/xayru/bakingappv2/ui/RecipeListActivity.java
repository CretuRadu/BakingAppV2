package com.example.xayru.bakingappv2.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.xayru.bakingappv2.R;
import com.example.xayru.bakingappv2.ViewModel.RecipeListViewModel;
import com.example.xayru.bakingappv2.data.Recipe;
import com.example.xayru.bakingappv2.data.UpdateService;
import com.example.xayru.bakingappv2.databinding.ActivityRecipeListBinding;

/**
 * An activity representing a list of Recipes. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RecipeDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RecipeListActivity extends AppCompatActivity {
    private static final String TAG = "RecipeListActivity";
    private boolean mTwoPane;
    public ActivityRecipeListBinding mBinding;
    private RecipeAdapter adapter;
    private boolean connection = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_recipe_list);
        connection = haveNetworkConnection();
        if (!connection) showNoConnection();
        adapter = new RecipeAdapter(recipeClickCallback);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());
        refresh();

    }
    private void subscribeUI(RecipeListViewModel listViewModel) {
        listViewModel.getRecipes().observe(this, recipes -> {
            if (recipes != null) {
                mBinding.include.setIsLoading(false);
                adapter.setRecipeList(recipes);
            } else {
                mBinding.include.setIsLoading(true);
            }
            mBinding.executePendingBindings();
        });
    }


    private final RecipeClickCallback recipeClickCallback = new RecipeClickCallback() {
        @Override
        public void onClick(Recipe recipe) {
            if (mTwoPane) {
                Bundle arguments = new Bundle();
                arguments.putInt(RecipeDetailFragment.ARG_RECIPE_ID, recipe.getId());
                RecipeDetailFragment fragment = new RecipeDetailFragment();
                fragment.setArguments(arguments);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.recipe_detail_container, fragment)
                        .commit();
            } else {
                Context context = getApplicationContext();
                Intent intent = new Intent(context, RecipeDetailActivity.class);
                intent.putExtra(RecipeDetailFragment.ARG_RECIPE_ID, recipe.getId());
                context.startActivity(intent);
            }
        }
    };

    public void showNoConnection() {
        Snackbar.make(findViewById(R.id.coordinator), "No internet connection", Snackbar.LENGTH_LONG).show();
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    private void refresh() {

        if (!connection) {
            showNoConnection();
        } else {
            startService(new Intent(this, UpdateService.class));
            final RecipeListViewModel listViewModel = ViewModelProviders.of(this).get(RecipeListViewModel.class);
            mBinding.include.recipeList.setAdapter(adapter);
            subscribeUI(listViewModel);
        }
    }
}



