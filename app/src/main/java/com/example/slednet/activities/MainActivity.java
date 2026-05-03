package com.example.slednet.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.room.Room;

import com.example.slednet.R;
import com.example.slednet.data.database.AppDatabase;
import com.example.slednet.data.models.Activity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Views

    private RadioGroup activity_type_group;
    private EditText activity_name_input;
    private LinearLayout past_activities_container;

    // db

    private AppDatabase database;
    private List<Activity> activities;


    // On Create

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Render the activity (connect to layout file)
        setContentView(R.layout.activity_main);

        // clear system ui
        clearSystemUI();

        // connect views
        linkViews();

        // get db instantiated
        linkDatabase();

        // check if permissons enabled; if not (either show popup, or at top small section asking, like linlayout)

    }

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

    private void linkViews() {
        activity_type_group = findViewById(R.id.activityTypeGroup);
        activity_name_input = findViewById(R.id.activityNameInput);
        past_activities_container = findViewById(R.id.past_activities_container);

    }

    private void linkDatabase() {
        database = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "activities.db").build();

        new Thread(() -> {
            activities = database.activityDao().getAll();

            runOnUiThread(() -> {
                for (Activity activity : activities) {
                    Log.d("NEGGADETHEHEHHFAIWHOFAWFOHOIWHDAIWHDOIWAJD IM HERE", activity.getTitle());
                    View card = getLayoutInflater().inflate(R.layout.item_activity, past_activities_container, false);

                    TextView title = card.findViewById(R.id.card_title);
                    TextView distance = card.findViewById(R.id.card_distance);
                    TextView time = card.findViewById(R.id.card_time);
                    TextView pace = card.findViewById(R.id.card_pace);

                    title.setText("<" + activity.getType() + "/> " + activity.getTitle());
                    distance.setText(String.valueOf(activity.getDistance()));
                    time.setText(String.valueOf(activity.getDuration()));
                    pace.setText("--:--"); // pace calculation later

                    past_activities_container.addView(card);
                }
            });
        }).start();
    }

    private void inflatePastActivies() {
        // iterate through each activity and add to the scrollview
        // how to make it so that instead of manually applying each attribute i can just use a drawable (is that what its called?)
    }

    private void checkPermissions() {

    }

    // Utility methods

    public void createActivity(View view) {

        int checkedId = activity_type_group.getCheckedRadioButtonId();

        if (checkedId != -1) {

            // check which radio button was clicked

            RadioButton selectedButton = findViewById(checkedId);

            // get the text of the button

            String activityType = selectedButton.getText().toString(); // will need to get this to reference type in enum

            // get the name of the activity

            String name = activity_name_input.getText().toString();

            // start the activity

//            test_output.setText("Starting " + activityType + ": " + name);

            // flag for delete vs save

//            Activity new_activity = new Activity(Activity.TYPE.valueOf(activityType), name);
            Activity new_activity = Activity.generateFake();

            new Thread(() -> {
                database.activityDao().addActivity(new_activity);
            }).start();

            startTracker(new_activity.getActivity_id());

        }
    }

    private void startTracker(int activity_id) {

        Intent intent = new Intent(this, TrackerActivity.class);

//        // tracker will reference db directly
        intent.putExtra("activity_id", activity_id);

        startActivity(intent);

        // moves to the tracker activity and inits gps and whatnot
    }

}