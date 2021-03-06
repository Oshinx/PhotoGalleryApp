package com.michael.Com6510.photogallary.activity;
//inspiration where gotten from the following sources   https://www.youtube.com/watch?v=USbTcGx1mD0&list=PLk7v1Z2rk4hjHrGKo9GqOtLs1e2bglHHA, https://www.youtube.com/watch?v=zHGgSd1wvxY
//https://www.youtube.com/watch?v=dVwR5Gpw1_E, https://www.youtube.com/results?search_query=telusko+android+beginners,https://www.youtube.com/watch?v=Zxf1mnP5zcw
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;

/**
 * Created by Jerry on 2018/1/4.
 */

@SuppressWarnings("ConstantConditions")
public class GPS_Service extends Service {
    private LocationListener listener;
    private LocationManager locationManager;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate() {
        listener=new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Intent i =new Intent("location_update");
                i.putExtra("latitude", location.getLatitude());
                i.putExtra("longitude",location.getLongitude());
                sendBroadcast(i);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            /**
             * check if the user turn on the location service,if not,go to settings
             * @param provider
             */

            @Override
            public void onProviderDisabled(String provider) {
                Intent i=new Intent (Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);

            }
        };
        locationManager= (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,3000,0,listener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(locationManager !=null){
            locationManager.removeUpdates(listener);
        }
    }
}
