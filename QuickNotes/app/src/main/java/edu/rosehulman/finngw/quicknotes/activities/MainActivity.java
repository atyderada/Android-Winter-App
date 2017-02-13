package edu.rosehulman.finngw.quicknotes.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import edu.rosehulman.finngw.quicknotes.R;
import edu.rosehulman.finngw.quicknotes.fragments.AlarmListFragment;
import edu.rosehulman.finngw.quicknotes.fragments.LoginFragment;
import edu.rosehulman.finngw.quicknotes.fragments.NoteListFragment;
import edu.rosehulman.finngw.quicknotes.fragments.ReminderListFragment;
import edu.rosehulman.finngw.quicknotes.models.Alarm;
import edu.rosehulman.finngw.quicknotes.models.Note;
import edu.rosehulman.finngw.quicknotes.models.Reminder;
import edu.rosehulman.finngw.quicknotes.utilities.Constants;
import edu.rosehulman.finngw.quicknotes.utilities.SharedPreferencesUtils;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        LoginFragment.OnLoginListener,
        NoteListFragment.OnNoteSelectedListener,
        AlarmListFragment.OnAlarmSelectedListener,
        ReminderListFragment.OnReminderSelectedListener {

    private Toolbar mToolbar;

    private FirebaseDatabase mFirebase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private OnCompleteListener mOnCompleteListener;

    private boolean onEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        mToolbar.setBackgroundColor(Color.WHITE);

        final TextView mTitleTextView = (TextView)findViewById(R.id.title_input);
        final TextView mDescriptionTextView = (TextView)findViewById(R.id.description_input);

        if (savedInstanceState == null) {
            initializeFirebase();
        }

        /*
        mAuth = FirebaseAuth.getInstance();
        initializeListeners();
        */

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.container, new NoteListFragment(), "notes");
        ft.commit();

        mFirebase = FirebaseDatabase.getInstance();

        onEdit = false;

        ImageButton mAlarmImgeView = (ImageButton) findViewById(R.id.alarm_button);
        mAlarmImgeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.container, new AlarmListFragment(), "alarms");
                ft.commit();
                addAlarm(mTitleTextView.getText().toString(), mDescriptionTextView.getText().toString());
                mTitleTextView.setText("");
                mDescriptionTextView.setText("");
                onEdit = false;
            }
        });

        ImageButton mReminderImageView = (ImageButton) findViewById(R.id.reminder_button);
        mReminderImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.container, new ReminderListFragment(), "reminders");
                ft.commit();
                addAlarm(mTitleTextView.getText().toString(), mDescriptionTextView.getText().toString());
                mTitleTextView.setText("");
                mDescriptionTextView.setText("");
                onEdit = false;
            }
        });

        ImageButton mNoteImageView = (ImageButton) findViewById(R.id.note_button);
        mNoteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.container, new NoteListFragment(), "notes");
                ft.commit();
                addNote(mTitleTextView.getText().toString(), mDescriptionTextView.getText().toString());
                mTitleTextView.setText("");
                mDescriptionTextView.setText("");
                onEdit = false;
            }
        });
    }

    private void initializeFirebase() {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        mFirebase = FirebaseDatabase.getInstance();
        mFirebase.getReference().keepSynced(true);
    }

    private void initializeListeners() {
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                Log.d(Constants.TAG, "In activity, authlistener");
                FirebaseUser user = firebaseAuth.getCurrentUser();

                Log.d(Constants.TAG, "Current user: " + user);
                if (user == null) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.container, new LoginFragment(), "login");
                    ft.commit();
                    Log.d("TTTTTTTTTT", "Changed to login");
                } else {
                    SharedPreferencesUtils.setCurrentUser(MainActivity.this, user.getUid());
                    Log.d(Constants.TAG, "User is authenticated");

                    // Currently, if rosefire, email is null. Will be fixed in next version.
                    if (firebaseAuth.getCurrentUser().getEmail() != null) {
                        // Email/password.
                        onLoginComplete(user.getUid());
                    }
                }
            }
        };

        mOnCompleteListener = new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(Constants.TAG, "In activity, oncompletelistener");
                if (!task.isSuccessful()) {
                    showLoginError("Authentication failed.");
                }
            }
        };
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
        Fragment switchTo = null;
        String tag = "";

        if (id == R.id.nav_notes) {
            switchTo = new NoteListFragment();
            tag = "notes";
        } else if (id == R.id.nav_alarms) {
            switchTo = new AlarmListFragment();
            tag = "alarms";
        } else if (id == R.id.nav_reminders) {
            switchTo = new AlarmListFragment();
            tag = "alarms";
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        if (switchTo != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, switchTo, tag);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

   private void showLoginError(String message) {
       LoginFragment loginFragment = (LoginFragment) getSupportFragmentManager().findFragmentByTag("login");
       loginFragment.onLoginError(message);
   }

    @Override
    public void onStart() {
        super.onStart();
        //mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthStateListener != null) {
            //mAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    public void onLoginComplete(String uid) {
        Log.d(Constants.TAG, "User is authenticated");

        SharedPreferencesUtils.setCurrentUser(this, uid);

        Fragment switchTo = new NoteListFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, switchTo);
        ft.commit();
    }

    @Override
    public void onLogin(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, mOnCompleteListener);
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
    public void onAlarmSelected(Alarm selectedAlarm) {

    }

    @Override
    public void onNoteSelected(Note selectedNote) {

    }

    @Override
    public void onReminderSelected(Reminder selectedReminder) {

    }

    /*
    @SuppressLint("InflateParams")
    private void showUsernameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Rose username");
        View view = getLayoutInflater().inflate(R.layout.dialog_get_rose_username, null);
        builder.setView(view);
        final EditText roseUsernameEditText = (EditText) view
                .findViewById(R.id.dialog_get_rose_username);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String roseUsername = roseUsernameEditText.getText().toString();
                        String uid = SharedPreferencesUtils.getCurrentUser(GradeRecorderActivity.this);
                        mOwnerRef = FirebaseDatabase.getInstance().getReference().child(Constants.OWNERS_PATH).child(uid);
                        mOwnerRef.child(Owner.USERNAME).setValue(roseUsername);
                        onLoginComplete(uid);
                    }
                }
        );
        builder.create().show();
    }
    */

    public void addAlarm(String title, String description) {

        final String alarmTitle = title;
        final String alarmDescription = description;

        // Launch Dialog
        Calendar mCurrentTime = Calendar.getInstance();
        int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mCurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Alarm alarm = new Alarm(alarmTitle, alarmDescription, mAuth.getCurrentUser().getUid(), hourOfDay, minute);
                DatabaseReference alarmRef = mFirebase.getReference(Constants.ALARMS_PATH).push();
                alarmRef.setValue(alarm);
            }
        }, hour, minute, true);
        mTimePicker.setTitle("Select Alarm Time");
        mTimePicker.show();
    }

    public void addNote(String title, String description) {
        final String noteTitle = title;
        final String noteDescription = description;

        Note note = new Note(noteTitle, noteDescription, "deradaam");
        DatabaseReference noteRef = mFirebase.getReference(Constants.NOTES_PATH).push();
        noteRef.setValue(note);
        Log.d("ADD_NOTE", "Adding Note");
    }

    public void addReminder(String title, String description) {
        final String reminderTitle = title;
        final String reminderDescription = description;

        // launch dialog
        Calendar mCurrentDate = Calendar.getInstance();
        int day = mCurrentDate.get(Calendar.DAY_OF_MONTH);
        int month = mCurrentDate.get(Calendar.MONTH);
        int year = mCurrentDate.get(Calendar.YEAR);
        DatePickerDialog mDatePicker;
        mDatePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Reminder reminder = new Reminder(reminderTitle, reminderDescription, mAuth.getCurrentUser().getUid(), year, month + 1, dayOfMonth);
                DatabaseReference reminderRef = mFirebase.getReference(Constants.REMINDERS_PATH).push();
                reminderRef.setValue(reminder);
            }
        }, year, month, day);
        mDatePicker.setTitle("Select a Date");
        mDatePicker.show();
    }
}
