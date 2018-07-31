package com.michael.Com6510.photogallary.database.entity;
//inspiration where gotten from the following sources   https://www.youtube.com/watch?v=USbTcGx1mD0&list=PLk7v1Z2rk4hjHrGKo9GqOtLs1e2bglHHA, https://www.youtube.com/watch?v=zHGgSd1wvxY
//https://www.youtube.com/watch?v=dVwR5Gpw1_E, https://www.youtube.com/results?search_query=telusko+android+beginners,https://www.youtube.com/watch?v=Zxf1mnP5zcw
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by Michael Oshinaike on 01/01/2018.
 */


 @Entity(foreignKeys = @ForeignKey(entity = Location.class,
                                    parentColumns = "id",
                                    childColumns = "location_id",
                                     onDelete =CASCADE))

public class Image implements Serializable {

    private static int idCounter = 1;
    @PrimaryKey(autoGenerate = true)
    private int id;//new

    private String image;

    private String title;
    private String description;
    private String date;
    private String city;
    private String country;


    @ColumnInfo(name = "location_id")
    private int locationId;


    public Image(String image){
        this.image = image;
        this.id =idCounter++;
    }



    // setters and getters for the innstance variables above
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
