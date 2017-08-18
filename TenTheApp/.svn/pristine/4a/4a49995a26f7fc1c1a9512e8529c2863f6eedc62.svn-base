package com.nvcomputers.ten.views.auth;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.nvcomputers.ten.R;
import com.nvcomputers.ten.utils.IntentHandler;
import com.nvcomputers.ten.utils.PreferenceKeys;
import com.nvcomputers.ten.utils.SharedPrefsHelper;

public class SignUpPolicyActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView cancelBtn;
    private TextView acceptBtn;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_policy);
        initUI();
    }

    private void initUI() {
        cancelBtn = (TextView) findViewById(R.id.cancel_btn);
        acceptBtn = (TextView) findViewById(R.id.accept_btn);
        webView = (WebView) findViewById(R.id.policy_web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        String url = "http://tentheapp.com/private-policy.html";
        webView.loadUrl(url);
        cancelBtn.setOnClickListener(this);
        acceptBtn.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //IntentHandler.switchActivity(SignUpPolicyActivity.this, Splash2Activity.class);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel_btn:
                //IntentHandler.switchActivity(SignUpPolicyActivity.this, Splash2Activity.class);
                finish();
                break;
            case R.id.accept_btn:

                SharedPrefsHelper sharedPrefsHelper=new SharedPrefsHelper(SignUpPolicyActivity.this);
                sharedPrefsHelper.save(PreferenceKeys.PREF_PRIVACY_POLICY,"1");
                IntentHandler.switchActivity(SignUpPolicyActivity.this, SignUpActivity.class);
                finish();
                break;

        }
    }
}
