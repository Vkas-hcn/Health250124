package com.jia.opens.golden.scales.towards.zsdk5f;

import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class Wj8uu extends WebChromeClient {
    @Override
    public void onProgressChanged(WebView webView, int i10) {
        super.onProgressChanged(webView, i10);
        if (i10 == 100) {
            A76fef.U655tg5(i10);
        }
    }
}
