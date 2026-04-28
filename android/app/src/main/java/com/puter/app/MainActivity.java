package com.puter.app;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;

import com.getcapacitor.BridgeActivity;

public class MainActivity extends BridgeActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Full-screen immersive mode
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        );
    }

    @Override
    public void onResume() {
        super.onResume();
        // Re-configure WebView settings after Capacitor bridge initializes
        configureWebView();
    }

    private void configureWebView() {
        WebView webView = getBridge().getWebView();
        if (webView == null) return;

        WebSettings settings = webView.getSettings();

        // Enable JavaScript (required for Puter)
        settings.setJavaScriptEnabled(true);

        // DOM storage for Puter's local state
        settings.setDomStorageEnabled(true);

        // Disable built-in zoom controls (Puter handles its own)
        settings.setSupportZoom(false);
        settings.setBuiltInZoomControls(false);
        settings.setDisplayZoomControls(false);

        // Enable hardware acceleration
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        // Enable file access for uploads
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);

        // Cache strategy
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setDatabaseEnabled(true);

        // Media autoplay (needed for Puter media apps)
        settings.setMediaPlaybackRequiresUserGesture(false);

        // Mixed content: deny — Puter is fully HTTPS
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_NEVER_ALLOW);

        // Prevent WebView from opening the system browser for navigation
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(
                    android.webkit.WebView view,
                    android.webkit.WebResourceRequest request) {
                String url = request.getUrl().toString();
                // All puter.com navigation stays in-app
                if (url.startsWith("https://puter.com")
                        || url.startsWith("https://api.puter.com")
                        || url.startsWith("https://js.puter.com")) {
                    return false; // Let WebView handle it
                }
                // Everything else: block or open in-app optionally
                return true;
            }
        });
    }
}
