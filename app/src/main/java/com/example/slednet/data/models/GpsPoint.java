package com.example.slednet.data.models;


// ===========================================================
// == [GPS POINT] ============================================
// ===========================================================


public class GpsPoint {


    // -----------------------------------------------------------
    // -- [INSTANCE VARIABLES] -----------------------------------
    // -----------------------------------------------------------


    // ID to relate the activity (in db).
    private final int ACTIVITY_ID;

    // Geographical coordinates.
    private final double longitude, latitude, altitude;

    // When was this point recorded?
    private final long timestamp;

    // Accuracy of the sensor.
    private final int accuracy;


    // -----------------------------------------------------------
    // -- [CONSTRUCTOR] ------------------------------------------
    // -----------------------------------------------------------


    // The one and only constructor.
    public GpsPoint(double longitude, double latitude, double altitude, long timestamp, int accuracy, int ACTIVITY_ID) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.altitude = altitude;
        this.timestamp = timestamp;
        this.accuracy = accuracy;
        this.ACTIVITY_ID = ACTIVITY_ID;
    }


    // -----------------------------------------------------------
    // -- [GETTERS] ----------------------------------------------
    // -----------------------------------------------------------


    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public int getACTIVITY_ID() {
        return ACTIVITY_ID;
    }

}
