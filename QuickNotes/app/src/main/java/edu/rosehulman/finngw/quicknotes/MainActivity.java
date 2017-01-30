package edu.rosehulman.finngw.quicknotes;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import java.util.HashMap;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private View mContentView;

    private HashMap<String, Folder> mAllLists;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Color.WHITE);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
