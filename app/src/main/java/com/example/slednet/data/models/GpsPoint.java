package com.example.slednet.data.models;


// ===========================================================
// == [GPS POINT] ============================================
// ===========================================================


public class GpsPoint {


    // -----------------------------------------------------------
    // -- [INSTANCE VARIABLES] -----------------------------------
    // -----------------------------------------------------------


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
    public GpsPoint(double longitude, double latitude, double altitude, long timestamp, int accuracy) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.altitude = altitude;
        this.timestamp = timestamp;
        this.accuracy = accuracy;
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

}
