package com.example.slednet.data.database;

import com.example.slednet.data.models.Activity;
import androidx.room.*;

import java.util.List;

@Dao
public interface ActivityDao {

    @Query("SELECT * FROM activities")
    List<Activity> getAll();

    @Insert
    void addActivity(Activity activity); // will contain id and everything; how will know unique id in runtime?

    // VVVVV implementation later VVVV

    @Delete
    void removeActivity(Activity activity); // which activity to delete

//    would replace everything but the ID for that activity
//    (main use case is changing name of an activity; implementation later)
//    @Query("")
//    void modifyActivity(Activity newActivity);

}
