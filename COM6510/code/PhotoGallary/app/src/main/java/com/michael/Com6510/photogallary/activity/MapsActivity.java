package com.michael.Com6510.photogallary.activity;
//inspiration where gotten from the following sources   https://www.youtube.com/watch?v=USbTcGx1mD0&list=PLk7v1Z2rk4hjHrGKo9GqOtLs1e2bglHHA, https://www.youtube.com/watch?v=zHGgSd1wvxY
//https://www.youtube.com/watch?v=dVwR5Gpw1_E, https://www.youtube.com/results?search_query=telusko+android+beginners,https://www.youtube.com/watch?v=Zxf1mnP5zcw
import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.michael.Com6510.photogallary.R;
import com.michael.Com6510.photogallary.database.AppDataBase;
import com.michael.Com6510.photogallary.database.entity.Image;
import com.michael.Com6510.photogallary.utils.Utils;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private GoogleMap mGoogleMap;
    private GoogleApiClient mGoogleApiClient;
    private List<Image> imageList;
    private List<com.michael.Com6510.photogallary.database.entity.Location> locations;
    private AppDataBase appDataBase;
    private int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (googleServicesAvailable()) {
            setContentView(R.layout.activity_maps);
            initMap();
        } else {
            //no Google Maps Layout

        }



    }

    /**
     * initialising the map
     */
    private void initMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * check whether google play service is available
     * @return boolean   true if  google play service is available, false if google play service is unavailable
     */
    public boolean googleServicesAvailable() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(this);
        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (api.isUserResolvableError(isAvailable)) {
            Dialog dialog = api.getErrorDialog(this, isAvailable, 0);
            dialog.show();
        } else {
            Toast.makeText(this, "cant connect to play services", Toast.LENGTH_LONG).show();
        }
        return false;

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        if(mGoogleMap!=null){
            mGoogleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                @Override
                public void onMarkerDragStart(Marker marker) {

                }

                @Override
                public void onMarkerDrag(Marker marker) {

                }

                @Override
                public void onMarkerDragEnd(Marker marker) {
                    Geocoder gc=new Geocoder(MapsActivity.this);
                    LatLng ll=marker.getPosition();
                    List<Address> list=null;
                    try {
                        list=gc.getFromLocation(ll.latitude,ll.longitude,1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Address add =list.get(0);
                    marker.setTitle(add.getLocality());
                    marker.showInfoWindow();

                }
            });
            /**
             * show info window when you click the marker
             */
            mGoogleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    View v=getLayoutInflater().inflate(R.layout.info_window,null);

                    TextView tvLat=(TextView)v.findViewById(R.id.tv_lat);
                    TextView tvLng=(TextView)v.findViewById(R.id.tv_lng);
                    TextView tvSnippet=(TextView)v.findViewById(R.id.tv_snippet);
                    ImageView image = v.findViewById(R.id.imageView1);

                    LatLng ll=marker.getPosition();
                    tvLat.setText("Latitude:"+ll.latitude);
                    tvLng.setText("Longitude"+ll.longitude);
                    tvSnippet.setText(marker.getSnippet());
                    Bitmap bitmap = Utils.decodeSampledBitmapFromResources(imageList.get(position).getImage(),40,40);
                    image.setImageBitmap(bitmap);
                    return v;
                }
            });
        }
        goToLocationZoom(53.381945, -1.482132, 8);

        Bundle bundle = getIntent().getExtras();
        imageList = (List<Image>) getIntent().getSerializableExtra("imagelist");
        for (int i = 0; i < imageList.size() ; i++) {
            try {
                File file  = new File(imageList.get(i).getImage());
                ExifInterface exifInterface = new ExifInterface(file.getAbsolutePath());
                float [] latlong = new float[2];
                if(exifInterface.getLatLong(latlong)){
                float latitude = latlong[0];
                float longitude = latlong[1];
               ;
                if(latitude != 0 && longitude != 0 ) {
                    setMarker(latitude,longitude);
                    position = i ;
                }}
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * go to one specified location according to latitude and longitude and zoom to a pointed times
     * @param lat   latitude
     * @param lng  longitude
     * @param zoom
     */

    private void goToLocationZoom(double lat, double lng, float zoom) {
        LatLng ll = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);
        mGoogleMap.moveCamera(update);

    }

    /**
     * set marker for the photo according to the latitude and longitude
     * @param lat   latitude
     * @param lng  longitude
     */

    private void setMarker(double lat, double lng) {
        MarkerOptions options=new MarkerOptions()
                .draggable(true)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                .position(new LatLng(lat,lng))
                .snippet("I am here");
        Marker marker=mGoogleMap.addMarker(options);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    /**
     * switch between different types of maps
     * @param item
     * @return boolean true if the menu option is selected, false if the menu option is not selected
     */

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mapTypeNone:
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NONE);
                break;
            case R.id.mapTypeNormal:
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case R.id.mapTypeSatellite:
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.mapTypeTerrain:
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
            case R.id.mapTypeHybrid:
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    LocationRequest mLocationRequest;

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if(location==null){
            Toast.makeText(this,"cant get current location",Toast.LENGTH_LONG).show();
        }else{
            LatLng ll=new LatLng(location.getLatitude(),location.getLongitude());
            CameraUpdate update= CameraUpdateFactory.newLatLngZoom(ll,15);
            mGoogleMap.animateCamera(update);
        }
    }


}
