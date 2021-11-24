package com.autentikar.clarotest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.PermissionRequest;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends AppCompatActivity {

    //WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //webView = new WebView(this);
        setContentView(R.layout.activity_web_view);
        checkCameraPermissionAndStartWebView();
    }

    private void checkCameraPermissionAndStartWebView() {
        int camera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int recordAudio = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        if (camera != PackageManager.PERMISSION_GRANTED || recordAudio != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO}, 1001);
        } else {
            setup();
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    void setup(){
        WebView webView = (WebView) findViewById(R.id.webView);
        webView.loadUrl("https://apiv3.qa.autentikar.com/oauth/authorize?client_id=1ef44193-26f3-41cf-9a01-f050bcf8a42b&response_type=code&flow_id=618157f2f80f42881c0a4b53&user=%257B%2522country%2522%3A%2522CL%2522%2C%2522doc_type%2522%3A%2522CI%2522%2C%2522id_num%2522%3A%2522159058387%2522%257D");
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebViewExtens());
        WebView.setWebContentsDebuggingEnabled(true);

        webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setDomStorageEnabled(true);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        checkCameraPermissionAndStartWebView();
    }
}