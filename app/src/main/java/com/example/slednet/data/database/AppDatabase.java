package com.example.slednet.data.database;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.slednet.data.models.Activity;
import com.example.slednet.utils.EnumConverters;

@Database(entities = {Activity.class}, version = 1) // table schema; blueprint entitiy
@TypeConverters(EnumConverters.class)

public abstract class AppDatabase extends RoomDatabase {

    public abstract ActivityDao activityDao();


}
