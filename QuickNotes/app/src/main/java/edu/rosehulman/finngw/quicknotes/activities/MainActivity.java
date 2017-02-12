package edu.rosehulman.finngw.quicknotes.activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

import edu.rosehulman.finngw.quicknotes.R;
import edu.rosehulman.finngw.quicknotes.fragments.AlarmListFragment;
import edu.rosehulman.finngw.quicknotes.fragments.LoginFragment;
import edu.rosehulman.finngw.quicknotes.fragments.NoteListFragment;
import edu.rosehulman.finngw.quicknotes.fragments.ReminderListFragment;
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

    private DatabaseReference mFirebaseRef;
    private DatabaseReference mOwnerRef;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private OnCompleteListener mOnCompleteListener;
    private GradeRecorderActivity.OwnerValueEventListener mOwnerValueEventListener;
    private static final int RC_ROSEFIRE_LOGIN = 1;

    private boolean onEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        mToolbar.setBackgroundColor(Color.WHITE);

        if (savedInstanceState == null) {
            initializeFirebase();
        }

        //mAuth = FirebaseAuth.getInstance();
        //initializeListeners();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);
        navigationView.setNavigationItemSelectedListener(this);

        mFirebaseRef = FirebaseDatabase.getInstance().getReference();

        onEdit = false;

        /*
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new CardListAdapter(null, this);
        mRecyclerView.setAdapter(mAdapter);

        mContentView = findViewById(R.id.fullscreen_content);

        ImageButton mAlarmImgeView = (ImageButton) findViewById(R.id.alarm_button);
        mAlarmImgeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.addAlarm(titleTextView.getText().toString(), descriptionTextView.getText().toString());
                titleTextView.setText("");
                descriptionTextView.setText("");
                onEdit = false;
            }
        });

        ImageButton mReminderImageView = (ImageButton) findViewById(R.id.reminder_button);
        mReminderImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.addReminder(titleTextView.getText().toString(), descriptionTextView.getText().toString());
                titleTextView.setText("");
                descriptionTextView.setText("");
                onEdit = false;
            }
        });

        ImageButton mNoteImageView = (ImageButton) findViewById(R.id.note_button);
        mNoteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.addNote(titleTextView.getText().toString(), descriptionTextView.getText().toString());
                titleTextView.setText("");
                descriptionTextView.setText("");
                onEdit = false;
            }
        });
        */
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

    private void initializeFirebase() {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        mFirebaseRef = FirebaseDatabase.getInstance().getReference();
        mFirebaseRef.keepSynced(true);
    }

    /*
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
                } else {
                    SharedPreferencesUtils.setCurrentUser(GradeRecorderActivity.this, user.getUid());
                    Log.d(Constants.TAG, "User is authenticated");
                    mOwnerRef = FirebaseDatabase.getInstance().getReference().child(Constants.OWNERS_PATH).child(user.getUid());
                    // MB: moved from here
                    // TODO: Need to differ, if auth via rosefire or email/password.
                    Log.d(Constants.TAG, " Provider: " + firebaseAuth.getCurrentUser().getProviderId());
                    Log.d(Constants.TAG, " user display name: " + firebaseAuth.getCurrentUser().getDisplayName());
                    Log.d(Constants.TAG, " user email: " + firebaseAuth.getCurrentUser().getEmail());
                    // Currently, if rosefire, email is null. Will be fixed in next version.
                    if (firebaseAuth.getCurrentUser().getEmail() != null) {
                        // Email/password.
                        if (mOwnerValueEventListener != null) {
                            mOwnerRef.removeEventListener(mOwnerValueEventListener);
                        }
                        Log.d(Constants.TAG, "Adding OwnerValueListener for " + mOwnerRef.toString());
                        mOwnerValueEventListener = new GradeRecorderActivity.OwnerValueEventListener();
                        mOwnerRef.addValueEventListener(mOwnerValueEventListener);
                        // TODO: This isn't triggering the dialog, as it should.
                    } else {
                        // Rosefire: Done
                        // MB: moved from above
                        mOwnerRef.child(Owner.USERNAME).setValue(user.getUid());
                        Log.d(Constants.TAG, "Rosefire worked. UID = " + user.getUid());
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
    */

    /*
   private void showLoginError(String message) {
       LoginFragment loginFragment = (LoginFragment) getSupportFragmentManager().findFragmentByTag("login");
       loginFragment.onLoginError(message);
   }
   */

    @Override
    public void onRosefireLogin() {
        Intent signInIntent = Rosefire.getSignInIntent(this, Constants.ROSEFIRE_REGISTRY_TOKEN);
        startActivityForResult(signInIntent, RC_ROSEFIRE_LOGIN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_ROSEFIRE_LOGIN) {
            RosefireResult result = Rosefire.getSignInResultFromIntent(data);
            if (result.isSuccessful()) {
                firebaseAuthWithRosefire(result);
            } else {
                showLoginError("Rosefire authentication failed.");
            }
        }
    }

    private void firebaseAuthWithRosefire(RosefireResult result) {
        mAuth.signInWithCustomToken(result.getToken())
                .addOnCompleteListener(mOnCompleteListener);
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
        // Was in fragment onDetach
        if (mOwnerValueEventListener != null) {
            mOwnerRef.removeEventListener(mOwnerValueEventListener);
        }
        mOwnerValueEventListener = null;
    }

    public void onLoginComplete(String uid) {
        Log.d(Constants.TAG, "User is authenticated");

        SharedPreferencesUtils.setCurrentUser(this, uid);

        // Check if they have a current course
        String currentCourseKey = SharedPreferencesUtils.getCurrentCourseKey(this);
        Fragment switchTo;
        if (currentCourseKey == null || currentCourseKey.isEmpty()) {
            switchTo = new CourseListFragment();
        } else {
            switchTo = AssignmentListFragment.newInstance(currentCourseKey);
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, switchTo);
        ft.commit();
    }

    @Override
    public void onLogin(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, mOnCompleteListener);
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
    public boolean onNavigationItemSelected(MenuItem item) {
        String currentCourseKey = SharedPreferencesUtils.getCurrentCourseKey(this);
        int id = item.getItemId();
        Fragment switchTo = null;
        String tag = "";

        // TODO: May be useful if I implement return to the chosen fragment after choosing a course.
        if (id == R.id.nav_sign_out) {
            Utils.signOut(this);
            switchTo = new LoginFragment();
            tag = "login";
        } else if (id == R.id.nav_courses || currentCourseKey == null) {
            switchTo = new CourseListFragment();
            tag = "courses";
        } else if (id == R.id.nav_assignments) {
            switchTo = AssignmentListFragment.newInstance(currentCourseKey);
            tag = "assignments";
        } else if (id == R.id.nav_students) {
            switchTo = new StudentListFragment();
            tag = "students";
        } else if (id == R.id.nav_owners) {
            switchTo = new OwnerListFragment();
            tag = "owners";
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

    @Override
    public void onAssignmentSelected(Assignment assignment) {
        // TODO: go to grade entry fragment
    }

    @Override
    public void onCourseSelected(Course selectedCourse) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, AssignmentListFragment.newInstance(selectedCourse.getKey()));
        ft.addToBackStack("course_fragment");
        ft.commit();
    }

    @Override
    public void onThisOwnerRemoved() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, new CourseListFragment());
        ft.commit();
    }

    class OwnerValueEventListener implements ValueEventListener {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            String username = (String) dataSnapshot.child(Owner.USERNAME).getValue();
            Log.d(Constants.TAG, "Rose username in LoginActivity: " + username);
            if (username == null) {
                showUsernameDialog();
            } else {
                if (mOwnerValueEventListener != null) {
                    mOwnerRef.removeEventListener(mOwnerValueEventListener);
                }
                // TODO: check if this is correct
                String currentUser = SharedPreferencesUtils.getCurrentUser(GradeRecorderActivity.this);
                Log.d(Constants.TAG, String.format(Locale.US, "Sharedprefs current user: [%s]\n", currentUser));
                onLoginComplete(currentUser);
            }
        }

        @Override
        public void onCancelled(DatabaseError firebaseError) {
            Log.d(Constants.TAG, "OwnerValueListener cancelled: " + firebaseError);
        }
    }

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
}
