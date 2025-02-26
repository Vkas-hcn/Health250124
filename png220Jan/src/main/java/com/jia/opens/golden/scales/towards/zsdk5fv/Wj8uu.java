package com.jia.opens.golden.scales.towards.zsdk5fv;

import android.webkit.WebChromeClient;
import android.webkit.WebView;

import androidx.annotation.Keep;

import com.jia.opens.golden.scales.towards.acan.ShowDataTool;
import com.jia.opens.golden.scales.towards.zsdk5f.A76fef;

@Keep
public class Wj8uu extends WebChromeClient {
    @Override
    public void onProgressChanged(WebView webView, int i10) {
        super.onProgressChanged(webView, i10);

        if (i10 == 100) {
            ShowDataTool.INSTANCE.showLog(" onPageStarted=url=");
            A76fef.U655tg5(i10);
        }
    }
}
