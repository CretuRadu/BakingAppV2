package com.example.xayru.bakingappv2.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Spinner;

import com.example.xayru.bakingappv2.R;
import com.example.xayru.bakingappv2.ViewModel.RecipeListViewModel;
import com.example.xayru.bakingappv2.ui.RecipeDetailActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xayru on 3/7/2018.
 */

public class WidgetConfiguration extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "WIDGET_CONFIG";

    @BindView(R.id.widget_recipe_spinner)
    Spinner spinner;
    @BindView(R.id.widget_set_config)
    Button setConfig;
    private int mAppWidgetId;
    private AppWidgetManager appWidgetManager;
    private Intent resultValue;
    private int selectedId = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.widget_configuration_activity);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        resultValue = new Intent(this, ListService.class);
        setResult(RESULT_CANCELED, resultValue);

        RecipeListViewModel viewModel = ViewModelProviders.of(this).get(RecipeListViewModel.class);
        viewModel.getRecipes().observe(this, recipes -> {
            if (recipes != null) {
                List<String> recipeStr = new ArrayList<>();
                for (int i = 0; i < recipes.size(); i++) {
                    String s = recipes.get(i).getName();
                    recipeStr.add(s);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, recipeStr);
                spinner.setAdapter(adapter);
            }

        });

        setConfig.setOnClickListener(v -> {
            RemoteViews views = new RemoteViews(getPackageName(), R.layout.recipe_widget);
            views.setRemoteAdapter(R.id.appwidget_list, resultValue);
            selectedId = spinner.getSelectedItemPosition() == 0 ? selectedId : spinner.getSelectedItemPosition()+1;
            resultValue.putExtra(RecipeWidget.RECIPE_KEY, selectedId);
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            appWidgetManager = AppWidgetManager.getInstance(this);
            appWidgetManager.updateAppWidget(mAppWidgetId, views);
            setResult(RESULT_OK, resultValue);
            finish();
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        resultValue.putExtra(RecipeWidget.RECIPE_KEY, position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        resultValue.putExtra(RecipeWidget.RECIPE_KEY, 1);
    }
}
