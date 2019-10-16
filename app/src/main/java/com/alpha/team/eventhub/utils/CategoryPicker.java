package com.alpha.team.eventhub.utils;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.alpha.team.eventhub.db.CategoryEntry;
import com.alpha.team.eventhub.db.CountryEntry;
import com.alpha.team.eventhub.entities.Category;
import com.alpha.team.eventhub.entities.Country;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by clasence on 27,November,2018
 * On my honor, as a Carnegie-Mellon Africa student, I have neither given nor received unauthorized assistance on this work.
 *
 * Class gets categories from ContentProvider
 */
public class CategoryPicker implements LoaderManager.LoaderCallbacks<Cursor> {
    private Context activity;
    private Spinner spinner;
    List<Category> categories = new ArrayList<>();
    List<String> categoryNames = new ArrayList<>();
    private final int MY_LOADER_ID=2;
    private FragmentActivity fragmentActivity;

    public CategoryPicker(FragmentActivity fragmentActivity, Context activity, Spinner spinner) {
        this.activity = activity;
        this.spinner = spinner;
        this.fragmentActivity = fragmentActivity;
    }

    /**
     * Start Loader
     */
    public void startLoad(){
        fragmentActivity.getSupportLoaderManager().initLoader(MY_LOADER_ID, null, this).forceLoad();
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        Uri uri = CategoryEntry.CATEGORY_CONTENT_URI;
        Log.e("generated_uri", uri.toString());
        return new CursorLoader(activity,
                uri,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {
            Log.e("hurray!!", "cursor data gotten");



            for (int i = 0; i < data.getCount(); i++) {
                int id = data.getInt(data.getColumnIndex(CategoryEntry._ID));
                int categoryId = data.getInt(data.getColumnIndex(CategoryEntry.COLUMN_CATEGORY_ID));
                String category_name = data.getString(data.getColumnIndex(CategoryEntry.COLUMN_NAME));
                String isFav = data.getString(data.getColumnIndex(CategoryEntry.COLUMN_FAVOURITE));
                Category category = new Category(id, categoryId, category_name, isFav);
                categories.add(category);
                categoryNames.add(category_name);
                data.moveToNext();
            }


            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, categoryNames);

            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // attaching data adapter to spinner
            spinner.setAdapter(dataAdapter);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    public List<Category>  getCategoryList(){
        return categories;
    }

    public List<String>  getCategoryNames(){
        return categoryNames;
    }
}
