package com.stimednp.aplikasimoviecataloguesub4.myservice;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.stimednp.aplikasimoviecataloguesub4.R;
import com.stimednp.aplikasimoviecataloguesub4.mymodel.MainViewModel;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by rivaldy on 8/18/2019.
 */

@SuppressLint("Registered")
public class GetMovieReleaseService extends JobService {
    public static final String TAG = GetMovieReleaseService.class.getSimpleName();
    private static final String API_KEY = "8b904530a7aced766995fa063ed27355";
    final String TIME_RELEASE = "2019-08-19";
    private MainViewModel mainViewModel;

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "onStartJob() Executed");
        getMovieRelease(params);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(TAG, "onStopJob() Executed");
        return true;
    }

    private void getMovieRelease(final JobParameters job) {
        Log.d(TAG, "Running");
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.themoviedb.org/3/discover/movie?api_key=" + API_KEY + "&primary_release_date.gte=" + TIME_RELEASE + "&primary_release_date.lte=" + TIME_RELEASE;
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
                        showNotification(getApplicationContext(), titleMovie, message, notifId);
                    }
                    jobFinished(job, false);
                } catch (Exception e) {
                    jobFinished(job, true);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                jobFinished(job, true);
            }
        });
    }

    private void showNotification(Context context, String title, String message, int notifId) {
        String CHANNEL_ID = "Channel_1";
        String CHANNEL_NAME = "Job scheduler channel";

        NotificationManager notificationManagerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.ic_movie_filter_black_24dp)
                .setContentText(message)
                .setColor(ContextCompat.getColor(context, android.R.color.black))
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
            notificationManagerCompat.notify(notifId, notification);
        }
    }
}
