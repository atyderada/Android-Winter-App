package edu.rosehulman.finngw.quicknotes.activities;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import edu.rosehulman.finngw.quicknotes.R;
import edu.rosehulman.finngw.quicknotes.fragments.AlarmDetailFragment;
import edu.rosehulman.finngw.quicknotes.fragments.AlarmListFragment;
import edu.rosehulman.finngw.quicknotes.fragments.BaseFragment;
import edu.rosehulman.finngw.quicknotes.fragments.LoginFragment;
import edu.rosehulman.finngw.quicknotes.fragments.NoteDetailFragment;
import edu.rosehulman.finngw.quicknotes.fragments.NoteListFragment;
import edu.rosehulman.finngw.quicknotes.fragments.ReminderDetailFragment;
import edu.rosehulman.finngw.quicknotes.fragments.ReminderListFragment;
import edu.rosehulman.finngw.quicknotes.models.Alarm;
import edu.rosehulman.finngw.quicknotes.models.Note;
import edu.rosehulman.finngw.quicknotes.models.Reminder;
import edu.rosehulman.finngw.quicknotes.utilities.AlarmReceiver;
import edu.rosehulman.finngw.quicknotes.utilities.Constants;
import edu.rosehulman.finngw.quicknotes.utilities.SharedPreferencesUtils;
import edu.rosehulman.finngw.quicknotes.utilities.Utils;
import edu.rosehulman.rosefire.Rosefire;
import edu.rosehulman.rosefire.RosefireResult;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        LoginFragment.OnLoginListener,
        NoteListFragment.OnNoteSelectedListener,
        AlarmListFragment.OnAlarmSelectedListener,
        ReminderListFragment.OnReminderSelectedListener, GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_GOOGLE_SIGN_IN = 1;
    private static final int RC_ROSEFIRE_SIGN_IN = 2;
    private FirebaseDatabase mFirebase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private OnCompleteListener<AuthResult> mOnCompleteListener;
    private Toolbar mToolbar;
    private GoogleApiClient mGoogleApiClient;

    private boolean onEdit;
    private String onEditChoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            initializeFirebase();
        }

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        mToolbar.setBackgroundColor(Color.WHITE);

        mAuth = FirebaseAuth.getInstance();
        initializeListeners();
        initializeGoogle();

        onEdit = false;
        onEditChoice = "note";

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);
        navigationView.setNavigationItemSelectedListener(this);

        mFirebase = FirebaseDatabase.getInstance();
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
                    ft.replace(R.id.content_main, new LoginFragment(), "login");
                    ft.commit();
                    mToolbar.setVisibility(View.GONE);
                } else {
                    SharedPreferencesUtils.setCurrentUser(MainActivity.this, user.getUid());
                    Log.d(Constants.TAG, "User is authenticated");
                    onLoginComplete(user.getUid());
                    mToolbar.setVisibility(View.VISIBLE);
                }
            }
        };

        mOnCompleteListener = new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    showLoginError("Authentication failed.");
                }
            }
        };
    }

    public void initializeGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_GOOGLE_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                showLoginError("Google Sign In Failed");
            }
        } else if(requestCode == RC_ROSEFIRE_SIGN_IN) {
            RosefireResult result = Rosefire.getSignInResultFromIntent(data);
            if(result.isSuccessful()) {
                firebaseAuthWithRosefire(result);
            } else {
                showLoginError("Rosefire authentication failed");
            }
        }
    }

    private void firebaseAuthWithRosefire(RosefireResult result) {
        mAuth.signInWithCustomToken(result.getToken()).addOnCompleteListener(mOnCompleteListener);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, mOnCompleteListener);
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
        if(id == R.id.action_logout) {
            Utils.signOut(this);
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
        Fragment swap = null;
        String swapTag = "";

        if (id == R.id.nav_notes) {
            if (onEdit) {
                swap = new BaseFragment();
                swapTag = "base";
            }
            switchTo = new NoteListFragment();
            tag = "notes";
        } else if (id == R.id.nav_alarms) {
            if (onEdit) {
                swap = new BaseFragment();
                swapTag = "base";
            }
            switchTo = new AlarmListFragment();
            tag = "alarms";
        } else if (id == R.id.nav_reminders) {
            if (onEdit) {
                swap = new BaseFragment();
                swapTag = "base";
            }
            switchTo = new ReminderListFragment();
            tag = "alarms";
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        if (swap != null) {
            onEdit = false;
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_main, swap, swapTag);
            ft.commit();
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
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthStateListener != null) {
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    public void onLoginComplete(String uid) {
        SharedPreferencesUtils.setCurrentUser(this, uid);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_main, new BaseFragment());
        ft.commit();

        FragmentTransaction ftt = getSupportFragmentManager().beginTransaction();
        ftt.replace(R.id.container, new NoteListFragment(), "notes");
        ftt.commit();
    }

    @Override
    public void onLogin(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(mOnCompleteListener);
    }

    @Override
    public void onGoogleLogin() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN);
    }

    @Override
    public void onRosefireLogin() {
        Intent signInIntent = Rosefire.getSignInIntent(this, Constants.ROSEFIRE_REGISTRY_TOKEN);
        startActivityForResult(signInIntent, RC_ROSEFIRE_SIGN_IN);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            fm.popBackStackImmediate();
            ft.commit();

            FragmentTransaction ftt = getSupportFragmentManager().beginTransaction();
            Fragment fragment = null;
            if (onEditChoice.equals("note")) {
                fragment = new NoteListFragment();
            } else if (onEditChoice.equals("alarm")) {
                fragment = new AlarmListFragment();
            } else if (onEditChoice.equals("reminder")) {
                fragment = new ReminderListFragment();
            }
            ftt.replace(R.id.container, fragment);
            ftt.commit();
        }
    }

    @Override
    public void onAlarmSelected(Alarm selectedAlarm) {
        onEdit = true;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = AlarmDetailFragment.newInstance(selectedAlarm);
        Slide slideTransition = new Slide(Gravity.RIGHT);
        slideTransition.setDuration(200);
        fragment.setEnterTransition(slideTransition);
        ft.replace(R.id.content_main, fragment);
        onEditChoice = "alarm";
        ft.addToBackStack("alarm");
        ft.commit();
    }

    @Override
    public void onNoteSelected(Note selectedNote) {
        onEdit = true;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = NoteDetailFragment.newInstance(selectedNote);
        Slide slideTransition = new Slide(Gravity.RIGHT);
        slideTransition.setDuration(200);
        fragment.setEnterTransition(slideTransition);
        ft.replace(R.id.content_main, fragment);
        onEditChoice = "note";
        ft.addToBackStack("note");
        ft.commit();
    }

    @Override
    public void onReminderSelected(Reminder selectedReminder) {
        onEdit = true;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = ReminderDetailFragment.newInstance(selectedReminder);
        Slide slideTransition = new Slide(Gravity.RIGHT);
        slideTransition.setDuration(200);
        fragment.setEnterTransition(slideTransition);
        ft.replace(R.id.content_main, fragment);
        onEditChoice = "reminder";
        ft.addToBackStack("reminder");
        ft.commit();
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

        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        PendingIntent alarmIntent;

        Intent intent = new Intent(this, AlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        // Launch Dialog
        final Calendar mCurrentTime = Calendar.getInstance();
        int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mCurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Alarm alarm = new Alarm(alarmTitle, alarmDescription, mAuth.getCurrentUser().getUid(), hourOfDay, minute);
                DatabaseReference alarmRef = mFirebase.getReference(Constants.ALARMS_PATH).push();
                alarmRef.setValue(alarm);
                mCurrentTime.setTimeInMillis(System.currentTimeMillis());
                mCurrentTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                mCurrentTime.set(Calendar.MINUTE, minute);
            }
        }, hour, minute, true);
        mTimePicker.setTitle("Select Alarm Time");
        mTimePicker.show();

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, mCurrentTime.getTimeInMillis(), 1000 * 60 * 1440, alarmIntent);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, new AlarmListFragment(), "alarms");
        ft.commit();
    }

    public void addNote(String title, String description) {
        final String noteTitle = title;
        final String noteDescription = description;

        Note note = new Note(noteTitle, noteDescription, mAuth.getCurrentUser().getUid());
        DatabaseReference noteRef = mFirebase.getReference(Constants.NOTES_PATH).push();
        noteRef.setValue(note);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, new NoteListFragment(), "notes");
        ft.commit();
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

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, new ReminderListFragment(), "reminders");
        ft.commit();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        showLoginError("Google Connection Failed");
    }


//    @Override
//    public void onSwipe() {
//        FragmentManager fm = getSupportFragmentManager();
//        FragmentTransaction ft = fm.beginTransaction();
//        fm.popBackStackImmediate();
//        ft.commit();
//    }
}
