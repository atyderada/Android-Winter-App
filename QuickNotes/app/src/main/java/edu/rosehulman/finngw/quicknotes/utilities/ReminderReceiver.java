package edu.rosehulman.finngw.quicknotes.utilities;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * Created by deradaam on 2/14/2017.
 */

public class ReminderReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        //this will send a notification message
        ComponentName comp = new ComponentName(context.getPackageName(),
                ReminderService.class.getName());
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
    }
}
