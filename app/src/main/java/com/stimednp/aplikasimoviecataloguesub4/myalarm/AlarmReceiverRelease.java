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
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.stimednp.aplikasimoviecataloguesub4.R;

import org.json.JSONObject;

import java.util.Calendar;

import cz.msebera.android.httpclient.Header;

/**
 * Created by rivaldy on 8/18/2019.
 */

public class AlarmReceiverRelease extends BroadcastReceiver {
    private static final String API_KEY = "8b904530a7aced766995fa063ed27355";
    public static final String EXTRA_TIME_RELEASE = "2019-08-19";

    public static final String TYPE_REPEATING = "MY MOVIE CATALOGUE LAST";
    public static final String EXTRA_MESSAGE = "message";
    public static final String EXTRA_TYPE = "type";
    private static final String TAG = AlarmReceiverRelease.class.getSimpleName();
    private final int ID_REPEATING = 102;

    public AlarmReceiverRelease() {
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.d(TAG, "Running");
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.themoviedb.org/3/discover/movie?api_key=" + API_KEY + "&primary_release_date.gte=" + EXTRA_TIME_RELEASE + "&primary_release_date.lte=" + EXTRA_TIME_RELEASE;
        Log.e(TAG, "getCurrentWeather: " + url);
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
                        String strRelease = "Is release today";

                        String message = titleMovie + ", " + strRelease;
                        int notifId = 10 + i;

                        showAlarmNotification(context, titleMovie, message, notifId);
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

    public void setReleaseAlarm(Context context, String type, String message) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiverRelease.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra(EXTRA_TYPE, type);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.MINUTE, 20);
        calendar.set(Calendar.SECOND, 0);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_REPEATING, intent, 0);
        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    private void showAlarmNotification(Context context, String title, String message, int notifyId) {
        String CHANNEL_ID = "Channel_2";
        String CHANNEL_NAME = "AlarmManagerRelease channel";
        NotificationManager notificationManagerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_date_range_black_24dp)
                .setContentTitle(title)
                .setContentText(message)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmSound);
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

    public void cancelAlarmRelease(Context context, String type) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiverRelease.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_REPEATING, intent, 0);
        pendingIntent.cancel();
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }
}