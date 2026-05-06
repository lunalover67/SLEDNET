package com.example.slednet.activities;


// ===========================================================
// ==[ IMPORTS ]==============================================
// ===========================================================


import androidx.appcompat.app.AppCompatActivity;
import com.example.slednet.data.models.GpsPoint;
import androidx.core.content.ContextCompat;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import androidx.core.app.ActivityCompat;
import androidx.core.view.WindowCompat;
import android.location.GnssStatus;
import android.location.Location;
import android.widget.TextView;
import android.graphics.Color;
import com.example.slednet.R;
import java.util.ArrayList;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.util.Log;
import android.Manifest;
import java.util.List;


// ===========================================================
// ==[ TRACKER ACTIVITY ]=====================================
// ===========================================================


public class TrackerActivity extends AppCompatActivity {


    // -------------------------------------------------------
    // --[ FIELDS ]-------------------------------------------
    // -------------------------------------------------------


    // GPS Setup.

    private final Handler SETUP_HANDLER = new Handler(); // Retry-loop, animation.
    private int dotCount = 0; // Animation (".", "..", "...").


    // GPS Management.

    private int FREQUENCY = 2000; // Can be changed (in ms).
    private int MIN_UPDATE_DISTANCE = 5; // Can be changed (in m).
    private boolean hasGpsFix = false; // Flag for if .
    private LocationManager locationManager; // System (hardware) access object.
    private LocationListener locationListener; // Callback object (updates are posted here).
    private List<GpsPoint> gpsPoints = new ArrayList<>(); // All the points plotted during the activity.

    // Views.

    private TextView gps_status;
    private TextView gps_subtext;
    private TextView demo_output;

    // GNSS (Global Navigation Satellite System)

    private final GnssStatus.Callback GNSS_CALLBACK = new GnssStatus.Callback() {

        @Override
        public void onSatelliteStatusChanged(GnssStatus status) {

            // Get number of satellites detected.
            int num_detected = status.getSatelliteCount();

            // Get number of satellites being used for position fixing.
//            int num_in_use = 0;
//            for (int i = 0; i < num_detected; i++) {
//
//                // Count satellites being used.
//                if (status.usedInFix(i)) {
//                    num_in_use++;
//                }
//
//            }

            // If GPS is not yet online, show progress.
            if (!hasGpsFix) {
                gps_subtext.setVisibility(View.VISIBLE);
//                gps_subtext.setText("USING [" + num_in_use + "] SATELLITES [" + num_detected + " DETECTED]");
                gps_subtext.setText("RECEIVING SIGNAL FROM [" + num_detected + "] SATELLITES");

            }
        }
    };


    // -------------------------------------------------------
    // --[ INITIALIZATION ]-----------------------------------
    // -------------------------------------------------------


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // [SYSTEM] Render the activity (connect to layout file).
        setContentView(R.layout.tracker_activity);

        // [INIT] Clear system UI.
        clearSystemUI();

        // [INIT] Link views.
        linkViews();

