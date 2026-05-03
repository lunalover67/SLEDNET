package com.example.slednet.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import com.example.slednet.R;
import com.example.slednet.data.models.Activity;

public class MainActivity extends AppCompatActivity {

    // Views

    private RadioGroup activity_group;
    private EditText activity_name_input;
    private TextView test_output;

    // On Create

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        clearSystemUI();

        // Render the activity (connect to layout file)
        setContentView(R.layout.activity_main);

        // connect views

        linkViews();

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
        activity_group = findViewById(R.id.activityTypeGroup);
        activity_name_input = findViewById(R.id.activityNameInput);
        test_output = findViewById(R.id.test);
    }

    private void link_database() {
        // link db to session
        // load previous activities
    }

    // Utility methods


    public void createActivity(View view) {

        int checkedId = activity_group.getCheckedRadioButtonId();

        if (checkedId != -1) {

            // check which radio button was clicked

            RadioButton selectedButton = findViewById(checkedId);

            // get the text of the button

            String activityType = selectedButton.getText().toString(); // will need to get this to reference type in enum

            // get the name of the activity

            String name = activity_name_input.getText().toString();

            // start the activity

            test_output.setText("Starting " + activityType + ": " + name);

            // flag for delete vs save

            //                                        TODO this should be latest db id + 1
            Activity new_activity = new Activity(0, Activity.TYPE.valueOf(activityType), name);

            startTracker(new_activity.getActivity_id());

        }
    }

    private void startTracker(int ACTIVITY_ID) {

        Intent intent = new Intent(this, TrackerActivity.class);

        // tracker will reference db directly
        intent.putExtra("activity_id", ACTIVITY_ID);

        startActivity(intent);

        // moves to the tracker activity and inits gps and whatnot
    }

}