package com.example.slednet.data.models;

public class Activity {

    // Initializers ; stuff to be initialized immediately

    private final int ACTIVITY_ID;
    private final long START_TIMESTAMP;

    // initialized on activity END; wont allow it to be final

    private long END_TIMESTAMP;

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

    public Activity(int ACTIVITY_ID, TYPE type, String title) {
        this.ACTIVITY_ID = ACTIVITY_ID;
        this.type = type;
        this.title = title;
        this.START_TIMESTAMP = System.currentTimeMillis();
    }

    public void endActivity() {
        this.END_TIMESTAMP = System.currentTimeMillis();

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


    public int getACTIVITY_ID() {
        return ACTIVITY_ID;
    }

    public long getSTART_TIMESTAMP() {
        return START_TIMESTAMP;
    }

    public long getEND_TIMESTAMP() {
        return END_TIMESTAMP;
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
