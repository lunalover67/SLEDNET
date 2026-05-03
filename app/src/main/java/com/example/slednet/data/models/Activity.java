package com.example.slednet.data.models;

import androidx.room.*;

@Entity(tableName = "activities")
public class Activity {

    // Initializers ; stuff to be initialized immediately

    @PrimaryKey(autoGenerate = true) // removes id handling from us *whew*
    private int activity_id;

    private long start_timestamp;

    // initialized on activity END; wont allow it to be final

    private long end_timestamp;

    // shortly after during processes
    private long duration; // in s
    private long distance; // in m

    // can be mutated

    private String title;

     // TYPE OF POSSIBLE ACTIVITIES

    private TYPE type;

    public enum TYPE {
        RUN,
        WALK
    }


    // -----------------------------------------------------------
    // -- [CONSTRUCTOR + END ACTIVITY] ---------------------------
    // -----------------------------------------------------------

    public Activity() {}

    @Ignore // Room wants an empty constructor
    public Activity(int ACTIVITY_ID, TYPE type, String title) {
        this.activity_id = ACTIVITY_ID;
        this.type = type;
        this.title = title;
        this.start_timestamp = System.currentTimeMillis();
    }

    public void endActivity() {
        this.end_timestamp = System.currentTimeMillis();

        // calculate duration / time in s and whatever db interactions here
    }


    // -----------------------------------------------------------
    // -- [HELPER FUNCTIONS] -------------------------------------
    // -----------------------------------------------------------

    // calculate time
    // calculate distance, pace blah blah blah


    // -----------------------------------------------------------
    // -- GETTERS ------------------------------------------------
    // -----------------------------------------------------------


    public int getActivity_id() {
        return activity_id;
    }

    public long getStart_timestamp() {
        return start_timestamp;
    }

    public long getEnd_timestamp() {
        return end_timestamp;
    }

    public long getDuration() {
        return duration;
    }

    public long getDistance() {
        return distance;
    }

    public String getTitle() {
        return title;
    }

    public TYPE getType() {
        return type;
    }

}
