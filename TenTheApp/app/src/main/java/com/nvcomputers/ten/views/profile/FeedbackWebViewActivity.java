package com.nvcomputers.ten.views.profile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.nvcomputers.ten.R;

public class FeedbackWebViewActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_web_view);
        webView = (WebView) findViewById(R.id.webView1);
        webView.getSettings().setJavaScriptEnabled(true);
        String url= "http://tentheapp.com/" +"feedback.html";
        webView.loadUrl(url);
    }
}
