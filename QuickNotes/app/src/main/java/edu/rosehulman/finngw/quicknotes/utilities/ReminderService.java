package edu.rosehulman.finngw.quicknotes.utilities;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import edu.rosehulman.finngw.quicknotes.R;
import edu.rosehulman.finngw.quicknotes.activities.StopAlarmActivity;

/**
 * Created by deradaam on 2/14/2017.
 */

public class ReminderService extends  IntentService {
    private NotificationManager reminderNotificationManager;

    public ReminderService() {
        super("ReminderService");
    }

    @Override
    public void onHandleIntent(Intent intent) {
        sendNotification("Fold Laundry");
    }

    private void sendNotification(String msg) {
        Log.d("ReminderService", "Preparing to send notification...: " + msg);
        reminderNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, StopAlarmActivity.class), 0);

        NotificationCompat.Builder alamNotificationBuilder = new NotificationCompat.Builder(
                this).setContentTitle("Reminder").setSmallIcon(R.drawable.logo_image)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentText(msg);

        alamNotificationBuilder.setContentIntent(contentIntent);

        reminderNotificationManager.notify(1, alamNotificationBuilder.build());
        Log.d("ReminderService", "Notification sent.");
    }
}
