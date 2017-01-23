package edu.rosehulman.finngw.quicknotes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import java.util.HashMap;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MainActivity extends AppCompatActivity {

    private View mContentView;

    private HashMap<String, Folder> mAllLists;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Crreating map for all folders
        mAllLists = new HashMap<>();
        mAllLists.put("Notes", new Folder("Notes"));
        mAllLists.put("Reminders", new Folder("Reminders"));
        mAllLists.put("Alarms", new Folder("Alarms"));

        for(int i = 1; i < 10; i++) {
            Card nCard = new Note("Title Sample", "Note " + i);
            mAllLists.get("Notes").addCard(nCard);
        }

        for(Card c : mAllLists.get("Notes").list) {
            Log.d("TTT", c.description);
        }

        for(int i = 1; i < 10; i++) {
            Card rCard = new Reminder("Title Sample 2", ("Reminder " + i), 1, 24, 17);
            mAllLists.get("Reminders").addCard(rCard);
        }

        for(Card c : mAllLists.get("Reminders").list) {
            Log.d("TTT", c.description);
        }

        for(int i = 1; i < 10; i++) {
            Card aCard = new Alarm("Title Sample Three", "Alarm " + i, 8, 15);
            mAllLists.get("Alarms").addCard(aCard);
        }

        for(Card c : mAllLists.get("Alarms").list) {
            Log.d("TTT", c.description);
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new CardListAdapter(mAllLists.get("Notes").list, null);
        mRecyclerView.setAdapter(mAdapter);

        mContentView = findViewById(R.id.fullscreen_content);

        ImageButton mAlarmImgeView = (ImageButton) findViewById(R.id.alarm_button);
        mAlarmImgeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter = new CardListAdapter(mAllLists.get("Alarms").list, null);
                mRecyclerView.setAdapter(mAdapter);
                Log.d("TTT", "Clicked");
            }
        });

        ImageButton mReminderImageView = (ImageButton) findViewById(R.id.reminder_button);
        mReminderImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter = new CardListAdapter(mAllLists.get("Reminders").list, null);
                mRecyclerView.setAdapter(mAdapter);
                Log.d("TTT", "Clicked");
            }
        });

        ImageButton mNoteImageView = (ImageButton) findViewById(R.id.note_button);
        mNoteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter = new CardListAdapter(mAllLists.get("Notes").list, null);
                mRecyclerView.setAdapter(mAdapter);
                Log.d("TTT", "Clicked");
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }
}
