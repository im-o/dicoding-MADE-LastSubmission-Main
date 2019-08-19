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
    private AlarmReceiverDaily alarmReceiverDaily;
    private AlarmReceiverRelease alarmReceiverRelease;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        toolbarReminder = findViewById(R.id.toolbar_reminder);
        switchDaily = findViewById(R.id.switch_daily_reminder);
        switchRelease = findViewById(R.id.switch_release_reminder);
        llBtnSettLang = findViewById(R.id.ll_btn_lang_settings);
        containerConstraint = findViewById(R.id.coordinator_reminder);

        alarmReceiverDaily = new AlarmReceiverDaily();
        alarmReceiverRelease = new AlarmReceiverRelease();

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

    private void setAlarmDaily(){
        alarmReceiverDaily.setOneTimeAlarm(this);
    }

    private void rebortAlarmDaily() {
        alarmReceiverDaily.cancelAlarm(this);
    }

    private void setAlarmRelease(){
        alarmReceiverRelease.setReleaseAlarm(this);
    }

    private void rebortAlarmRelease() {
        alarmReceiverRelease.cancelAlarmRelease(this);
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
                setAlarmDaily();
                showSnackbar("Reminder for return to app is enable, and will notify 07:00 am");
            } else {
                rebortAlarmDaily();
                showSnackbar("Reminder for return to app is rebort");
            }

        } else if (id == switchRelease.getId()) {
            if (switchRelease.isChecked()) {
                setAlarmRelease();
                showSnackbar("Reminder showing the movie that released today is enable, and will notify 08:00 am");
            } else {
                rebortAlarmRelease();
                showSnackbar("Reminder showing the movie that released today is rebort");
            }
        }
    }
}
