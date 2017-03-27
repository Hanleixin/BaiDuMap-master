package com.example.mylocation;

        import android.app.Application;

        import com.baidu.mapapi.SDKInitializer;

/**
 * Created by wuyinlei on 2016/3/1.
 */
public class MyApplication  extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化
        SDKInitializer.initialize(getApplicationContext());
    }
}
