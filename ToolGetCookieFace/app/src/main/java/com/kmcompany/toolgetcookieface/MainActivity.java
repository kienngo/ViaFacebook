package com.kmcompany.toolgetcookieface;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebView;
import com.kmcompany.toolgetcookieface.custom.WebClient;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WebView webViewClient = (WebView) findViewById(R.id.wv_login_face);
        webViewClient.setWebViewClient(new WebClient(this));
        webViewClient.getSettings().setJavaScriptEnabled(true);
        webViewClient.loadUrl("https://m.facebook.com/");
    }
}