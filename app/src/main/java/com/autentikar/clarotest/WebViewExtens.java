package com.autentikar.clarotest;

import android.annotation.TargetApi;
import android.os.Build;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;

public class WebViewExtens extends WebChromeClient {

    //@TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onPermissionRequest(PermissionRequest request) {
        final String[] requestedResources = request.getResources();
        //request.grant(requestedResources);
         for (String r : requestedResources) {

            if (r.equals(PermissionRequest.RESOURCE_VIDEO_CAPTURE) || r.equals(PermissionRequest.RESOURCE_AUDIO_CAPTURE)) {
                request.grant(requestedResources);
                break;
            }
        }
    }

    @Override
    public void onPermissionRequestCanceled(PermissionRequest request) {
        super.onPermissionRequestCanceled(request);
    }
}
