package com.alpha.team.eventhub.activities;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alpha.team.eventhub.R;
import com.alpha.team.eventhub.adapters.HomeRecyclerAdapter;
import com.alpha.team.eventhub.db.EventEntry;
import com.alpha.team.eventhub.entities.Event;
import com.alpha.team.eventhub.network.CheckInternet;
import com.alpha.team.eventhub.sharedprefrence.ServicePreferences;
import com.alpha.team.eventhub.utils.CustomRecyclerOnClick;
import com.alpha.team.eventhub.utils.GridDecorator;

import java.util.ArrayList;

import static com.alpha.team.eventhub.utils.Constants.CURENT_EVENT;
import static com.alpha.team.eventhub.utils.Constants.DATE_SOURCE;
import static com.alpha.team.eventhub.utils.Constants.EVENT_LIST;
import static com.alpha.team.eventhub.utils.Constants.LAYOUT_MAN_STATE;
import static com.alpha.team.eventhub.utils.Constants.REGISTERED_LOADER_ID;

/**
 * Created by clasence on 01,December,2018
 * On my honor, as a Carnegie-Mellon Africa student, I have neither given nor received unauthorized assistance on this work.
 */
public class RegisteredEventsActivity extends AppCompatActivity {


    private Toolbar toolbar;
    private ServicePreferences servicePreferences;


    private RecyclerView recyclerView;
    private ProgressBar pbLoad;
    private ImageView imvNoInt;
    private TextView tvNoInt;
    private TextView tvReload;

    private ArrayList<Event> eventList;
    private HomeRecyclerAdapter adapter;
    //recycler manager
    RecyclerView.LayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered_events);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        servicePreferences = new ServicePreferences(getApplicationContext());
        bindViews();
        recoverDataOrLoad(savedInstanceState);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
            }
        }
        return true;
    }




    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(eventList.size()>0) {
            outState.putParcelableArrayList(EVENT_LIST, eventList);
            outState.putParcelable(LAYOUT_MAN_STATE, mLayoutManager.onSaveInstanceState());
        }

    }

    /**
     * Start loader to load data from SQLite
     */
    private void startLoader() {
        if (getSupportLoaderManager().getLoader(REGISTERED_LOADER_ID) == null) {
            getSupportLoaderManager().initLoader(REGISTERED_LOADER_ID, null, new Listerners()).forceLoad();
        } else {
            getSupportLoaderManager().restartLoader(REGISTERED_LOADER_ID, null, new Listerners()).forceLoad();
        }
    }

    /**
     * Bind all views from foreground to java equivalents
     */
    private void bindViews() {
        recyclerView = findViewById(R.id.homeRecycler);
        pbLoad = findViewById(R.id.pbload);
        tvNoInt = findViewById(R.id.tv_noint);
        tvReload = findViewById(R.id.tv_reload);
        imvNoInt = findViewById(R.id.imv_noint);

        tvNoInt.setVisibility(View.GONE);
        imvNoInt.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        pbLoad.setVisibility(View.VISIBLE);
        tvReload.setVisibility(View.GONE);
        eventList = new ArrayList<>();

    }

    /**
     * Recover data from savedInstanceState or load from the database
     * @param savedInstanceState
     */
    private void recoverDataOrLoad(Bundle savedInstanceState){

        if(savedInstanceState!=null && savedInstanceState.containsKey(EVENT_LIST)){
            recyclerView.setVisibility(View.VISIBLE);
            pbLoad.setVisibility(View.GONE);

            mLayoutManager = new GridLayoutManager(this, 1);
            eventList =  savedInstanceState.getParcelableArrayList(EVENT_LIST);
            adapter = new HomeRecyclerAdapter(eventList, this,new Listerners());

            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.addItemDecoration(new GridDecorator(1, 5, true));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(adapter);
            mLayoutManager.onRestoreInstanceState(savedInstanceState.getParcelable(LAYOUT_MAN_STATE));

            pbLoad.setVisibility(View.GONE);
        }else {
            adapter = new HomeRecyclerAdapter(eventList, this,new Listerners());

            mLayoutManager = new GridLayoutManager(this, 1);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.addItemDecoration(new GridDecorator(1, 5, true));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(adapter);
            startLoader();

        }
    }

    /**
     * Class implements all the listerners for this class.
     */
    private class Listerners implements View.OnClickListener,CustomRecyclerOnClick, LoaderManager.LoaderCallbacks<Cursor> {

        @Override
        public void onClick(View v) {

        }

        @Override
        public void onCustomClick(int position) {
            Intent intent = new Intent(getApplicationContext(),EventDetailsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable(CURENT_EVENT,eventList.get(position));
            bundle.putInt(DATE_SOURCE, 0);
            intent.putExtras(bundle);
            startActivity(intent);
        }

        @NonNull
        @Override
        public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
            Uri uri = EventEntry.EVENT_CONTENT_URI;
            return new CursorLoader(getApplicationContext(), uri, null, null, null, null);
        }

        @Override
        public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
            pbLoad.setVisibility(View.GONE);
            if (cursor != null && cursor.moveToFirst()) {
                eventList.clear();
                while (!cursor.isAfterLast()) {
                    Event event = new Event();
                    event.setId(cursor.getInt(cursor.getColumnIndex(EventEntry._ID)));
                    event.setUserId(cursor.getInt(cursor.getColumnIndex(EventEntry.COLUMN_USER_ID)));
                    event.setCountryId(cursor.getInt(cursor.getColumnIndex(EventEntry.COLUMN_COUNTRY_ID)));
                    event.setCityId(cursor.getInt(cursor.getColumnIndex(EventEntry.COLUMN_CITY_ID)));
                    event.setCategoryId(cursor.getInt(cursor.getColumnIndex(EventEntry.COLUMN_CATEGORY_ID)));
                    event.setEventStatus(cursor.getInt(cursor.getColumnIndex(EventEntry.COLUMN_EVENT_STATUS)));

                    event.setEventName(cursor.getString(cursor.getColumnIndex(EventEntry.COLUMN_EVENT_NAME)));
                    event.setAddress(cursor.getString(cursor.getColumnIndex(EventEntry.COLUMN_ADDRESS)));
                    event.setDescription(cursor.getString(cursor.getColumnIndex(EventEntry.COLUMN_DESCRIPTION)));
                    event.setPicture(cursor.getString(cursor.getColumnIndex(EventEntry.COLUMN_PICTURE)));
                    event.setDateFrom(cursor.getString(cursor.getColumnIndex(EventEntry.COLUMN_DATE_FROM)));
                    event.setDateTo(cursor.getString(cursor.getColumnIndex(EventEntry.COLUMN_DATE_TO)));
                    event.setCreatedAt(cursor.getString(cursor.getColumnIndex(EventEntry.COLUMN_CREATED_AT)));
                    event.setUpdatedAt(cursor.getString(cursor.getColumnIndex(EventEntry.COLUMN_UPDATED_AT)));
                    event.setAcceptOnRegistration(cursor.getInt(cursor.getColumnIndex(EventEntry.COLUMN_ACCEPT_REG)));

                    event.setLatitude(cursor.getDouble(cursor.getColumnIndex(EventEntry.COLUMN_LATITUDE)));
                    event.setLongitude(cursor.getDouble(cursor.getColumnIndex(EventEntry.COLUMN_LONGITUDE)));

                    eventList.add(event);
                    cursor.moveToNext();
                }
                adapter.notifyDataSetChanged();

            }else{
                Toast.makeText(getApplicationContext(),getString(R.string.no_fav),Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onLoaderReset(@NonNull Loader<Cursor> loader) {

        }
    }


}
