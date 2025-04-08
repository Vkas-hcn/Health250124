package com.jia.opens.golden.scales.towards.zsdk5f;

import android.util.Log;

import androidx.annotation.Keep;

/**
 * 
 * Description:
 **/
@Keep
public class A76fef {

    static {
        try {
            System.loadLibrary("sugarer");
        } catch (Exception e) {
            Log.e("TAG", "static initializer: DQVAXV"+e.getMessage());
        }
    }
	////注意:透明页面的onDestroy方法加上: (this.getWindow().getDecorView() as ViewGroup).removeAllViews()
	////  override fun onDestroy() {
    ////    (this.getWindow().getDecorView() as ViewGroup).removeAllViews()
    ////    super.onDestroy()
    ////}
    @Keep
    public static native void Kiwfdjs(Object context);//1.传应用context.(在主进程里面初始化一次)
    @Keep
    public static native void Amje664f(Object context);//1.传透明Activity对象(在透明页面onCreate调用).
    @Keep
    public static native void U655tg5(int idex);

}
