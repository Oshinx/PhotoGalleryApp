package com.michael.Com6510.photogallary.database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Michael Oshinaike on 04/01/2018.
 */

@Entity
public class Location {
     @Ignore
     private static  int idCounter = 144;

     @PrimaryKey
     public int id = 0;



    public double latitude;



    public double longitude;
     public double accuracy;

     public Location(double latitude, double longitude, int accuracy){
         this.latitude = latitude;
         this.longitude = longitude;
         this.accuracy = accuracy;
         this.id = idCounter++;
     }

     public int getId(){return this.id;}

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }



}
