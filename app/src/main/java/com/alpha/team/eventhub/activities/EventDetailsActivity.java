package com.alpha.team.eventhub.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alpha.team.eventhub.R;
import com.alpha.team.eventhub.db.EventEntry;
import com.alpha.team.eventhub.entities.Event;
import com.alpha.team.eventhub.exceptions.EventHubException;
import com.alpha.team.eventhub.listerners.AddToCalendar;
import com.alpha.team.eventhub.listerners.LikeEvent;
import com.alpha.team.eventhub.listerners.ListernerContext;
import com.alpha.team.eventhub.listerners.RegisterEvent;
import com.alpha.team.eventhub.listerners.ShowMap;
import com.alpha.team.eventhub.listerners.VisitWebsite;
import com.alpha.team.eventhub.network.RetrofitClientInstance;
import com.alpha.team.eventhub.sharedprefrence.UserPreference;
import com.alpha.team.eventhub.utils.GeneralHelpers;
import com.squareup.picasso.Picasso;

import java.util.Date;

import static com.alpha.team.eventhub.utils.Constants.*;

/**
 * Created by clasence on 25,November,2018
 * On my honor, as a Carnegie-Mellon Africa student, I have neither given nor received unauthorized assistance on this work.
 *Activity shows details of an event and allows for actions on activity
 */

public class EventDetailsActivity extends AppCompatActivity {

