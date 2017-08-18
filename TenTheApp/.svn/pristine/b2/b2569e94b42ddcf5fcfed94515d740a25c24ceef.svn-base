package com.nvcomputers.ten.views.auth;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.nvcomputers.ten.R;
import com.nvcomputers.ten.utils.IntentHandler;
import com.nvcomputers.ten.utils.PreferenceKeys;
import com.nvcomputers.ten.utils.SharedPrefsHelper;

import java.util.Locale;

public class Splash2Activity extends AppCompatActivity implements View.OnClickListener {

    private Button splashLoginBtn;
    private Button splashSignUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash2);
        initUI();
    }

    private void initUI() {
        splashLoginBtn = (Button) findViewById(R.id.button_login);
        splashSignUpButton = (Button) findViewById(R.id.button_register);
        splashLoginBtn.setOnClickListener(this);
        splashSignUpButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_login:
                IntentHandler.switchActivity(Splash2Activity.this, LoginActivity.class);
                finish();
                break;
            case R.id.button_register:
                SharedPrefsHelper sharedPrefsHelper = new SharedPrefsHelper(this);
                String policy = sharedPrefsHelper.get(PreferenceKeys.PREF_PRIVACY_POLICY);
                if (TextUtils.isEmpty(policy)) {
                    IntentHandler.switchActivity(Splash2Activity.this, SignUpPolicyActivity.class);
                } else {
                    IntentHandler.switchActivity(Splash2Activity.this, SignUpActivity.class);
                }
                break;
        }

    }
}
