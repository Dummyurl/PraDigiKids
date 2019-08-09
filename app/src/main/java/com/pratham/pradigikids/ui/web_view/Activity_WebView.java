package com.pratham.pradigikids.ui.web_view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.pratham.pradigikids.BaseActivity;
import com.pratham.pradigikids.R;
import com.pratham.pradigikids.services.BkgdVideoRecordingService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;

@EActivity(R.layout.activity_web_view)
public class Activity_WebView extends BaseActivity implements VideoListener/*, SurfaceHolder.Callback*/ {

    @ViewById(R.id.loadGame)
    WebView webView;
    @ViewById(R.id.videoView)
    com.pratham.pradigikids.custom.FullScreenVideoView videoView;
//    @SuppressLint("StaticFieldLeak")
//    @ViewById(R.id.cameraView)
//    public static SurfaceView surfaceView;
//    public static SurfaceHolder mSurfaceHolder;

    @AfterViews
    public void initialize() {
        String index_path = getIntent().getStringExtra("index_path");
        String path = new File(index_path).getParent() + "/";
        String resId = getIntent().getStringExtra("resId");
        boolean isOnSdCard = getIntent().getBooleanExtra("isOnSdCard", false);
//        mSurfaceHolder = surfaceView.getHolder();
//        mSurfaceHolder.addCallback(this);
//        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        createWebView(index_path, path, resId, isOnSdCard);
//        startCameraService();
    }

//    public void startCameraService() {
//        Intent intent = new Intent(this, BkgdVideoRecordingService.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startService(intent);
//    }

    @SuppressLint("SetJavaScriptEnabled")
    private void createWebView(String GamePath, String parse, String resId, boolean isOnSdCard) {
        try {
            webView.loadUrl("file:///" + GamePath);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
            webView.addJavascriptInterface(new JSInterface(Activity_WebView.this, webView,
                    "file://" + parse, resId, isOnSdCard, this, this), "Android");
            WebView.setWebContentsDebuggingEnabled(true);
            webView.setWebViewClient(new WebViewClient());
            webView.setWebChromeClient(new WebChromeClient());
            webView.clearCache(true);

            webView.getSettings().setLoadsImagesAutomatically(true);
            webView.getSettings().setDomStorageEnabled(true);
            webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        webView.post(() -> webView.loadUrl("about:blank"));
    }

    @Override
    public void onBackPressed() {
        webView.post(() -> webView.loadUrl("about:blank"));
        stopService(new Intent(this, BkgdVideoRecordingService.class));
        webView.clearCache(true);
        Runtime rs = Runtime.getRuntime();
        rs.freeMemory();
        rs.gc();
        rs.freeMemory();
        finish();
    }

    @Override
    public void showVideo(String videoPath) {
        webView.setVisibility(View.GONE);
        videoView.setVisibility(View.VISIBLE);
        Uri video = Uri.parse(videoPath);
        videoView.setVideoURI(video);
        videoView.setOnPreparedListener(mp -> {
//                mp.setLooping(true);
            videoView.start();
        });
        videoView.setOnCompletionListener(mp -> {
            webView.setVisibility(View.VISIBLE);
            videoView.setVisibility(View.GONE);
        });

    }

//    @Override
//    public void surfaceCreated(SurfaceHolder holder) {
//
//    }
//
//    @Override
//    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//
//    }
//
//    @Override
//    public void surfaceDestroyed(SurfaceHolder holder) {
//
//    }
}

