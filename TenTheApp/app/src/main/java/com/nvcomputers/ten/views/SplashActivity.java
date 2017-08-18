package com.nvcomputers.ten.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.nvcomputers.ten.R;
import com.nvcomputers.ten.api.GetRestAdapter;
import com.nvcomputers.ten.utils.PreferenceKeys;
import com.nvcomputers.ten.utils.SharedPrefsHelper;
import com.nvcomputers.ten.views.auth.Splash2Activity;
import com.nvcomputers.ten.views.home.LandingActivity;

public class SplashActivity extends AppCompatActivity {
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    //public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    //public static final String REGISTRATION_COMPLETE = "registrationComplete";
    private static final String TAG = "SplashActivity";
    private SharedPrefsHelper sharedPrefsHelper;
    private Thread timerThread;
    //Creating a broadcast receiver for gcm registration
    //private BroadcastReceiver mRegistrationBroadcastReceiver;
    private boolean isReceiverRegistered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPrefsHelper = new SharedPrefsHelper(SplashActivity.this);

        String imageTag = sharedPrefsHelper.get(PreferenceKeys.PREF_IMAGE_TAG, "");
        if (imageTag == null || imageTag.equals("")) {
            sharedPrefsHelper.save(PreferenceKeys.PREF_IMAGE_TAG, String.valueOf(System.currentTimeMillis()));
        }

        timerThread = new Thread() {
            public void run() {
                try {
                    sleep(3000);
                    String user_id = sharedPrefsHelper.get(PreferenceKeys.PREF_USER_ID, "");
                    if (user_id == null || user_id.equals("")) {
                        GetRestAdapter.retrofitInterface = null;
                        Intent intent = new Intent(SplashActivity.this, Splash2Activity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        TTApplication.userName = sharedPrefsHelper.get(PreferenceKeys.PREF_USER_NAME, "");
                        TTApplication.password = sharedPrefsHelper.get(PreferenceKeys.PREF_PASSWORD, "");
                        Intent intent = new Intent(SplashActivity.this, LandingActivity.class);//HomeScreen
                        startActivity(intent);
                        finish();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        timerThread.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (timerThread != null && timerThread.isAlive()) {
            timerThread.interrupt();
            finish();
        }
    }


}
