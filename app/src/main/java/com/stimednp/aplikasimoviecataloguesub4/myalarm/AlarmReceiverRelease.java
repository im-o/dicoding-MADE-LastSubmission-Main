package com.stimednp.aplikasimoviecataloguesub4.myalarm;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.stimednp.aplikasimoviecataloguesub4.R;
import com.stimednp.aplikasimoviecataloguesub4.addingmethod.AllOtherMethod;
import com.stimednp.aplikasimoviecataloguesub4.myactivity.MainActivity;

import org.json.JSONObject;

import java.util.Calendar;

import cz.msebera.android.httpclient.Header;

import static com.stimednp.aplikasimoviecataloguesub4.addingmethod.Constant.API_KEY;

/**
 * Created by rivaldy on 8/18/2019.
 */

public class AlarmReceiverRelease extends BroadcastReceiver {
    private static final String TAG = AlarmReceiverRelease.class.getSimpleName();
    private int notifId = (int) (-1 * System.currentTimeMillis());
    private int ALARM_ID = 101;

    public AlarmReceiverRelease() {
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        String currentDate = AllOtherMethod.getCurrentDates();
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.themoviedb.org/3/discover/movie?api_key=" + API_KEY + "&primary_release_date.gte=" + currentDate + "&primary_release_date.lte=" + currentDate;
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                Log.d(TAG, result);
                try {
                    JSONObject responseObject = new JSONObject(result);
                    int totalResult = responseObject.getInt("total_results");
                    for (int i = 0; i < totalResult; i++) {
                        String titleMovie = responseObject.getJSONArray("results").getJSONObject(i).getString("title");
                        String strRelease = context.getResources().getString(R.string.str_release);
                        String message = titleMovie + ", " + strRelease;
                        showAlarmNotification(context, titleMovie, message, notifId + i);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

    }

    public void setReleaseAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiverRelease.class);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ALARM_ID, intent, 0);
        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    private void showAlarmNotification(Context context, String title, String message, int notifyId) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, notifyId, intent, PendingIntent.FLAG_ONE_SHOT);
        String CHANNEL_ID = "Channel_2";
        String CHANNEL_NAME = "AlarmManagerRelease channel";
        NotificationManager notificationManagerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_primary_green_moviedb)
                .setContentTitle(title)
                .setContentText(message)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmSound)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});
            builder.setChannelId(CHANNEL_ID);
            if (notificationManagerCompat != null) {
                notificationManagerCompat.createNotificationChannel(channel);
            }
        }
        Notification notification = builder.build();
        if (notificationManagerCompat != null) {
            notificationManagerCompat.notify(notifyId, notification);
        }
    }

    public void cancelAlarmRelease(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiverRelease.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ALARM_ID, intent, 0);
        pendingIntent.cancel();
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }
}
