package com.example.koekata.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;

import com.example.koekata.ui.main.DailyTaskList.DailyTaskListViewModel;

import java.util.List;
import java.util.Map;


public class NotificationUtils extends ContextWrapper {

    public NotificationUtils(Context base) {
        super(base);
    }

    public void setReminder(int hourOfDay) {
        // TODO
        Intent intent = new Intent(this, ReminderBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                getTimeInMillis(hourOfDay, 0, 0),
                AlarmManager.INTERVAL_DAY,
                pendingIntent);
    }

    private long getTimeInMillis(int hour, int min, int sec) {
        // TODO
        return System.currentTimeMillis() + 60 * 1000L;
    }
}
