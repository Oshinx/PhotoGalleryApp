package com.michael.Com6510.photogallary.utils;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.os.Environment;
import android.widget.Toast;

import com.michael.Com6510.photogallary.activity.MainActivity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static android.media.ExifInterface.TAG_MAKE;

/**
 * Created by Michael Oshinaike on 04/01/2018.
 */

public class Utils extends Application {
    private ExifInterface exifInterface;
    private String getlatitude;
    private String getlongtitude        ;
    private String[] getLatitudeLongtitude;
    private String[] exifInformation;
    private Context context;
    /**
     * This method reads and write the latitude and longtitude of an image Exif Header file.
     *
     * @param file
     * @param latitude
     * @param longtitude
     * @return an array consiting of the latitude and longtitude
     */

    @SuppressLint("NewApi")
    public String[] setlocationinExifHeader(List<File> file, String latitude, String longtitude) {
        getLatitudeLongtitude = new String[2];
        File real = file.get(0);


        try {
            FileWriter writer = new FileWriter(real);

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            InputStream stream = new FileInputStream(real);
            exifInterface = new ExifInterface(stream);
            exifInterface.setAttribute(ExifInterface.TAG_MAKE, String.valueOf(1));
            exifInterface.setAttribute(ExifInterface.TAG_GPS_LATITUDE, latitude);
            exifInterface.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, longtitude);
            getlatitude = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
            getlongtitude = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
            getLatitudeLongtitude[0] = getlatitude;
            getLatitudeLongtitude[1] = getlongtitude;
            String make = exifInterface.getAttribute(TAG_MAKE);
            System.out.println("make " + make);
            System.out.println("lat " + getLatitudeLongtitude[0] + "  long " + getLatitudeLongtitude[1]);


        } catch (IOException e) {
            e.printStackTrace();
        }
        return getLatitudeLongtitude;
    }


    /**
     *
     * @param context
     * @return
     */


    public static String createIntialImageFolder(Context context) {
        File dir = new File(Environment.getExternalStorageDirectory() + "/" + "Pictures" + "/" + "Easy");
        boolean created = dir.mkdirs();
        if (created) {
            System.out.println("worked");
            Toast.makeText(context, "created", Toast.LENGTH_LONG).show();
        } else {
            System.out.println("not working");
        }
        return dir.getAbsolutePath();
    }

    private static String[] getExifDetails(String filePath) throws IOException {
        String[] exifDetails = new String[6];

        ExifInterface exifInterface = new ExifInterface(filePath);
        //exifInterface.getLatLong(ExifInterface.TAG_GPS_LATITUDE_REF2)


        return null;
    }

    /**
     * The
     * @param path
     * @param reqWidth
     * @param reqHeight
     * @return
     */

    public static Bitmap decodeSampledBitmapFromResources(String path, int reqWidth, int reqHeight) {


        //create an instance of the factory.options  class
        //inJustDecode allows us to read the file and get metadata without
        //loading the file into memory .
        final BitmapFactory.Options  options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path,options);

        //Calculate the size the image is going to be reduced to
        options.inSampleSize = calculateInSampleSize(options, reqWidth,reqHeight);

        //Decode bitmap with inSampledSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path,options);
    }

    /**
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight){

        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampledSize = 1;


        if(height > reqHeight || width > reqWidth){
            final int halfHeight = height/2;
            final int halfWidth = width /2 ;


            //Calculates the largest inSampleSize value that is a power of 2
            //and stores the one greater than the others.

            while ((halfHeight /inSampledSize) >= reqHeight
                    && (halfWidth / inSampledSize) >=reqWidth)
            {
                inSampledSize *= 2;

            }

        }
        return inSampledSize;
    }










}