    private ImageView imvPicture;
    private ImageView imvLike;
    private TextView tvEventName;
    private TextView tvLocation;
    private TextView tvLocation2;
    private TextView tvDate;
    private TextView tvDescription;
    public static TextView tvLike;
    private Button btnAddCalendar;
    private Button btnLocation;
    private Button btnWebsite;
    private Button btnRegister;
    private Event event;
    private UserPreference userPreference;
    private boolean isRegistered = false;
    private boolean isLiked = false;
    private boolean justLiked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bindViews();
        Bundle bundle = getIntent().getExtras();
        event = bundle.getParcelable(CURENT_EVENT);
        startLoader();
        loadPicture();
        populateViews();
    }

    /**
     * Start the Loader to load events from local database through ContentProvider
     */

    private void startLoader() {
        if (getSupportLoaderManager().getLoader(EVENT_DETAILS_LOADER_ID) == null) {
            getSupportLoaderManager().initLoader(EVENT_DETAILS_LOADER_ID, null, new Listerner()).forceLoad();
        } else {
            getSupportLoaderManager().restartLoader(EVENT_DETAILS_LOADER_ID, null, new Listerner()).forceLoad();
        }
    }

    /**
     * Bind all views from foreground to java equivalents
     */
    private void bindViews() {
        imvPicture = findViewById(R.id.imv_thumbnail);
        imvLike = findViewById(R.id.imv_like);
        tvEventName = findViewById(R.id.tv_eventname);
        tvLocation = findViewById(R.id.tv_location);
        tvDate = findViewById(R.id.tv_date);
        tvDescription = findViewById(R.id.tv_description);
        tvLike = findViewById(R.id.tv_like);
        tvLocation2 = findViewById(R.id.tv_location2);
        btnAddCalendar = findViewById(R.id.btn_add_calendar);
        btnLocation = findViewById(R.id.btn_see_location);
        btnWebsite = findViewById(R.id.btn_website);
        btnRegister = findViewById(R.id.btn_register);

        btnRegister.setOnClickListener(new Listerner());
        btnWebsite.setOnClickListener(new Listerner());
        btnLocation.setOnClickListener(new Listerner());
        btnAddCalendar.setOnClickListener(new Listerner());
        imvLike.setOnClickListener(new Listerner());
        userPreference = new UserPreference(getApplicationContext());

    }


    /**
     * Load picture from server
     */
    private void loadPicture() {
        String path = RetrofitClientInstance.BASE_URL + event.getPicture();
        Picasso.with(getApplicationContext()).load(path).fit()
                .error(android.R.drawable.ic_menu_slideshow)
                .placeholder(android.R.drawable.ic_menu_slideshow)
                .into(imvPicture);
    }

    /**
     * Populate views with relevant event information
     */
    private void populateViews() {
        tvLocation.setText(event.getAddress());
        tvLocation2.setText(event.getAddress());
        tvEventName.setText(event.getEventName());
        tvLike.setText(String.valueOf(event.getLikeCount()));
        tvDescription.setText(event.getDescription());

        if (GeneralHelpers.isLiked(getApplicationContext(), event)) {
            isLiked = true;
            justLiked = true;
            imvLike.setBackground(getResources().getDrawable(R.color.colorBrown));

        }


        if (event.getDateTo() != null && event.getDateFrom() != null) {
            Date dateFrom = null;
            try {
                dateFrom = GeneralHelpers.formatDate(event.getDateFrom());

                Date dateTo = GeneralHelpers.formatDate(event.getDateTo());
                String startString = "";
                String endString = "";
                if (dateFrom != null) {
                    startString = dateFrom.toString().substring(0, 16);
                }
                if (dateTo != null) {
                    endString = dateTo.toString().substring(0, 16);

                }

                if (dateFrom != null) {
                    tvDate.setText(startString + " " + getString(R.string.to) + " " + endString);
                }
            } catch (EventHubException e) {
                e.printStackTrace();
            }
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                return true;
            }
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (justLiked) {
            Intent intent = new Intent();
            intent.putExtra(SEND_DATA_TO_ACTIVITY, 1);
            setResult(RESULT_OK, intent);
            Log.e("intent", intent.toString());
            finish();
        }
        super.onBackPressed();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            if (requestCode == MAPS_PERMISSIONS_CODE) {

                if (event.getLatitude() != 0.0 && event.getLatitude() != 0.0) {
                    ListernerContext context = new ListernerContext(new ShowMap());
                    context.executeStrategy(EventDetailsActivity.this, btnLocation, event);
                }
            } else if (requestCode == CALENDAR_PERMISSIONS_CODE) {
                if (event.getDateFrom() != null && event.getDateTo() != null) {
                    ListernerContext context = new ListernerContext(new AddToCalendar());
                    context.executeStrategy(EventDetailsActivity.this, btnAddCalendar, event);

                }


            }


        }
    }

    /**
     * Class implements all the Listerners for the main class
     */
    private class Listerner implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {

        @Override
        public void onClick(View v) {
            ListernerContext context;
            switch (v.getId()) {
                case R.id.btn_website: {
                    context = new ListernerContext(new VisitWebsite());
                    context.executeStrategy(EventDetailsActivity.this, v, event);
                    break;
                }
                case R.id.btn_register: {
                    if (!isRegistered) {
                        context = new ListernerContext(new RegisterEvent());
                        context.executeStrategy(EventDetailsActivity.this, v, event);
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.user_already_registered), Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                case R.id.imv_like: {
                    if (!isLiked) {
                        context = new ListernerContext(new LikeEvent());
                        context.executeStrategy(EventDetailsActivity.this, v, event);
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.user_already_liked), Toast.LENGTH_SHORT).show();

                    }
                    break;
                }
                case R.id.btn_see_location: {
                    if (event.getLatitude() != 0.0 && event.getLatitude() != 0.0) {
                        context = new ListernerContext(new ShowMap());
                        context.executeStrategy(EventDetailsActivity.this, v, event);
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.no_location), Toast.LENGTH_SHORT).show();

                    }
                    break;
                }
                case R.id.btn_add_calendar: {

                    if (event.getDateFrom() != null && event.getDateTo() != null) {
                        context = new ListernerContext(new AddToCalendar());
                        context.executeStrategy(EventDetailsActivity.this, v, event);
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.no_location), Toast.LENGTH_SHORT).show();

                    }
                }

            }
        }

        @NonNull
        @Override
        public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
            String selection = EventEntry.COLUMN_EVENT_ID + " = ? ";
            String[] selectionArguments = new String[]{String.valueOf(event.getEventId())};


            Uri uri = EventEntry.EVENT_CONTENT_URI;
            return new CursorLoader(getApplicationContext(), uri, null, selection, selectionArguments, null);
        }

        @Override
        public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
            if (cursor != null && cursor.moveToFirst()) {
                if (cursor.getCount() > 0) {
                    isRegistered = true;
                    btnRegister.setVisibility(View.GONE);
                } else {
                    isRegistered = false;
                }
            } else {
                isRegistered = false;
            }
        }

        @Override
        public void onLoaderReset(@NonNull Loader<Cursor> loader) {

        }
    }
}
