package com.alpha.team.eventhub.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alpha.team.eventhub.R;
import com.alpha.team.eventhub.adapters.HomeRecyclerAdapter;
import com.alpha.team.eventhub.entities.*;
import com.alpha.team.eventhub.network.CheckInternet;
import com.alpha.team.eventhub.network.RetrofitClientInstance;
import com.alpha.team.eventhub.network.RetrofitService;
import com.alpha.team.eventhub.services.NetworkChangeReceiver;
import com.alpha.team.eventhub.services.UpdateService;
import com.alpha.team.eventhub.sharedprefrence.ServicePreferences;
import com.alpha.team.eventhub.sharedprefrence.UserPreference;
import com.alpha.team.eventhub.utils.Constants;
import com.alpha.team.eventhub.utils.CustomRecyclerOnClick;
import com.alpha.team.eventhub.utils.GridDecorator;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.alpha.team.eventhub.utils.Constants.*;

/**
 * Created by clasence on 03,December,2018
 * On my honor, as a Carnegie-Mellon Africa student, I have neither given nor received unauthorized assistance on this work.
 * Activity holds the main view for the application
 */

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ServicePreferences servicePreferences;
    private UserPreference userPreference;


    private RecyclerView recyclerView;
    private ProgressBar pbLoad;
    private ImageView imvNoInt;
    private TextView tvNoInt;
    private TextView tvReload;

    private ArrayList<Event> eventList;
    private HomeRecyclerAdapter adapter;
    private int type = 0;
    private Double longitude = 0.0, latitude = 0.0;

    //recycler manager
    RecyclerView.LayoutManager mLayoutManager;

    private FusedLocationProviderClient mFusedLocationProviderClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        servicePreferences = new ServicePreferences(getApplicationContext());
        userPreference = new UserPreference(getApplicationContext());
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());

        setUpDrawer();
        launchReceiver();
        bindViews();
        recoverDataOrLoad(savedInstanceState);
        subscribeFirebase();
        startUpdateService();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (eventList.size() > 0) {
            outState.putParcelableArrayList(EVENT_LIST, eventList);
            outState.putParcelable(LAYOUT_MAN_STATE, mLayoutManager.onSaveInstanceState());
        }

    }


    /**
     * Set up the navigation drawer
     *
     * @param navigationMenu
     */
    private void setupNavigationMenu(Menu navigationMenu) {
        if (userPreference.isLoggedIn()) {
            navigationMenu.findItem(R.id.login_register).setTitle(getString(R.string.logout));
        } else {
            navigationMenu.findItem(R.id.login_register).setTitle(getString(R.string.login_register));
        }

    }

    /**
     * Change email of navigation view headerview when user signs in
     *
     * @param view
     */
    private void changeEmail(View view) {

        if (userPreference.isLoggedIn()) {
            try {
                TextView tvEmail = view.findViewById(R.id.tv_email);
                tvEmail.setText(userPreference.getUser().getEmail());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    /**
     * Subscribe to firebase topic
     */
    private void subscribeFirebase() {
        if (CheckInternet.isNetworkAvailable(getApplicationContext())) {
            FirebaseMessaging.getInstance().subscribeToTopic(getString(R.string.firebase_msg));
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
        tvReload.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        pbLoad.setVisibility(View.VISIBLE);
        eventList = new ArrayList<>();
        tvReload.setOnClickListener(new Listerners());

    }


    /**
     * Recover data from savedInstanceState or load from the database
     * @param savedInstanceState
     */
    private void recoverDataOrLoad(Bundle savedInstanceState) {

        if (savedInstanceState != null && savedInstanceState.containsKey(EVENT_LIST)) {
            recyclerView.setVisibility(View.VISIBLE);
            pbLoad.setVisibility(View.GONE);

            mLayoutManager = new GridLayoutManager(this, 1);
            eventList = savedInstanceState.getParcelableArrayList(EVENT_LIST);
            adapter = new HomeRecyclerAdapter(eventList, this, new Listerners());

            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.addItemDecoration(new GridDecorator(1, 5, true));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(adapter);
            mLayoutManager.onRestoreInstanceState(savedInstanceState.getParcelable(LAYOUT_MAN_STATE));


        } else {
            adapter = new HomeRecyclerAdapter(eventList, this, new Listerners());

            mLayoutManager = new GridLayoutManager(this, 1);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.addItemDecoration(new GridDecorator(1, 5, true));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(adapter);

            if (CheckInternet.isNetworkAvailable(this)) {
                getEvents(type);
            } else {
                showError(getString(R.string.noint));
            }

        }
    }


    /**
     * Show error on view
     * @param error
     */
    private void showError(String error) {
        tvNoInt.setVisibility(View.VISIBLE);
        tvReload.setVisibility(View.VISIBLE);
        imvNoInt.setVisibility(View.VISIBLE);
        tvNoInt.setText(error);
        recyclerView.setVisibility(View.GONE);
        pbLoad.setVisibility(View.GONE);

    }


    /**
     * Hide the error view
     */
    private void hideError() {
        tvNoInt.setVisibility(View.GONE);
        tvReload.setVisibility(View.GONE);
        imvNoInt.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        pbLoad.setVisibility(View.VISIBLE);
    }

    /**
     * Hide progressbar
     */
    private void hideProgressBar() {
        recyclerView.setVisibility(View.VISIBLE);
        pbLoad.setVisibility(View.GONE);
    }


    /**
     * Launch the broadcast receiver for connectivity change listerning if local database is not upd to date
     *
     */
    private void launchReceiver() {
        if (!servicePreferences.getGotCategories() || !servicePreferences.getGotCountries()) {
            IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
            getApplicationContext().registerReceiver(new NetworkChangeReceiver(), intentFilter);
        }
    }


    /**
     * Start the service to update countries and categories locally
     */
    private void startUpdateService() {
        if (CheckInternet.isNetworkAvailable(getApplicationContext())) {
            if (!servicePreferences.getGotCountries()) {
                Intent intent = new Intent(getApplicationContext(), UpdateService.class);
                intent.setAction(Constants.COLUMN_GOT_COUNTRIES);
                startService(intent);
            }

            if (!servicePreferences.getGotCategories()) {
                Intent intent = new Intent(getApplicationContext(), UpdateService.class);
                intent.setAction(Constants.COLUMN_GOT_CATEGORIES);
                startService(intent);
            }
        }
    }

    /**
     * setup Drawer
     */

    private void setUpDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new Listerners());
        changeEmail(navigationView.getHeaderView(0));
        setupNavigationMenu(navigationView.getMenu());
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh: {
                recoverDataOrLoad(null);
                break;
            }
            case R.id.by_likes: {
                type = 0;
                recoverDataOrLoad(null);
                break;
            }
            case R.id.by_distance: {
                type = 1;

                buildLocationChain(this);
            }
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * Get events
     */
    private void getEvents(int type) {
        //create retrofit interface
        hideError();
        RetrofitService retrofitService = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);

        Call<List<Event>> call = null;
        if (type == 0) {
            call = retrofitService.getEventsByLike();
        } else if (type == 1) {

            HashMap<String, Object> map = new HashMap<>();
            map.put("latitude", latitude);
            map.put("longitude", longitude);
            call = retrofitService.getEventsByLocation(map);
        }


        call.enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                Log.e("response", response.body().toString());
                hideProgressBar();
                if (response.code() == 200) {
                    eventList.clear();
                    eventList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {
                showError(t.toString());
                Log.e("MainActivity", t.toString());
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SEND_DATA_TO_ACTIVITY_CODE) {
            Log.e("request", "in request code");
            if (resultCode == RESULT_OK) {
                Log.e("result", "in result code");
                recoverDataOrLoad(null);
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            if (requestCode == MAIN_MAPS_PERMISSIONS_CODE) {

                buildLocationChain(MainActivity.this);
            }


        }
    }


    /**
     * Build the algorithm to get user's current location
     * @param context
     */
    private void buildLocationChain(final Activity context) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                checkAndOpenGPS(context);
                getLastLocation();
            } else {
                //Request Location Permission
                askLocationPermission(context);
            }
        } else {
            checkAndOpenGPS(context);
            getLastLocation();
        }
    }

    /**
     * Request location permission from user
     * @param context
     */
    private void askLocationPermission(final Activity context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Show explanation if need be
            if (ActivityCompat.shouldShowRequestPermissionRationale(context,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                //show rationale
                new AlertDialog.Builder(context)
                        .setTitle(context.getString(R.string.permission_needed))
                        .setMessage(context.getString(R.string.ask_permission))
                        .setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(context,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                                        MAIN_MAPS_PERMISSIONS_CODE);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(context,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MAIN_MAPS_PERMISSIONS_CODE);
            }
        }
    }


    /**
     * Get last known location of user
     */
    private void getLastLocation() {
        try {
            mFusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(new Listerners())
                    .addOnFailureListener(new Listerners());


        } catch (SecurityException e) {
        }
    }


    /**
     * Check if Location is turned on on device and prompt the user to turn it on
     * @param context
     */
    private void checkAndOpenGPS(final Activity context) {
        final LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);


            builder.setMessage(context.getString(R.string.turn_on_gps))
                    .setPositiveButton(context.getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogInterface, int id) {
                                    context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                    dialogInterface.dismiss();
                                }
                            })
                    .setNegativeButton(context.getString(R.string.cancel),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogInterface, int id) {
                                    dialogInterface.cancel();
                                }
                            });
            builder.create().show();
        }

    }


    /**
     * Class implements all the listerners for this class.
     */
    private class Listerners implements NavigationView.OnNavigationItemSelectedListener, CustomRecyclerOnClick, View.OnClickListener, OnSuccessListener<Location>, OnFailureListener {
        @SuppressWarnings("StatementWithEmptyBody")
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            // Handle navigation view item clicks here.
            int id = item.getItemId();

            if (id == R.id.about) {
                Intent intent = new Intent(getApplicationContext(), InfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(VIEW_PICKER, 0);
                intent.putExtras(bundle);
                startActivity(intent);
                return true;

            } else if (id == R.id.help) {
                Intent intent = new Intent(getApplicationContext(), InfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(VIEW_PICKER, 1);
                intent.putExtras(bundle);
                startActivity(intent);
                return true;

            } else if (id == R.id.feedback) {
                Intent intent = new Intent(getApplicationContext(), InfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(VIEW_PICKER, 2);
                intent.putExtras(bundle);
                startActivity(intent);
                return true;
            } else if (id == R.id.login_register) {
                if (userPreference.isLoggedIn()) {
                    if (userPreference.logoutUser()) {
                        servicePreferences.clearData();
                        Toast.makeText(getApplicationContext(), getString(R.string.logout_success), Toast.LENGTH_SHORT).show();
                        item.setTitle(getString(R.string.login_register));

                        onBackPressed();
                    }
                    return true;
                } else {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    return true;
                }

            } else if (id == R.id.settings) {

                return true;
            } else if (id == R.id.update_fav) {
                return true;

            } else if (id == R.id.register_event) {
                return true;

            } else if (id == R.id.registeredEvent) {
                Intent intent = new Intent(getApplicationContext(), RegisteredEventsActivity.class);
                startActivity(intent);
                return true;
            }


            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }

        @Override
        public void onCustomClick(int position) {
            Intent intent = new Intent(getApplicationContext(), EventDetailsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable(CURENT_EVENT, eventList.get(position));
            bundle.putInt(DATE_SOURCE, 0);
            intent.putExtras(bundle);
            startActivityForResult(intent, SEND_DATA_TO_ACTIVITY_CODE);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_reload: {
                    recoverDataOrLoad(null);
                }
            }
        }

        @Override
        public void onFailure(@NonNull Exception e) {
            type = 0;
            Toast.makeText(getApplicationContext(), getString(R.string.failed_last_position), Toast.LENGTH_SHORT).show();
            recoverDataOrLoad(null);
        }

        @Override
        public void onSuccess(Location location) {
            if (location != null) {
                Log.e("stk", "Location result received onSuccess");
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                type = 1;
                recoverDataOrLoad(null);
            } else {
                type = 0;
                Toast.makeText(getApplicationContext(), getString(R.string.failed_last_position), Toast.LENGTH_SHORT).show();
                recoverDataOrLoad(null);
            }

        }
    }


}
