package edu.rosehulman.finngw.quicknotes.utilities;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by Matt Boutell on 10/2/2015. Utility class that handles bookkeeping and one-off
 * access to Firebase.
 */
public class Utils {


    public static void signOut(Context context) {
        // Remove UID and active course from shared prefs
        SharedPreferencesUtils.removeCurrentCourseKey(context);
        SharedPreferencesUtils.removeCurrentUser(context);

        // Log out
        FirebaseAuth.getInstance().signOut();
    }
}
