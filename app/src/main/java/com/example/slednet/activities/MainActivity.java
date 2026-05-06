package com.example.slednet.activities;


// ===========================================================
// ==[ IMPORTS ]==============================================
// ===========================================================


import com.example.slednet.data.database.AppDatabase;
import com.example.slednet.data.models.Activity;
import androidx.appcompat.app.AppCompatActivity;
import com.example.slednet.utils.EnumConverters;
import androidx.core.view.WindowCompat;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.EditText;
import android.content.Intent;
import com.example.slednet.R;
import androidx.room.Room;
import android.view.View;
import android.os.Bundle;
import java.util.List;


// ===========================================================
// ==[ MAIN ACTIVITY ]========================================
// ===========================================================


public class MainActivity extends AppCompatActivity {


    // -------------------------------------------------------
    // --[ FIELDS ]-------------------------------------------
    // -------------------------------------------------------


    // Views.

    private RadioGroup activity_type_group;
    private EditText activity_name_input;
    private LinearLayout past_activities_container;

    // Database-related.

    private AppDatabase database;
    private List<Activity> activities;


    // -------------------------------------------------------
    // --[ INITIALIZATION ]-----------------------------------
    // -------------------------------------------------------


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // [SYSTEM] Render the activity (connect to layout file).
        setContentView(R.layout.activity_main);

        // [INIT] Clear system UI.
        clearSystemUI();

        // [INIT] Link views.
        linkViews();

        // [INIT] Link DB.
        linkDatabase();

        // [NOTE] Permission checking occurs in (Tracker.java).

    }

    // Links DB and creates past activity ui.
    private void linkDatabase() {

        // [CONNECT] Connect DB using Room (where file is automatically dealt with by Room).
        database = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "activities.db").build();

        // [READING] Use seperate thread to read from DB and inflate on UI thread.
        new Thread(() -> {

            // [LOAD] Read DB (get all previous activities).
            activities = database.activityDao().getAll();

            // [RENDER/DRAW] Create all the UI sections for the activities.
            runOnUiThread(() -> {
                inflatePastActivies();
            });

        }).start();
    }


    // -------------------------------------------------------
    // --[ UI METHODS ]---------------------------------------
    // -------------------------------------------------------


    // Re-used this method from older projects.
    private void clearSystemUI() {

        // [CLARITY] Gets rid of the top status bar.
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        // [CLARITY] Gets rid of the bottom navigation bar.
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                          View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    // Initialize object references.
    private void linkViews() {
        activity_type_group = findViewById(R.id.activityTypeGroup);
        activity_name_input = findViewById(R.id.activityNameInput);
        past_activities_container = findViewById(R.id.past_activities_container);
    }

    // Render / Draw all the previous activity
    private void inflatePastActivies() {

        // Iterate through all the past activities in backwards order.
        for (int i = activities.size() - 1; i >=0; i--) {

            // Get activity object.
            Activity activity = activities.get(i);

            // Create card with preset layout defined in (item_activity.xml).
            View card = getLayoutInflater().inflate(R.layout.item_activity, past_activities_container, false);

            // Create object references for each segment.
            TextView title = card.findViewById(R.id.card_title);
            TextView distance = card.findViewById(R.id.card_distance);
            TextView time = card.findViewById(R.id.card_time);
            TextView pace = card.findViewById(R.id.card_pace);

            // Update values in the card with activity data.
            title.setText("<" + activity.getType() + "/> " + activity.getTitle());
            distance.setText(String.valueOf(activity.getDistance()));
            time.setText(String.valueOf(activity.getDuration()));
            pace.setText("--:--"); // TODO [Pace calculation later].

            // Add the card to the list (container).
            past_activities_container.addView(card);
        }

    }


    // -------------------------------------------------------
    // --[ UTILITY METHODS ]----------------------------------
    // -------------------------------------------------------


    // Behaviour for "GO" button.
    public void createActivity(View view) {

        // Get the selected ID (by default it is "RUN").
        int selected_id = activity_type_group.getCheckedRadioButtonId();

        // If something is checked (-1 means nothing is checked --> so this is redundant in a sense).
        // Though by default "RUN" is selected meaning SOMETHING is selected.
        if (selected_id != -1) {


            // Get the selected button context text ("RUN", "WALK", etc).
            RadioButton selectedButton = findViewById(selected_id);
            String activityType = selectedButton.getText().toString();

            // Get the "ACTIVITY NAME" input.
            String name = activity_name_input.getText().toString();

            // Checks if the string is blank ("", "   ", anything like that).
            if (name.isBlank()) {
                name = "Unnamed Activity";
            }

            // Create the activity with all the entered details (currently generates BS data).
            Activity new_activity = Activity.generateFake(name, EnumConverters.stringToType(activityType));

            // Use background thread to add this activity to the DB.
            new Thread(() -> {
                database.activityDao().addActivity(new_activity);
            }).start();

            // Open (TrackerActivity.java) with this new activity.
            openTracker(new_activity.getActivity_id());

        }
    }

    // Switches activity to (TrackerActivity.java).
    private void openTracker(int activity_id) {

        // Create intent.
        Intent intent = new Intent(this, TrackerActivity.class);

        // Pass the new activity id into the next activity.
        intent.putExtra("activity_id", activity_id);

        // Start the activity.
        startActivity(intent);

    }

}