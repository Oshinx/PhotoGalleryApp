package com.michael.Com6510.photogallary.activity;
//inspiration where gotten from the following sources   https://www.youtube.com/watch?v=USbTcGx1mD0&list=PLk7v1Z2rk4hjHrGKo9GqOtLs1e2bglHHA, https://www.youtube.com/watch?v=zHGgSd1wvxY
//https://www.youtube.com/watch?v=dVwR5Gpw1_E, https://www.youtube.com/results?search_query=telusko+android+beginners
import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.GradientDrawable;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.stetho.DumperPluginsProvider;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.dumpapp.DumperPlugin;
import com.michael.Com6510.photogallary.database.ImageDao;
import com.michael.Com6510.photogallary.database.entity.Image;
import com.michael.Com6510.photogallary.database.entity.Location;
import com.michael.Com6510.photogallary.R;
import com.michael.Com6510.photogallary.adapter.Adapter;
import com.michael.Com6510.photogallary.database.AppDataBase;
import com.michael.Com6510.photogallary.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private MenuInflater menuInflater;
    private Menu menu;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private static final int REQUEST_READ_EXTERNAL_STORAGE = 2987;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 7829;
    private static final String TAG = "Mainactivity";
    private List<Image> listOfImages;
    private Activity activity;
    private DateFormat dateFormat;
    private Date date;
    private FloatingActionButton listOfActions;
    private FloatingActionButton fabcamera;
    private Utils utils;
    private boolean databaseLoaded = false;
    private AppDataBase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set the activity variable to Mainactivity
        activity = this;

        //get t    initEasyImage();he toolbar and  set it on the actionBar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initEasyImage();

        database = AppDataBase.getDataBase(activity);
        //instantiating the variables listOfImages
        listOfImages = database.imageDao().getAllImages();
        //get the recycler view from the layout file
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        checkForCameraHardware();

        //  specify the number of columns
        int numberOfColumns = 3;
        //set the layout of the recyler view  with  number of columns
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager( numberOfColumns, 1));
        dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        date = new Date();

        adapter = new Adapter(listOfImages);
        listOfImages.size();
        recyclerView.setAdapter(adapter);

        checkPermissions(activity);


        //Floating button that will allow us to get the images from the Gallery
        FloatingActionButton fabGallery = (FloatingActionButton) findViewById(R.id.fab_gallery);
        fabGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EasyImage.openGallery(getActivity(), 0);
            }
        });
        FloatingActionButton fabMap = (FloatingActionButton) findViewById(R.id.fab_map);
        fabMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                intent.putExtra("imagelist", (Serializable)listOfImages);
                startActivity(intent);
            }
        });

        fabcamera = (FloatingActionButton) findViewById(R.id.fab_camera);
        fabcamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EasyImage.openCamera(getActivity(), 0);
            }
        });

    }


    /**
     * intializes the EasyImage with a set of objects
     */
    private void initEasyImage() {
        EasyImage.configuration(this)
                //set the folder name for the images
                .setImagesFolderName("Easy")
                //adds new picture to the gallery

                .setCopyTakenPhotosToPublicGalleryAppFolder(true)
                //still not sure           //adds selected images
                .setCopyPickedImagesToPublicGalleryAppFolder(false)
                //it allows for multiple picture selection in the gallery
                .setAllowMultiplePickInGallery(true)
                .getFolderName();

    }


    /**
     * checks the read and write permission at runtime for  andriod operating systems
     * greater than lollipop 5.1(marshmallow and above);
     *
     * @param context the context in which the checkpermission method  will occur
     */

    private void checkPermissions(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                alertBuilder.setCancelable(true);
                alertBuilder.setTitle("Permission necessary ");
                alertBuilder.setMessage("External storage permission is necessary");
                alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_EXTERNAL_STORAGE);
                    }
                });
                AlertDialog alertDialog = alertBuilder.create();
                alertDialog.show();

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_EXTERNAL_STORAGE);
            }


        }
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                android.support.v7.app.AlertDialog.Builder alertBuilder = new android.support.v7.app.AlertDialog.Builder(context);
                alertBuilder.setCancelable(true);
                alertBuilder.setTitle("Permission neccessary");
                alertBuilder.setMessage("Writing external storage permission is neccessary");
                alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);
                    }
                });
                //   utils.createIntialImageFolder(activity);
                android.support.v7.app.AlertDialog alertDialog = alertBuilder.create();
                alertDialog.show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);

            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                //displays the error that could occur during image picking
                e.printStackTrace();
            }

            @Override
            public void onImagesPicked(@NonNull List<File> imageFiles, EasyImage.ImageSource source, int type) {
                onPhotosReturned(imageFiles);
                Toast.makeText(MainActivity.this, "selected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {
                Toast.makeText(MainActivity.this, "image not saved", Toast.LENGTH_SHORT).show();
            }
        });


    }

    /**
     * add the selected image to the grid
     *
     * @param returnedPhotos
     */

    private void onPhotosReturned(List<File> returnedPhotos) {

        // utils.setlocationinExifHeader(returnedPhotos,"41.3947688","2.0787279");
        listOfImages.addAll(getImageElements(returnedPhotos));
        //tells the adapter that the data is changed the grid is updated based on this information
        adapter.notifyDataSetChanged();

        recyclerView.scrollToPosition(listOfImages.size() - 1);
    }

    /**
     * @param returnedPhotos
     * @return
     */
    private List<Image> getImageElements(List<File> returnedPhotos) {
        List<Image> imageList = new ArrayList<>();

        for (File file : returnedPhotos) {

            try {
                Location location = new Location(41.3949627,2.0086812,20);
                Image image = new Image(file.getAbsolutePath());
                image.setTitle(file.getName());
                image.setDescription("No Description");
                image.setCity("Barcelona");
                image.setCountry("Spain");
                image.setDate(new ExifInterface(file.getAbsolutePath()).getAttribute(ExifInterface.TAG_DATETIME));
                image.setLocationId(location.getId());

                new InsertIntoDatabaseTask(image,location).execute();
                imageList.add(image);
                System.out.println(image);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return imageList;

    }




    public Activity getActivity() {
        return activity;
    }


    /**
     * The method was overriden to inflate the menu
     *
     * @param menu a object is passed
     * @return true
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.mainactivity_menu, menu);
        MenuItem item = menu.findItem(R.id.action_setting);
        int order = item.getOrder();
        menu.removeItem(order);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            Toast.makeText(getApplicationContext(), "search working", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.staggered_grid) {
            Toast.makeText(getApplicationContext(), " staggered grid", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.action_setting) {
            Intent intent = new Intent(MainActivity.this, Settings.class);
            activity.startActivity(intent);

        } else if (id == R.id.linear_grid) {
            Toast.makeText(getApplicationContext(), "linear Grid", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     *Thi
     *
     */

    private class InsertIntoDatabaseTask extends AsyncTask<Void, Void, Void> {

        private Image image;
        private Location location;
        public InsertIntoDatabaseTask (Image image, Location location){
            this.image = image;
            this.location = location;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            database.imageDao().insertLocation(location);
            database.imageDao().insertImage(image);



            return null;

        }
        public Image getImage() {
            return image;
        }

        public void setImage(Image image) {
            this.image = image;
        }

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }
    }







    /**
     * checks for camera hardware if present displays camera button
     * else it hides it from the user.
     *
     */
    public void checkForCameraHardware(){
        PackageManager pm = activity.getPackageManager();
        if(!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            fabcamera.setVisibility(View.GONE);
        }
    }


}