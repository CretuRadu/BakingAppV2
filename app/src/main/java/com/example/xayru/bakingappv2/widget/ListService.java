package com.example.xayru.bakingappv2.widget;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.xayru.bakingappv2.R;
import com.example.xayru.bakingappv2.data.Ingredient;
import com.example.xayru.bakingappv2.data.RecipeDatabase;
import com.example.xayru.bakingappv2.ui.RecipeDetailActivity;

import java.util.List;

import static com.google.android.exoplayer2.mediacodec.MediaCodecInfo.TAG;

/**
 * Created by xayru on 3/7/2018.
 */

public class ListService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this, intent);
    }

    private class ListRemoteViewsFactory implements RemoteViewsFactory {
        private List<Ingredient> ingredients;
        private Context context;
        private int recipeId;

        public ListRemoteViewsFactory(Context applicationContext, Intent intent) {
            context = applicationContext;
            recipeId = intent.getIntExtra(RecipeWidget.RECIPE_KEY,0);
            Log.d(TAG, "ListRemoteViewsFactory: Recipe ID " + recipeId);
        }

        @Override
        public void onCreate() {
            RecipeDatabase database = RecipeDatabase.getInstance(context);
            Thread thread = new Thread(() -> {
                ingredients = database.ingredientsDao().getAllIngredientsForRecipeSync(recipeId);
            });
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            onDataSetChanged();

        }

        @Override
        public void onDataSetChanged() {
        }

        @Override
        public int getCount() {
            return ingredients == null ? 0 : ingredients.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_item);
            String ingString = ingredients.get(position).getIngredient()
                    + "\n" +
                    ingredients.get(position).getQuantity()
                    + " " +
                    ingredients.get(position).getMeasure();
            views.setTextViewText(R.id.content_widget, ingString);
            views.setTextViewText(R.id.id_widget, position + 1 + " ");

            return views;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public void onDestroy() {
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }
    }
}
