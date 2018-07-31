package com.michael.Com6510.photogallary.database;
//inspiration where gotten from the following sources   https://www.youtube.com/watch?v=USbTcGx1mD0&list=PLk7v1Z2rk4hjHrGKo9GqOtLs1e2bglHHA, https://www.youtube.com/watch?v=zHGgSd1wvxY
//https://www.youtube.com/watch?v=dVwR5Gpw1_E, https://www.youtube.com/results?search_query=telusko+android+beginners,https://www.youtube.com/watch?v=Zxf1mnP5zcw
//https://www.youtube.com/results?search_query=room+database,https://www.youtube.com/watch?v=_wka82BdRow,https://www.youtube.com/watch?v=MfHsPGQ6bgE
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.provider.LiveFolders;

import com.michael.Com6510.photogallary.database.entity.Image;
import com.michael.Com6510.photogallary.database.entity.Location;

import java.util.List;

/**
 * Created by Michael Oshinaike on 04/01/2018.
 */

@Dao
public interface ImageDao {
    @Query("SELECT * FROM Image"
            + " INNER JOIN  LOCATION ON location.id = Image.location_id "
            + " WHERE location.latitude <= (:latitude + :radius)"
            + " AND location.latitude >= (:latitude - :radius)"
            + " AND location.longitude <= (:longitude + :radius)"
            + " AND location.longitude >= (:longitude - :radius)"
    )
    public List<Image> findImageByArea(double latitude, double longitude, int radius);

    @Query("SELECT * FROM Image WHERE title = :title")
    public List<Image> findImageByTitle(String title);

    @Query("SELECT * FROM Image")
    public List<Image> getAllImages();

    @Query("SELECT * FROM Image WHERE image = :image")
    public List<Image> getImage(String image);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertImage(Image image);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insertLocation(Location location);

    @Update
    void update(Image image);

    @Delete
    void deleteImage(Image image);



}
