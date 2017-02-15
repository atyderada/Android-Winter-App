package edu.rosehulman.finngw.quicknotes.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import edu.rosehulman.finngw.quicknotes.R;

public class StopAlarmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_alarm);
    }

    public void closeApplication(View view) {
        finish();
    }
}
