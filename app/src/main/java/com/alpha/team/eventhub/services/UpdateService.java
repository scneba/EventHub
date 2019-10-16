package com.alpha.team.eventhub.services;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.alpha.team.eventhub.db.CategoryEntry;
import com.alpha.team.eventhub.db.CountryEntry;
import com.alpha.team.eventhub.entities.Category;
import com.alpha.team.eventhub.entities.Country;
import com.alpha.team.eventhub.exceptions.EventHubException;
import com.alpha.team.eventhub.exceptions.ExceptionFactory;
import com.alpha.team.eventhub.network.RetrofitClientInstance;
import com.alpha.team.eventhub.network.RetrofitService;
import com.alpha.team.eventhub.sharedprefrence.ServicePreferences;
import com.alpha.team.eventhub.utils.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by clasence on 25,November,2018
 * On my honor, as a Carnegie-Mellon Africa student, I have neither given nor received unauthorized assistance on this work.
 *
 * Intent Service for country and category update
 */
public class UpdateService extends IntentService {


    private ServicePreferences servicePreferences;

    public UpdateService() {
        super("UpdateService");

    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        servicePreferences = new ServicePreferences(getApplicationContext());
        if (intent.getAction().equalsIgnoreCase(Constants.COLUMN_GOT_COUNTRIES)) {
            getAndSaveCountries();
        } else if (intent.getAction().equalsIgnoreCase(Constants.COLUMN_GOT_CATEGORIES)) {
            getAndSaveCategories();
        }

    }

    /**
     * Get countries from database and save to SQLite database
     */
    private void getAndSaveCountries() {
        //create retrofit interface
        RetrofitService retrofitService = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
        Call<List<Country>> call = retrofitService.getCountries();
        call.enqueue(new Callback<List<Country>>() {
            @Override
            public void onResponse(Call<List<Country>> call, Response<List<Country>> response) {
                if (response.code() == 200) {
                    ContentValues[] allValues = new ContentValues[response.body().size()];
                    for (int i = 0; i < response.body().size(); i++) {
                        Country country = response.body().get(i);
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(CountryEntry.COLUMN_COUNTRY_ID, country.getCountry_id());
                        contentValues.put(CountryEntry.COLUMN_CODE, country.getCode());
                        contentValues.put(CountryEntry.COLUMN_NAME, country.getName());
                        allValues[i] = contentValues;
                    }
                    if (!servicePreferences.getGotCountries()) {
                        int number = getContentResolver().bulkInsert(CountryEntry.COUNTRY_CONTENT_URI, allValues);
                        if (number > 0) {
                            servicePreferences.updateGotCountries(true);
                            Log.e("hurrayys!", "size is " + number);
                        }
                    }

                }


            }

            @Override
            public void onFailure(Call<List<Country>> call, Throwable t) {
                Log.e("GetCountriesService", t.toString());
            }
        });
    }


    /**
     * Get Categories from database and save to SQLite database
     */
    private void getAndSaveCategories() {
        //create retrofit interface
        RetrofitService retrofitService = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
        Call<List<Category>> call = retrofitService.getCategories();
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.code() == 200) {
                    ContentValues[] allValues = new ContentValues[response.body().size()];
                    for (int i = 0; i < response.body().size(); i++) {
                        Category category = response.body().get(i);
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(CategoryEntry.COLUMN_CATEGORY_ID, category.getCategoryId());
                        contentValues.put(CategoryEntry.COLUMN_NAME, category.getCategoryId());
                        contentValues.put(CategoryEntry.COLUMN_FAVOURITE, false);
                        contentValues.put(CountryEntry.COLUMN_NAME, category.getName());
                        allValues[i] = contentValues;
                    }
                    if(!servicePreferences.getGotCategories()) {
                        int number = getContentResolver().bulkInsert(CategoryEntry.CATEGORY_CONTENT_URI, allValues);
                        if (number > 0) {
                            Log.e("hurrayys!", "size is " + number);
                            servicePreferences.updateGotCategories(true);
                        }
                    }

                }


            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Log.e("GetCountriesService", t.toString());
            }
        });
    }
}
