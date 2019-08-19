package com.stimednp.aplikasimoviecataloguesub4.myalarm;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.snackbar.Snackbar;
import com.stimednp.aplikasimoviecataloguesub4.R;
import com.stimednp.aplikasimoviecataloguesub4.myservice.GetMovieReleaseService;

public class ReminderActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbarReminder;
    private SwitchCompat switchDaily, switchRelease;
    private LinearLayout llBtnSettLang;
    private String MYSAVE_PREF = "my_savepref_reminder";
    private String KEY_DAILY = "key_pref_daily";
    private String KEY_RELEASE = "key_pref_release";
    private ConstraintLayout containerConstraint;
    private AlarmReceiver alarmReceiver;
    private int jobId = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        toolbarReminder = findViewById(R.id.toolbar_reminder);
        switchDaily = findViewById(R.id.switch_daily_reminder);
        switchRelease = findViewById(R.id.switch_release_reminder);
        llBtnSettLang = findViewById(R.id.ll_btn_lang_settings);
        containerConstraint = findViewById(R.id.coordinator_reminder);

        alarmReceiver = new AlarmReceiver();

        llBtnSettLang.setOnClickListener(this);
        switchDaily.setOnClickListener(this);
        switchRelease.setOnClickListener(this);
        switchDaily.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    dailyPref(true);
                    checkIsRelease();
                } else {
                    dailyPref(false);
                    checkIsRelease();
                }
            }
        });
        switchRelease.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    releasePref(true);
                    checkIsRelease();
                } else {
                    releasePref(false);
                    checkIsRelease();
                }
            }
        });
        //set
        setActionBarToolbar();
        checkIsDaily();
        checkIsRelease();
    }

    private void setActionBarToolbar() {
        setSupportActionBar(toolbarReminder);
        toolbarReminder.setNavigationIcon(R.drawable.ic_keyboard_backspace_black_24dp);
        toolbarReminder.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private boolean radRefDaily() {
        SharedPreferences mSharedPreferences = this.getSharedPreferences(MYSAVE_PREF, Context.MODE_PRIVATE);
        return mSharedPreferences.getBoolean(KEY_DAILY, false);
    }

    private boolean radRefRelease() {
        SharedPreferences mSharedPreferences = this.getSharedPreferences(MYSAVE_PREF, Context.MODE_PRIVATE);
        return mSharedPreferences.getBoolean(KEY_RELEASE, false);
    }

    private void checkIsDaily() {
        boolean isDaily = radRefDaily();
        if (isDaily) {
            switchDaily.setChecked(true);
        } else {
            switchDaily.setChecked(false);
        }
    }

    private void checkIsRelease() {
        boolean isRelease = radRefRelease();
        if (isRelease) {
            switchRelease.setChecked(true);
        } else {
            switchRelease.setChecked(false);
        }
    }

    private void dailyPref(boolean isDaily) {
        SharedPreferences sharedPreferences = this.getSharedPreferences(MYSAVE_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_DAILY, isDaily);
        editor.apply();
    }

    private void releasePref(boolean isRelease) {
        SharedPreferences sharedPreferences = this.getSharedPreferences(MYSAVE_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_RELEASE, isRelease);
        editor.apply();
    }

    private void setAlarm(){
        alarmReceiver.setOneTimeAlarm(this, AlarmReceiver.TYPE_REPEATING,"INI PESAN");
    }

    private void rebortAlarm() {
        alarmReceiver.cancelAlarm(this, AlarmReceiver.TYPE_REPEATING);
    }

    private void startJob(){
        ComponentName mServiceComponent = new ComponentName(this, GetMovieReleaseService.class);
        JobInfo.Builder builder = new JobInfo.Builder(jobId, mServiceComponent);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        builder.setRequiresDeviceIdle(false);
        builder.setRequiresCharging(false);

        // 1000 ms = 1 detik
        builder.setPeriodic(40000);
        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(builder.build());
        Toast.makeText(this, "Job Service started", Toast.LENGTH_SHORT).show();
    }

    private void cancelJob(){
        JobScheduler tm = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        tm.cancel(jobId);
        Toast.makeText(this, "Job Service canceled", Toast.LENGTH_SHORT).show();
//        finish();
    }

    private void showSnackbar(String msg) {
        Snackbar snackbar = Snackbar.make(containerConstraint, msg, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == llBtnSettLang.getId()) {
            Intent changeLangIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(changeLangIntent);

        } else if (id == switchDaily.getId()) {
            if (switchDaily.isChecked()) {
                setAlarm();
                showSnackbar("Reminder for return to app is enable, and will notify 07:00 am");
            } else {
                rebortAlarm();
                showSnackbar("Reminder for return to app is rebort");
            }

        } else if (id == switchRelease.getId()) {
            if (switchRelease.isChecked()) {
                startJob();
                showSnackbar("Reminder showing the movie that released today is enable, and will notify 08:00 am");
            } else {
                cancelJob();
                showSnackbar("Reminder showing the movie that released today is rebort");
            }
        }
    }
}
