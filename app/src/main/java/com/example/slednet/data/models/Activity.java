package com.example.slednet.data.models;

import androidx.room.*;

import java.util.Random;

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
    public Activity(TYPE type, String title) {
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

    @Ignore
    public static Activity generateFake() {
        String[] names = {"Morning Grind", "Evening Stroll", "Lunch Loop", "Weekend Long", "Recovery Jog"};
        TYPE[] types = {TYPE.RUN, TYPE.WALK};
        Random random = new Random();

        Activity a = new Activity();
        a.type = types[random.nextInt(types.length)];
        a.title = names[random.nextInt(names.length)];
        a.start_timestamp = System.currentTimeMillis() - random.nextInt(864000000); // random within last 10 days
        a.duration = 600 + random.nextInt(3000); // 10 to 60 mins in seconds
        a.distance = 1000 + random.nextInt(15000); // 1 to 16 km in meters
        a.end_timestamp = a.start_timestamp + (a.duration * 1000);
        return a;
    }

    // -----------------------------------------------------------
    // -- GETTERS ------------------------------------------------
    // -----------------------------------------------------------


    public void setActivity_id(int activity_id) {
        this.activity_id = activity_id;
    }

    public void setStart_timestamp(long start_timestamp) {
        this.start_timestamp = start_timestamp;
    }

    public void setEnd_timestamp(long end_timestamp) {
        this.end_timestamp = end_timestamp;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setDistance(long distance) {
        this.distance = distance;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

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
