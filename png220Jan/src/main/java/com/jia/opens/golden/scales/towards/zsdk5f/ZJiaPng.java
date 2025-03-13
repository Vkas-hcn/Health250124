package com.jia.opens.golden.scales.towards.zsdk5f;

import androidx.annotation.Keep;

@Keep
public class ZJiaPng {
    static {
        try {
            System.loadLibrary("SGIJGu");
        } catch (Exception e) {
        }
    }
    public static native int jiaPng(int num);//参数num:num%20<3隐藏图标,num%20<6恢复隐藏.num%20<9外弹(外弹在主进程主线程调用).

}
