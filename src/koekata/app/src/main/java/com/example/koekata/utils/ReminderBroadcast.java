package com.example.koekata.utils;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.koekata.BaseApplication;
import com.example.koekata.R;
import com.example.koekata.ui.auth.AuthActivity;

public class ReminderBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        sendNotification(context);
    }

    private void sendNotification(Context ctx) {
        if (ctx == null)
            return;

        Intent resultIntent = new Intent(ctx, AuthActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(ctx);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(getNotificationId(), PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification= new NotificationCompat.Builder(ctx, BaseApplication.CHANNEL_ID)
                .setContentTitle("Daily Tasks")
                .setContentText("See what tasks you have today.")
                .setSmallIcon(R.drawable.anya)
                .setContentIntent(resultPendingIntent)
                .build();

        NotificationManagerCompat manager = NotificationManagerCompat.from(ctx);
        manager.notify(getNotificationId(), notification);
    }

    private int getNotificationId() {
        long time = System.currentTimeMillis() / 10000007;
        return (int) time;
    }
}