        // [INIT] Reach out to satellites to get GPS.
        startGpsSetup();

    }


    // -------------------------------------------------------
    // --[ UI METHODS ]---------------------------------------
    // -------------------------------------------------------


    private void clearSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    private void linkViews() {
        gps_status = findViewById(R.id.gps_status);
        demo_output = findViewById(R.id.demo_output);
        gps_subtext = findViewById(R.id.gps_subtext);
    }


    // -------------------------------------------------------
    // --[ GPS SETUP METHODS ]--------------------------------
    // -------------------------------------------------------


    // Entry point to GPS system.
    private void startGpsSetup() {

        // [INIT] Check / Request FINE-ACCESS permissions.
        ensureLocationPermission();

        // [COMMENT] Looks wrong cuz there's more to GPS.
        //           But for control flow this is easier.

    }

    // Checks location permissions or requests it.
    private void ensureLocationPermission() {

        // If (FINE_ACCESS_LOCATION) granted, start the GPS session.
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            startGpsSession();
        }

        // Otherwise, request the permission.
        else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    @Override // What to do once user interacts with the permission pop-up.
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // If (SOMETHING) WAS DONE and that (SOMETHING) was the requested permission (FINE_ACCESS_LOCATION) granted.
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startGpsSession();
        }

        // Otherwise (if nothing done, or permission not granted), make that clear.
        else {

            // Remove satellite monitoring. TODO manage where this appears/disappears.
            gps_subtext.setVisibility(View.GONE);

            // Show status.
            gps_status.setText("[FINE ACCESS DENIED -> GPS INACTIVE]");
            gps_status.setBackgroundColor(android.graphics.Color.parseColor("#e01f37"));
            gps_status.setTextColor(android.graphics.Color.parseColor("#FFFFFF"));
        }
    }

    // When aquiring GPS, this runs the (".", "..", "...") animation.
    private void startAcquiringAnimation() {
        SETUP_HANDLER.post(new Runnable() {
            @Override
            public void run() {

                // Once GPS online, no need for this.
                if (hasGpsFix) return;

                // TODO place this in correct place (as to not re-run it).
                gps_status.setBackgroundColor(android.graphics.Color.parseColor("#F5F5F5"));
                gps_status.setTextColor(android.graphics.Color.parseColor("#222222"));

                // Show correct amount of dots.
                dotCount = (dotCount % 3) + 1;
                String dots = ".".repeat(dotCount);
                gps_status.setText("[GPS ACQUIRING" + dots + "]");

                // Re-run this process after 600ms.
                SETUP_HANDLER.postDelayed(this, 600);

            }
        });
    }

    // If GPS provider is disabled, poll until it becomes enabled, then restart GPS session.
    private void startRetryLoop()   {
        SETUP_HANDLER.post(new Runnable() {

            @Override
            public void run() {

                // If the GPS becomes active, start the session.
                if (isGpsProvidedEnabled()) {
                    startGpsSession();
                }

                // Otherwise poll the GPS again in 1 second.
                else {
                    SETUP_HANDLER.postDelayed(this, 1000);
                }
            }
        });
    }


    // -------------------------------------------------------
    // --[ GPS SESSION METHODS ]------------------------------
    // -------------------------------------------------------


    // Once all GPS stuff is finally good to go, start the session.
    private void startGpsSession() {

        // Init GPS object references.
        initializeGpsSystem();

        // Confirm that the GPS provider is up (location isn't disabled on a system level).
        if (!isGpsProvidedEnabled()) {
            handleGpsDisabled();
            return;
        }

        // Aquisition starts here.
        prepareForGpsAcquisition();

        // Get location updates.
        beginLocationUpdates();

    }

    // Prepares system GPS interface and enables GNSS monitoring.
    private void initializeGpsSystem() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationManager.registerGnssStatusCallback(GNSS_CALLBACK, SETUP_HANDLER);
    }

    // What to do if the GPS provider goes down.
    private void handleGpsDisabled() {

        // Update UI.
        gps_subtext.setVisibility(View.GONE);
        gps_status.setText("[LOCATION DISABLED]");
        gps_status.setBackgroundColor(Color.parseColor("#e01f37"));
        gps_status.setTextColor(Color.WHITE);

        // Start polling for the provider.
        startRetryLoop();

    }

    // Updates flags and UI for GPS acquisition.
    private void prepareForGpsAcquisition() {
        hasGpsFix = false;
        startAcquiringAnimation();
        gps_subtext.setVisibility(View.VISIBLE);
    }

    // Setup (locationListener) and start listening!
    private void beginLocationUpdates() {

        // Initialize location listener.
        locationListener = createLocationListener();

        // If (ACCESS_FINE_LOCATION) is granted, start requesting data from location provider every (FREQUENCY)ms with a min change radius of (MIN_UPDATE_DISTANCE)m.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, FREQUENCY, MIN_UPDATE_DISTANCE, locationListener);

        }
    }

    // (LocationListener) Factory for organization.
    private LocationListener createLocationListener() {
        return new LocationListener() {

            // What to do when there has been a change in location from the provider.

            @Override
            public void onLocationChanged(Location location) {
                handleLocationUpdate(location);
            }

            // If the location provider is disabled.

            @Override
            public void onProviderDisabled(String provider) {
                handleGpsDisabled();
            }

            // What to do when the location provider has been enabled.

            @Override
            public void onProviderEnabled(String provider) {
                startGpsSession();
            }
        };
    }

    // Handles incoming GPS location updates.
    private void handleLocationUpdate(Location location) {

        // Ensure that the fix is still on.
        // Note: Runs only once on first valid location.
        if (!hasGpsFix) {

            // Update flag.
            hasGpsFix = true;

            // Update UI
            gps_status.setText("[GPS ACTIVE]");
            gps_subtext.setVisibility(View.GONE);
            gps_status.setTextColor(Color.parseColor("#FFFFFF"));
            gps_status.setBackgroundColor(Color.parseColor("#3ac572"));

        }

        // TODO [TRACKING DONE HERE]

    }


    // -------------------------------------------------------
    // --[ TRACKING METHODS ]----------------------------------
    // -------------------------------------------------------

    // In progress...

    // -------------------------------------------------------
    // --[ UTILITY METHODS ]----------------------------------
    // -------------------------------------------------------


    // Checks if on a system level the GPS works (is enabled).
    public boolean isGpsProvidedEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    // On "<" button pressed, return to main activity.
    public void returnToMain(View view) {
        finish();
    }

    // Runs when finish() is called.
    // Because (locationManager) and (locationListener) run independently, we need to manually destroy them.

    @Override
    protected void onDestroy() {

        // Default Android clearing, etc.
        super.onDestroy();

        // Stop location updates.
        if (locationManager != null && locationListener != null) {
            locationManager.removeUpdates(locationListener);
        }

        // Stop GNSS callbacks.
        if (locationManager != null) {
            locationManager.unregisterGnssStatusCallback(GNSS_CALLBACK);
        }

        // Stop handlers / loops.
        SETUP_HANDLER.removeCallbacksAndMessages(null);

    }
}