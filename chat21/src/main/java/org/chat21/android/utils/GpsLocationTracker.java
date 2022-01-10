package org.chat21.android.utils;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.ContextThemeWrapper;

import androidx.core.app.ActivityCompat;

import org.chat21.android.R;


/**
 * Gps location tracker class
 * to get users location and other information related to location
 */
public class GpsLocationTracker {

    /**
     * min distance change to get location update
     */
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATE = 10;
    /**
     * min time for location update
     * 60000 = 1min
     */
    private static final long MIN_TIME_FOR_UPDATE = 3000;
    /**
     * context of calling class
     */
    private final Context mContext;
    LocationListener locationListener;
    /**
     * flag for gps status
     */
    private boolean isGpsEnabled = false;
    /**
     * flag for network status
     */
    private boolean isNetworkEnabled = false;
    /**
     * flag for gps
     */
    private boolean canGetLocation = false;
    /**
     * location
     */
    private Location mLocation;
    /**
     * latitude
     */
    private double mLatitude;
    /**
     * longitude
     */
    private double mLongitude;
    /**
     * location manager
     */
    private LocationManager mLocationManager;

    private static final String TAG = "GpsLocationTracker";

    /**
     * @param mContext         constructor of the class
     * @param locationListener
     */
    public GpsLocationTracker(Context mContext, LocationListener locationListener) {

        this.mContext = mContext;
        this.locationListener = locationListener;
        getLocation();
    }

    /**
     * @return location
     */
    public Location getLocation() {

        try {

            mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

            /*getting status of the gps*/
            isGpsEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            /*getting status of network provider*/
            isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGpsEnabled && !isNetworkEnabled) {

                /*no location provider enabled*/
            } else {

                this.canGetLocation = true;



                /*if gps is enabled then get location using gps*/
                if (isGpsEnabled) {

                    mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_FOR_UPDATE, MIN_DISTANCE_CHANGE_FOR_UPDATE, locationListener);

                    if (mLocationManager != null) {

                        mLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                        if (mLocation != null) {

                            mLatitude = mLocation.getLatitude();

                            mLongitude = mLocation.getLongitude();
                            Log.d(TAG, "getLocation:isGpsEnabled " + mLatitude);

                        }

                    }

                }

                /*

                 */
                /*getting location from network provider*//*

                if (mLocation == null&&isNetworkEnabled) {


                    mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_FOR_UPDATE, MIN_DISTANCE_CHANGE_FOR_UPDATE, locationListener);

                    if (mLocationManager != null) {

                        mLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                        if (mLocation != null) {

                            mLatitude = mLocation.getLatitude();

                            mLongitude = mLocation.getLongitude();

                            Log.d(TAG, "getLocation:isNetworkEnabled "+mLatitude);
                        }
                    }

                }
*/


            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return mLocation;
    }

    public Location getmLocation() {
        if (mLocation == null) {
            return getLocation();

        }
        return mLocation;

    }

    /**
     * call this function to stop using gps in your application
     */
    public void stopUsingGps() {

        if (mLocationManager != null) {

            mLocationManager.removeUpdates(locationListener);

        }
    }

    /**
     * @return latitude
     * <p/>
     * function to get latitude
     */
    public double getLatitude() {

        if (mLocation != null) {


            mLatitude = mLocation.getLatitude();
        }
        return mLatitude;
    }

    /**
     * @return longitude
     * function to get longitude
     */
    public double getLongitude() {

        if (mLocation != null) {

            mLongitude = mLocation.getLongitude();

        }

        return mLongitude;
    }

    /**
     * @return to check gps or wifi is enabled or not
     */
    public boolean canGetLocation() {

        return this.canGetLocation;
    }

    /**
     * function to prompt user to open
     * settings to enable gps
     */
    public void showSettingsAlert() {

        AlertDialog.Builder mAlertDialog = new AlertDialog.Builder(new ContextThemeWrapper(mContext, R.style.AppTheme));

        mAlertDialog.setTitle("Gps Disabled");

        mAlertDialog.setMessage("gps is not enabled . do you want to enable ?");

        mAlertDialog.setPositiveButton("settings", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                Intent mIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(mIntent);
            }
        });

        mAlertDialog.setNegativeButton("cancle", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.cancel();

            }
        });

        final AlertDialog mcreateDialog = mAlertDialog.create();
        mcreateDialog.show();
    }



}