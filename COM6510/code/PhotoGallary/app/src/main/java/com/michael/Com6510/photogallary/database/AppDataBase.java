package com.michael.Com6510.photogallary.database;
//inspiration where gotten from the following sources   https://www.youtube.com/watch?v=USbTcGx1mD0&list=PLk7v1Z2rk4hjHrGKo9GqOtLs1e2bglHHA, https://www.youtube.com/watch?v=zHGgSd1wvxY
//https://www.youtube.com/watch?v=dVwR5Gpw1_E, https://www.youtube.com/results?search_query=telusko+android+beginners,https://www.youtube.com/watch?v=Zxf1mnP5zcw
//https://www.youtube.com/results?search_query=room+database,https://www.youtube.com/watch?v=_wka82BdRow,https://www.youtube.com/watch?v=MfHsPGQ6bgE
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;

import com.michael.Com6510.photogallary.database.entity.Image;
import com.michael.Com6510.photogallary.database.entity.Location;

/**
 * Created by Michael Oshinaike on 04/01/2018.
 */

@Database(entities = {Image.class, Location.class}, version = 2)
public abstract class AppDataBase extends RoomDatabase {

    public static final String DATABASENAME = "images_database";
    private static AppDataBase DATABASE;

    public abstract ImageDao imageDao();


    //this instanciation is used for migration in the event of changing the varrious tables in
    //the database
    public static final Migration MIGRATION_1_2 = new Migration(1,2) {

        @Override
        public void migrate(SupportSQLiteDatabase database) {
        //nothing is implemented because no changes made

        }
    };


    public static AppDataBase getDataBase(Context context){
      if(DATABASE == null){
          DATABASE = Room.databaseBuilder(context.getApplicationContext(),AppDataBase.class,DATABASENAME)
                  .allowMainThreadQueries()
                  .build();
      }
   return DATABASE; }
}
