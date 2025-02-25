package com.jia.opens.golden.scales.towards.zsdk5f;

import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jia.opens.golden.scales.towards.acan.ShowDataTool;

public class Ccan7t extends WebViewClient {
	
    @Override
    public void onPageStarted(WebView view, String url, android.graphics.Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        ShowDataTool.INSTANCE.showLog(" onPageStarted=url="+url);

    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        ShowDataTool.INSTANCE.showLog(" onPageFinished=url="+url);
    }
}
