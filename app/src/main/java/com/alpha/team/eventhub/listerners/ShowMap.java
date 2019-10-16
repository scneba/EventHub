package com.alpha.team.eventhub.listerners;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;

import com.alpha.team.eventhub.R;
import com.alpha.team.eventhub.entities.Event;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Locale;

import static com.alpha.team.eventhub.utils.Constants.MAPS_PERMISSIONS_CODE;

/**
 * Created by clasence on 01,December,2018
 * On my honor, as a Carnegie-Mellon Africa student, I have neither given nor received unauthorized assistance on this work.
 * <p>
 * Show Map to the event location
 */
public class ShowMap implements Strategy, OnSuccessListener<Location>, OnFailureListener {
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Double longitude = 0.0, latitude = 0.0;
    private Event event;
    private Activity context;

    @Override
    public void execute(Activity context, View v, Event event) {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        this.event = event;
        this.context = context;

        buildLocationChain(context);
    }

    /**
     * Build the algorithm to get user's current location
     *
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
     *
     * @param context
     */
    private void askLocationPermission(final Activity context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
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
                                        MAPS_PERMISSIONS_CODE);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(context,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MAPS_PERMISSIONS_CODE);
            }
        }
    }


    /**
     * Get last known location of user
     */
    private void getLastLocation() {
        try {
            mFusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(this)
                    .addOnFailureListener(this);


        } catch (SecurityException e) {
        }
    }


    /**
     * Check if Location is turned on on device and prompt the user to turn it on
     *
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

    @Override
    public void onFailure(@NonNull Exception e) {
        String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f,%f (%s)",
                context.getString(R.string.my_location), event.getLatitude(), event.getLongitude(), event.getAddress());

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Intent normalIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            context.startActivity(normalIntent);

        }

    }

    @Override
    public void onSuccess(Location location) {
        String uri = null;
        if (location != null) {
            Log.e("stk", "Location result received onSuccess");
            latitude = location.getLatitude();
            longitude = location.getLongitude();

            uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?saddr=%f,%f(%s)&daddr=%f,%f (%s)",
                    latitude, longitude, context.getString(R.string.my_location), event.getLatitude(), event.getLongitude(), event.getAddress());


        } else {

            uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f,%f (%s)",
                    context.getString(R.string.my_location), event.getLatitude(), event.getLongitude(), event.getAddress());


        }

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Intent normalIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            context.startActivity(normalIntent);

        }
    }
}
