package com.michael.Com6510.photogallary.activity;
//inspiration where gotten from the following sources   https://www.youtube.com/watch?v=USbTcGx1mD0&list=PLk7v1Z2rk4hjHrGKo9GqOtLs1e2bglHHA, https://www.youtube.com/watch?v=zHGgSd1wvxY
//https://www.youtube.com/watch?v=dVwR5Gpw1_E, https://www.youtube.com/results?search_query=telusko+android+beginners,https://www.youtube.com/watch?v=Zxf1mnP5zcw
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.stetho.inspector.network.RequestBodyHelper;
import com.google.android.gms.common.ConnectionResult;
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

import com.michael.Com6510.photogallary.Task.UploadImageToServerTask;
import com.michael.Com6510.photogallary.database.AppDataBase;
import com.michael.Com6510.photogallary.database.entity.Image;
import com.michael.Com6510.photogallary.R;
import com.michael.Com6510.photogallary.adapter.SwipeAdapter;
import com.michael.Com6510.photogallary.database.entity.Location;

import org.json.JSONArray;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class ShowImage extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {


    private FloatingActionButton fabUpload;
    private List<Image> imageList;
    private TextView detailsTextView;
    private TextView editTextView;
    private TextView saveTextView;
    private TextView cancelTextView;
    private ImageButton backImageBtn;
    private TextView detailText;
    private TextView titleTextView;
    private TextView descriptionTextView;
    private TextView dateTextView;
    private TextView orientationTextView;
    private TextView sizeTextView;
    private TextView resolutionTextView;
    private EditText titleEditText;
    private EditText descriptionEditText;
    private AppDataBase database;
    private LinearLayout detailsTable;
    private int position;
    private GoogleMap mGoogleMap;
    private GoogleApiClient mGoogleApiClient;
    private final float ZOOM = 15f;
    private ViewPager viewPager;
    private SwipeAdapter swipeAdapter;
    private ImageButton locationDeleteButton;
    private MapFragment map;
    private Activity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        activity = this;

        //transit to the details activity
        fabUpload = (FloatingActionButton) findViewById(R.id.fab_upload_file);
        detailsTextView = findViewById(R.id.details_action);
        editTextView = findViewById(R.id.edit_action);
        detailsTable = (LinearLayout) findViewById(R.id.details_table);
        saveTextView = findViewById(R.id.save_action);
        cancelTextView = findViewById(R.id.cancel_action);
        backImageBtn = findViewById(R.id.back_action);
        detailText = findViewById(R.id.details_text);
        titleTextView = findViewById(R.id.title_textview);
        titleEditText = findViewById(R.id.title_edittext);
        descriptionTextView = findViewById(R.id.description_textview);
        descriptionEditText = findViewById(R.id.description_edittext);
        dateTextView = findViewById(R.id.date_textview);
        orientationTextView = findViewById(R.id.orientation_textview);
        sizeTextView = findViewById(R.id.size_textview);
        resolutionTextView = findViewById(R.id.resolution_textview);
        fabUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final JSONArray jsonArray = new JSONArray();
                try {
                    ExifInterface exifInterface = new ExifInterface(imageList.get(position).getImage());
                    float[] latLong = new float[2];
                    exifInterface.getLatLong(latLong);
                    String latitude = String.valueOf(latLong[0]);
                    String longitude = String.valueOf(latLong[1]);

                    jsonArray.put(imageList.get(position).getTitle());
                    jsonArray.put(imageList.get(position).getDescription());
                    jsonArray.put(latitude);
                    jsonArray.put(longitude);


                } catch (IOException e) {
                    e.printStackTrace();
                }


                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        new UploadImageToServerTask(jsonArray.toString(), new File(imageList.get(position).getImage()), activity).execute();

                    }
                });

                t.start();

            }
        });


        initMap();

        //initialize view components
        detailsTextView = findViewById(R.id.details_action);
        editTextView = findViewById(R.id.edit_action);
        detailsTable = (LinearLayout) findViewById(R.id.details_table);
        saveTextView = findViewById(R.id.save_action);
        cancelTextView = findViewById(R.id.cancel_action);
        backImageBtn = findViewById(R.id.back_action);
        detailText = findViewById(R.id.details_text);
        titleTextView = findViewById(R.id.title_textview);
        titleEditText = findViewById(R.id.title_edittext);
        descriptionTextView = findViewById(R.id.description_textview);
        descriptionEditText = findViewById(R.id.description_edittext);
        dateTextView = findViewById(R.id.date_textview);
        orientationTextView = findViewById(R.id.orientation_textview);
        sizeTextView = findViewById(R.id.size_textview);
        resolutionTextView = findViewById(R.id.resolution_textview);
        locationDeleteButton = findViewById(R.id.delete_location_button);
        map = (MapFragment) getFragmentManager().findFragmentById(R.id.map1);

        //


        detailsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //set the visibilty of the detailstextview to gone and sets the components blow  to visible
                //when detailstextview is clicked.


                detailsTextView.setVisibility(View.GONE);
                editTextView.setVisibility(View.VISIBLE);
                detailsTable.setVisibility(View.VISIBLE);
                backImageBtn.setVisibility(View.VISIBLE);
                detailText.setVisibility(View.VISIBLE);
                displayImageBasicInformation(titleTextView, descriptionTextView);
                displayImageExifdata(dateTextView, orientationTextView, sizeTextView, resolutionTextView);

            }
        });

        cancelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTextView.setVisibility(View.GONE);
                cancelTextView.setVisibility(View.GONE);
                detailsTextView.setVisibility(View.GONE);
                editTextView.setVisibility(View.VISIBLE);
                detailsTable.setVisibility(View.VISIBLE);
                backImageBtn.setVisibility(View.VISIBLE);
                detailText.setVisibility(View.VISIBLE);
                titleTextView.setVisibility(View.VISIBLE);
                descriptionTextView.setVisibility(View.VISIBLE);
                titleEditText.setVisibility(View.GONE);
                descriptionEditText.setVisibility(View.GONE);
                displayImageBasicInformation(titleTextView, descriptionTextView);
                displayImageExifdata(dateTextView, orientationTextView, sizeTextView, resolutionTextView);

            }
        });


        editTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //hides the edittextview and displays the below component
                //also allow for  editing the the image components
                editTextView.setVisibility(View.GONE);
                saveTextView.setVisibility(View.VISIBLE);
                cancelTextView.setVisibility(View.VISIBLE);
                detailText.setVisibility(View.GONE);
                backImageBtn.setVisibility(View.GONE);
                editImageInformation(titleEditText, descriptionEditText);

            }
        });

        saveTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTextView.setVisibility(View.GONE);
                cancelTextView.setVisibility(View.GONE);
                detailsTextView.setVisibility(View.GONE);
                editTextView.setVisibility(View.VISIBLE);
                detailsTable.setVisibility(View.VISIBLE);
                backImageBtn.setVisibility(View.VISIBLE);
                detailText.setVisibility(View.VISIBLE);
                titleTextView.setVisibility(View.VISIBLE);
                descriptionTextView.setVisibility(View.VISIBLE);
                titleEditText.setVisibility(View.GONE);
                descriptionEditText.setVisibility(View.GONE);
                displayImageBasicInformation(titleTextView, descriptionTextView);
                displayImageExifdata(dateTextView, orientationTextView, sizeTextView, resolutionTextView);
                String title = titleEditText.getText().toString();
                String description = descriptionEditText.getText().toString();
                imageList.get(position).setTitle(title);
                imageList.get(position).setDescription(description);
                new UpdateImageInformationTask(imageList.get(position)).execute();
                titleTextView.setText(title);
                descriptionTextView.setText(description);
            }
        });

        locationDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    ExifInterface exifInterface = new ExifInterface(imageList.get(position).getImage());
                    exifInterface.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, "63783873873837");
                    exifInterface.setAttribute(ExifInterface.TAG_GPS_LATITUDE, "874848387387");
                    exifInterface.saveAttributes();
                    map.getView().setVisibility(View.GONE);
                    locationDeleteButton.setVisibility(View.GONE);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        backImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backImageBtn.setVisibility(View.GONE);
                detailsTextView.setVisibility(View.GONE);
                editTextView.setVisibility(View.GONE);
                detailsTable.setVisibility(View.GONE);
                detailText.setVisibility(View.GONE);
                detailsTextView.setVisibility(View.VISIBLE);
            }
        });


        Bundle b = getIntent().getExtras();

        if (b != null) {
            //this is the image position in the itemList
            position = b.getInt("position");
            imageList = (ArrayList<Image>) getIntent().getSerializableExtra("imageList");
            if (position != -1) {
                viewPager = (ViewPager) findViewById(R.id.view_pager);
                swipeAdapter = new SwipeAdapter(this, imageList, position);
                System.out.println(position);
                viewPager.setAdapter(swipeAdapter);
                viewPager.setOffscreenPageLimit(2);
                viewPager.setCurrentItem(position);

            }
        }
    }


    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_DENIED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 60);
                return;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 60 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            uploadToServer();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 60);
            }
            return;
        }
    }

    private void uploadToServer() {
        File file = new File(imageList.get(position).getImage());
        try {
            ExifInterface exifInterface = new ExifInterface(file.getAbsolutePath());
            float[] latlong = new float[2];
            String fileName = file.getName();
            exifInterface.getLatLong(latlong);
            JSONArray array = new JSONArray();
            array.put(imageList.get(position).getTitle());
            array.put(imageList.get(position).getDescription());
            array.put(String.valueOf(latlong[0]));
            array.put(String.valueOf(latlong[1]));


            String content_type = getFileType(file.getPath());
            OkHttpClient client = new OkHttpClient();
            RequestBody fileBody = RequestBody.create(MediaType.parse(content_type), file);
            MultipartBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("type", content_type)
                    .addFormDataPart("uploadImage", fileName, fileBody).build();

            Request request = new Request.Builder()
                    .url("/uploadpicture")
                    .post(requestBody)
                    .build();

            Toast.makeText(getApplicationContext(), "sent", Toast.LENGTH_LONG);


        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    private boolean checkInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if (networkInfo == null && networkInfo.isConnected()) {
            NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if ((mobile != null && mobile.isConnectedOrConnecting() || (wifi != null && wifi.isConnectedOrConnecting())))
                return true;
            else return false;
        } else {
            return false;
        }
    }


    private String getFileType(String path) {
        String extension = MimeTypeMap.getFileExtensionFromUrl(path);
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(extension);
    }

    private void initMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map1);
        mapFragment.getMapAsync(this);
    }


    public void displayImageBasicInformation(TextView titleTextView, TextView descriptionTextView) {
        titleTextView.setText(imageList.get(position).getTitle());
        descriptionTextView.setText(imageList.get(position).getDescription());
    }

    public void displayImageExifdata(TextView dateTextView, TextView orientation, TextView size, TextView resolutionTextView) {

        ExifInterface exifInterface = null;
        try {
            exifInterface = new ExifInterface(imageList.get(position).getImage());
            File file = new File(imageList.get(position).getImage());
            dateTextView.setText(exifInterface.getAttribute(ExifInterface.TAG_DATETIME));
            orientation.setText(exifInterface.getAttribute(ExifInterface.TAG_ORIENTATION));
            size.setText(getFileSize(imageList.get(position).getImage()) + "Kb");//(int) file.length(
            resolutionTextView.setText(exifInterface.getAttribute(ExifInterface.TAG_IMAGE_WIDTH) + " X  " +
                    exifInterface.getAttribute(ExifInterface.TAG_IMAGE_LENGTH));
            float[] latlong = new float[2];
            if (exifInterface.getLatLong(latlong)) {
                float latitude = latlong[0];
                float longitude = latlong[1];
                goToLocationZoom(latitude, longitude, ZOOM);
                setMarker(latitude, longitude);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void editImageInformation(EditText title, EditText description) {
        titleTextView.setVisibility(View.GONE);
        descriptionTextView.setVisibility(View.GONE);
        title.setVisibility(View.VISIBLE);
        description.setVisibility(View.VISIBLE);
        title.setText(imageList.get(position).getTitle());
        description.setText(imageList.get(position).getDescription());

    }

    LocationRequest mLocationRequest;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        if (mGoogleMap != null) {
            mGoogleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                @Override
                public void onMarkerDragStart(Marker marker) {

                }

                @Override
                public void onMarkerDrag(Marker marker) {

                }

                @Override
                public void onMarkerDragEnd(Marker marker) {
                    Geocoder gc = new Geocoder(ShowImage.this);
                    LatLng ll = marker.getPosition();
                    List<Address> list = null;
                    try {
                        list = gc.getFromLocation(ll.latitude, ll.longitude, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Address add = list.get(0);
                    marker.setTitle(add.getLocality());
                    marker.showInfoWindow();


                }
            });
        }


    }


    private void goToLocationZoom(double lat, double lng, float zoom) {
        LatLng ll = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);
        mGoogleMap.moveCamera(update);

    }

    private void setMarker(double lat, double lng) {
        MarkerOptions options = new MarkerOptions()
                .draggable(true)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                .position(new LatLng(lat, lng))
                .snippet("I am here");
        Marker marker = mGoogleMap.addMarker(options);
    }

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
    public void onLocationChanged(android.location.Location location) {
        if (location == null) {
            Toast.makeText(this, "cant get current location", Toast.LENGTH_LONG).show();
        } else {
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, 15);
            mGoogleMap.animateCamera(update);
        }

    }


    private class UpdateImageInformationTask extends AsyncTask<Void, Void, Void> {

        private Image image;

        public UpdateImageInformationTask(Image image) {
            this.image = image;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            database = AppDataBase.getDataBase(activity);
            database.imageDao().update(image);
            imageList = database.imageDao().getAllImages();

            return null;
        }

        public Image getImage() {
            return image;
        }

        public void setImage(Image image) {
            this.image = image;
        }

    }

    public String getFileSize(String path) {
        File file = new File(path);
        double fileSize = file.length() / 1024;
        return Double.toString(fileSize);
    }



    public boolean NetworkAvailable(){
      //  ConnectivityManager connectivityManager =getSystemService(activity.CONNECTIVITY_SERVICE);
    return true;}
}





