package com.jia.opens.golden.scales.towards.zsdk5fv;

import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Keep;

import com.jia.opens.golden.scales.towards.acan.ShowDataTool;

@Keep
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
